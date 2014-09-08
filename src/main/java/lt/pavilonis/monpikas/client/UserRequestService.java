package lt.pavilonis.monpikas.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class UserRequestService {

   @Value(("${Rest.GetUser}"))
   private String userUrl;

   @Autowired
   private RestTemplate rt;

   public User requestUser(String barcode) {
      return rt.getForObject(userUrl + "{user}", User.class, barcode);
   }

   public User fakeRequest(String barcode) {
      if (barcode.equals("0001")) return new User("Stepanas Stepanovicius", true, LocalDateTime.now());
      if (barcode.equals("0002")) return new User("Pranas Pranavicius", false, LocalDateTime.now());
      if (barcode.equals("0003")) return new User("Vovanas Vovanovicius", true, LocalDateTime.now().minusDays(3));
      if (barcode.equals("0004")) return new User("Bronius Bronevicius", true, LocalDateTime.now().minusDays(3));
      return null;
   }
}
