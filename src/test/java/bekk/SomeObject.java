package bekk;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SomeObject implements Serializable {
   private String name;
   private int number;
   private OtherObject obj;
   private Map<Integer, String> map;

   public SomeObject(String name, int number, OtherObject obj) {
      this.name = name;
      this.number = number;
      this.obj = obj;
      map = new HashMap<Integer, String>();
      map.put(1, "test");
      map.put(2, "test2");
   }
}
