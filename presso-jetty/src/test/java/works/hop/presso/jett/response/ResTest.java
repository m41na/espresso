package works.hop.presso.jett.response;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.junit.jupiter.api.Test;
import works.hop.presso.api.application.IApplication;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ResTest {

    @Test
    void attachment_without_filename_will_set_one_headers() {
        Response response = mock(Response.class);
        Res res = new Res(mock(IApplication.class), mock(Request.class), response);
        res.attachment();
        verify(response, times(1)).setHeader(anyString(), endsWith("attachment"));
    }

    @Test
    void attachment_with_filename_will_set_two_headers() {
        Response response = mock(Response.class);
        Res res = new Res(mock(IApplication.class), mock(Request.class), response);
        res.attachment("logo.png");
        verify(response, times(1)).setHeader(anyString(), endsWith("\"logo.png\""));
        verify(response, times(1)).setHeader(anyString(), endsWith("image/png"));
    }
}