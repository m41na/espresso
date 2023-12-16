package works.hop.presso.mkdn.js;

import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ExecuteJsTest {

    @Test
    void evaluate_printing_javascript_date_in_java() {
        Reader sr = new StringReader("function printDate(date) { console.log(date.toString()) }");
        ExecuteJs js = new ExecuteJs(sr, "date_reader");
        var val = js.eval("printDate", func -> func.execute(new Date()));
        System.out.println("eval result: " + val);
    }
}