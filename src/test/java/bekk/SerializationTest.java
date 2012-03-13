package bekk;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ObjectBuffer;
import com.hazelcast.impl.GroupProperties;
import com.hazelcast.nio.DefaultSerializer;
import com.hazelcast.nio.FastByteArrayOutputStream;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SerializationTest {

   private final SomeObject obj = new SomeObject("test", 123, new OtherObject("value", 321));

   @Test
   public void standardJavaSerialization() throws Exception {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(500);
      ObjectOutputStream oos = new ObjectOutputStream(baos);

      oos.writeObject(obj);

      System.out.println("Standard Java size: " + baos.size());
   }
   
   @Test
   public void kryoSerialization() throws Exception {
      Kryo kryo = new Kryo();
      kryo.register(SomeObject.class);
      kryo.register(OtherObject.class);
      kryo.register(HashMap.class);
      ObjectBuffer objectBuffer = new ObjectBuffer(kryo, 500);

      byte[] bytes = objectBuffer.writeObjectData(obj);

      System.out.println("Kryo size: " + bytes.length);
   }

   @Test
   public void hazelcastSerialization() throws Exception {
      System.setProperty(GroupProperties.PROP_SERIALIZER_GZIP_ENABLED, "true");
      DefaultSerializer serializer = new DefaultSerializer();
      FastByteArrayOutputStream oos = new FastByteArrayOutputStream(500);

      serializer.write(oos, obj);

      System.out.println("Hazelcast size: " + oos.size());
   }
}
