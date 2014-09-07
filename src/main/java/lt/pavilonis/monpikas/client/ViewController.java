package lt.pavilonis.monpikas.client;

import com.google.common.collect.EvictingQueue;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import lt.pavilonis.monpikas.client.model.Card;
import lt.pavilonis.monpikas.client.model.CardBig;
import lt.pavilonis.monpikas.client.model.CardSmall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static lt.pavilonis.monpikas.client.App.root;
import static lt.pavilonis.monpikas.client.App.stage;

@Controller
public class ViewController {

   @Autowired
   private CardBig first;
   @Autowired
   private CardSmall second;
   @Autowired
   private CardSmall third;
   @Autowired
   private CardSmall forth;
   @Autowired
   private CardSmall fifth;
   @Autowired
   private BarcodeScannerInputHandler inputHandler;

   private EvictingQueue<User> users = EvictingQueue.create(5);
   private List<Card> cards = Arrays.asList(first, second, third, forth, fifth);

   @PostConstruct
   public void initialize() {

      first.initialize();
      first.updateUserInfo(new User("Ivanas Ivanauskas Ivanovicius", true, LocalDateTime.now()));

      second.initialize();
      second.updateUserInfo(new User("Ivanas Ėčęą„ūųšė Ivanovičius", true, LocalDateTime.now()));

      root.getChildren().addAll(second, first);
      stage.addEventHandler(KeyEvent.KEY_TYPED, inputHandler);

      //test event handling
      stage.addEventHandler(KeyEvent.KEY_TYPED, (KeyEvent k) -> {
         if (k.getCharacter().equals("a")) {
            first.updateUserInfo(new User("Stepanas Stepanovicius", true, LocalDateTime.now()));
            System.out.println("got a");
         }
         if (k.getCharacter().equals("b")) {
            first.updateUserInfo(new User("Pranas Pranavicius", false, LocalDateTime.now()));
            System.out.println("got b");
         }
         if (k.getCharacter().equals("c")) {
            first.updateUserInfo(new User("Vovanas Vovanovicius", true, LocalDateTime.now().minusDays(3)));
            System.out.println("got c");
         }
         if (k.getCharacter().equals("d")) {
            second.updateUserInfo(new User("Bronius Bronevicius", true, LocalDateTime.now().minusDays(3)));
            System.out.println("got d");
         }
      });
   }

   public void requestUser(List<Integer> bcNumber) {
      System.out.println("Requesting user with bc: "+bcNumber);
   }
}
