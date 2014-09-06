package lt.pavilonis.monpikas.client;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(value = "prototype")
@Component
public class CardSmall extends Card {

   public CardSmall() {
   }

   @Override
   protected void initialize() {

      super.initialize();
      setLayoutX(605);
      setLayoutY(20);

      grid.setPadding(new Insets(20));
      grid.setHgap(10);

      ColumnConstraints ccLeft = new ColumnConstraints(142.5);
      ColumnConstraints ccRight = new ColumnConstraints(667.5);
      ccLeft.setHalignment(HPos.CENTER);
      ccRight.setHalignment(HPos.CENTER);
      grid.getColumnConstraints().addAll(ccLeft, ccRight);

      RowConstraints rc = new RowConstraints(190);
      rc.setValignment(VPos.CENTER);
      grid.getRowConstraints().add(rc);

      ICON_NO_PHOTO.setScaleX(5);
      ICON_NO_PHOTO.setScaleY(5);
      ICON_NO_PHOTO.setStrokeWidth(0.5);
      grid.add(ICON_NO_PHOTO, 0, 0);

      nameText.setWrappingWidth(640);
      nameText.setFont(Font.font("SansSerif", 60));
      grid.add(nameText, 1, 0);

      outerRect.setWidth(810);
      outerRect.setHeight(210);

      innerRect.setX(10);
      innerRect.setY(10);
      innerRect.setWidth(790);
      innerRect.setHeight(190);

      getChildren().add(outerRect);
      getChildren().add(innerRect);
      getChildren().add(grid);
   }
}
