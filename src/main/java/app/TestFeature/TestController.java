package app.TestFeature;

import app.UpstreamErrors.ApiErrorHandler;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import rx.Observable;
import rx.Single;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class TestController {
    private Client client;
    private ApiErrorHandler apiErrorHandler;
    private String url;

    @Autowired
    public TestController(@Qualifier("apiHttpClient") Client client,
                          ApiErrorHandler apiErrorHandler,
                          @Value("url") String url) {
        this.client = client;
        this.apiErrorHandler = apiErrorHandler;
        this.url = url;
    }

    @RequestMapping("/test")
    public Single<String> article() {
        return getStringResponse(url).toSingle();
    }

    private Observable<String> getStringResponse(String url) {
        WebTarget webTarget = client.target(url);
        return RxObservable.from(webTarget).request()
                .rx()
                .get(String.class)
                .onErrorResumeNext(e -> apiErrorHandler.error(e));
    }
}
