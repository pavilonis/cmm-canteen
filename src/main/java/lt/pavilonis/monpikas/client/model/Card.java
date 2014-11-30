package lt.pavilonis.monpikas.client.model;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lt.pavilonis.monpikas.client.dto.ClientPupilDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static java.util.Arrays.asList;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class Card extends Group {

   private static final Logger LOG = getLogger(Card.class.getSimpleName());

   protected ResponseEntity<ClientPupilDto> response;

   @Value("${Card.Icon.NoPhotoContent}")
   protected String ICON_NO_PHOTO_CONTENT_PATH;

   @Value("${Card.Message.NoPermission}")
   private String NO_PERMISSION_MSG;

   @Value("${Card.Message.AlreadyHadDinner}")
   private String ALREADY_HAD_MEAL_MSG;

   protected final FlowPane PHOTO_CONTAINER = new FlowPane();
   protected final SVGPath ICON_NO_PHOTO = new SVGPath();
   protected final Rectangle outerRect = new Rectangle();
   protected final Rectangle innerRect = new Rectangle();
   protected final Text nameText = new Text();
   protected final Text gradeText = new Text();
   protected final GridPane grid = new GridPane();
   protected final Duration ANIMATION_DURATION = Duration.seconds(.2);
   protected ImageView imageView = new ImageView();
   TranslateTransition translate = new TranslateTransition(ANIMATION_DURATION, this);

   public void initialize() {
      setVisible(false);
      PHOTO_CONTAINER.setAlignment(Pos.CENTER);

      ICON_NO_PHOTO.setContent(ICON_NO_PHOTO_CONTENT_PATH);
      ICON_NO_PHOTO.setStroke(Color.DARKGREY);
      ICON_NO_PHOTO.setFill(Color.LIGHTGRAY);

      imageView.setPreserveRatio(true);
      imageView.fitWidthProperty().bind(PHOTO_CONTAINER.widthProperty().asObject());
      imageView.fitHeightProperty().bind(PHOTO_CONTAINER.heightProperty());

      outerRect.setArcHeight(20);
      outerRect.setArcWidth(20);
      outerRect.setStroke(Color.BLACK);
      //outerRect.setEffect(new DropShadow(10, 5, 5, Color.DARKGRAY));

      innerRect.setArcHeight(20);
      innerRect.setArcWidth(20);
      innerRect.setFill(Color.WHITE);
      innerRect.setStroke(Color.BLACK);
   }

   public List<Transition> getTransitions() {
      return asList(translate);
   }

   protected void update() {
      ClientPupilDto dto = response.getBody();

      switch (response.getStatusCode()) {

         case ACCEPTED:
            decorate(dto.getName(), GREEN, dto.getPortion());
            log("Pupil " + dto.getName() + " is getting his meal");
            break;

         case ALREADY_REPORTED:
            decorate(dto.getName(), RED, ALREADY_HAD_MEAL_MSG);
            log("Pupil " + dto.getName() + " already had hist meal");
            break;

         case FORBIDDEN:
            decorate(dto.getName(), RED, NO_PERMISSION_MSG);
            log("Pupil " + dto.getName() + " has no permission");
            break;

         case NOT_FOUND:
            decorate("Nežinomas mokinys", RED, "");
            log("Pupil not found: ");
            break;

         case SERVICE_UNAVAILABLE:
            decorate("Serveris nerastas", RED, "");
            break;

         case INTERNAL_SERVER_ERROR:
            decorate("Serveris klaida", RED, "");
            LOG.error("Server error");
            break;

         default:
            decorate("Nežinoma klaida", RED, "");
            LOG.error("Unknown error");
      }
   }

   private void log(String text) {
      if (this instanceof CardBig) LOG.info(text);
   }

   protected void decorate(String name, Color color, Object desc) {
      nameText.setText(name);
      outerRect.setFill(color);
   }

   protected void setPhoto() {
      Thread th = new Thread(new Task<Void>() {
         @Override
         protected Void call() throws Exception {
            Platform.runLater(() -> {
               ObservableList<Node> container = PHOTO_CONTAINER.getChildren();
               container.clear();
               if (image() == null || image().getProgress() == 1.0 && (image().getWidth() == 0.0 || image().getHeight() == 0.0)) {
                  container.add(ICON_NO_PHOTO);
               } else {
                  imageView.setImage(image());
                  container.add(imageView);
                  imageView.setY(50);
               }
            });
            return null;
         }
      });
      th.setDaemon(true);
      th.start();
   }

   public void setResponse(ResponseEntity<ClientPupilDto> response) {
      this.response = response;
   }

   protected void ensureVisible() {
      if (!isVisible()) {
         setVisible(true);
      }
   }

   private Image image() {
      return response.getBody() != null
            ? response.getBody().getImage()
            : null;
   }
}
