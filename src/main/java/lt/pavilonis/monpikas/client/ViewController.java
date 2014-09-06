package lt.pavilonis.monpikas.client;

import com.google.common.collect.EvictingQueue;
import org.springframework.stereotype.Controller;

@Controller
public class ViewController {

   private EvictingQueue<User> users = EvictingQueue.create(5);

   //private List<Card> cards = Arrays.asList(new UserInfoBig())
}
