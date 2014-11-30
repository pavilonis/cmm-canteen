package lt.pavilonis.monpikas.client.dto;

import javafx.scene.image.Image;
import lt.pavilonis.monpikas.client.model.Portion;

import java.io.Serializable;

public class ClientPupilDto implements Serializable {

   private long cardId;
   private String name;
   private Portion portion;
   private String grade;
   private Image image;

   public ClientPupilDto() {
   }

   public ClientPupilDto(long cardId, String name, Portion portion, String grade) {
      this.cardId = cardId;
      this.name = name;
      this.portion = portion;
      this.grade = grade;
   }

   public long getCardId() {
      return cardId;
   }

   public void setCardId(long cardId) {
      this.cardId = cardId;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Portion getPortion() {
      return portion;
   }

   public void setPortion(Portion portion) {
      this.portion = portion;
   }

   public String getGrade() {
      return grade;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }

   public Image getImage() {
      return image;
   }

   public void setImage(Image image) {
      this.image = image;
   }
}
