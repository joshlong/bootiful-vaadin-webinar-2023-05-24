package bootiful.vaadin;

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
    ApplicationRunner applicationRunner(PostClient client, PostRepository repository) {
        return args -> {
            repository.deleteAll();
            repository.saveAll(client.getPosts());
        };
    }

    @Bean
    PostClient postClient(WebClient.Builder builder) {
        var wc = builder.baseUrl("https://jsonplaceholder.typicode.com/").build();
        var wca = WebClientAdapter.forClient(wc);
        return HttpServiceProxyFactory
                .builder(wca)
                .build()
                .createClient(PostClient.class);
    }
}

@Route("")
class PostsView extends VerticalLayout {

    PostsView(PostRepository repository) {

        var h1 = new H1("Posts from your favorite news: an AI");

        var grid = new Grid<>(Post.class);
        grid.setItems(repository.findAll());
        grid.addColumn(Post::postId).setHeader("ID");
        grid.addColumn(Post::title).setHeader("Title");
        grid.addColumn(Post::body).setHeader("Post");

        add(h1, grid);
    }
}


interface PostRepository extends ListCrudRepository<Post, Integer> {
}

record Post(@Id Integer postId, String title, String body) {
}

interface PostClient {

    @GetExchange("/posts")
    Collection<Post> getPosts();
}

