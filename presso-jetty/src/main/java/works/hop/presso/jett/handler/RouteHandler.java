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

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;
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
            IMatched matchedInfo = this.routable.select(ReqMethod.valueOf(request.getMethod().toUpperCase()), target);
            if (matchedInfo.getHandlers() != null) {
                invokeHandler((IApplication) this.routable, target, baseRequest, matchedInfo);
            } else {
                //Is there a sub-app which can be used instead?
                String fullTarget = String.format("/%s/%s", baseRequest.getContextPath(), target).replaceAll("/+", "/");
                Optional<IApplication> subAppExists = lookupMatch((Application) this.routable, fullTarget);

                if (subAppExists.isPresent()) {
                    IRoutable routable = (IRoutable) subAppExists.get();
                    IMatched matchedSub = routable.select(ReqMethod.valueOf(request.getMethod().toUpperCase()), "/");
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

    private Optional<IApplication> lookupMatch(Application root, String target) {
        Optional<IApplication> foundOptional = root.getSubApplications().entrySet().stream()
                .filter(entry -> {
                            String fullPath = String.format("/%s/%s", root.getBasePath(), entry.getKey()).replaceAll("null", "").replaceAll("/+", "/");
                            String regex = PathUtils.pathToRegex(fullPath);
                            return Pattern.matches(regex, target);
                        }
                )
                .map(Map.Entry::getValue)
                .findFirst();

        if (foundOptional.isEmpty()) {
            for (IApplication subApp : root.getSubApplications().values()) {
                Optional<IApplication> subFound = lookupMatch((Application) subApp, target);
                if (subFound.isPresent()) {
                    return subFound;
                }
            }
        }
        return foundOptional;
    }
}
