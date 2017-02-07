package lt.pavilonis.scan.monpikas.client;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

@Configuration
@ComponentScan({"lt.pavilonis.scan", "resources"})
@PropertySource({"inner.properties", "file:/opt/monpikas/monpikas.properties"})
public class App extends Application {

   static final Group root = new Group();
   static Stage stage;

   public static void main(String[] args) {
      launch(args);
   }

   @Override
   public void start(Stage stage) throws Exception {
      App.stage = stage;
      new AnnotationConfigApplicationContext(getClass());
      stage.setScene(new Scene(root, 1440, 900, Color.WHITE));
      stage.show();
   }

   @Value("${Rest.Auth.Username}")
   private String username;

   @Value("${Rest.Auth.Password}")
   private String password;

   @Bean
   public UserRequestService userRequestService() {
      return new UserRequestService();
   }

   @Bean
   public RestTemplate getRestTemplate() {
      RestTemplate rest = new RestTemplate();
      rest.setInterceptors(Collections.singletonList(authenticatingInterceptor()));
      rest.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
      rest.setErrorHandler(new CustomResponseErrorHandler());
      return rest;
   }

   @Bean
   public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
      PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
      conf.setFileEncoding("UTF-8");
      return conf;
   }

   private ClientHttpRequestInterceptor authenticatingInterceptor() {
      return (request, body, execution) -> {
         String creds = username + ":" + password;
         byte[] base64credBytes = Base64.getEncoder().encode(creds.getBytes());

         HttpHeaders headers = request.getHeaders();
         headers.add("Authorization", "Basic " + new String(base64credBytes));
         headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
         return execution.execute(request, body);
      };
   }
}
