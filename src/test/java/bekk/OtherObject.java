package bekk;

import java.io.Serializable;

public class OtherObject implements Serializable {
   private String someValue;
   private int number;

   public OtherObject(String someValue, int number) {
      this.someValue = someValue;
      this.number = number;
   }
}
