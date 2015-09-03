package lt.pavilonis.monpikas.client.dto;

import javafx.scene.image.Image;
import lt.pavilonis.monpikas.client.enumeration.PupilType;
import lt.pavilonis.monpikas.client.model.Meal;

import java.io.Serializable;

public class ClientPupilDto implements Serializable {

   private long id;
   private String name;
   private Meal meal;
   private String grade;
   private PupilType type;
   private Image image;

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Meal getMeal() {
      return meal;
   }

   public void setMeal(Meal meal) {
      this.meal = meal;
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

   public PupilType getType() {
      return type;
   }

   public void setType(PupilType type) {
      this.type = type;
   }
}
