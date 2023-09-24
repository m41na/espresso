package works.hop.presso.jett.application;

import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.INext;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

import static works.hop.presso.jett.Espresso.express;

public class Ex5AppAllHandler {

    public static void main(String[] args) {
        var requireAuthentication = new IMiddleware() {

            @Override
            public void handle(IRequest req, IResponse res, INext next) {
                System.out.println("Authenticating user"); // Authenticating user
                next.ok();
            }
        };

        var loadUser = new IMiddleware() {

            @Override
            public void handle(IRequest req, IResponse res, INext next) {
                System.out.println("Loading user"); // Loading user
                next.ok();
            }
        };

        var app = express();

//        app.all("/*", requireAuthentication, loadUser);
        app.all("/*", requireAuthentication);
        app.all("/*", loadUser);

        app.get("/", (req, res, next) -> {
            res.send("All handler Homepage");
        });

        app.listen(3000);
    }
}
