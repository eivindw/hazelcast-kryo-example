package eivindw;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ObjectBuffer;
import com.hazelcast.impl.GroupProperties;
import com.hazelcast.nio.Data;
import com.hazelcast.nio.Serializer;
import eivindw.domain.OtherObject;
import eivindw.domain.SomeObject;
import eivindw.io.KryoWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

public class SerializationTest {

   private final SomeObject obj = new SomeObject("test", 123, new OtherObject("value", 321));
   
   @BeforeClass
   public static void setupClass() {
      System.setProperty(GroupProperties.PROP_SERIALIZER_GZIP_ENABLED, "true");
   }

   @Test
   public void standardJavaSerialization() throws Exception {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(500);
      ObjectOutputStream oos = new ObjectOutputStream(baos);

      oos.writeObject(obj);

      System.out.println("Standard Java size: " + baos.size());

      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);

      SomeObject deserObj = (SomeObject) ois.readObject();

      assertEquals(obj, deserObj);
   }

   @Test
   public void standardJavaSerializationWithKryoWrapper() throws Exception {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(500);
      ObjectOutputStream oos = new ObjectOutputStream(baos);

      oos.writeObject(new KryoWrapper(obj));

      System.out.println("Standard Java with Kryo wrapper size: " + baos.size());

      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);

      SomeObject deserObj = (SomeObject) ois.readObject();

      assertEquals(obj, deserObj);
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

      assertEquals(obj, deserObj);
   }

   @Test
   public void hazelcastStandardSerialization() throws Exception {
      Serializer serializer = new Serializer();

      Data data = serializer.writeObject(obj);

      System.out.println("Hazelcast standard size: " + data.size());

      SomeObject deserObj = (SomeObject) serializer.readObject(data);

      assertEquals(obj, deserObj);
   }

   @Test
   public void hazelcastWrapperSerialization() throws Exception {
      Serializer serializer = new Serializer();

      Data data = serializer.writeObject(new KryoWrapper(obj));

      System.out.println("Hazelcast kryo wrapper size: " + data.size());

      SomeObject deserObj = (SomeObject) serializer.readObject(data);

      assertEquals(obj, deserObj);
   }
}
