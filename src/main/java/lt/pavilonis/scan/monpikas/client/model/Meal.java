package lt.pavilonis.scan.monpikas.client.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import lt.pavilonis.scan.monpikas.client.enumeration.MealType;

import java.text.DecimalFormat;
import java.util.Map;

public final class Meal {

   private static final Map<MealType, String> translations = ImmutableMap.of(
         MealType.BREAKFAST, "Pusryčiai",
         MealType.LUNCH, "Priešpiečiai",
         MealType.DINNER, "Pietus",
         MealType.LATE_SUPPER, "Vakarienė",
         MealType.SUPPER, "Naktipiečiai"
   );

   private final Long id;
   private final String name;
   private final MealType type;
   private final double price;

   public Meal(
         @JsonProperty("id") Long id,
         @JsonProperty("name") String name,
         @JsonProperty("type") MealType type,
         @JsonProperty("price") double price) {
      this.id = id;
      this.name = name;
      this.type = type;
      this.price = price;
   }

   @Override
   public String toString() {
      return translations.get(type) + " " + new DecimalFormat("0.00").format(price);
   }
}
