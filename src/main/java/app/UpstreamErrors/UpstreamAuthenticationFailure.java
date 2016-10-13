package app.UpstreamErrors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_GATEWAY, reason="Could not authenticate upstream")
public class UpstreamAuthenticationFailure extends RuntimeException {
    public UpstreamAuthenticationFailure(String message) {
        super(message);
    }
}
