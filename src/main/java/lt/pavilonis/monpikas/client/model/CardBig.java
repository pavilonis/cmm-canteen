package lt.pavilonis.monpikas.client.model;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lt.pavilonis.monpikas.client.ViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static javafx.animation.Interpolator.EASE_IN;
import static javafx.geometry.VPos.CENTER;

@Component
public class CardBig extends Card {

   @Value("${Audio.Success}")
   private String PLAY_SUCCESS_SOUND_CMD;

   @Value("${Audio.SystemError}")
   private String PLAY_SYS_ERROR_SOUND_CMD;

   @Value("${Audio.Error}")
   private String PLAY_ERROR_SOUND_CMD;

   @Value("${Card.Icon.StatusOkContent}")
   private String ICON_STATUS_OK_CONTENT;

   @Value("${Card.Icon.StatusRejectedContent}")
   private String ICON_STATUS_REJECT_CONTENT;

   @Autowired
   ViewController controller;

   private final Text DESCRIPTION = new Text();
   private final SVGPath ICON_STATUS_OK = new SVGPath();
   private final SVGPath ICON_STATUS_REJECT = new SVGPath();
   private final FlowPane STATUS_MSG_FLOW_PANE = new FlowPane();
   private final ScaleTransition scale = new ScaleTransition(ANIMATION_DURATION.multiply(5), this);
   private final FadeTransition fade = new FadeTransition(ANIMATION_DURATION.multiply(5), this);
   private final Rectangle gradeRect = new Rectangle();

   @Override
   public void initialize() {

      super.initialize();
      setLayoutX(20);
      setLayoutY(20);

      translate.setDuration(ANIMATION_DURATION.multiply(5));

      ICON_NO_PHOTO.setScaleX(19);
      ICON_NO_PHOTO.setScaleY(19);
      ICON_NO_PHOTO.setStrokeWidth(0.2);

      ICON_STATUS_OK.setContent(ICON_STATUS_OK_CONTENT);
      ICON_STATUS_OK.setScaleX(5);
      ICON_STATUS_OK.setScaleY(5);
      ICON_STATUS_OK.setFill(Color.GREEN);
      ICON_STATUS_OK.setStroke(Color.DARKGREY);
      ICON_STATUS_OK.setStrokeWidth(0.2);

      ICON_STATUS_REJECT.setContent(ICON_STATUS_REJECT_CONTENT);
      ICON_STATUS_REJECT.setScaleX(6);
      ICON_STATUS_REJECT.setScaleY(6);
      ICON_STATUS_REJECT.setFill(Color.RED);
      ICON_STATUS_REJECT.setStroke(Color.DARKGREY);
      ICON_STATUS_REJECT.setStrokeWidth(0.2);

      DESCRIPTION.setFont(Font.font("SansSerif", 50));

      ColumnConstraints columnConstraint = new ColumnConstraints(540);
      columnConstraint.setHalignment(HPos.CENTER);
      grid.getColumnConstraints().add(columnConstraint);

      RowConstraints rcTop = new RowConstraints(480);
      rcTop.setValignment(CENTER);
      RowConstraints rcMiddle = new RowConstraints(180);
      rcMiddle.setValignment(CENTER);
      RowConstraints rcBottom = new RowConstraints(160);
      rcBottom.setValignment(CENTER);

      grid.getRowConstraints().addAll(rcTop, rcMiddle, rcBottom);
      grid.add(PHOTO_CONTAINER, 0, 0);

      nameText.setWrappingWidth(540);
      nameText.setFont(Font.font("SansSerif", 70));
      nameText.setTextAlignment(TextAlignment.CENTER);
      grid.add(nameText, 0, 1);

      outerRect.setWidth(580);
      outerRect.setHeight(860);

      innerRect.setX(20);
      innerRect.setY(20);
      innerRect.setWidth(540);
      innerRect.setHeight(820);

      gradeRect.setX(390);
      gradeRect.setY(355);
      gradeRect.setWidth(160);
      gradeRect.setHeight(120);
      gradeRect.setArcHeight(20);
      gradeRect.setArcWidth(20);
      gradeRect.setFill(Color.WHITE);
      gradeRect.setStroke(Color.BLACK);

      gradeText.setFont(Font.font("SansSerif", 70));
      gradeText.setY(440);
      gradeText.setX(400);

      STATUS_MSG_FLOW_PANE.setAlignment(Pos.CENTER);
      STATUS_MSG_FLOW_PANE.setHgap(60);
      grid.add(STATUS_MSG_FLOW_PANE, 0, 2);
      grid.setPadding(new Insets(20));

      getChildren().add(outerRect);
      getChildren().add(innerRect);
      getChildren().add(grid);
      getChildren().add(gradeRect);
      getChildren().add(gradeText);

      fade.setInterpolator(EASE_IN);
   }

   @Override
   public void update() {
      double originX = getTranslateX();
      double originY = getTranslateY();
      translate.setToX(605);
      translate.setToY(-655);
      translate.setOnFinished(event -> {
         setVisible(false);
         super.update();
         setTranslateX(originX);
         setTranslateY(originY);
         sleepAndRun();
      });
      translate.play();
   }

   @Override
   protected void decorate(String title, Color color, Object desc) {
      super.decorate(title, color, desc);

      ObservableList<Node> container = STATUS_MSG_FLOW_PANE.getChildren();
      container.clear();

      if (response.getStatusCode() == HttpStatus.ACCEPTED) {
         container.add(ICON_STATUS_OK);
         controller.playSound(PLAY_SUCCESS_SOUND_CMD);
      } else {
         container.add(ICON_STATUS_REJECT);
         controller.playSound(PLAY_ERROR_SOUND_CMD);
      }
      DESCRIPTION.setText(desc.toString());
      container.add(DESCRIPTION);

      gradeText.setText(response.getBody() != null ? response.getBody().getGrade() : "");
   }

   @Override
   public List<Transition> getTransitions() {
      return asList(scale, fade, translate);
   }

   protected void sleepAndRun() {
      Thread th = new Thread(new Task<Void>() {
         @Override
         protected Void call() throws Exception {
            Platform.runLater(() -> setPhoto());
            sleep((long) ANIMATION_DURATION.toMillis());
            Platform.runLater(() -> {
               fade.setFromValue(0);
               fade.setToValue(1);
               fade.play();
               setVisible(true);
            });
            return null;
         }
      });
      th.setDaemon(true);
      th.start();
   }
}
