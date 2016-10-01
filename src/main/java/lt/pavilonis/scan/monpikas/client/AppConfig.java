package lt.pavilonis.scan.monpikas.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan({"lt.pavilonis.scan", "resources"})
@PropertySource({"inner.properties", "file:/opt/monpikas/monpikas.properties"})
public class AppConfig {

   @Bean
   public UserRequestService firstService() {
      return new UserRequestService();
   }

   @Bean
   public RestTemplate getRestTemplate() {
      return new RestTemplate();
   }

   @Bean
   public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
      PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
      conf.setFileEncoding("UTF-8");
      return conf;
   }
}
