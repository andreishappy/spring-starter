package app.UpstreamErrors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_GATEWAY, reason="Upstream API error")
public class UpstreamRequestFailure extends RuntimeException {
    public UpstreamRequestFailure(String message) {
        super(message);
    }
}
