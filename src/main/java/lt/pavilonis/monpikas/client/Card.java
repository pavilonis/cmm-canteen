package lt.pavilonis.monpikas.client;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public abstract class Card extends Group {

   @Value("${Card.Icon.NoPhotoContent}")
   protected String ICON_NO_PHOTO_CONTENT;
   protected final SVGPath ICON_NO_PHOTO = new SVGPath();
   protected final Rectangle outerRect = new Rectangle();
   protected final Rectangle innerRect = new Rectangle();
   protected final Text nameText = new Text();
   protected final GridPane grid = new GridPane();
   protected final Duration ANIMATION_DURATION = Duration.seconds(0.5);
   FadeTransition fade = new FadeTransition(ANIMATION_DURATION, this);
   TranslateTransition translate = new TranslateTransition(ANIMATION_DURATION, this);

   protected void initialize() {
      ICON_NO_PHOTO.setContent(ICON_NO_PHOTO_CONTENT);
      ICON_NO_PHOTO.setStroke(Color.DARKGREY);
      ICON_NO_PHOTO.setFill(Color.LIGHTGRAY);

      outerRect.setArcHeight(20);
      outerRect.setArcWidth(20);
      outerRect.setStroke(Color.BLACK);
      outerRect.setEffect(new DropShadow(10, 5, 5, Color.DARKGRAY));

      innerRect.setArcHeight(20);
      innerRect.setArcWidth(20);
      innerRect.setFill(Color.WHITE);
      innerRect.setStroke(Color.BLACK);

      fade.setInterpolator(Interpolator.EASE_IN);
   }

   protected void updateUserInfo(User user) {
      if (userIsSet()) {
         animateUpdate(user);
      } else {
         update(user);
      }
   }

   protected abstract void animateUpdate(User user);

   protected void update(User user) {
      checkIfDinnerAllowed(user);
   }

   protected boolean checkIfDinnerAllowed(User user) {
      nameText.setText(user.getName());
      boolean allowed = user.isDinnerPermission() && user.getLastDinner().getDayOfYear() != LocalDateTime.now().getDayOfYear();
      outerRect.setFill((allowed) ? Color.GREEN : Color.RED);
      return allowed;
   }

   protected boolean userIsSet() {
      return !nameText.getText().isEmpty();
   }
}
