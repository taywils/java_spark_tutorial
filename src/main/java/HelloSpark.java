import static spark.Spark.*;
import spark.*;

import java.util.ArrayList;

public class HelloSpark {
    // Just store POST data within a ArrayList for now
    public static ArrayList<String> things = new ArrayList<String>();

    public static void main(String[] args) {
        get(new Route("/list") {
            @Override
            public Object handle(Request request, Response response) {
                StringBuilder html = new StringBuilder();

                if (HelloSpark.things.isEmpty()) {
                    html.append("<b>Try adding some things to your list</b>");
                } else {
                    html.append("<ul>");
                    for (String thing : HelloSpark.things) {
                        html.append("<li>").append(thing).append("</p>");
                    }
                    html.append("</ul>");
                }

                return html.toString();
            }
        });

        post(new Route("/add/:item") {
            @Override
            public Object handle(Request request, Response response) {
                HelloSpark.things.add(request.params(":item"));
                response.status(200);
                return response;
            }
        });
    }
}