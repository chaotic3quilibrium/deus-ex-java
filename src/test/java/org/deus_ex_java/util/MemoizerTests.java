package org.deus_ex_java.util;

import org.deus_ex_java.util.Memoizer.InsertionOrder;
import org.deus_ex_java.util.Memoizer.MethodOverride;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * File: org.java_underling.util.MemoizerTests.java
 * <p>
 * Version: v2025.08.02
 **/
public class MemoizerTests {

  @Test
  public void testLazyInstantiation() {
    var threadSleepMillis = 1000;
    var memoizerValue = Integer.valueOf(10);
    var counter = new int[]{0};
    var isExceptionDetected = new boolean[1];
    var memoizer = Memoizer.lazyInstantiation(() -> {
      try {
        Thread.sleep(threadSleepMillis);
      } catch (InterruptedException e) {
        isExceptionDetected[0] = true;
        throw new RuntimeException(e);
      }
      ++counter[0];

      return memoizerValue + counter[0] - 1;
    });
    assertEquals(0, counter[0]);
    var threads = Stream.<Thread>generate(() ->
            new Thread(
                null,
                () -> {
                  var memoizerResultThreadSpawned = memoizer.get();
                  assertEquals(memoizerValue, memoizerResultThreadSpawned);
                }))
        .limit(10)
        .toList();
    assertEquals(0, counter[0]);
    assertFalse(isExceptionDetected[0]);
    var startMillis = System.currentTimeMillis();
    threads.forEach(Thread::start);
    assertEquals(0, counter[0]);
    var memoizerResultThreadMain = memoizer.get(); //thread blocks until timeout in memoizer elapses
    var totalMillis = System.currentTimeMillis() - startMillis;
    //System.out.println("totalMillis: " + totalMillis);
    assertEquals(1, counter[0]);
    assertFalse(isExceptionDetected[0]);
    assertTrue(totalMillis >= threadSleepMillis);
    assertEquals(memoizerValue, memoizerResultThreadMain);
    var memoizerResultThreadMain2 = memoizer.get(); //get the value again
    assertEquals(1, counter[0]); //ensure that the executeExactlyOnce is not re-executed
    assertFalse(isExceptionDetected[0]); //ensure that no exception was generated (which seems impossible if the prior statement confirmed the lambda was not re-executed
  }

  @Test
  public void testLazyInstantiationSupplierException() {
    var memoizerValue = Integer.valueOf(10);
    var counter = new int[]{0};
    var memoizer = Memoizer.lazyInstantiation(() -> {
      ++counter[0];
      var oopsie = 999 / (counter[0] - 1);
      //should never get here as prior expression contains a divide by zero
      System.out.println("oopsie: " + oopsie);

      return memoizerValue + counter[0] - 1;
    });
    assertEquals(0, counter[0]);
    var thread = new Thread(
        null,
        () -> {
          var memoizerResultThreadSpawned = memoizer.get();
          // memoizer.get throws an exception which we swallow and return null
          assertNull(memoizerResultThreadSpawned);
        });
    var isExceptionDetected = new boolean[1];
    Thread.setDefaultUncaughtExceptionHandler(
        (t, e) -> {
          // memoizer.get exception is swallowed and thread's assert should pass
          isExceptionDetected[0] = true;
        });
    thread.start();
    assertNull(memoizer.get()); //block until the other thread gets out of the way and allows this one through
    assertEquals(1, counter[0]); //ensure the thread executed the executeExactlyOnce lambda exactly once, prior to the exception
    assertFalse(isExceptionDetected[0]); //ensure the exception was successfully caught and suppressed
    assertNull(memoizer.get()); //ensure null is consistently being returned
    assertEquals(1, counter[0]); //ensure we are not re-executing the executeExactlyOnce lambda
  }

