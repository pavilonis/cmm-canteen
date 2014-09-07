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
   private final int CORRECTION = 2;
   private long lastInput;

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
            digits.forEach(d -> digits.set(digits.indexOf(d), correct(d)));
            listenerController.requestUser(digits);
            digits.clear(); //TODO maybe digits should be cleared before controller call
         }
      }
      lastInput = now;
   }

   private int correct(int digit) {
      digit += CORRECTION;
      if (digit >= 10) {
         digit -= 10;
      } else if (digit < 0) {
         digit += 10;
      }
      return digit;
   }
}

