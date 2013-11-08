import static spark.Spark.*;
import spark.*;

public class HelloSpark {
    public static void main(String[] args) {
        get(new Route("/hello") {
            @Override
            public Object handle(Request request, Response response) {
                return "Hello Spark MVC Framework!";
            }
        });

        get(new Route("/goodbye") {
            @Override
            public Object handle(Request request, Response response) {
                return "Goodbye Spark MVC Framework!";
            }
        });

        get(new Route("/parameter/:param") {
            @Override
            public Object handle(Request request, Response response) {
                StringBuffer myParam = new StringBuffer(request.params(":param"));
                return "I reversed your param for ya \"" + myParam.reverse() + "\"";
            }
        });
    }
}