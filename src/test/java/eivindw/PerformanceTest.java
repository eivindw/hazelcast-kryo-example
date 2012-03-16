package eivindw;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.impl.GroupProperties;
import eivindw.domain.OtherObject;
import eivindw.domain.SomeObject;
import eivindw.io.KryoSerializer;
import io.W;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

import static junit.framework.Assert.assertNotNull;

public class PerformanceTest {

   private static final int COUNT = 1000;
   private static final SomeObject[] objects = new SomeObject[COUNT];

   @BeforeClass
   public static void initHazelcast() {
      System.out.println("Initializaing Hazelcast");
      KryoSerializer.register(SomeObject.class, OtherObject.class, HashMap.class);
      System.setProperty(GroupProperties.PROP_LOGGING_TYPE, "none");
      Hazelcast.getDefaultInstance();
      for(int i = 0; i < COUNT; i++) {
         objects[i] = new SomeObject("name" + i, i, new OtherObject("val" + i, i));
      }
      System.out.println("Done creating " + COUNT + " test objects");
   }

   @AfterClass
   public static void shutdownHazelcast() {
      System.out.println("Shutting down Hazelcast");
      Hazelcast.shutdownAll();
   }

   @Test
   public void runMany() {
      int count = 0;
      while(++count <= 5) {
         System.out.println("### Run " + count + " ###");
         standardSerialization();
         kryoWrapperValuesSerialization();
      }
   }

   @Test @Ignore
   public void standardSerialization() {
      runTest("standard", false, false);
   }

   @Test @Ignore
   public void kryoWrapperValuesSerialization() {
      runTest("wrpValue", true, false);
   }

   @Test @Ignore
   public void kryoWrapperKeySerialization() {
      runTest("wrpKey", false, true);
   }

   @Test @Ignore
   public void kryoWrapperAllSerialization() {
      runTest("wrpAll", true, true);
   }

   private void runTest(String mapName, boolean wrapValue, boolean wrapKey) {
      IMap<Object, Object> testMap = Hazelcast.getMap(mapName);
      long before = System.nanoTime();
      for (int i = 0; i < COUNT; i++) {
         SomeObject obj = objects[i];
         Object key = wrapKey ? new W(i) : i;
         Object value = wrapValue ? new W(obj) : obj;
         testMap.put(key, value);
      }
      for (int i = 0; i < COUNT; i++) {
         Object key = wrapKey ? new W(i) : i;
         SomeObject obj = (SomeObject) testMap.get(key);
         assertNotNull(obj);
      }
      long after = System.nanoTime();
      int randomKey = new Random().nextInt(COUNT);
      long oneEntryCost = testMap.getMapEntry(wrapKey ? new W(randomKey) : randomKey).getCost();
      double estimatedCost = (oneEntryCost * COUNT) / 1e6;
      System.out.println(String.format(
         "%s - time: %.1f ms size one: %d bytes estimated size: %.1f MB", mapName, (after - before) / 1e6, oneEntryCost, estimatedCost));
      testMap.clear();
   }
}
