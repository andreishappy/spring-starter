package app.TestFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Configuration
public class HttpClientConfiguration {
    @Autowired
    Environment env;

    @Bean(name = "apiHttpClient")
    public Client apiHttpClient() {
        return ClientBuilder.newClient();
    }
}
