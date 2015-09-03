package lt.pavilonis.monpikas.client.model;


import com.google.common.collect.ImmutableMap;
import lt.pavilonis.monpikas.client.enumeration.MealType;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Map;


public class Meal implements Serializable {

   private static final Map<MealType, String> translations = ImmutableMap.of(
         MealType.BREAKFAST, "Pusryčiai",
         MealType.LUNCH, "Priešpiečiai",
         MealType.DINNER, "Pietus",
         MealType.LATE_SUPPER, "Vakarienė",
         MealType.SUPPER, "Naktipiečiai"
   );

   private Long id;

   private String name;

   private MealType type;

   private double price;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public MealType getType() {
      return type;
   }

   public void setType(MealType type) {
      this.type = type;
   }

   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   @Override
   public String toString() {
      return translations.get(type) + " " + new DecimalFormat("0.00").format(price);
   }
}