  private void testFactoriesExhaustiveHelper(
      boolean isDefiningDefaultDeriveVFromK,
      MethodOverride methodOverride,
      InsertionOrder insertionOrder
  ) {
    Function<Integer, Integer> lambdaDefaultTimes2 = (integer) -> integer * 2;
    Function<Integer, Integer> lambdaOverrideMinus1 = (integer) -> integer - 1;
    Function<Integer, Integer> lambdaOverridePlus1 = (integer) -> integer + 1;
    Memoizer<Integer, Integer> memoizer =
        isDefiningDefaultDeriveVFromK
            ? methodOverride == MethodOverride.ALLOWED //isAllowingMethodOverride
            ? insertionOrder == InsertionOrder.RETAIN //isRetainingInsertionOrder
            ? Memoizer.from(lambdaDefaultTimes2, methodOverride, insertionOrder)
            : Memoizer.from(lambdaDefaultTimes2, methodOverride)
            : insertionOrder == InsertionOrder.RETAIN //isRetainingInsertionOrder
                ? Memoizer.from(lambdaDefaultTimes2, methodOverride, insertionOrder)
                : Memoizer.from(lambdaDefaultTimes2)
            : insertionOrder == InsertionOrder.RETAIN //isRetainingInsertionOrder
                ? Memoizer.from(insertionOrder)
                : Memoizer.from();
    assertTrue(memoizer.keySet().isEmpty());
    assertEquals(methodOverride, memoizer.getMethodOverride());
    assertEquals(insertionOrder, memoizer.getInsertionOrder());
    if (isDefiningDefaultDeriveVFromK) {
      if (methodOverride == MethodOverride.ALLOWED) {
        //get(k) should return a value
        assertEquals(50, memoizer.get(25)); //default lambda invoked
        //get(k, overrideDeriveVFromK) should return a value
        assertEquals(26, memoizer.get(27, lambdaOverrideMinus1)); //override lambda invoked with default lambda ignored
        assertFalse(memoizer.keySet().isEmpty());
        assertEquals(Set.of(25, 27), memoizer.keySet());
        assertEquals(50, memoizer.get(25)); //default lambda ignored
        assertEquals(52, memoizer.get(26)); //default lambda invoked
        assertEquals(Set.of(25, 27, 26), memoizer.keySet());
        if (insertionOrder == InsertionOrder.RETAIN) {
          var keys = IntStream.range(20, 30)
              .mapToObj(index -> {
                memoizer.get(index); //override lambda invoked for all but 25, 26, and 27

                return index;
              })
              .toList();
          assertNotEquals(keys, memoizer.keySet().stream().toList());
          assertEquals(List.of(25, 27, 26, 20, 21, 22, 23, 24, 28, 29), memoizer.keySet().stream().toList());
        }
      } else {
        //get(k, overrideDeriveVFromK) should throw an exception
        var unsupportedOperationException =
            assertThrows(
                UnsupportedOperationException.class,
                () -> memoizer.get(15, lambdaOverrideMinus1)
            );
        assertEquals(
            "overrideDeriveVFromK is restricted from being provided by the from() factory method selected",
            unsupportedOperationException.getMessage());
        assertTrue(memoizer.keySet().isEmpty());
        //get(k) should return a value
        assertEquals(30, memoizer.get(15)); //default lambda invoked
        assertFalse(memoizer.keySet().isEmpty());
        assertEquals(Set.of(15), memoizer.keySet());
        assertEquals(30, memoizer.get(15)); //default lambda ignored
        assertEquals(34, memoizer.get(17)); //default lambda invoked
        assertEquals(Set.of(15, 17), memoizer.keySet());
        if (insertionOrder == InsertionOrder.RETAIN) {
          var keys = IntStream.range(10, 20)
              .mapToObj(index -> {
                memoizer.get(index); //override lambda invoked for all but 15 and 17

                return index;
              })
              .toList();
          assertNotEquals(keys, memoizer.keySet().stream().toList());
          assertEquals(List.of(15, 17, 10, 11, 12, 13, 14, 16, 18, 19), memoizer.keySet().stream().toList());
        }
      }
    } else {
      assertEquals(MethodOverride.ALLOWED, methodOverride); //isAllowingMethodOverride);
      //get(k) should throw an exception
      var illegalArgumentException =
          assertThrows(
              IllegalArgumentException.class,
              () -> memoizer.get(5)
          );
      assertEquals(
          "when calling the get() method without providing an overrideDeriveVFromK function, defaultDeriveVFromK must be provided in the from() factory method",
          illegalArgumentException.getMessage());
      assertTrue(memoizer.keySet().isEmpty());
      //get(k, overrideDeriveVFromK) should return a value
      assertEquals(4, memoizer.get(5, lambdaOverrideMinus1)); //override lambda invoked
      assertFalse(memoizer.keySet().isEmpty());
      assertEquals(Set.of(5), memoizer.keySet());
      assertEquals(4, memoizer.get(5)); //should now work as the key is present
      assertEquals(4, memoizer.get(5, lambdaOverridePlus1)); //override lambda ignored
      assertEquals(Set.of(5), memoizer.keySet());
      assertEquals(8, memoizer.get(7, lambdaOverridePlus1)); //override lambda invoked
      assertEquals(Set.of(5, 7), memoizer.keySet());
      assertEquals(8, memoizer.get(7)); //should now work as the key is present
      assertEquals(8, memoizer.get(7, lambdaOverrideMinus1)); //override lambda ignored
      assertEquals(Set.of(5, 7), memoizer.keySet());
      if (insertionOrder == InsertionOrder.RETAIN) {
        var keys = IntStream.range(0, 10)
            .mapToObj(index -> {
              memoizer.get(index, lambdaOverrideMinus1); //override lambda invoked for all but 5 and 7

              return index;
            })
            .toList();
        assertNotEquals(keys, memoizer.keySet().stream().toList());
        assertEquals(List.of(5, 7, 0, 1, 2, 3, 4, 6, 8, 9), memoizer.keySet().stream().toList());
      }
    }
  }

  @Test
  public void testFactoriesExhaustive() {
    //test all variations of from()
    //  from()
    //      equivalent: from(null, true, false)
    //  from(Function<K, V> defaultDeriveVFromK)
    //      equivalent: from(defaultDeriveVFromK, false, false)
    //  from(Function<K, V> defaultDeriveVFromK, boolean isAllowingMethodOverride)
    //      equivalent: from(defaultDeriveVFromK, isAllowingMethodOverride, false)
    //  from(boolean isRetainingInsertionOrder)
    //      equivalent: from(null, true, isRetainingInsertionOrder)
    //  from(Function<K, V> defaultDeriveVFromK, boolean isAllowingMethodOverride, boolean isRetainingInsertionOrder)
    Stream.of(false, true)
        .forEach(isDefiningDefaultDeriveVFromK ->
            (isDefiningDefaultDeriveVFromK
                ? Stream.of(MethodOverride.INHIBITED, MethodOverride.ALLOWED)
                : Stream.of(MethodOverride.ALLOWED))
                .forEach(methodOverride ->
                    Stream.of(InsertionOrder.NONE, InsertionOrder.RETAIN)
                        .forEach(insertionOrder ->
                            testFactoriesExhaustiveHelper(
                                isDefiningDefaultDeriveVFromK,
                                methodOverride,
                                insertionOrder))));
  }
}
