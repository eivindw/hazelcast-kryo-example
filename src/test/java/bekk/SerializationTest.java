package bekk;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ObjectBuffer;
import com.hazelcast.impl.GroupProperties;
import com.hazelcast.nio.Data;
import com.hazelcast.nio.Serializer;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

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
   public void standardJavaSerializationWithKryoWrapper() throws Exception {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(500);
      ObjectOutputStream oos = new ObjectOutputStream(baos);

      oos.writeObject(new KryoWrapper<SomeObject>(obj));

      System.out.println("Standard Java with Kryo wrapper size: " + baos.size());
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

      SomeObject deserObj = objectBuffer.readObjectData(bytes, SomeObject.class);

      assertEquals(obj.getName(), deserObj.getName());
   }

   @Test
   public void hazelcastSerialization() throws Exception {
      System.setProperty(GroupProperties.PROP_SERIALIZER_GZIP_ENABLED, "true");
      Serializer serializer = new Serializer();

      Data data = serializer.writeObject(obj);

      System.out.println("Hazelcast size: " + data.size());

      SomeObject deserObj = ((SomeObject) serializer.readObject(data)).unpack();

      assertEquals(obj.getName(), deserObj.getName());
   }
}
