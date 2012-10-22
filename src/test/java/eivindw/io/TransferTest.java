package eivindw.io;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.impl.GroupProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TransferTest {

   private HazelcastInstance instance;

   @Before
   public void setup() {
      System.out.println("Building test-grid");
      Config config = new Config();
      config.setProperty(GroupProperties.PROP_LOGGING_TYPE, "none");

      Hazelcast.newHazelcastInstance(config);
      Hazelcast.newHazelcastInstance(config);
      Hazelcast.newHazelcastInstance(config);

      config.setLiteMember(true);
      instance = Hazelcast.newHazelcastInstance(config);
   }

   @After
   public void cleanup() {
      System.out.println("Shutting down test-grid");
      Hazelcast.shutdownAll();
   }

   @Test
   public void shouldTransferQuickly() {
      System.out.println("Starting put-test");
      IMap<Integer, String> map = instance.getMap("transfer");

      long before = System.nanoTime();
      for(int i = 0; i < 10000; i++) {
         map.put(i, "test" + i);
      }
      long after = System.nanoTime();

      System.out.println("Time put: " + (after-before)/1e9 + " s");
   }

   @Test
   public void shouldTransferQuicker() {
      System.out.println("Starting putAll-test");
      IMap<Integer, String> map = instance.getMap("transfer");

      long before = System.nanoTime();
      Map<Integer, String> tmpMap = new HashMap<Integer, String>();
      for(int i = 0; i < 10000; i++) {
         tmpMap.put(i, "test" + i);
      }
      map.putAll(tmpMap);
      long after = System.nanoTime();

      System.out.println("Time putAll: " + (after-before)/1e9 + " s");
   }
}
