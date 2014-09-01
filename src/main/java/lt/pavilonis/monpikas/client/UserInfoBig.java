package lt.pavilonis.monpikas.client;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalDateTime;

public class UserInfoBig extends Group {

   public UserInfoBig(User user) {

      this.setLayoutX(20);
      this.setLayoutY(20);

      GridPane grid = new GridPane();
      SVGPath icon = new SVGPath();
      icon.setContent("M20.771,12.364c0,0,0.849-3.51,0-4.699c-0.85-1.189-1.189-1.981-3.058-2.548s-1.188-0.454-2.547-0.396c-1.359,0.057-2.492,0.792-2.492,1.188c0,0-0.849,0.057-1.188,0.397c-0.34,0.34-0.906,1.924-0.906,2.321s0.283,3.058,0.566,3.624l-0.337,0.113c-0.283,3.283,1.132,3.68,1.132,3.68c0.509,3.058,1.019,1.756,1.019,2.548s-0.51,0.51-0.51,0.51s-0.452,1.245-1.584,1.698c-1.132,0.452-7.416,2.886-7.927,3.396c-0.511,0.511-0.453,2.888-0.453,2.888h26.947c0,0,0.059-2.377-0.452-2.888c-0.512-0.511-6.796-2.944-7.928-3.396c-1.132-0.453-1.584-1.698-1.584-1.698s-0.51,0.282-0.51-0.51s0.51,0.51,1.02-2.548c0,0,1.414-0.397,1.132-3.68H20.771z");
      icon.setScaleX(19);
      icon.setScaleY(19);
      icon.setStrokeWidth(0.2);
      icon.setStroke(Color.DARKGREY);
      icon.setFill(Color.LIGHTGRAY);

      ColumnConstraints columnConstraint = new ColumnConstraints(580);
      columnConstraint.setHalignment(HPos.CENTER);
      grid.getColumnConstraints().add(columnConstraint);

      RowConstraints rcTop = new RowConstraints(500);
      rcTop.setValignment(VPos.CENTER);
      RowConstraints rcMiddle = new RowConstraints(200);
      rcMiddle.setValignment(VPos.CENTER);
      RowConstraints rcBottom = new RowConstraints(160);
      rcBottom.setValignment(VPos.CENTER);
      grid.getRowConstraints().addAll(rcTop, rcMiddle, rcBottom);
      grid.add(icon, 0, 0);

      Text name = new Text(user.getName());
      name.setFont(Font.font("SansSerif", 70));
      name.setSmooth(true);
      name.setWrappingWidth(540);
      name.setTextAlignment(TextAlignment.CENTER);
      grid.add(name, 0, 1);

      Rectangle outerRect = new Rectangle(580, 860);
      outerRect.setArcHeight(20);
      outerRect.setArcWidth(20);
      outerRect.setStroke(Color.BLACK);
      outerRect.setEffect(new DropShadow(10, 5, 5, Color.DARKGRAY));

      Rectangle innerRect = new Rectangle(20, 20, 540, 820);
      innerRect.setFill(Color.WHITE);
      innerRect.setArcHeight(20);
      innerRect.setArcWidth(20);
      innerRect.setStroke(Color.BLACK);

      SVGPath statusIcon = new SVGPath();
      Text txt = null;
      //TODO need better condition
      if (user.isDinnerPermission() && user.getLastDinner().getDayOfYear() != LocalDateTime.now().getDayOfYear()) {
         statusIcon.setContent("M2.379,14.729 5.208,11.899 12.958,19.648 25.877,6.733 28.707,9.561 12.958,25.308z");
         statusIcon.setFill(Color.GREEN);
         outerRect.setFill(Color.GREEN);
      } else {
         statusIcon.setContent("M24.778,21.419 19.276,15.917 24.777,10.415 21.949,7.585 16.447,13.087 10.945,7.585 8.117,10.415 13.618,15.917 8.116,21.419 10.946,24.248 16.447,18.746 21.948,24.248z");
         statusIcon.setFill(Color.RED);
         outerRect.setFill(Color.RED);
         if (!user.isDinnerPermission()) {
            txt = new Text("No Permission");
         } else {
            txt = new Text("Already had dinner");
         }
      }
      statusIcon.setScaleX(6);
      statusIcon.setScaleY(6);
      statusIcon.setStrokeWidth(0.2);
      statusIcon.setStroke(Color.DARKGREY);
      FlowPane flow = new FlowPane();
      flow.setAlignment(Pos.CENTER);
      flow.setHgap(50);
      flow.getChildren().add(statusIcon);
      if (txt != null) {
         txt.setFont(Font.font("SansSerif", 40));
         flow.getChildren().add(txt);
      }
      grid.add(flow, 0, 2);

      this.getChildren().add(outerRect);
      this.getChildren().add(innerRect);
      this.getChildren().add(grid);
   }
}
