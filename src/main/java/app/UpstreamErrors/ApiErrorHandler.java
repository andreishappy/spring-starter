package app.UpstreamErrors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rx.Observable;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Component
@Slf4j
public class ApiErrorHandler {
    public Observable<String> error(Throwable e) {
        if (e.getCause() instanceof WebApplicationException) {
            WebApplicationException webApplicationException = (WebApplicationException) e.getCause();
            return errorFromResponse(webApplicationException.getResponse());
        }
        return Observable.error(e);
    }

    private Observable<String> errorFromResponse(Response response) {
        int status = response.getStatus();
        if (status == 401) {
            return Observable.error(new UpstreamAuthenticationFailure("Failed to authenticate upstream"));
        }

        log.error("Upstream request failed with status " + status);
        return Observable.error(new UpstreamRequestFailure("Upstream request failed: " + status));
    }
}
