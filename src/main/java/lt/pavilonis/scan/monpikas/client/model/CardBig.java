package lt.pavilonis.scan.monpikas.client.model;

import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.lang.Runtime.getRuntime;
import static javafx.geometry.VPos.CENTER;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public final class CardBig extends Card {

   private static final Logger LOG = getLogger(CardBig.class.getSimpleName());

   @Value("${audio.success}")
   private String playSuccessSoundCmd;

   @Value("${audio.systemError}")
   private String playSysErrorSoundCmd;

   @Value("${audio.error}")
   private String playErrorSoundCmd;

   @Value("${card.icon.statusOkContent}")
   private String iconStatusOkContent;

   @Value("${card.icon.statusRejectedContent}")
   private String iconStatusRejectContent;

   private final Text descriptionText = new Text();
   private final SVGPath iconStatusOk = new SVGPath();
   private final SVGPath iconStatusReject = new SVGPath();
   private final FlowPane statusMessagePane = new FlowPane();
   private final Rectangle gradeRect = new Rectangle();

   @Override
   public void initialize() {

      super.initialize();
      setLayoutX(20);
      setLayoutY(20);

      iconNoPhoto.setScaleX(19);
      iconNoPhoto.setScaleY(19);
      iconNoPhoto.setStrokeWidth(0.2);

      iconStatusOk.setContent(iconStatusOkContent);
      iconStatusOk.setScaleX(5);
      iconStatusOk.setScaleY(5);
      iconStatusOk.setFill(Color.GREEN);
      iconStatusOk.setStroke(Color.DARKGREY);
      iconStatusOk.setStrokeWidth(0.2);

      iconStatusReject.setContent(iconStatusRejectContent);
      iconStatusReject.setScaleX(6);
      iconStatusReject.setScaleY(6);
      iconStatusReject.setFill(Color.RED);
      iconStatusReject.setStroke(Color.DARKGREY);
      iconStatusReject.setStrokeWidth(0.2);

      descriptionText.setFont(Font.font("SansSerif", 50));

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
      grid.add(photoContainer, 0, 0);

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

      statusMessagePane.setAlignment(Pos.CENTER);
      statusMessagePane.setHgap(60);
      grid.add(statusMessagePane, 0, 2);
      grid.setPadding(new Insets(20));

      getChildren().add(outerRect);
      getChildren().add(innerRect);
      getChildren().add(grid);
      getChildren().add(gradeRect);
      getChildren().add(gradeText);
   }

   @Override
   public void update() {
      super.update();
      Platform.runLater(() -> {
         setPhoto();
         if (!isVisible()) {
            setVisible(true);
         }
      });
   }

   @Override
   protected void log(String text) {
      LOG.info(text);
   }

   @Override
   protected void decorate(String title, Color color, Object desc) {

      super.decorate(title, color, desc);

      Platform.runLater(() -> {

         ObservableList<Node> container = statusMessagePane.getChildren();
         container.clear();

         if (response.getStatusCode() == HttpStatus.ACCEPTED) {
            container.add(iconStatusOk);
            playSound(playSuccessSoundCmd);
         } else {
            container.add(iconStatusReject);
            playSound(playErrorSoundCmd);
         }
         descriptionText.setText(desc.toString());
         container.add(descriptionText);

         gradeText.setText(response.getBody() != null ? response.getBody().getGrade() : "");
      });
   }

   private void playSound(String soundCmd) {
      Platform.runLater(() -> {
         try {
            getRuntime().exec(soundCmd);
         } catch (IOException e) {
            LOG.error("Could not play sound: " + e);
         }
      });
   }
}
