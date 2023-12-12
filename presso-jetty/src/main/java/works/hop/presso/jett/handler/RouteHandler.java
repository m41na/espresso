package works.hop.presso.jett.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.middleware.IErrorHandler;
import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.request.ReqMethod;
import works.hop.presso.api.routeable.IMatched;
import works.hop.presso.api.routeable.IRoutable;
import works.hop.presso.jett.application.Application;
import works.hop.presso.jett.application.PathUtils;
import works.hop.presso.jett.request.Req;
import works.hop.presso.jett.response.Res;
import works.hop.presso.jett.routable.HandleNext;
import works.hop.presso.jett.routable.MatchedInfo;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RouteHandler extends AbstractHandler {

    final IRoutable routable;

    private static void invokeHandler(IApplication app, String target, Request baseRequest, IMatched matchedInfo) {
        if (matchedInfo.getHandlers() != null) {
            HandleNext next = new HandleNext();
            Req req = new Req(app, baseRequest, matchedInfo.getParams(), PathUtils.extractQueryVariables(baseRequest.getQueryParameters()));
            Res res = new Res(app, baseRequest, baseRequest.getResponse());

            //invoke param callbacks
            for (Map.Entry<String, String> entry : matchedInfo.getParams().entrySet()) {
                if (((Application) app).getPathParamCallbacks().containsKey(entry.getKey())) {
                    ((Application) app).getPathParamCallbacks().get(entry.getKey()).handle(req, res, next, entry.getValue());
                }
            }

            //invoke request handlers
            for (IMiddleware handler : matchedInfo.getHandlers()) {

                //invoke handler function
                try {
                    handler.handle(req, res, next);
                } catch (Exception e) {
                    next.error(null, e);
                }

                //handle error if any
                if (next.hasError()) {
                    handleRequestException((Application) app, next.getError(), req, res, next);
                    break;
                }
            }
        } else {
            Exception error = new RuntimeException(String.format("No handlers for '%s' request were found", target));
            HandleNext next = new HandleNext();
            next.error(null, error);
            Req req = new Req(app, baseRequest, matchedInfo.getParams(), PathUtils.extractQueryVariables(baseRequest.getQueryParameters()));
            Res res = new Res(app, baseRequest, baseRequest.getResponse());

            handleRequestException((Application) app, error, req, res, next);
        }
    }

    private static void handleRequestException(Application app, Exception error, Req req, Res res, HandleNext next) {
        for (IErrorHandler err : app.getErrorHandlers()) {
            if (err.isHandled()) {
                break;
            }
            err.handle(error, req, res, next);
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        //search for handler in the app first, then in the sub-apps if need be
        try {
            if (this.routable == null || !this.routable.canRoute()) {
                Res res = new Res(null, baseRequest, baseRequest.getResponse());
                res.end("No routing information is available", Charset.defaultCharset().name());
                return;
            }

            //Can this app be used to handle request?
            ReqMethod method = ReqMethod.valueOf(request.getMethod().toUpperCase());
            IMatched matchedInfo = this.routable.select(method, target);
            if (matchedInfo.getHandlers() != null) {
                invokeHandler((IApplication) this.routable, target, baseRequest, matchedInfo);
            } else {
                //Is there a sub-app that can be used instead?
                String fullTarget = String.format("/%s/%s", baseRequest.getContextPath(), target).replaceAll("/+", "/");
                IMatched matchedSub = lookupSubApp((Application) this.routable, method, fullTarget);

                if (matchedSub.getHandlers() != null) {
                    invokeHandler((IApplication) routable, target, baseRequest, matchedSub);
                } else {
                    Res res = new Res(null, baseRequest, baseRequest.getResponse());
                    res.end(String.format("No configured route handler for %s", target), Charset.defaultCharset().name());
                }
            }
        } finally {
            baseRequest.setHandled(true);
        }
    }

    private IMatched lookupSubApp(Application root, ReqMethod method, String target) {
        // look up sub-app having "/" before looking other sub-apps
        for (Map.Entry<String, IApplication> entry : root.getSubApplications().entrySet()) {
            Application application = (Application) entry.getValue();
            if (entry.getKey().equals("/")) {
                IMatched matched = application.select(method, target);
                if (matched.getHandlers() != null) {
                    return matched;
                } else {
                    //depth-first search on this child
                    IMatched matchedSub = lookupSubApp(application, method, target);
                    if (matchedSub.getHandlers() != null) {
                        return matchedSub;
                    }
                }
            } else {
                String fullPath = String.format("/%s/%s", root.getBasePath(), entry.getKey())
                        .replaceAll("null", "")
                        .replaceAll("/+", "/");
                String regex = PathUtils.pathToRegex(fullPath);
                if (Pattern.matches(regex, target)) {
                    //depth-first-search on the matched siblings
                    IMatched matched = lookupSubApp(application, method, target);
                    if (matched.getHandlers() != null) {
                        return matched;
                    }
                }
            }
        }
        return new MatchedInfo();
    }
}
