package lt.pavilonis.monpikas.client;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BarcodeScannerInputHandler implements EventHandler<KeyEvent> {

   @Autowired
   private ViewController listenerController;
   private List<Integer> digits = new ArrayList<>();
   private final long INPUT_PAUSE = 50;
   private long lastInput;
   private StringBuilder barcode = new StringBuilder();

   @Override
   public void handle(KeyEvent event) {
      long now = System.nanoTime();

      //clearing last input (could be entered from keyboard)
      if ((now - lastInput) / 1000000 > INPUT_PAUSE) {
         digits.clear();
      }

      Character c = event.getCharacter().charAt(0);
      if (Character.isDigit(c)) {
         digits.add(Character.getNumericValue(c));
         if (digits.size() == 4) {
            barcode.setLength(0);
            digits.forEach(barcode::append);
            listenerController.scanEventAction(barcode.toString());
            digits.clear(); //TODO maybe digits should be cleared BEFORE controller call
         }
      }
      lastInput = now;
   }
}

