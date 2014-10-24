package lt.pavilonis.monpikas.client;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.input.KeyEvent;
import lt.pavilonis.monpikas.client.dto.ClientPupilDto;
import lt.pavilonis.monpikas.client.model.Card;
import lt.pavilonis.monpikas.client.model.CardBig;
import lt.pavilonis.monpikas.client.model.CardSmall;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.PostConstruct;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static lt.pavilonis.monpikas.client.App.root;
import static lt.pavilonis.monpikas.client.App.stage;

@Controller
public class ViewController {

   private static final Logger LOG = Logger.getLogger(ViewController.class.getSimpleName());

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

   private EvictingQueue<ClientPupilDto> clientPupilDtos = EvictingQueue.create(5);
   private List<Card> cards;
   private List<Transition> transitions = new ArrayList<>();

   @PostConstruct
   public void initialize() {
//      first.setNext(second);
//      second.setNext(third);
//      third.setNext(forth);
//      forth.setNext(fifth);

      cards = asList(first, second, third, forth, fifth);
      //cards = asList(fifth, forth, third, second, first);
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
         if (k.getCharacter().equals("b")) scanEventAction("5010");
         if (k.getCharacter().equals("c")) scanEventAction("0003");
         if (k.getCharacter().equals("d")) scanEventAction("0004");
         if (k.getCharacter().equals("e")) scanEventAction("0533");
      });
   }

   public void scanEventAction(String barcode) {
      if (transitionActive()) {
         LOG.info("Transition is active, canceled request for bc: " + barcode);
      } else {
         LOG.info("Requesting user with bc: " + barcode);
         ClientPupilDto dto;
         try {
            dto = userRequestService.requestUser(barcode);
         } catch (HttpClientErrorException e1) {
            if (e1.getStatusCode() == HttpStatus.NOT_FOUND) {
               dto = new ClientPupilDto("Nėra ryšio");
               LOG.info("HttpClientErrorException - no connection: " + e1);
            } else if (e1.getStatusCode() == HttpStatus.BAD_REQUEST) {
               dto = new ClientPupilDto("Nerasta sistemoje!");
               LOG.info("HttpClientErrorException - pupil not found: " + e1);
            } else {
               dto = new ClientPupilDto("Nežinoma klaida!");
               LOG.info("HttpClientErrorException - Unknown error: " + e1);
            }
         } catch (ConnectException ce) {
            dto = new ClientPupilDto("Nežinoma klaida!");
            LOG.info("ConnectException - unknown error: " + ce);
         } catch (Exception e) {
            dto = new ClientPupilDto("Nežinoma klaida!");
            LOG.info("Exception - unknown error: " + e);
         }
         clientPupilDtos.add(dto);
         updateView();
      }
   }

   public void updateView() {
      i = 0;
      Lists.reverse(new ArrayList<>(clientPupilDtos))
            .forEach(dto -> {
               cards.get(i).setDto(dto);
               cards.get(i).update();
               i++;
            });
      //first.update();
   }

   public boolean transitionActive() {
      for (Transition t : transitions) {
         if (t.getStatus().equals(Animation.Status.RUNNING))
            return true;
      }
      return false;
   }
}
