package lt.pavilonis.monpikas.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan({"lt.pavilonis.monpikas.client", "resources"})
@PropertySource({"app.properties"})
public class AppConfig {

   @Bean
   public FirstService firstService() {
      return new FirstService();
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

//   @Bean
//   public static ReloadableResourceBundleMessageSource getReloadableResourceBundleMessageSource() {
//      ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
//      source.setDefaultEncoding("UTF-8");
//      return source;
//   }
}
