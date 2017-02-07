package lt.pavilonis.scan.monpikas.client;

import com.google.common.collect.EvictingQueue;
import javafx.scene.input.KeyEvent;
import lt.pavilonis.scan.monpikas.client.dto.User;
import lt.pavilonis.scan.monpikas.client.model.Card;
import lt.pavilonis.scan.monpikas.client.model.CardBig;
import lt.pavilonis.scan.monpikas.client.model.CardSmall;
import lt.pavilonis.scan.service.ScannerReadEventObserver;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.reverse;
import static lt.pavilonis.scan.monpikas.client.App.root;
import static lt.pavilonis.scan.monpikas.client.App.stage;
import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class ViewController extends ScannerReadEventObserver {

   private static final Logger LOG = getLogger(ViewController.class.getSimpleName());

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
   private UserRequestService userRequestService;

   private double y = 20;

   private EvictingQueue<ResponseEntity<User>> responses = EvictingQueue.create(5);

   private List<Card> cards;

   @PostConstruct
   public void initialize() {
      fifth.setNext(forth);
      forth.setNext(third);
      third.setNext(second);
      second.setNext(first);
      cards = Arrays.asList(first, second, third, forth, fifth);
      cards.forEach(card -> {
         card.initialize();
         if (cards.indexOf(card) != 0) {
            card.setLayoutY(y);
            y += 220;
         }
      });
      root.getChildren().addAll(cards);

      //test event handling
      stage.addEventHandler(KeyEvent.KEY_TYPED, (KeyEvent k) -> {
         if (k.getCharacter().equals("a")) consumeScannerInput("DE0C776C");
         if (k.getCharacter().equals("b")) consumeScannerInput("6769");
      });
   }

   @Override
   protected void consumeScannerInput(String cardCode) {

      cardCode = cardCode + "000000";
      LOG.info("Requesting user with cardCode: " + cardCode);

      try {
         responses.add(
               userRequestService.requestUser(cardCode)
         );
      } catch (ResourceAccessException e) {
         LOG.error("no connection to server: " + e);
         responses.add(
               new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
         );
      } catch (HttpStatusCodeException e) {
         LOG.error("Unknown error: " + e);
      }

      int i = 0;
      //update cards content
      for (ResponseEntity<User> response : reverse(new ArrayList<>(responses))) {
         cards.get(i++).setResponse(response);
      }
      //start visual update sequence
      fifth.update();
   }
}
