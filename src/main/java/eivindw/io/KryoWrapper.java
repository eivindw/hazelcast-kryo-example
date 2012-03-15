package eivindw.io;

import com.esotericsoftware.kryo.ObjectBuffer;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class KryoWrapper implements Serializable {
   
   private byte[] serialized;

   public KryoWrapper(Object target) {
      ObjectBuffer objectBuffer = new ObjectBuffer(KryoSerializer.getKryo());
      serialized = objectBuffer.writeClassAndObject(target);
   }

   private Object readResolve() throws ObjectStreamException {
      ObjectBuffer objectBuffer = new ObjectBuffer(KryoSerializer.getKryo());
      return objectBuffer.readClassAndObject(serialized);
   }
}
