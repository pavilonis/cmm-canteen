package lt.pavilonis.monpikas.client.model;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
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
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static java.util.Arrays.asList;

public abstract class Card extends Group {

   protected ClientPupilDto dto;

   @Value("${Card.Icon.NoPhotoContent}")
   protected String ICON_NO_PHOTO_CONTENT_PATH;

   protected final FlowPane PHOTO_CONTAINER = new FlowPane();
   protected final SVGPath ICON_NO_PHOTO = new SVGPath();
   protected final Rectangle outerRect = new Rectangle();
   protected final Rectangle innerRect = new Rectangle();
   protected final Text nameText = new Text();
   protected final GridPane grid = new GridPane();
   protected final Duration ANIMATION_DURATION = Duration.seconds(.2);
   protected ImageView imageView = new ImageView();
   protected Image image;
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
      outerRect.setEffect(new DropShadow(10, 5, 5, Color.DARKGRAY));

      innerRect.setArcHeight(20);
      innerRect.setArcWidth(20);
      innerRect.setFill(Color.WHITE);
      innerRect.setStroke(Color.BLACK);
   }

   public List<Transition> getTransitions() {
      return asList(translate);
   }

   public abstract void update();

   //TODO refactor, should only check, not do some logic
   protected boolean checkIfDinnerAllowed() {
      nameText.setText(dto.getName());
      boolean allowed = dto.isDinnerPermitted() && !dto.isHadDinnerToday();
      outerRect.setFill((allowed) ? Color.GREEN : Color.RED);
      return allowed;
   }

   protected void setPhoto() {
      Thread th = new Thread(new Task<Void>() {
         @Override
         protected Void call() throws Exception {
            Platform.runLater(() -> {
               PHOTO_CONTAINER.getChildren().clear();
               if (image.getProgress() == 1.0 && (image.getWidth() == 0.0 || image.getHeight() == 0.0)) {
                  PHOTO_CONTAINER.getChildren().add(ICON_NO_PHOTO);
               } else {
                  imageView.setImage(image);
                  PHOTO_CONTAINER.getChildren().add(imageView);
                  imageView.setY(50);
               }
            });
            return null;
         }
      });
      th.setDaemon(true);
      th.start();
   }

   public void setDto(ClientPupilDto dto) {
      this.dto = dto;
   }

   protected void ensureVisible() {
      if (!isVisible()) {
         setVisible(true);
      }
   }

   public void setImage(Image image) {
      this.image = image;
   }
}
