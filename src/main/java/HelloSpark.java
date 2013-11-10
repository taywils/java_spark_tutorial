import static spark.Spark.*;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

public class HelloSpark {
    public static Deque<Article> articles = new ArrayDeque<Article>();

    public static void main(String[] args) {
        get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {
                String title = "My Blog";
                String createArticleLink = "<a href='/article/create'>Write Article</a>";
                StringBuilder html = new StringBuilder();

                html.append("<h1>").append(title).append("</h1>").append(createArticleLink);
                html.append("<hr>");

                if(HelloSpark.articles.isEmpty()) {
                    html.append("<b>No articles have been posted</b>");
                } else {
                    for(Article article : HelloSpark.articles) {
                        html.append("Title: ").append(article.getTitle())
                            .append("<br/>")
                            .append(article.getCreatedAt())
                            .append("<br/>")
                            .append("Summary: ").append(article.getSummary())
                            .append("<br/>")
                            .append(article.getEditLink()).append(" | ").append(article.getDeleteLink())
                            .append("</p>");
                    }
                }

                return html.toString();
            }
        });

        get(new Route("/article/create") {
            @Override
            public Object handle(Request request, Response response) {
                StringBuilder form = new StringBuilder();

                form.append("<form id='article-create-form' method='POST' action='/article/create'>")
                        .append("Title: <input type='text' name='article-title' />")
                        .append("<br/>")
                        .append("Summary: <input type='text' name='article-summary' />")
                        .append("<br/>")
                    .append("</form>")
                    .append("<textarea name='article-content' rows='4' cols='50' form='article-create-form'></textarea>")
                    .append("<br/>")
                    .append("<input type='submit' value='Publish' form='article-create-form' />");

                return form.toString();
            }
        });

        post(new Route("/article/create") {
            @Override
            public Object handle(Request request, Response response) {
                String title = request.queryParams("article-title");
                String summary = request.queryParams("article-summary");
                String content = request.queryParams("article-content");

                Article article = new Article(title, summary, content, HelloSpark.articles.size() + 1);

                HelloSpark.articles.addFirst(article);

                response.status(201);
                response.redirect("/");
                return "";
            }
        });

        get(new Route("/article/read/:id") {
            @Override
            public Object handle(Request request, Response response) {
                Integer id = Integer.parseInt(request.params(":id"));
                StringBuilder html = new StringBuilder();

                for(Article article : HelloSpark.articles) {
                    if(id.equals(article.getId())) {
                        html.append("<a href='/'>Home</a>").append("<p />")
                            .append("Title: ").append(article.getTitle()).append("<br />")
                            .append(article.getCreatedAt())
                            .append("<p>").append(article.getContent()).append("</p>");
                        break;
                    }
                }
                return html.toString();
            }
        });
    }
}