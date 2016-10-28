package app.application;

import com.netflix.hystrix.strategy.HystrixPlugins;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.HealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.repository.configuration.RedisRepositoryConfigurationExtension;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, HealthIndicatorAutoConfiguration.RedisHealthIndicatorConfiguration.class, RedisRepositoryConfigurationExtension.class})
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) {
        mainThatReturnsContext(args);
    }

    public static ApplicationContext mainThatReturnsContext(String [] args) {
        HystrixPlugins.getInstance().registerCommandExecutionHook(new CustomHystrixCommandExecutionHook());

        if (System.getProperty("spring.profiles.active") == null) {
            throw new MissingConfigurationProperty("spring.profiles.active should be set");
        }

        return SpringApplication.run(Application.class, args);
    }

}
