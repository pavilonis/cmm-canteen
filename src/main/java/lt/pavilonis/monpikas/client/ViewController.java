package lt.pavilonis.monpikas.client;

import com.google.common.collect.EvictingQueue;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import lt.pavilonis.monpikas.client.dto.ClientPupilDto;
import lt.pavilonis.monpikas.client.model.Card;
import lt.pavilonis.monpikas.client.model.CardBig;
import lt.pavilonis.monpikas.client.model.CardSmall;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.PostConstruct;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.reverse;
import static java.lang.Runtime.getRuntime;
import static java.util.Arrays.asList;
import static lt.pavilonis.monpikas.client.App.root;
import static lt.pavilonis.monpikas.client.App.stage;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.ALREADY_REPORTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Controller
public class ViewController {

   private static final Logger LOG = getLogger(ViewController.class.getSimpleName());

   @Value("${Images.PhotoBasePath}")
   private String PHOTO_BASE_PATH;

   @Value("${Images.Extension}")
   private String IMAGE_EXTENSION;

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

   private EvictingQueue<ResponseEntity<ClientPupilDto>> responses = EvictingQueue.create(5);

   private List<Card> cards;
   private List<Transition> transitions = new ArrayList<>();

   @PostConstruct
   public void initialize() {
      fifth.setNext(forth);
      forth.setNext(third);
      third.setNext(second);
      second.setNext(first);
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

      //test event handling
      stage.addEventHandler(KeyEvent.KEY_TYPED, (KeyEvent k) -> {
         if (k.getCharacter().equals("a")) scanEventAction("5002");
         if (k.getCharacter().equals("b")) scanEventAction("6769");
         if (k.getCharacter().equals("c")) scanEventAction("5016");
         if (k.getCharacter().equals("d")) scanEventAction("5027");
         if (k.getCharacter().equals("e")) scanEventAction("5017");
      });
   }

   public void scanEventAction(String barcode) {

      if (transitionActive()) {
         LOG.info("Transition is active, canceled request for bc: " + barcode);
         return;
      }

      LOG.info("Requesting user with barcode: " + barcode);

      ResponseEntity<ClientPupilDto> response = new ResponseEntity<>(OK);
      try {
         response = userRequestService.requestUser(barcode);
      } catch (ResourceAccessException e) {
         LOG.error("no connection to server: " + e);
         response = new ResponseEntity<>(SERVICE_UNAVAILABLE);
      } catch (HttpStatusCodeException e) {
         LOG.error("Unknown error: " + e);
      }

      responses.add(response);
      setDtoImage(response);

      int i = 0;
      //update cards content
      for (ResponseEntity<ClientPupilDto> r : reverse(new ArrayList<>(responses))) {
         cards.get(i++).setResponse(r);
      }
      //start visual update sequence
      fifth.update();
   }

   public boolean transitionActive() {
      for (Transition t : transitions) {
         if (t.getStatus().equals(Animation.Status.RUNNING))
            return true;
      }
      return false;
   }

   public void playSound(String soundCmd) {
      Thread th = new Thread(new Task<Void>() {
         @Override
         protected Void call() throws Exception {
            getRuntime().exec(soundCmd);
            return null;
         }
      });
      th.setDaemon(true);
      th.start();
   }

   private void setDtoImage(ResponseEntity<ClientPupilDto> response) {
      HttpStatus code = response.getStatusCode();
      boolean pupilExists = code == ACCEPTED || code == ALREADY_REPORTED || code == FORBIDDEN;
      //String remoteImgUrl = "http://www.leenh.org/Pages/LeeNH_Building/pics/image003.jpg";       //for testing
      if (pupilExists) {
         String path = PHOTO_BASE_PATH + response.getBody().getId() + IMAGE_EXTENSION;
         Image image = new Image(path, 0, 0, true, false, true);
         response.getBody().setImage(image);
      }
   }

   private boolean checkRemoteImage(String url) {
      try {
         URL u = new URL(url);
         HttpURLConnection http = (HttpURLConnection) u.openConnection();
         http.setInstanceFollowRedirects(false);
         http.setRequestMethod("HEAD");
         http.connect();
         return (http.getResponseCode() == HttpURLConnection.HTTP_OK);
      } catch (Exception e) {
         return false;
      }
   }
}
