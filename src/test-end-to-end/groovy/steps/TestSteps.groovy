package steps

import helpers.MockAPIHelper
import helpers.RestTemplateHelper
import helpers.SetupHelper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

ResponseEntity<Map> lastResponse

Before() {
    SetupHelper.startApplicationIfNecessary()
}

After() {
    lastResponse = null
    SetupHelper.cleanup()
}

Given(~"the api returns hello") { ->
    MockAPIHelper.stubApi("/test", "GET", "hello.json", HttpStatus.OK)
}

When(~"I make a request for /test") { ->
    lastResponse = RestTemplateHelper.getWithSuppressedExceptions("http://localhost:8080/test", String.class)
}

Then(~"the request says hello") { ->
    lastResponse.body == "hello"
}
