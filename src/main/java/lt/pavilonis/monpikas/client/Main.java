package lt.pavilonis.monpikas.client;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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

      CardBig uib = context.getBean(CardBig.class);
      uib.initialize();
      uib.updateUserInfo(new User("Ivanas Ivanauskas Ivanovicius", true, LocalDateTime.now()));

      CardSmall uis = context.getBean(CardSmall.class);
      uis.initialize();
      uis.updateUserInfo(new User("Ivanas Ėčęą„ūųšė Ivanovičius", true, LocalDateTime.now()));

      root.getChildren().addAll(uis, uib);
      stage.addEventHandler(KeyEvent.KEY_TYPED, (KeyEvent k) -> {
         if (k.getCharacter().equals("a")) {
            uib.updateUserInfo(new User("Stepanas Stepanovicius", true, LocalDateTime.now()));
            System.out.println("got a");
         }
         if (k.getCharacter().equals("b")) {
            uib.updateUserInfo(new User("Pranas Pranavicius", false, LocalDateTime.now()));
            System.out.println("got b");
         }
         if (k.getCharacter().equals("c")) {
            uib.updateUserInfo(new User("Vovanas Vovanovicius", true, LocalDateTime.now().minusDays(3)));
            System.out.println("got c");
         }
         if (k.getCharacter().equals("d")) {
            uis.updateUserInfo(new User("Bronius Bronevicius", true, LocalDateTime.now().minusDays(3)));
            System.out.println("got d");
         }
      });
      stage.setScene(scene);
      stage.show();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
