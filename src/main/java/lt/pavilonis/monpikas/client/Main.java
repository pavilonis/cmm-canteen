package lt.pavilonis.monpikas.client;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;

public class Main extends Application {

   @Override
   public void start(Stage stage) throws Exception {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      Group root = new Group();
      Scene scene = new Scene(root, 1440, 900, Color.WHITE);


//      Image img = new Image(loader.getResource("images/nophoto.png").toString(), false);
//      ImageView imgView = new ImageView(img);
//      DropShadow ds = new DropShadow(10, 5, 5, Color.DARKGRAY);
//      imgView.setEffect(ds);

      //rightSide.add(rec, 1, i);
//         FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), r);
//         fadeIn.setFromValue(1.0);
//         fadeIn.setToValue(0.0);
      //fadeIn.play();
      //g.getChildren().add(r);
      //rightSide.add(g, 1, i);
      //g.getChildren().add(r);
      //g.getChildren().add(r2);

      CardBig uib = context.getBean(CardBig.class);
      uib.initialize();
      uib.updateUserInfo(new User("Ivanas Ivanauskas Ivanovicius", true, LocalDateTime.now()));

      CardSmall uis = context.getBean(CardSmall.class);
      uis.initialize();
      uis.updateUserInfo(new User("Ivanas Ėčęą„ūųšė Ivanovičius", true, LocalDateTime.now()));

      Path p = new Path(new MoveTo(440, 115), new LineTo(440, 760), new ClosePath());
//      TranslateTransition tt  = new TranslateTransition(Duration.seconds(3), uis);
//      tt.setFromX(0);
//      tt.setFromY(115);
//      tt.setToX(0);
//      tt.setToY(760);
//      tt.setCycleCount(20);
//      tt.setAutoReverse(true);
//      tt.setInterpolator(Interpolator.EASE_IN);
//      tt.play();
      PathTransition pt = new PathTransition(Duration.seconds(10), p, uis);
      pt.setCycleCount(20);
      pt.setAutoReverse(true);
      pt.play();

      root.getChildren().addAll(uib, uis);
      stage.addEventHandler(KeyEvent.KEY_TYPED, (KeyEvent k) -> {
         uib.updateUserInfo(new User("Stepanas Stepanovicius", true, LocalDateTime.now()));
         System.out.println("name changed");
      });
      stage.setScene(scene);
      stage.show();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
