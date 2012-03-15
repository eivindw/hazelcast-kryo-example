package eivindw.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SomeObject implements Serializable {
   private String name;
   private int number;
   private OtherObject obj;
   private Map<Integer, String> map;

   public SomeObject() {
   }

   public SomeObject(String name, int number, OtherObject obj) {
      this.name = name;
      this.number = number;
      this.obj = obj;
      map = new HashMap<Integer, String>();
      map.put(1, "test");
      map.put(2, "test2");
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      SomeObject that = (SomeObject) o;

      if (number != that.number) return false;
      if (map != null ? !map.equals(that.map) : that.map != null) return false;
      if (name != null ? !name.equals(that.name) : that.name != null) return false;
      if (obj != null ? !obj.equals(that.obj) : that.obj != null) return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = name != null ? name.hashCode() : 0;
      result = 31 * result + number;
      result = 31 * result + (obj != null ? obj.hashCode() : 0);
      result = 31 * result + (map != null ? map.hashCode() : 0);
      return result;
   }
}
