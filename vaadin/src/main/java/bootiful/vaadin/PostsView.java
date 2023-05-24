package bootiful.vaadin;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class PostsView extends VerticalLayout {

    public PostsView(PostRepository repository) {

        var h1 = new H1("Posts from your favorite news: an AI");

        var grid = new Grid<>(Post.class);
        grid.setItems(repository.findAll());
        grid.addColumn(Post::postId).setHeader("ID");
        grid.addColumn(Post::title).setHeader("Title");
        grid.addColumn(Post::body).setHeader("Post");

        add(h1, grid);
    }
}
