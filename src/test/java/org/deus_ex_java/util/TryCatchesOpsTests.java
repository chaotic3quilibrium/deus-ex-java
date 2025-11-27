package org.deus_ex_java.util;

import org.deus_ex_java.lang.FatalThrowable;
import org.deus_ex_java.lang.WrappedCheckedException;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

public class TryCatchesOpsTests {
  @Test
  public void testTryCatchWrapVoidSupplier() {
    //success and optional is empty
    var optionalRuntimeExceptionIsEmpty = TryCatchesOps.wrap(() -> {
      //noinspection ConstantValue
      if (true) {
        @SuppressWarnings("unused")
        var x = 1; //NO OP
      }
    });
    assertTrue(optionalRuntimeExceptionIsEmpty.isEmpty());
    //exception on deus-ex-java FatalThrowable
    var deusExJavaFatalThrowable = assertThrows(
        FatalThrowable.class,
        () ->
            TryCatchesOps.wrap(() -> {
              //noinspection ConstantValue
              if (true) {
                FatalThrowable.filterToFatalThrowable(new LinkageError("testier"))
                    .ifPresent(fatalThrowable -> {

                      throw fatalThrowable;
                    });
              }
            }));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.LinkageError - testier", deusExJavaFatalThrowable.getMessage());
    //exception on JVM fatal throwable
    var fatalThrowable = assertThrows(
        FatalThrowable.class,
        () -> TryCatchesOps.wrap(() -> {
          //noinspection ConstantValue
          if (true) {
            throw new LinkageError("testier");
          }
        }));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.LinkageError - testier", fatalThrowable.getMessage());
    //erred failure and option is non-empty
    var optionalIllegalArgumentException = TryCatchesOps.wrap(() -> {
      //noinspection ConstantValue
      if (true) {
        throw new IllegalArgumentException();
      }
    });
    assertFalse(optionalIllegalArgumentException.isEmpty());
    assertEquals(IllegalArgumentException.class, optionalIllegalArgumentException.get().getClass());
    //exception on unexpected throwable class
    var illegalStateException = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrap(() -> {
                  //noinspection ConstantValue
                  if (true) {
                    throw new IllegalStateException("testy");
                  }
                },
                NumberFormatException.class));
    assertEquals("testy", illegalStateException.getMessage());
  }

  @Test
  public void testTryCatchWrapSupplier() {
    //success and optional is empty
    var eitherRight = TryCatchesOps.wrap(() -> {
      //noinspection ConstantValue
      if (true) {
        @SuppressWarnings("unused")
        var x = 1; //NO OP
      }

      return 1;
    });
    assertTrue(eitherRight.isRight());
    assertEquals(1, eitherRight.getRight());
    //exception on deus-ex-java FatalThrowable
    var deusExJavaFatalThrowable = assertThrows(
        FatalThrowable.class,
        () ->
            TryCatchesOps.wrap(() -> {
              //noinspection ConstantValue
              if (true) {
                FatalThrowable.filterToFatalThrowable(new LinkageError("testier"))
                    .ifPresent(fatalThrowable -> {

                      throw fatalThrowable;
                    });
              }

              return 1;
            }));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.LinkageError - testier", deusExJavaFatalThrowable.getMessage());
    //exception on JVM fatal throwable
    var fatalThrowable = assertThrows(
        FatalThrowable.class,
        () -> TryCatchesOps.wrap(() -> {
          //noinspection ConstantValue
          if (true) {
            throw new LinkageError("testier");
          }

          return 1;
        }));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.LinkageError - testier", fatalThrowable.getMessage());
    //erred failure and option is non-empty
    var eitherLeft = TryCatchesOps.wrap(() -> {
      //noinspection ConstantValue
      if (true) {
        throw new IllegalArgumentException();
      }

      return 1;
    });
    assertTrue(eitherLeft.isLeft());
    assertEquals(IllegalArgumentException.class, eitherLeft.getLeft().getClass());
    //exception on unexpected throwable class
    var illegalStateException = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrap(() -> {
                  //noinspection ConstantValue
                  if (true) {
                    throw new IllegalStateException("testy");
                  }

                  return 1;
                },
                NumberFormatException.class));
    assertEquals("testy", illegalStateException.getMessage());
  }

  @Test
  public void testTryCatchWrapCheckedExceptionVoidSupplierCheckedException() {
    //success and optional is empty
    var optionalRuntimeExceptionIsEmpty = TryCatchesOps.wrapCheckedException(() -> {
      //noinspection ConstantValue
      if (false) {
        //noinspection resource
        InputStreamReader.nullReader().reset();
      }
      //noinspection ConstantValue
      if (true) {
        @SuppressWarnings("unused")
        var x = 1; //NO OP
      }
    });
    assertTrue(optionalRuntimeExceptionIsEmpty.isEmpty());
    //exception on deus-ex-java FatalThrowable
    var deusExJavaFatalThrowable = assertThrows(
        FatalThrowable.class,
        () ->
            TryCatchesOps.wrapCheckedException(() -> {
              //noinspection ConstantValue
              if (false) {
                //noinspection resource
                InputStreamReader.nullReader().reset();
              }
              //noinspection ConstantValue
              if (true) {
                FatalThrowable.filterToFatalThrowable(new LinkageError("testier"))
                    .ifPresent(fatalThrowable -> {

                      throw fatalThrowable;
                    });
              }
            }));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.LinkageError - testier", deusExJavaFatalThrowable.getMessage());
    //exception on JVM fatal throwable
    var fatalThrowable = assertThrows(
        FatalThrowable.class,
        () -> TryCatchesOps.wrapCheckedException(() -> {
          //noinspection ConstantValue
          if (false) {
            //noinspection resource
            InputStreamReader.nullReader().reset();
          }
          //noinspection ConstantValue
          if (true) {
            throw new InterruptedException("testier");
          }
        }));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.InterruptedException - testier", fatalThrowable.getMessage());
    //erred failure and option is non-empty
    var optionalIllegalArgumentException = TryCatchesOps.wrapCheckedException(() -> {
      //noinspection ConstantValue
      if (false) {
        //noinspection resource
        InputStreamReader.nullReader().reset();
      }
      //noinspection ConstantValue
      if (true) {
        throw new IllegalArgumentException();
      }
    });
    assertFalse(optionalIllegalArgumentException.isEmpty());
    assertEquals(IllegalArgumentException.class, optionalIllegalArgumentException.get().getClass());
    //exception on unexpected throwable class
    var illegalStateException = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrapCheckedException(() -> {
                  //noinspection ConstantValue
                  if (false) {
                    //noinspection resource
                    InputStreamReader.nullReader().reset();
                  }
                  //noinspection ConstantValue
                  if (true) {
                    throw new IllegalStateException("testy");
                  }
                },
                NumberFormatException.class));
    assertEquals("testy", illegalStateException.getMessage());
    //exception on checked exception
    var throwable = assertThrows(
        WrappedCheckedException.class,
        () -> TryCatchesOps.wrapCheckedException(() -> {
          //noinspection resource
          InputStreamReader.nullReader().reset(); //intentionally throwing a checked exception
        }));
    assertEquals("wrapCheckedException(VoidSupplierCheckedException) failure - reset() not supported", throwable.getMessage());
  }

  @Test
  public void testTryCatchWrapCheckedExceptionSupplierCheckedException() {
    //success and either is right
    var eitherRight = TryCatchesOps.wrapCheckedException(() -> {
      //noinspection ConstantValue
      if (false) {
        //noinspection resource
        InputStreamReader.nullReader().reset();
      }
      //noinspection ConstantValue
      if (true) {
        @SuppressWarnings("unused")
        var x = 1; //NO OP
      }

      return 1;
    });
    assertTrue(eitherRight.isRight());
    assertEquals(1, eitherRight.getRight());
    //exception on deus-ex-java FatalThrowable
    var deusExJavaFatalThrowable = assertThrows(
        FatalThrowable.class,
        () ->
            TryCatchesOps.wrapCheckedException(() -> {
              //noinspection ConstantValue
              if (false) {
                //noinspection resource
                InputStreamReader.nullReader().reset();
              }
              //noinspection ConstantValue
              if (true) {
                FatalThrowable.filterToFatalThrowable(new LinkageError("testier"))
                    .ifPresent(fatalThrowable -> {

                      throw fatalThrowable;
                    });
              }

              return 1;
            }));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.LinkageError - testier", deusExJavaFatalThrowable.getMessage());
    //exception on JVM fatal throwable
    var fatalThrowable = assertThrows(
        FatalThrowable.class,
        () -> TryCatchesOps.wrapCheckedException(() -> {
          //noinspection ConstantValue
          if (false) {
            //noinspection resource
            InputStreamReader.nullReader().reset();
          }
          //noinspection ConstantValue
          if (true) {
            throw new LinkageError("testier");
          }

          return 1;
        }));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.LinkageError - testier", fatalThrowable.getMessage());
    //erred failure and either is left
    var eitherLeft = TryCatchesOps.wrapCheckedException(() -> {
      //noinspection ConstantValue
      if (false) {
        //noinspection resource
        InputStreamReader.nullReader().reset();
      }
      //noinspection ConstantValue
      if (true) {
        throw new IllegalArgumentException();
      }

      return 1;
    });
    assertTrue(eitherLeft.isLeft());
    assertEquals(IllegalArgumentException.class, eitherLeft.getLeft().getClass());
    //exception on unexpected RuntimeException
    var illegalStateException = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrapCheckedException(() -> {
                  //noinspection ConstantValue
                  if (false) {
                    //noinspection resource
                    InputStreamReader.nullReader().reset();
                  }
                  //noinspection ConstantValue
                  if (true) {
                    throw new IllegalStateException("testy");
                  }

                  return 1;
                },
                NumberFormatException.class));
    assertEquals("testy", illegalStateException.getMessage());
    //exception on checked exception
    var wrappedCheckedException = assertThrows(
        WrappedCheckedException.class,
        () -> TryCatchesOps.wrapCheckedException(() -> {
              //noinspection resource
              InputStreamReader.nullReader().reset(); //intentionally throwing a checked exception

              return 1;
            },
            //must pass this, or the default of Exception is passed and then an Either.right is returned
            NumberFormatException.class));
    assertEquals("wrapCheckedException(SupplierCheckedException) failure - reset() not supported", wrappedCheckedException.getMessage());
  }
}
