package bekk;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.Callable;

public class NodeServer {
   private static final String MAP_NAME = "testmap";

   public static void main(String[] args) throws Exception {
      Hazelcast.getDefaultInstance();

      PrintStream commandline = System.out;
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      commandline.println("\nHazelcast node started!\n");

      boolean keepRunning = true;
      while (keepRunning) {
         commandline.print("> ");
         commandline.flush();
         String line = in.readLine();
         if (line == null) {
            break;
         }

         keepRunning = processLine(line);
      }

      Hazelcast.shutdownAll();
      System.exit(0);
   }

   private static boolean processLine(String line) throws Exception {
      if("quit".equals(line)) {
         return false;
      } else if("put".equals(line)) {
         putRandomValues();
      } else if("print".equals(line)) {
         printValues();
      } else if("sum".equals(line)) {
         runSumJob();
      }
      return true;
   }

   private static void runSumJob() throws Exception {
      MultiTask<Integer> task = new MultiTask<Integer>(new SumCallable(), Hazelcast.getCluster().getMembers());
      Hazelcast.getExecutorService().execute(task);
      int sumAll = 0;
      for(int result : task.get()) {
         System.out.println("Adding sum: " + result);
         sumAll += result;
      }
      System.out.println("Sum of all: " + sumAll);
   }

   private static void printValues() {
      IMap<Integer, NumberHolder> map = Hazelcast.getMap(MAP_NAME);
      for(int key : map.localKeySet()) {
         System.out.println("Key: "  + key + " Value: " + map.get(key));
      }
   }

   private static void putRandomValues() {
      IMap<Integer, NumberHolder> map = Hazelcast.getMap(MAP_NAME);
      Random rand = new Random();
      for(int i = 1; i < 20; i++) {
         map.put(rand.nextInt(100000), new NumberHolder(rand.nextInt(100), rand.nextInt(100)));
      }
   }

   private static class NumberHolder implements Serializable {
      private int num1;
      private int num2;

      private NumberHolder(int num1, int num2) {
         this.num1 = num1;
         this.num2 = num2;
      }
      
      int getSum() {
         return num1 + num2;
      }

      @Override
      public String toString() {
         return "numbers: " + num1 + " " + num2 + " sum: " + getSum();
      }
   }

   private static class SumCallable implements Callable<Integer>, Serializable {
      public Integer call() throws Exception {
         System.out.println("Calculating total sum for local values");
         IMap<Integer, NumberHolder> map = Hazelcast.getMap(MAP_NAME);
         int sum = 0;
         for(int key : map.localKeySet()) {
            sum += map.get(key).getSum();
         }
         System.out.println("My sum: " + sum);
         return sum;
      }
   }
}
