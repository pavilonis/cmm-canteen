package lt.pavilonis.scan.monpikas.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lt.pavilonis.scan.monpikas.client.enumeration.PupilType;
import lt.pavilonis.scan.monpikas.client.model.Meal;

public final class User {

   private final String cardCode;
   private final String name;
   private final Meal meal;
   private final String grade;
   private final PupilType type;
   private final String base16photo;

   public User(
         @JsonProperty("cardCode") String cardCode,
         @JsonProperty("name") String name,
         @JsonProperty("meal") Meal meal,
         @JsonProperty("grade") String grade,
         @JsonProperty("type") PupilType type,
         @JsonProperty("base16photo") String base16photo) {

      this.cardCode = cardCode;
      this.name = name;
      this.meal = meal;
      this.grade = grade;
      this.type = type;
      this.base16photo = base16photo;
   }

   public String getCardCode() {
      return cardCode;
   }

   public String getName() {
      return name;
   }

   public Meal getMeal() {
      return meal;
   }

   public String getGrade() {
      return grade;
   }

   public PupilType getType() {
      return type;
   }

   public String getBase16photo() {
      return base16photo;
   }
}
