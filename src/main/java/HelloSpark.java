import static spark.Spark.*;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerRoute;

import java.util.*;

public class HelloSpark {
    public static ArticleDbService<Article> articleDbService = new ArticleServletDao<Article>();

    public static void main(String[] args) {
        get(new FreeMarkerRoute("/") {
            @Override
            public ModelAndView handle(Request request, Response response) {
                Map<String, Object> viewObjects = new HashMap<String, Object>();
                ArrayList<Article> articles = articleDbService.readAll();

                if (articles.isEmpty()) {
                    viewObjects.put("hasNoArticles", "Welcome, please click \"Write Article\" to begin.");
                } else {
                    Deque<Article> showArticles = new ArrayDeque<Article>();

                    for (Article article : articles) {
                        if (article.readable()) {
                            showArticles.addFirst(article);
                        }
                    }

                    viewObjects.put("articles", showArticles);
                }

                viewObjects.put("templateName", "articleList.ftl");

                return modelAndView(viewObjects, "layout.ftl");
            }
        });

        get(new FreeMarkerRoute("/article/create") {
            @Override
            public Object handle(Request request, Response response) {
                Map<String, Object> viewObjects = new HashMap<String, Object>();

                viewObjects.put("templateName", "articleForm.ftl");

                return modelAndView(viewObjects, "layout.ftl");
            }
        });

        post(new Route("/article/create") {
            @Override
            public Object handle(Request request, Response response) {
                String title = request.queryParams("article-title");
                String summary = request.queryParams("article-summary");
                String content = request.queryParams("article-content");

                Article article = new Article(title, summary, content, articleDbService.readAll().size());

                articleDbService.create(article);

                response.status(201);
                response.redirect("/");
                return "";
            }
        });

        get(new FreeMarkerRoute("/article/read/:id") {
            @Override
            public Object handle(Request request, Response response) {
                Integer id = Integer.parseInt(request.params(":id"));
                Map<String, Object> viewObjects = new HashMap<String, Object>();

                viewObjects.put("templateName", "articleRead.ftl");

                viewObjects.put("article", articleDbService.readOne(id));

                return modelAndView(viewObjects, "layout.ftl");
            }
        });

        get(new FreeMarkerRoute("/article/update/:id") {
            @Override
            public Object handle(Request request, Response response) {
                Integer id = Integer.parseInt(request.params(":id"));
                Map<String, Object> viewObjects = new HashMap<String, Object>();

                viewObjects.put("templateName", "articleForm.ftl");

                viewObjects.put("article", articleDbService.readOne(id));

                return modelAndView(viewObjects, "layout.ftl");
            }
        });

        post(new Route("/article/update/:id") {
            @Override
            public Object handle(Request request, Response response) {
                Integer id      = Integer.parseInt(request.queryParams("article-id"));
                String title    = request.queryParams("article-title");
                String summary  = request.queryParams("article-summary");
                String content  = request.queryParams("article-content");

                articleDbService.update(id, title, summary, content);

                response.status(200);
                response.redirect("/");
                return "";
            }
        });

        get(new Route("/article/delete/:id") {
            @Override
            public Object handle(Request request, Response response) {
                Integer id = Integer.parseInt(request.params(":id"));

                articleDbService.delete(id);

                response.status(200);
                response.redirect("/");
                return "";
            }
        });
    }
}