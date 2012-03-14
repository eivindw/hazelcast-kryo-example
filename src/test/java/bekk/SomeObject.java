package bekk;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ObjectBuffer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SomeObject implements Externalizable {
   private String name;
   private int number;
   private OtherObject obj;
   private Map<Integer, String> map;

   private transient byte[] bytesSer;

   public SomeObject() {
   }

   public String getName() {
      return name;
   }

   public int getNumber() {
      return number;
   }

   public OtherObject getObj() {
      return obj;
   }

   public Map<Integer, String> getMap() {
      return map;
   }

   public SomeObject(String name, int number, OtherObject obj) {
      this.name = name;
      this.number = number;
      this.obj = obj;
      map = new HashMap<Integer, String>();
      map.put(1, "test");
      map.put(2, "test2");
   }

   public void writeExternal(ObjectOutput out) throws IOException {
      Kryo kryo = new Kryo();
      kryo.register(SomeObject.class);
      kryo.register(OtherObject.class);
      kryo.register(HashMap.class);
      ObjectBuffer objectBuffer = new ObjectBuffer(kryo, 500);

      out.write(objectBuffer.writeObjectData(this));
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
      bytesSer = new byte[in.available()];
      in.read(bytesSer);
   }

   public SomeObject unpack() {
      Kryo kryo = new Kryo();
      kryo.register(SomeObject.class);
      kryo.register(OtherObject.class);
      kryo.register(HashMap.class);
      ObjectBuffer objectBuffer = new ObjectBuffer(kryo, 500);

      return objectBuffer.readObjectData(bytesSer, SomeObject.class);
   }
}
