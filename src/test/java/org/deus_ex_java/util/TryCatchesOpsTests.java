package org.deus_ex_java.util;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

public class TryCatchesOpsTests {
  @SuppressWarnings("unchecked")
  private static <T extends Throwable> void throwStrippedCheckedException(
      Throwable throwable
  ) throws T {
    throw (T) throwable;
  }

  @Test
  public void testTryCatchWrapVoidSupplier() {
    //optional empty returned because no exception was thrown
    var optionalRuntimeExceptionIsEmpty = TryCatchesOps.wrap(() -> {
      //noinspection ConstantValue
      if (true) {
        @SuppressWarnings("unused")
        var x = 1; //NO OP
      }
    });
    assertTrue(optionalRuntimeExceptionIsEmpty.isEmpty());
    //nothing returned and no exception was thrown
    assertDoesNotThrow(() ->
        TryCatchesOps.wrapOrThrow(() -> {
          //noinspection ConstantValue
          if (true) {
            @SuppressWarnings("unused")
            var x = 1; //NO OP
          }
        }));
    //exception (fatal) thrown with wrap on JVM fatal throwable
    var fatalThrowable = assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrap(() -> {
          //noinspection ConstantValue
          if (true) {
            throw new LinkageError("testyWrap");
          }
        }));
    assertEquals("testyWrap", fatalThrowable.getMessage());
    //exception (fatal) thrown with wrapOrThrow on JVM fatal throwable
    assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrapOrThrow(() -> {
          //noinspection ConstantValue
          if (true) {
            throw new LinkageError("testyWrapOrThrow");
          }
        }));
    //optional exception returned for the RuntimeException descendant
    var optionalIllegalArgumentException = TryCatchesOps.wrap(() -> {
      //noinspection ConstantValue
      if (true) {
        throw new IllegalArgumentException("testyWrap");
      }
    });
    assertFalse(optionalIllegalArgumentException.isEmpty());
    assertEquals(IllegalArgumentException.class, optionalIllegalArgumentException.get().getClass());
    //exception thrown on the RuntimeException descendant
    assertThrows(
        IllegalArgumentException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
              //noinspection ConstantValue
              if (true) {
                throw new IllegalArgumentException("testyWrapOrThrow");
              }
            }));
    //optional exception returned on being explicitly found within the passed list of exceptions
    var optionalL = TryCatchesOps.wrap(() -> {
          //noinspection ConstantValue
          if (true) {
            throw new IllegalStateException("testyWrap");
          }
        },
        IllegalStateException.class,
        NumberFormatException.class);
    assertFalse(optionalL.isEmpty());
    assertEquals(IllegalStateException.class, optionalL.get().getClass());
    assertEquals("testyWrap", optionalL.get().getMessage());
    //exception thrown on being explicitly found within the passed list of exceptions
    assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
                  //noinspection ConstantValue
                  if (true) {
                    throw new IllegalStateException("testyWrapOrThrow");
                  }
                },
                IllegalStateException.class,
                NumberFormatException.class));
    //exception thrown with wrap on NOT being explicitly found within the passed list of exceptions
    var notAWrapExpectedNumberFormatException = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrap(() -> {
                  //noinspection ConstantValue
                  if (true) {
                    throw new IllegalStateException("testyWrap");
                  }
                },
                IllegalArgumentException.class,
                NumberFormatException.class));
    assertEquals("testyWrap", notAWrapExpectedNumberFormatException.getMessage());
    //exception thrown with wrapOrThrow on NOT being explicitly found within the passed list of exceptions
    var notAWrapOrThrowExpectedNumberFormatException = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
                  //noinspection ConstantValue
                  if (true) {
                    throw new IllegalStateException("testyWrapOrThrow");
                  }
                },
                IllegalArgumentException.class,
                NumberFormatException.class));
    assertEquals("testyWrapOrThrow", notAWrapOrThrowExpectedNumberFormatException.getMessage());
    //optional checked exception returned with wrap on being explicitly sneaky with a checked exception within list of exceptions
    var optionalIOException = TryCatchesOps.wrap(() -> {
          //noinspection ConstantValue
          if (true) {
            throwStrippedCheckedException(new IOException("optional sneaky checked exception"));
          }
        },
        IOException.class,
        NumberFormatException.class);
    assertTrue(optionalIOException.isPresent());
    assertEquals(IOException.class, optionalIOException.get().getClass());
    assertEquals("optional sneaky checked exception", optionalIOException.get().getMessage());
    //wrapped exception thrown with wrapOrThrow on being explicitly sneaky with a checked exception within list of exceptions
    var wrappedCheckedExceptionWrapOrThrow = assertThrows(
        WrappedCheckedException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
                  //noinspection ConstantValue
                  if (true) {
                    throwStrippedCheckedException(new IOException("wrapped sneaky wrapOrThrow checked exception"));
                  }
                },
                IOException.class,
                NumberFormatException.class));
    assertEquals("wrapped sneaky wrapOrThrow checked exception", wrappedCheckedExceptionWrapOrThrow.getCause().getMessage());
    //wrapped checked exception throw with wrap on being explicitly sneaky with a checked exception NOT in list of exceptions
    var checkedExceptionWrap = assertThrows(
        WrappedCheckedException.class,
        () ->
            TryCatchesOps.wrap(() -> {
                  //noinspection ConstantValue
                  if (true) {
                    throwStrippedCheckedException(new IOException("wrapped sneaky wrap checked exception"));
                  }
                },
                IllegalStateException.class,
                NumberFormatException.class));
    assertEquals("wrapped sneaky wrap checked exception", checkedExceptionWrap.getCause().getMessage());
    //wrapped checked exception thrown with wrapOrThrow on being explicitly sneaky with a checked exception NOT in list of exceptions
    var checkedExceptionWrapOrThrow = assertThrows(
        WrappedCheckedException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
                  //noinspection ConstantValue
                  if (true) {
                    throwStrippedCheckedException(new IOException("wrapped sneaky wrapOrThrow checked exception"));
                  }
                },
                IllegalStateException.class,
                NumberFormatException.class));
    assertEquals("wrapped sneaky wrapOrThrow checked exception", checkedExceptionWrapOrThrow.getCause().getMessage());
  }

  @Test
  public void testTryCatchWrapSupplier() {
    //Either.right returned because no exception was thrown
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
    //exception (fatal) thrown on JVM fatal throwable
    var fatalThrowable = assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrap(() -> {
          //noinspection ConstantValue
          if (true) {
            throw new LinkageError("testier");
          }

          return 1;
        }));
    //Either.left exception returned for RuntimeException descendant
    var eitherLeft = TryCatchesOps.wrap(() -> {
      //noinspection ConstantValue
      if (true) {
        throw new IllegalArgumentException();
      }

      return 1;
    });
    assertTrue(eitherLeft.isLeft());
    assertEquals(IllegalArgumentException.class, eitherLeft.getLeft().getClass());
    //exception thrown on the RuntimeException descendant
    assertThrows(
        IllegalArgumentException.class,
        () -> TryCatchesOps.wrapOrThrow(() -> {
          //noinspection ConstantValue
          if (true) {
            throw new IllegalArgumentException();
          }

          return 1;
        }));
    //exception thrown on being explicitly found within the passed list of exceptions
    assertThrows(
        IllegalArgumentException.class,
        () -> TryCatchesOps.wrapOrThrow(() -> {
              //noinspection ConstantValue
              if (true) {
                throw new IllegalArgumentException();
              }

              return 1;
            },
            IllegalStateException.class,
            NumberFormatException.class));
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
    //exception on JVM fatal throwable
    var fatalThrowable = assertThrows(
        InterruptedException.class,
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
    //throw exception on the explicitly wrapped RuntimeException
    assertThrows(
        IllegalArgumentException.class,
        () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
          //noinspection ConstantValue
          if (false) {
            //noinspection resource
            InputStreamReader.nullReader().reset();
          }
          //noinspection ConstantValue
          if (true) {
            throw new IllegalArgumentException();
          }
        }));
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
    //exception on JVM fatal throwable
    var fatalThrowable = assertThrows(
        LinkageError.class,
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
    //throw exception on the explicitly wrapped RuntimeException
    assertThrows(
        IllegalArgumentException.class,
        () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
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
        }));

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
