package io;

import eivindw.io.KryoSerializer;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class W implements Serializable {
   
   private byte[] s;

   public W(Object target) {
      s = KryoSerializer.write(target);
   }

   private Object readResolve() throws ObjectStreamException {
      return KryoSerializer.read(s);
   }
}
