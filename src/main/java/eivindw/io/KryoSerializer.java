package eivindw.io;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ObjectBuffer;

public final class KryoSerializer {

   private static final Kryo kryo = new Kryo();

   static {
      kryo.setRegistrationOptional(true);
   }
   
   public static void register(Class... classes) {
      for(Class clazz : classes) {
         kryo.register(clazz);
      }
   }

   public static byte[] write(Object obj) {
      ObjectBuffer objectBuffer = new ObjectBuffer(kryo);
      return objectBuffer.writeClassAndObject(obj);
   }
   
   public static Object read(byte[] bytes) {
      ObjectBuffer objectBuffer = new ObjectBuffer(kryo);
      return objectBuffer.readClassAndObject(bytes);
   }
}
