package lt.pavilonis.scan.monpikas.client;

import lt.pavilonis.scan.monpikas.client.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;

@Service
public class UserRequestService {

   @Value("${rest.pupilRequestUrl}")
   private String pupilRequestUrl;

   @Autowired
   private RestTemplate rt;

   public ResponseEntity<User> requestUser(String barcode) {

      URI uri = UriComponentsBuilder.fromUriString(pupilRequestUrl)
            .pathSegment(barcode)
            .build()
            .toUri();

      return rt.getForEntity(uri, User.class);
   }
}
