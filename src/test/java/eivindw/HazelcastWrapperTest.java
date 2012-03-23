package eivindw;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import eivindw.domain.OtherObject;
import eivindw.domain.SomeObject;
import eivindw.io.KryoWrapper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class HazelcastWrapperTest {

   private IMap<Integer, Object> test;

   @AfterClass
   public static void shutdown() {
      Hazelcast.shutdownAll();
   }

   @Before
   public void setup() {
      test = Hazelcast.getMap("test");
      test.clear();
   }

   @Test
   public void simpleQuery() {
      assertQueryWorks(false);
   }

   @Test
   public void simpleQueryWithWrapper() {
      assertQueryWorks(true);
   }

   private void assertQueryWorks(boolean useWrapper) {
      test.put(1, wrap(new SomeObject("name1", 5, new OtherObject("value1", 1)), useWrapper));
      Object obj2 = new SomeObject("name2", 3, new OtherObject("value2", 2));
      test.put(2, wrap(obj2, useWrapper));
      test.put(3, wrap(new SomeObject("name3", 5, new OtherObject("value3", 3)), useWrapper));

      assertEquals(3, test.size());
      assertEquals(2, test.values(new SqlPredicate("number = 5")).size());
      assertEquals(obj2, test.values(new SqlPredicate("number = 3")).iterator().next());
   }

   private Object wrap(Object obj, boolean useWrapper) {
      return useWrapper ? new KryoWrapper(obj) : obj;
   }
}
