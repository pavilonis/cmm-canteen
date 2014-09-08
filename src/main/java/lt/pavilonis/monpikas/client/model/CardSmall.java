package lt.pavilonis.monpikas.client.model;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import lt.pavilonis.monpikas.client.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(value = "prototype")
@Component
public class CardSmall extends Card {

   @Override
   public void initialize() {

      super.initialize();
      setLayoutX(620);

      grid.setPadding(new Insets(20));
      grid.setHgap(10);

      ColumnConstraints ccLeft = new ColumnConstraints(142.5);
      ColumnConstraints ccRight = new ColumnConstraints(657.5);
      ccLeft.setHalignment(HPos.CENTER);
      ccRight.setHalignment(HPos.CENTER);
      grid.getColumnConstraints().addAll(ccLeft, ccRight);

      RowConstraints rc = new RowConstraints(180);
      rc.setValignment(VPos.CENTER);
      grid.getRowConstraints().add(rc);

      ICON_NO_PHOTO.setScaleX(5);
      ICON_NO_PHOTO.setScaleY(5);
      ICON_NO_PHOTO.setStrokeWidth(0.5);
      grid.add(ICON_NO_PHOTO, 0, 0);

      nameText.setWrappingWidth(640);
      nameText.setFont(Font.font("SansSerif", 60));
      grid.add(nameText, 1, 0);

      outerRect.setWidth(800);
      outerRect.setHeight(200);

      innerRect.setX(10);
      innerRect.setY(10);
      innerRect.setWidth(780);
      innerRect.setHeight(180);

      getChildren().add(outerRect);
      getChildren().add(innerRect);
      getChildren().add(grid);
   }

   @Override
   protected void animateUpdate(User user) {
      double originY = getTranslateY();
      translate.setToY(230);
      translate.setOnFinished(event -> {
         setTranslateY(originY);
         update(user);
      });
      translate.play();
   }
}
