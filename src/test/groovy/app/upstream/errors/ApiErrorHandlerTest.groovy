package app.upstream.errors

import rx.observers.TestSubscriber
import spock.lang.Specification

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response

class ApiErrorHandlerTest extends Specification {
    ApiErrorHandler errorHandlerUnderTest = new ApiErrorHandler()
    Response response

    def setup() {
        response = Stub()
    }

    def "upstream authentication error if cause is a 401"() {
        given:
            TestSubscriber<String> testSubscriber = new TestSubscriber<>()
            response.getStatus() >> 401
            Exception exception = new Exception(new WebApplicationException(response));
        when:
            errorHandlerUnderTest.error(exception).subscribe(testSubscriber)
        then:
            testSubscriber.assertError(UpstreamAuthenticationFailure)
    }

    def "upstream request failed if cause is WebApplicationException"() {
        given:
            TestSubscriber<String> testSubscriber = new TestSubscriber<>()
            response.getStatus() >> status
            Exception exception = new Exception(new WebApplicationException(response));
        when:
            errorHandlerUnderTest.error(exception).subscribe(testSubscriber)
        then:
            testSubscriber.assertError(UpstreamRequestFailure)
        where:
            status | _
            402    | _
            499    | _
            500    | _
            599    | _
    }

    def "returns exception if its not a WebApplicationException"() {
        given:
            TestSubscriber<String> testSubscriber = new TestSubscriber<>()
            Exception exception = new Exception()
        when:
            errorHandlerUnderTest.error(exception).subscribe(testSubscriber)
        then:
            testSubscriber.assertError(Exception)
    }

}

