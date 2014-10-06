package lt.pavilonis.monpikas.client.model;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public abstract class Card extends Group {

   protected ClientPupilDto dto;

   @Value("${Card.Icon.NoPhotoContent}")
   protected String ICON_NO_PHOTO_CONTENT_PATH;

   @Value("${Images.PhotoBasePath}")
   private String PHOTO_BASE_PATH;

   @Value("${Images.Extension}")
   private String IMAGE_EXTENSION;

   protected final FlowPane PHOTO_CONTAINER = new FlowPane();
   protected final SVGPath ICON_NO_PHOTO = new SVGPath();
   protected final Rectangle outerRect = new Rectangle();
   protected final Rectangle innerRect = new Rectangle();
   protected final Text nameText = new Text();
   protected final GridPane grid = new GridPane();
   protected final Duration ANIMATION_DURATION = Duration.seconds(0.5);
   protected ImageView userPhoto = new ImageView();
   FadeTransition fade = new FadeTransition(ANIMATION_DURATION, this);
   TranslateTransition translate = new TranslateTransition(ANIMATION_DURATION, this);

   public void initialize() {
      setVisible(false);
      PHOTO_CONTAINER.setAlignment(Pos.CENTER);

      ICON_NO_PHOTO.setContent(ICON_NO_PHOTO_CONTENT_PATH);
      ICON_NO_PHOTO.setStroke(Color.DARKGREY);
      ICON_NO_PHOTO.setFill(Color.LIGHTGRAY);

      userPhoto.setPreserveRatio(true);
      userPhoto.fitWidthProperty().bind(PHOTO_CONTAINER.widthProperty().asObject());
      userPhoto.fitHeightProperty().bind(PHOTO_CONTAINER.heightProperty());

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

   public List<Transition> getTransitions() {
      return Arrays.asList(translate, fade);
   }

   public abstract void animate();

   public abstract void update();

   //TODO refactor, should only check, not do some logic
   protected boolean checkIfDinnerAllowed() {
      nameText.setText(dto.getName());
      boolean allowed = dto.isDinnerPermitted() && !dto.isHadDinnerToday();
      outerRect.setFill((allowed) ? Color.GREEN : Color.RED);
      return allowed;
   }

   protected void setPhoto() {
      //String remoteImgUrl = "http://www.leenh.org/Pages/LeeNH_Building/pics/image003.jpg";
      String remoteImgUrl = PHOTO_BASE_PATH + dto.getCardId() + IMAGE_EXTENSION;
      PHOTO_CONTAINER.getChildren().clear();
      if (remoteImageExists(remoteImgUrl)) {
         Image img = new Image(remoteImgUrl, 0, 0, true, false, true);
         userPhoto.setImage(img);
         PHOTO_CONTAINER.getChildren().add(userPhoto);
         userPhoto.setY(50);
      } else {
         PHOTO_CONTAINER.getChildren().add(ICON_NO_PHOTO);
      }
   }

   private boolean remoteImageExists(String url) {
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

   public void setDto(ClientPupilDto dto) {
      this.dto = dto;
   }

   protected void ensureVisible() {
      if (!isVisible()) {
         setVisible(true);
      }
   }
}
