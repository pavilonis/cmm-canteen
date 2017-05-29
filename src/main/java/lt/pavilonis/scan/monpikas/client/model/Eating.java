package lt.pavilonis.scan.monpikas.client.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import lt.pavilonis.scan.monpikas.client.enumeration.EatingType;

import java.text.DecimalFormat;
import java.util.Map;

public final class Eating {

   private static final Map<EatingType, String> translations = ImmutableMap.of(
         EatingType.BREAKFAST, "Pusryčiai",
         EatingType.LUNCH, "Priešpiečiai",
         EatingType.DINNER, "Pietus",
         EatingType.LATE_SUPPER, "Vakarienė",
         EatingType.SUPPER, "Naktipiečiai"
   );

   private final Long id;
   private final String name;
   private final EatingType type;
   private final double price;

   public Eating(
         @JsonProperty("id") Long id,
         @JsonProperty("name") String name,
         @JsonProperty("type") EatingType type,
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
