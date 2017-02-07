package lt.pavilonis.scan.monpikas.client.model;

import com.google.common.io.BaseEncoding;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
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
import lt.pavilonis.scan.monpikas.client.dto.User;
import lt.pavilonis.scan.monpikas.client.enumeration.PupilType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class Card extends Group {

   private static final Logger LOG = getLogger(Card.class.getSimpleName());

   protected ResponseEntity<User> response;

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

      innerRect.setArcHeight(20);
      innerRect.setArcWidth(20);
      innerRect.setFill(Color.WHITE);
      innerRect.setStroke(Color.BLACK);
   }

   protected void update() {
      User dto = response.getBody();

      switch (response.getStatusCode()) {

         case ACCEPTED:
            decorate(dto.getName(), dto.getType() == PupilType.SOCIAL ? GREEN : BLUE, dto.getMeal());
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

         case MULTIPLE_CHOICES:
            decorate("Netinkamas laikas", RED, "");
            break;

         case INTERNAL_SERVER_ERROR:
            decorate("Serverio klaida", RED, "");
            LOG.error("Server error");
            break;

         default:
            decorate("Nežinoma klaida", RED, "");
            LOG.error("Unknown error");
      }
   }

   private void log(String text) {
      if (this instanceof CardBig) {
         LOG.info(text);
      }
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
               Image image = getImage();
               if (image == null) {
                  container.add(ICON_NO_PHOTO);
               } else {
                  imageView.setImage(image);
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

   public void setResponse(ResponseEntity<User> response) {
      this.response = response;
   }

   protected void ensureVisible() {
      if (!isVisible()) {
         setVisible(true);
      }
   }

   private Image getImage() {

      if (true)
         return null;

      if (response == null || response.getBody() == null) {
         return null;
      }

      String base16image = response.getBody().getBase16photo();

      return StringUtils.isNotBlank(base16image) && BaseEncoding.base16().canDecode(base16image)
            ? extractImageFromString(base16image)
            : null;
   }

   private Image extractImageFromString(String base16image) {
      byte[] imageBytes = BaseEncoding.base16().decode(base16image);

      try (ByteArrayInputStream input = new ByteArrayInputStream(imageBytes)) {

         BufferedImage image = ImageIO.read(input);
         return SwingFXUtils.toFXImage(image, null);

      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }
}
