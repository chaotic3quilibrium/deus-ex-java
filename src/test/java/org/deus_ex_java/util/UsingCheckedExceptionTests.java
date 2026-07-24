package org.deus_ex_java.util;

import org.deus_ex_java.lang.ControlBreakThrowable;
import org.deus_ex_java.lang.WrappedCheckedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsingCheckedExceptionTests {

  private static final class TestControlBreakThrowable extends ControlBreakThrowable {
    TestControlBreakThrowable(String message) {
      super(message);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T extends Throwable> void sneakyThrow(Throwable throwable) throws T {
    throw (T) throwable;
  }

  private static class MockResource implements AutoCloseable {
    private final String name;
    private final List<String> closeLog;
    private final Exception closeException;
    private final Throwable fatalCloseThrowable;
    private boolean closed = false;

    MockResource(String name, List<String> closeLog) {
      this(name, closeLog, null, null);
    }

    MockResource(String name, List<String> closeLog, Exception closeException) {
      this(name, closeLog, closeException, null);
    }

    MockResource(String name, List<String> closeLog, Throwable fatalCloseThrowable) {
      this(name, closeLog, null, fatalCloseThrowable);
    }

    MockResource(String name, List<String> closeLog, Exception closeException, Throwable fatalCloseThrowable) {
      this.name = name;
      this.closeLog = closeLog;
      this.closeException = closeException;
      this.fatalCloseThrowable = fatalCloseThrowable;
    }

    @Override
    public void close() throws Exception {
      closed = true;
      if (closeLog != null) {
        closeLog.add(name);
      }
      if (fatalCloseThrowable != null) {
        if (fatalCloseThrowable instanceof RuntimeException re) throw re;
        if (fatalCloseThrowable instanceof Error err) throw err;
        if (fatalCloseThrowable instanceof Exception ex) throw ex;
        throw new RuntimeException(fatalCloseThrowable);
      }
      if (closeException != null) {
        throw closeException;
      }
    }

    public boolean isClosed() {
      return closed;
    }
  }

  @Test
  public void testSingleResource() {
    var arrayCharsX100 = new char[100];
    var runtimeExceptionOrIntegerA = UsingCheckedException.apply(
        () -> new StringReader("x"),
        stringReader -> stringReader.read(arrayCharsX100)
    );
    assertTrue(runtimeExceptionOrIntegerA.isRight());
    assertEquals(1, runtimeExceptionOrIntegerA.getRight());

    var wrappedCheckedExceptionOrIntegerB = UsingCheckedException.apply(
        () -> new StringReader("x"),
        stringReader -> {
          stringReader.close();
          return stringReader.read(arrayCharsX100);
        }
    );
    assertTrue(wrappedCheckedExceptionOrIntegerB.isLeft());
    assertEquals(WrappedCheckedException.class, wrappedCheckedExceptionOrIntegerB.getLeft().getClass());
    assertEquals(IOException.class, wrappedCheckedExceptionOrIntegerB.getLeft().getCause().getClass());

    var integerA = UsingCheckedException.applyUnsafe(
        () -> new StringReader("x"),
        stringReader -> stringReader.read(arrayCharsX100)
    );
    assertEquals(1, integerA);

    assertThrows(
        WrappedCheckedException.class,
        () -> UsingCheckedException.applyUnsafe(
            () -> new StringReader("x"),
            stringReader -> {
              stringReader.close();
              return stringReader.read(arrayCharsX100);
            }
        )
    );

    var illegalStateException = assertThrows(
        IllegalStateException.class,
        () -> UsingCheckedException.applyUnsafe(
            () -> new StringReader("x"),
            stringReader -> {
              if (true) {
                throw new IllegalStateException("oopsie");
              }
              return 0;
            }
        )
    );
    assertEquals("oopsie", illegalStateException.getMessage());
  }

  @Test
  public void testTwoResources() {
    var closeLog = new ArrayList<String>();
    var res = UsingCheckedException.apply(
        () -> new MockResource("A", closeLog),
        () -> new MockResource("B", closeLog),
        tuple -> tuple._1().name + tuple._2().name
    );
    assertTrue(res.isRight());
    assertEquals("AB", res.getRight());
    assertEquals(List.of("B", "A"), closeLog);

    closeLog.clear();
    var valUnsafe = UsingCheckedException.applyUnsafe(
        () -> new MockResource("A", closeLog),
        () -> new MockResource("B", closeLog),
        tuple -> tuple._1().name + tuple._2().name
    );
    assertEquals("AB", valUnsafe);
    assertEquals(List.of("B", "A"), closeLog);

    closeLog.clear();
    var resNested = UsingCheckedException.applyNested(
        () -> new MockResource("A", closeLog),
        a -> new MockResource("B", closeLog),
        tuple -> tuple._1().name + tuple._2().name
    );
    assertTrue(resNested.isRight());
    assertEquals("AB", resNested.getRight());
    assertEquals(List.of("B", "A"), closeLog);

    closeLog.clear();
    var valNestedUnsafe = UsingCheckedException.applyNestedUnsafe(
        () -> new MockResource("A", closeLog),
        a -> new MockResource("B", closeLog),
        tuple -> tuple._1().name + tuple._2().name
    );
    assertEquals("AB", valNestedUnsafe);
    assertEquals(List.of("B", "A"), closeLog);
  }

  @Test
  public void testThreeResources() {
    var closeLog = new ArrayList<String>();
    var res = UsingCheckedException.apply(
        () -> new MockResource("A", closeLog),
        () -> new MockResource("B", closeLog),
        () -> new MockResource("C", closeLog),
        tuple -> tuple._1().name + tuple._2().name + tuple._3().name
    );
    assertTrue(res.isRight());
    assertEquals("ABC", res.getRight());
    assertEquals(List.of("C", "B", "A"), closeLog);

    closeLog.clear();
    var valUnsafe = UsingCheckedException.applyUnsafe(
        () -> new MockResource("A", closeLog),
        () -> new MockResource("B", closeLog),
        () -> new MockResource("C", closeLog),
        tuple -> tuple._1().name + tuple._2().name + tuple._3().name
    );
    assertEquals("ABC", valUnsafe);
    assertEquals(List.of("C", "B", "A"), closeLog);

    closeLog.clear();
    var resNested = UsingCheckedException.applyNested(
        () -> new MockResource("A", closeLog),
        a -> new MockResource("B", closeLog),
        tuple2 -> new MockResource("C", closeLog),
        tuple3 -> tuple3._1().name + tuple3._2().name + tuple3._3().name
    );
    assertTrue(resNested.isRight());
    assertEquals("ABC", resNested.getRight());
    assertEquals(List.of("C", "B", "A"), closeLog);

    closeLog.clear();
    var valNestedUnsafe = UsingCheckedException.applyNestedUnsafe(
        () -> new MockResource("A", closeLog),
        a -> new MockResource("B", closeLog),
        tuple2 -> new MockResource("C", closeLog),
        tuple3 -> tuple3._1().name + tuple3._2().name + tuple3._3().name
    );
    assertEquals("ABC", valNestedUnsafe);
    assertEquals(List.of("C", "B", "A"), closeLog);
  }

  @Test
  public void testFourResources() {
    var closeLog = new ArrayList<String>();
    var res = UsingCheckedException.apply(
        () -> new MockResource("A", closeLog),
        () -> new MockResource("B", closeLog),
        () -> new MockResource("C", closeLog),
        () -> new MockResource("D", closeLog),
        t -> t._1().name + t._2().name + t._3().name + t._4().name
    );
    assertTrue(res.isRight());
    assertEquals("ABCD", res.getRight());
    assertEquals(List.of("D", "C", "B", "A"), closeLog);

    closeLog.clear();
    var valUnsafe = UsingCheckedException.applyUnsafe(
        () -> new MockResource("A", closeLog),
        () -> new MockResource("B", closeLog),
        () -> new MockResource("C", closeLog),
        () -> new MockResource("D", closeLog),
        t -> t._1().name + t._2().name + t._3().name + t._4().name
    );
    assertEquals("ABCD", valUnsafe);
    assertEquals(List.of("D", "C", "B", "A"), closeLog);

    closeLog.clear();
    var resNested = UsingCheckedException.applyNested(
        () -> new MockResource("A", closeLog),
        a -> new MockResource("B", closeLog),
        t2 -> new MockResource("C", closeLog),
        t3 -> new MockResource("D", closeLog),
        t4 -> t4._1().name + t4._2().name + t4._3().name + t4._4().name
    );
    assertTrue(resNested.isRight());
    assertEquals("ABCD", resNested.getRight());
    assertEquals(List.of("D", "C", "B", "A"), closeLog);

    closeLog.clear();
    var valNestedUnsafe = UsingCheckedException.applyNestedUnsafe(
        () -> new MockResource("A", closeLog),
        a -> new MockResource("B", closeLog),
        t2 -> new MockResource("C", closeLog),
        t3 -> new MockResource("D", closeLog),
        t4 -> t4._1().name + t4._2().name + t4._3().name + t4._4().name
    );
    assertEquals("ABCD", valNestedUnsafe);
    assertEquals(List.of("D", "C", "B", "A"), closeLog);
  }

  @Test
  public void testFiveResources() {
    var closeLog = new ArrayList<String>();
    var res = UsingCheckedException.apply(
        () -> new MockResource("A", closeLog),
        () -> new MockResource("B", closeLog),
        () -> new MockResource("C", closeLog),
        () -> new MockResource("D", closeLog),
        () -> new MockResource("E", closeLog),
        t -> t._1().name + t._2().name + t._3().name + t._4().name + t._5().name
    );
    assertTrue(res.isRight());
    assertEquals("ABCDE", res.getRight());
    assertEquals(List.of("E", "D", "C", "B", "A"), closeLog);

    closeLog.clear();
    var valUnsafe = UsingCheckedException.applyUnsafe(
        () -> new MockResource("A", closeLog),
        () -> new MockResource("B", closeLog),
        () -> new MockResource("C", closeLog),
        () -> new MockResource("D", closeLog),
        () -> new MockResource("E", closeLog),
        t -> t._1().name + t._2().name + t._3().name + t._4().name + t._5().name
    );
    assertEquals("ABCDE", valUnsafe);
    assertEquals(List.of("E", "D", "C", "B", "A"), closeLog);

    closeLog.clear();
    var resNested = UsingCheckedException.applyNested(
        () -> new MockResource("A", closeLog),
        a -> new MockResource("B", closeLog),
        t2 -> new MockResource("C", closeLog),
        t3 -> new MockResource("D", closeLog),
        t4 -> new MockResource("E", closeLog),
        t5 -> t5._1().name + t5._2().name + t5._3().name + t5._4().name + t5._5().name
    );
    assertTrue(resNested.isRight());
    assertEquals("ABCDE", resNested.getRight());
    assertEquals(List.of("E", "D", "C", "B", "A"), closeLog);

    closeLog.clear();
    var valNestedUnsafe = UsingCheckedException.applyNestedUnsafe(
        () -> new MockResource("A", closeLog),
        a -> new MockResource("B", closeLog),
        t2 -> new MockResource("C", closeLog),
        t3 -> new MockResource("D", closeLog),
        t4 -> new MockResource("E", closeLog),
        t5 -> t5._1().name + t5._2().name + t5._3().name + t5._4().name + t5._5().name
    );
    assertEquals("ABCDE", valNestedUnsafe);
    assertEquals(List.of("E", "D", "C", "B", "A"), closeLog);
  }

  @Test
  public void testPrimaryExceptionPreservationWithCloseFailure() {
    var closeEx = new IOException("close failed");
    var res = UsingCheckedException.apply(
        () -> new MockResource("A", null, closeEx),
        r -> {
          throw new IllegalStateException("primary body error");
        }
    );
    assertTrue(res.isLeft());
    var primaryEx = res.getLeft();
    assertEquals(IllegalStateException.class, primaryEx.getClass());
    assertEquals("primary body error", primaryEx.getMessage());
    assertEquals(1, primaryEx.getSuppressed().length);
    assertEquals(closeEx, primaryEx.getSuppressed()[0]);
  }

  @Test
  public void testFatalThrowablePropagation() {
    assertThrows(
        InterruptedException.class,
        () -> UsingCheckedException.apply(
            () -> new MockResource("A", null),
            r -> {
              throw new InterruptedException("interrupted!");
            }
        )
    );

    assertThrows(
        OutOfMemoryError.class,
        () -> UsingCheckedException.apply(
            () -> new MockResource("A", null),
            r -> {
              throw new OutOfMemoryError("OOM!");
            }
        )
    );

    assertThrows(
        TestControlBreakThrowable.class,
        () -> UsingCheckedException.apply(
            () -> new MockResource("A", null),
            r -> {
              sneakyThrow(new TestControlBreakThrowable("break!"));
              return "ok";
            }
        )
    );

    assertThrows(
        InterruptedException.class,
        () -> UsingCheckedException.apply(
            () -> new MockResource("A", null, new InterruptedException("close interrupted")),
            r -> "ok"
        )
    );
  }
}
