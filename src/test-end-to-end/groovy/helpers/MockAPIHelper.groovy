package helpers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class MockAPIHelper {

    private static def apiBaseUrl = "http://localhost:3000"

    static def resetApi() {
        reset(apiBaseUrl)
    }

    static def stubApi(String endpoint, String method, HttpStatus status) {
        stubApi(endpoint, method, null, status)
    }

    static def stubApi(String endpoint, String method, String responseFile, HttpStatus status) {
        stub(apiBaseUrl, endpoint, null, null, method, responseFile, status);
    }

    private static def reset(String host) {
        def template = new RestTemplate();
        return template.postForEntity("${host}/configure/clean", null, String.class);
    }

    private static def stub(String host, String endpoint, String query, Object body, String method, String responseFile, HttpStatus status) {
        def template = new RestTemplate();

        def configurationBody = [
                "request" : [
                        "path"  : endpoint,
                        "method": method,
                        "query": query,
                        "body": body
                ]
                ,
                "response": [
                        "file"  : responseFile,
                        "status": status.value()
                ]
        ]

        ResponseEntity<String> response = template.postForEntity("${host}/configure", configurationBody, String.class)
        return response
    }

    private static List getCalls(host) {
        def template = new RestTemplate();
        def response = template.getForEntity("${host}/configure/get-calls", List.class)
        response.body
    }
}
