package lt.pavilonis.scan.monpikas.client.model;

import com.google.common.io.BaseEncoding;
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

   @Value("${card.icon.noPhotoContent}")
   protected String iconNoPhotoContentPath;

   @Value("${card.message.noPermission}")
   private String noPermissionMessage;

   @Value("${card.message.alreadyHadDinner}")
   private String alreadyHadMealMessage;

   protected final FlowPane photoContainer = new FlowPane();
   protected final SVGPath iconNoPhoto = new SVGPath();
   protected final Rectangle outerRect = new Rectangle();
   protected final Rectangle innerRect = new Rectangle();
   protected final Text nameText = new Text();
   protected final Text gradeText = new Text();
   protected final GridPane grid = new GridPane();
   protected final ImageView imageView = new ImageView();

   public void initialize() {
      setVisible(false);
      photoContainer.setAlignment(Pos.CENTER);

      iconNoPhoto.setContent(iconNoPhotoContentPath);
      iconNoPhoto.setStroke(Color.DARKGREY);
      iconNoPhoto.setFill(Color.LIGHTGRAY);

      imageView.setPreserveRatio(true);
      imageView.fitWidthProperty().bind(photoContainer.widthProperty().asObject());
      imageView.fitHeightProperty().bind(photoContainer.heightProperty());

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
            decorate(dto.getName(), RED, alreadyHadMealMessage);
            log("Pupil " + dto.getName() + " already had hist meal");
            break;

         case FORBIDDEN:
            decorate(dto.getName(), RED, noPermissionMessage);
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

   protected void log(String text) {/*should be overridden by big card*/}

   protected void decorate(String name, Color color, Object desc) {
      nameText.setText(name);
      outerRect.setFill(color);
   }

   protected void setPhoto() {
      Thread th = new Thread(new Task<Void>() {
         @Override
         protected Void call() throws Exception {
            Platform.runLater(() -> {
               ObservableList<Node> container = photoContainer.getChildren();
               container.clear();
               Image image = getImage();
               if (image == null) {
                  container.add(iconNoPhoto);
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
