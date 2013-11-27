import static spark.Spark.*;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerRoute;

import java.util.*;

public class HelloSpark {
    public static Deque<Article> articles = new ArrayDeque<Article>();

    public static void main(String[] args) {
        get(new FreeMarkerRoute("/") {
            @Override
            public ModelAndView handle(Request request, Response response) {
                Map<String, Object> viewObjects = new HashMap<String, Object>();

                if(HelloSpark.articles.isEmpty()) {
                    viewObjects.put("hasNoArticles", "Welcome, please click \"Write Article\" to begin.");
                } else {
                    ArrayList<Article> showArticles = new ArrayList<Article>();

                    for(Article article : HelloSpark.articles) {
                        if(article.readable()) {
                            showArticles.add(article);
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

                Article article = new Article(title, summary, content, HelloSpark.articles.size() + 1);

                HelloSpark.articles.addFirst(article);

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

                for(Article article : HelloSpark.articles) {
                    if(id.equals(article.getId())) {
                        viewObjects.put("article", article);
                        break;
                    }
                }

                return modelAndView(viewObjects, "layout.ftl");
            }
        });

        get(new FreeMarkerRoute("/article/update/:id") {
            @Override
            public Object handle(Request request, Response response) {
                Integer id = Integer.parseInt(request.params(":id"));
                Map<String, Object> viewObjects = new HashMap<String, Object>();

                viewObjects.put("templateName", "articleForm.ftl");

                for(Article article : HelloSpark.articles) {
                    if(id.equals(article.getId())) {
                        viewObjects.put("article", article);
                        break;
                    }
                }

                return modelAndView(viewObjects, "layout.ftl");
            }
        });

        post(new Route("/article/update/:id") {
            @Override
            public Object handle(Request request, Response response) {
                String title    = request.queryParams("article-title");
                String summary  = request.queryParams("article-summary");
                String content  = request.queryParams("article-content");
                Integer id      = Integer.parseInt(request.queryParams("article-id"));

                for(Article article : HelloSpark.articles) {
                    if(id.equals(article.getId())) {
                        article.setTitle(title);
                        article.setContent(content);
                        article.setSummary(summary);
                    }
                }

                response.status(200);
                response.redirect("/");
                return "";
            }
        });

        get(new Route("/article/delete/:id") {
            @Override
            public Object handle(Request request, Response response) {
                Integer id = Integer.parseInt(request.params(":id"));

                for(Article article : HelloSpark.articles) {
                    if(id.equals(article.getId())) {
                        article.delete();
                    }
                }

                response.status(200);
                response.redirect("/");
                return "";
            }
        });
    }
}