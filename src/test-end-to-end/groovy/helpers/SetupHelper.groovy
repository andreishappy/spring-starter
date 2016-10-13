package helpers

import com.netflix.hystrix.Hystrix
import app.Application.Application
import org.springframework.context.ApplicationContext
import org.springframework.core.env.AbstractEnvironment

class SetupHelper {
    static boolean alreadyStarted = false;

    static def startApplicationIfNecessary() {
        if (!alreadyStarted) {
            if (!System.getProperties().containsKey(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME)) {
                System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "local")
            }
            System.setProperty("jsonlog", "false")
            ApplicationContext context = Application.mainThatReturnsContext(new String[0])
            alreadyStarted = true
            cleanup();
        }
    }

    static def cleanup() {
        MockAPIHelper.resetApi()
        resetHystrix()
    }

    static def resetHystrix() {
        Hystrix.reset()
    }
}
