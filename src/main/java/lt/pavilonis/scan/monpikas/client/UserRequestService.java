package lt.pavilonis.scan.monpikas.client;

import lt.pavilonis.scan.monpikas.client.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static java.util.Collections.singletonList;

@Service
public class UserRequestService {

   @Value(("${Rest.pupilRequestUrl}"))
   private String pupilRequestUrl;

   @Value(("${Rest.Auth.Username}"))
   private String username;

   @Value(("${Rest.Auth.Password}"))
   private String password;

   @Autowired
   private RestTemplate rt;

   @PostConstruct
   private void setErrorHandler() {
      rt.setErrorHandler(new CustomResponseErrorHandler());
   }

   public ResponseEntity<User> requestUser(String barcode) {

      String creds = username + ":" + password;
      byte[] base64credsBytes = Base64.getEncoder().encode(creds.getBytes());

      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Basic " + new String(base64credsBytes));
      headers.setAccept(singletonList(MediaType.APPLICATION_JSON));

      String url = pupilRequestUrl + barcode;
      HttpEntity<String> request = new HttpEntity<>(headers);
      rt.setMessageConverters(getMessageConverters());
      return rt.exchange(url, HttpMethod.GET, request, User.class);
   }

   private List<HttpMessageConverter<?>> getMessageConverters() {
      List<HttpMessageConverter<?>> converters = new ArrayList<>();
      converters.add(new MappingJackson2HttpMessageConverter());
      return converters;
   }
}
