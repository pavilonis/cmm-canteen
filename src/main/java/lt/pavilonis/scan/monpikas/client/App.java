package lt.pavilonis.scan.monpikas.client;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class App extends Application {

   static Group root = new Group();
   static Stage stage;

   @Override
   public void start(Stage stage) throws Exception {
      App.stage = stage;
      new AnnotationConfigApplicationContext(AppConfig.class);
      stage.setScene(new Scene(root, 1440, 900, Color.WHITE));
      stage.show();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
