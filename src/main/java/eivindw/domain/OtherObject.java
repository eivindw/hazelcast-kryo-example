package eivindw.domain;

import java.io.Serializable;

public class OtherObject implements Serializable {
   private String someValue;
   private int number;

   public OtherObject() {
   }

   public OtherObject(String someValue, int number) {
      this.someValue = someValue;
      this.number = number;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      OtherObject that = (OtherObject) o;

      if (number != that.number) return false;
      if (someValue != null ? !someValue.equals(that.someValue) : that.someValue != null) return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = someValue != null ? someValue.hashCode() : 0;
      result = 31 * result + number;
      return result;
   }
}
