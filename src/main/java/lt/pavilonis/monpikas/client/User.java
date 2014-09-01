package lt.pavilonis.monpikas.client;

import java.time.LocalDateTime;

public class User {
   private String name;
   private boolean dinnerPermission;
   private LocalDateTime lastDinner;

   public User(String name, boolean dinnerPermission, LocalDateTime lastDinner) {
      this.name = name;
      this.dinnerPermission = dinnerPermission;
      this.lastDinner = lastDinner;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isDinnerPermission() {
      return dinnerPermission;
   }

   public void setDinnerPermission(boolean dinnerPermission) {
      this.dinnerPermission = dinnerPermission;
   }

   public LocalDateTime getLastDinner() {
      return lastDinner;
   }

   public void setLastDinner(LocalDateTime lastDinner) {
      this.lastDinner = lastDinner;
   }
}
