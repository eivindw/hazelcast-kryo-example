package eivindw.io;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class KryoWrapper implements Serializable {

   private byte[] s;

   public KryoWrapper(Object target) {
      s = KryoSerializer.write(target);
   }

   private Object readResolve() throws ObjectStreamException {
      return KryoSerializer.read(s);
   }
}
