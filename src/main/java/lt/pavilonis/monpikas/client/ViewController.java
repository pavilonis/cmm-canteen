package lt.pavilonis.monpikas.client;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import lt.pavilonis.monpikas.client.model.Card;
import lt.pavilonis.monpikas.client.model.CardBig;
import lt.pavilonis.monpikas.client.model.CardSmall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
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
   @Autowired
   private UserRequestService userRequestService;
   private double y = 20;
   private int i;

   private EvictingQueue<User> users = EvictingQueue.create(5);
   private List<Card> cards;
   private List<Transition> transitions = new ArrayList<>();

   @PostConstruct
   public void initialize() {
      cards = asList(first, second, third, forth, fifth);
      cards.forEach(c -> {
         c.initialize();
         transitions.addAll(c.getTransitions());
         if (cards.indexOf(c) != 0) {
            c.setLayoutY(y);
            y += 220;
         }
      });
      root.getChildren().addAll(cards);
      stage.addEventHandler(KeyEvent.KEY_TYPED, inputHandler);
      stage.addEventHandler(InputEvent.ANY, System.out::println);

      //test event handling
      stage.addEventHandler(KeyEvent.KEY_TYPED, (KeyEvent k) -> {
         if (k.getCharacter().equals("a")) scanEventAction("0001");
         if (k.getCharacter().equals("b")) scanEventAction("0002");
         if (k.getCharacter().equals("c")) scanEventAction("0003");
         if (k.getCharacter().equals("d")) scanEventAction("0004");
      });
   }

   public void scanEventAction(String barcode) {
      if (transitionActive()) {
         System.out.println("Transition is active, canceled request for bc: " + barcode);
         return;
      }
      System.out.println("Requesting user with bc: " + barcode);
      //userRequestService.requestUser(barcode);
      users.add(userRequestService.fakeRequest(barcode));
      updateView();
   }

   public void updateView() {
      i = 0;
      Lists.reverse(new ArrayList<>(users)).forEach(u -> cards.get(i++).updateUserInfo(u));
   }

   public boolean transitionActive() {
      for (Transition t : transitions) {
         if (t.getStatus().equals(Animation.Status.RUNNING))
            return true;
      }
      return false;
   }
}
