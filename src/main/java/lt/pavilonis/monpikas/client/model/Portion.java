package lt.pavilonis.monpikas.client.model;

import lt.pavilonis.monpikas.client.enumeration.PortionType;

import java.io.Serializable;
import java.text.DecimalFormat;

import static lt.pavilonis.monpikas.client.enumeration.PortionType.BREAKFAST;

public class Portion implements Serializable {

   public Portion() {
   }

   private Long id;

   private String name;

   private PortionType type;

   private double price;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Double getPrice() {
      return price;
   }

   public void setPrice(Double price) {
      this.price = price;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public PortionType getType() {
      return type;
   }

   public void setType(PortionType type) {
      this.type = type;
   }

   @Override
   public String toString() {
      return (type == BREAKFAST ? "Pusryčiai " : "Pietus ") + new DecimalFormat("0.00").format(price);
   }
}
