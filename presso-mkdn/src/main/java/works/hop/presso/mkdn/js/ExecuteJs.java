package works.hop.presso.mkdn.js;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

public class ExecuteJs {

    final Context ctx;

    public ExecuteJs(File source) {
        this.ctx = Context.newBuilder("js")
                .allowAllAccess(true)
                .build();
        try {
            this.ctx.eval(Source.newBuilder("js", source).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ExecuteJs(Reader source, String name) {
        this.ctx = Context.create("js");
        try {
            this.ctx.eval(Source.newBuilder("js", source, name).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Value eval(String functionName, Function<Value, Value> function) {
        Value callable = this.ctx.getBindings("js").getMember(functionName);
        return function.apply(callable);
    }
}
