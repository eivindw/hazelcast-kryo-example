package bekk;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ObjectBuffer;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;

public class KryoWrapper<T> implements Serializable {
   
   private byte[] serialized;

   public KryoWrapper(T target) {
      Kryo kryo = new Kryo();
      kryo.register(SomeObject.class);
      kryo.register(OtherObject.class);
      kryo.register(HashMap.class);
      ObjectBuffer objectBuffer = new ObjectBuffer(kryo, 500);

      serialized = objectBuffer.writeClassAndObject(target);
   }

   Object readResolve() throws ObjectStreamException {
      Kryo kryo = new Kryo();
      kryo.register(SomeObject.class);
      kryo.register(OtherObject.class);
      kryo.register(HashMap.class);
      ObjectBuffer objectBuffer = new ObjectBuffer(kryo, 500);
      
      return objectBuffer.readClassAndObject(serialized);
   }
}
