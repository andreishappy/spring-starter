package app.Application;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.HystrixObservableCommand;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomHystrixCommandExecutionHook extends HystrixCommandExecutionHook {

    @Override
    public <T> void onFallbackStart(HystrixInvokable<T> commandInstance) {
        HystrixObservableCommand hystrixCommand = (HystrixObservableCommand) commandInstance;
        if (hystrixCommand.isFailedExecution()) {
            log.error("Fallback due to Exception:", hystrixCommand.getFailedExecutionException());
        } else {
            boolean threadPoolRejected = hystrixCommand.isResponseThreadPoolRejected();
            boolean timedOut = hystrixCommand.isResponseTimedOut();
            String group = hystrixCommand.getCommandGroup().name();
            log.error("Fallback due to Hystrix health. threadPoolRejected: {} responseTimedOut: {} group: {}", threadPoolRejected, timedOut, group);
        }
    }
}
