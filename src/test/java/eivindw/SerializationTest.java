package eivindw;

import com.hazelcast.nio.Data;
import com.hazelcast.nio.Serializer;
import eivindw.domain.OtherObject;
import eivindw.domain.SomeObject;
import eivindw.io.KryoSerializer;
import io.W;
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
      KryoSerializer.register(SomeObject.class, OtherObject.class, HashMap.class);
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

      oos.writeObject(new W(obj));

      System.out.println("Standard Java with Kryo wrapper size: " + baos.size());

      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);

      SomeObject deserObj = (SomeObject) ois.readObject();

      assertEquals(obj, deserObj);
   }

   @Test
   public void kryoSerialization() throws Exception {
      byte[] bytes = KryoSerializer.write(obj);

      System.out.println("Kryo size: " + bytes.length);

      SomeObject deserObj = (SomeObject) KryoSerializer.read(bytes);

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

      Data data = serializer.writeObject(new W(obj));

      System.out.println("Hazelcast kryo wrapper size: " + data.size());

      SomeObject deserObj = (SomeObject) serializer.readObject(data);

      assertEquals(obj, deserObj);
   }
}
