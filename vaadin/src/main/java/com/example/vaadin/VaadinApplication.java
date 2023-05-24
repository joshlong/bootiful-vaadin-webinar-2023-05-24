package com.example.vaadin;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Collection;

@SpringBootApplication
public class VaadinApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaadinApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(PostApiClient client,
                                        PostRepository repository) {
        return args -> {
            repository.deleteAll();
            repository.saveAll(client.getAllPosts());
            repository.findAll().forEach(System.out::println);
        };
    }

    @Bean
    PostApiClient postApiClient(WebClient.Builder builder) {
        var wc = builder.baseUrl("https://jsonplaceholder.typicode.com").build();
        var wca = WebClientAdapter.forClient(wc);
        return HttpServiceProxyFactory
                .builder(wca)
                .build()
                .createClient(PostApiClient.class);
    }

}


@Route("")
class PostsView extends VerticalLayout {

    PostsView(PostRepository repository) {

        var h1 = new H1("Posts by your favorite AI");

        var grid = new Grid<>(Post.class);
        grid.setItems(repository.findAll());
        grid.addColumn(Post::postId).setHeader("Post ID");
        grid.addColumn(Post::title).setHeader("Title");
        grid.addColumn(Post::body).setHeader("Text");

        add(h1, grid);
    }
}

interface PostApiClient {

    @GetExchange("/posts")
    Collection<Post> getAllPosts();
}

interface PostRepository extends ListCrudRepository<Post, Integer> {
}

// look ma, no Lombok!
record Post(@Id Integer postId, String title, String body) {
}