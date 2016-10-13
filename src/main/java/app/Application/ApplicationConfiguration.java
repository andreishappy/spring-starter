package app.Application;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;

@Configuration
@Slf4j
public class ApplicationConfiguration {

    @Value("${server.minThreads}")
    private Integer minThreads;

    @Value("${server.maxThreads}")
    private Integer maxThreads;

    @Value("${server.threadIdleTimeoutMillis}")
    private Integer threadIdleTimeoutMillis;

    @Value("${server.queueCapacity}")
    private Integer queueCapacity;

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() {
        JettyEmbeddedServletContainerFactory jettyContainer = new JettyEmbeddedServletContainerFactory();

        ArrayBlockingQueue<Runnable> requestQueue = new ArrayBlockingQueue<>(queueCapacity);
        QueuedThreadPool threadPool = new QueuedThreadPool(minThreads, maxThreads, threadIdleTimeoutMillis, requestQueue);

        jettyContainer.setThreadPool(threadPool);

        log.info("Jetty Embedded Server - minThreads: {}, maxThreads: {}, threadIdleTimeoutMillis: {}, queueCapacity: {}", minThreads, maxThreads, threadIdleTimeoutMillis, queueCapacity);

        return jettyContainer;
    }
}
