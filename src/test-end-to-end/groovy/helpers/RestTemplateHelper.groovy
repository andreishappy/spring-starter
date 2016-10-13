package helpers

import org.springframework.boot.test.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate

class RestTemplateHelper {

    static RestTemplate template() {
        RestTemplate template = new TestRestTemplate();
        template.setErrorHandler(new DefaultResponseErrorHandler(){
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            }});
        template
    }

    static <T> ResponseEntity<T> getWithSuppressedExceptions(String url, Class<T> clas) {
        return template().getForEntity(url, clas)
    }

    static <T> ResponseEntity<T> postWithSuppressedExceptions(String url, Class<T> clas, Object body) {
        return template().postForEntity(url, body, clas)
    }
}
