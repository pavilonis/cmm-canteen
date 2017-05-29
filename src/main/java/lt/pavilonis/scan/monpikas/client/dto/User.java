package lt.pavilonis.scan.monpikas.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lt.pavilonis.scan.monpikas.client.enumeration.PupilType;
import lt.pavilonis.scan.monpikas.client.model.Eating;

public final class User {

   private final String cardCode;
   private final String name;
   private final Eating eating;
   private final String grade;
   private final PupilType type;
   private final String base16photo;

   public User(
         @JsonProperty("cardCode") String cardCode,
         @JsonProperty("name") String name,
         @JsonProperty("eating") Eating eating,
         @JsonProperty("grade") String grade,
         @JsonProperty("type") PupilType type,
         @JsonProperty("base16photo") String base16photo) {

      this.cardCode = cardCode;
      this.name = name;
      this.eating = eating;
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

   public Eating getEating() {
      return eating;
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
