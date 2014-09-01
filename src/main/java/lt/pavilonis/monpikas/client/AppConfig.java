package lt.pavilonis.monpikas.client;

import lt.pavilonis.monpikas.client.services.FirstService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan({"lt.pavilonis.monpikas.client.services"})
public class AppConfig {

   @Bean
   public FirstService firstService() {
      return new FirstService();
   }

   @Bean
   public RestTemplate getRestTemplate() {
      return new RestTemplate();
   }
}
