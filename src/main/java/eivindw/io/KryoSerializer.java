package eivindw.io;

import com.esotericsoftware.kryo.Kryo;
import eivindw.domain.OtherObject;
import eivindw.domain.SomeObject;

import java.util.HashMap;

public final class KryoSerializer {

   private static final Kryo kryo = new Kryo();

   static {
      kryo.setRegistrationOptional(true);
      kryo.register(SomeObject.class);
      kryo.register(OtherObject.class);
      kryo.register(HashMap.class);
   }

   public static Kryo getKryo() {
      return kryo;
   }
}
