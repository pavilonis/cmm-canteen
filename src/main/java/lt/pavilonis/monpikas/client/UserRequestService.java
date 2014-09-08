package lt.pavilonis.monpikas.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserRequestService {

   @Autowired
   private RestTemplate rt;

   public String SaySomething() {
      return "I sit on a table";
   }
}
