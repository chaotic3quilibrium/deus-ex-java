package org.deus_ex_java.util;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"unchecked", "unused", "ConstantValue", "DataFlowIssue", "resource", "SpellCheckingInspection"})
public class TryCatchesOpsTests {
  private static <T extends Throwable> void throwStrippedCheckedException(
      Throwable throwable
  ) throws T {
    throw (T) throwable;
  }

  @Test
  public void testVoidSupplier() {
    //Optional empty returned because no exception was thrown
    var optionalIsEmpty = TryCatchesOps.wrap(() -> {
      if (true) {
        var x = 1; //NO OP
      }
    });
    assertTrue(optionalIsEmpty.isEmpty());
    //nothing returned and no exception was thrown
    assertDoesNotThrow(() ->
        TryCatchesOps.wrapOrThrow(() -> {
          if (true) {
            var x = 1; //NO OP
          }
        }));
    //exception (fatal) thrown with wrap on JVM fatal throwable
    var fatalThrowableLinkageErrorWrap = assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrap(() -> {
          if (true) {
            throw new LinkageError("testyWrap");
          }
        }));
    assertEquals("testyWrap", fatalThrowableLinkageErrorWrap.getMessage());
    //exception (fatal) thrown with wrapOrThrow on JVM fatal throwable
    var fatalThrowableLinkageErrorWrapOrThrow = assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrapOrThrow(() -> {
          if (true) {
            throw new LinkageError("testyWrapOrThrow");
          }
        }));
    assertEquals("testyWrapOrThrow", fatalThrowableLinkageErrorWrapOrThrow.getMessage());
    //Optional runtime exception returned for the RuntimeException descendant
    var optionalIllegalArgumentException = TryCatchesOps.wrap(() -> {
      if (true) {
        throw new IllegalArgumentException("testyWrap");
      }
    });
    assertFalse(optionalIllegalArgumentException.isEmpty());
    assertEquals(IllegalArgumentException.class, optionalIllegalArgumentException.get().getClass());
    assertEquals("testyWrap", optionalIllegalArgumentException.get().getMessage());
    //exception thrown on the RuntimeException descendant
    var illegalArgumentExceptionRuntimeDescendant = assertThrows(
        IllegalArgumentException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
              if (true) {
                throw new IllegalArgumentException("testyWrapOrThrow");
              }
            }));
    assertEquals("testyWrapOrThrow", illegalArgumentExceptionRuntimeDescendant.getMessage());
    //optional exception returned on being explicitly found within the passed list of exceptions
    var optionalRuntimeExceptionFoundInList = TryCatchesOps.wrap(() -> {
          if (true) {
            throw new IllegalStateException("testyWrap");
          }
        },
        IllegalStateException.class,
        NumberFormatException.class);
    assertFalse(optionalRuntimeExceptionFoundInList.isEmpty());
    assertEquals(IllegalStateException.class, optionalRuntimeExceptionFoundInList.get().getClass());
    assertEquals("testyWrap", optionalRuntimeExceptionFoundInList.get().getMessage());
    //exception thrown on being explicitly found within the passed list of exceptions
    var illegalRuntimeExceptionFoundInList = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
                  if (true) {
                    throw new IllegalStateException("testyWrapOrThrow");
                  }
                },
                IllegalStateException.class,
                NumberFormatException.class));
    assertEquals("testyWrapOrThrow", illegalRuntimeExceptionFoundInList.getMessage());
    //exception thrown with wrap on NOT being explicitly found within the passed list of exceptions
    var illegalRuntimeExceptionNotFoundInListWrap = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrap(() -> {
                  if (true) {
                    throw new IllegalStateException("testyWrap");
                  }
                },
                IllegalArgumentException.class,
                NumberFormatException.class));
    assertEquals("testyWrap", illegalRuntimeExceptionNotFoundInListWrap.getMessage());
    //exception thrown with wrapOrThrow on NOT being explicitly found within the passed list of exceptions
    var illegalRuntimeExceptionNotFoundInListWrapOrThrow = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
                  if (true) {
                    throw new IllegalStateException("testyWrapOrThrow");
                  }
                },
                IllegalArgumentException.class,
                NumberFormatException.class));
    assertEquals("testyWrapOrThrow", illegalRuntimeExceptionNotFoundInListWrapOrThrow.getMessage());
    //Optional checked exception returned with wrap on being explicitly sneaky with a checked exception within list of exceptions
    var optionalCheckedExceptionFoundInList = TryCatchesOps.wrap(() -> {
          if (true) {
            throwStrippedCheckedException(new IOException("optional sneaky checked exception"));
          }
        },
        IOException.class,
        NumberFormatException.class);
    assertTrue(optionalCheckedExceptionFoundInList.isPresent());
    assertEquals(IOException.class, optionalCheckedExceptionFoundInList.get().getClass());
    assertEquals("optional sneaky checked exception", optionalCheckedExceptionFoundInList.get().getMessage());
    //wrapped exception thrown with wrapOrThrow on being explicitly sneaky with a checked exception within list of exceptions
    var wrappedCheckedExceptionFoundInList = assertThrows(
        WrappedCheckedException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
                  if (true) {
                    throwStrippedCheckedException(new IOException("wrapped sneaky wrapOrThrow checked exception"));
                  }
                },
                IOException.class,
                NumberFormatException.class));
    assertEquals("wrapped sneaky wrapOrThrow checked exception", wrappedCheckedExceptionFoundInList.getCause().getMessage());
    //wrapped checked exception throw with wrap on being explicitly sneaky with a checked exception NOT in list of exceptions
    var wrappedCheckedExceptionNotFoundInListWrap = assertThrows(
        WrappedCheckedException.class,
        () ->
            TryCatchesOps.wrap(() -> {
                  if (true) {
                    throwStrippedCheckedException(new IOException("wrapped sneaky wrap checked exception"));
                  }
                },
                IllegalStateException.class,
                NumberFormatException.class));
    assertEquals(IOException.class, wrappedCheckedExceptionNotFoundInListWrap.getCause().getClass());
    assertEquals("wrapped sneaky wrap checked exception", wrappedCheckedExceptionNotFoundInListWrap.getCause().getMessage());
    //wrapped checked exception thrown with wrapOrThrow on being explicitly sneaky with a checked exception NOT in list of exceptions
    var wrappedCheckedExceptionNotFoundInListWrapOrThrow = assertThrows(
        WrappedCheckedException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
                  if (true) {
                    throwStrippedCheckedException(new IOException("wrapped sneaky wrapOrThrow checked exception"));
                  }
                },
                IllegalStateException.class,
                NumberFormatException.class));
    assertEquals(IOException.class, wrappedCheckedExceptionNotFoundInListWrapOrThrow.getCause().getClass());
    assertEquals("wrapped sneaky wrapOrThrow checked exception", wrappedCheckedExceptionNotFoundInListWrapOrThrow.getCause().getMessage());
  }

  @Test
  public void testSupplier() {
    //Either right returned with wrap because no exception was thrown
    var eitherRight = TryCatchesOps.wrap(() -> {
      if (true) {
        var x = 1; //NO OP
      }

      return 1;
    });
    assertTrue(eitherRight.isRight());
    assertEquals(1, eitherRight.getRight());
    //result returned with wrapAndThrow because no exception was thrown
    var integer = assertDoesNotThrow(() ->
        TryCatchesOps.wrapOrThrow(() -> {
          if (true) {
            var x = 1; //NO OP
          }

          return 1;
        }));
    assertEquals(1, integer);
    //Either right returned with wrap because no exception was thrown
    var eitherRightWithList = TryCatchesOps.wrap(() -> {
          if (true) {
            var x = 1; //NO OP
          }

          return 1;
        },
        IllegalStateException.class,
        NumberFormatException.class);
    assertTrue(eitherRightWithList.isRight());
    assertEquals(1, eitherRightWithList.getRight());
    //result returned with wrapAndThrow because no exception was thrown
    var integerWithList = assertDoesNotThrow(() ->
        TryCatchesOps.wrapOrThrow(() -> {
              if (true) {
                var x = 1; //NO OP
              }

              return 1;
            },
            IllegalStateException.class,
            NumberFormatException.class));
    assertEquals(1, integerWithList);
    //exception (fatal) thrown with wrap on JVM fatal throwable
    var fatalThrowableLinkageErrorWrap = assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrap(() -> {
          if (true) {
            throw new LinkageError("testyWrap");
          }

          return 1;
        }));
    assertEquals("testyWrap", fatalThrowableLinkageErrorWrap.getMessage());
    //exception (fatal) thrown with wrapOrThrow on JVM fatal throwable
    var fatalThrowableLinkageErrorWrapOrThrow = assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrapOrThrow(() -> {
          if (true) {
            throw new LinkageError("testyWrapOrThrow");
          }

          return 1;
        }));
    assertEquals("testyWrapOrThrow", fatalThrowableLinkageErrorWrapOrThrow.getMessage());
    //Either left exception returned for RuntimeException descendant
    var eitherLeft = TryCatchesOps.wrap(() -> {
      if (true) {
        throw new IllegalArgumentException("testyWrap");
      }

      return 1;
    });
    assertTrue(eitherLeft.isLeft());
    assertEquals(IllegalArgumentException.class, eitherLeft.getLeft().getClass());
    assertEquals("testyWrap", eitherLeft.getLeft().getMessage());
    //exception thrown on the RuntimeException descendant
    var illegalArgumentExceptionRuntimeExceptionDescendant = assertThrows(
        IllegalArgumentException.class,
        () ->
            TryCatchesOps.wrapOrThrow(() -> {
              if (true) {
                throw new IllegalArgumentException("testyWrapOrThrow");
              }

              return 1;
            }));
    assertEquals("testyWrapOrThrow", illegalArgumentExceptionRuntimeExceptionDescendant.getMessage());
    //Either left exception returned on being explicitly found within the passed list of exceptions
    var eitherLeftFoundInList = TryCatchesOps.wrap(() -> {
          if (true) {
            throw new IllegalStateException("testyWrap");
          }

          return 1;
        },
        IllegalStateException.class,
        NumberFormatException.class);
    assertTrue(eitherLeftFoundInList.isLeft());
    assertEquals(IllegalStateException.class, eitherLeftFoundInList.getLeft().getClass());
    assertEquals("testyWrap", eitherLeftFoundInList.getLeft().getMessage());
    //exception thrown on being explicitly found within the passed list of exceptions
    var illegalStateExceptionFoundInList = assertThrows(
        IllegalStateException.class,
        () -> TryCatchesOps.wrapOrThrow(() -> {
              if (true) {
                throw new IllegalStateException("testyWrapOrThrow");
              }

              return 1;
            },
            IllegalStateException.class,
            NumberFormatException.class));
    assertEquals("testyWrapOrThrow", illegalStateExceptionFoundInList.getMessage());
    //exception thrown with wrap on NOT being explicitly found within the passed list of exceptions
    var illegalStateExceptionNotFoundInListWrap = assertThrows(
        IllegalStateException.class,
        () -> TryCatchesOps.wrap(() -> {
              if (true) {
                throw new IllegalStateException("testyWrap");
              }

              return 1;
            },
            IllegalArgumentException.class,
            NumberFormatException.class));
    assertEquals("testyWrap", illegalStateExceptionNotFoundInListWrap.getMessage());
    //exception thrown with wrapOrThrow on NOT being explicitly found within the passed list of exceptions
    var illegalStateExceptionNotFoundInListWrapOrThrow = assertThrows(
        IllegalStateException.class,
        () -> TryCatchesOps.wrapOrThrow(() -> {
              if (true) {
                throw new IllegalStateException("testyWrapOrThrow");
              }

              return 1;
            },
            IllegalArgumentException.class,
            NumberFormatException.class));
    assertEquals("testyWrapOrThrow", illegalStateExceptionNotFoundInListWrapOrThrow.getMessage());
    //Either left checked exception returned with wrap on being explicitly sneaky with a checked exception within list of exceptions
    var eitherLeftCheckedException = TryCatchesOps.wrap(() -> {
          if (true) {
            throwStrippedCheckedException(new IOException("optional sneaky wrap checked exception"));
          }

          return 1;
        },
        IOException.class,
        NumberFormatException.class);
    assertTrue(eitherLeftCheckedException.isLeft());
    assertEquals(IOException.class, eitherLeftCheckedException.getLeft().getClass());
    assertEquals("optional sneaky wrap checked exception", eitherLeftCheckedException.getLeft().getMessage());
    //wrapped exception thrown with wrapOrThrow on being explicitly sneaky with a checked exception within list of exceptions
    var wrappedCheckedExceptionFoundInList = assertThrows(
        WrappedCheckedException.class,
        () -> TryCatchesOps.wrapOrThrow(() -> {
              if (true) {
                throwStrippedCheckedException(new IOException("optional sneaky wrapOrThrow checked exception"));
              }

              return 1;
            },
            IOException.class,
            NumberFormatException.class));
    assertEquals(IOException.class, wrappedCheckedExceptionFoundInList.getCause().getClass());
    assertEquals("optional sneaky wrapOrThrow checked exception", wrappedCheckedExceptionFoundInList.getCause().getMessage());
    //wrapped checked exception throw with wrap on being explicitly sneaky with a checked exception NOT in list of exceptions
    var wrappedCheckedExceptionNotFoundInListWrap = assertThrows(
        WrappedCheckedException.class,
        () -> TryCatchesOps.wrap(() -> {
              if (true) {
                throwStrippedCheckedException(new IOException("optional sneaky wrap checked exception"));
              }

              return 1;
            },
            IllegalStateException.class,
            NumberFormatException.class));
    assertEquals(IOException.class, wrappedCheckedExceptionNotFoundInListWrap.getCause().getClass());
    assertEquals("optional sneaky wrap checked exception", wrappedCheckedExceptionNotFoundInListWrap.getCause().getMessage());
    var wrappedCheckedExceptionNotFoundInListWrapOrThrow = assertThrows(
        WrappedCheckedException.class,
        () -> TryCatchesOps.wrapOrThrow(() -> {
              if (true) {
                throwStrippedCheckedException(new IOException("optional sneaky wrapOrThrow checked exception"));
              }

              return 1;
            },
            IllegalStateException.class,
            NumberFormatException.class));
    assertEquals(IOException.class, wrappedCheckedExceptionNotFoundInListWrapOrThrow.getCause().getClass());
    assertEquals("optional sneaky wrapOrThrow checked exception", wrappedCheckedExceptionNotFoundInListWrapOrThrow.getCause().getMessage());
  }

  private void voidFunctionWithCheckedException() throws Exception {
    if (false) {
      InputStreamReader.nullReader().reset();
    }
  }

  @Test
  public void testVoidSupplierCheckedException() {
    //Optional empty returned because no exception was thrown
    var optionalIsEmpty = TryCatchesOps.wrapCheckedException(() -> {
      voidFunctionWithCheckedException();
      if (true) {
        var x = 1; //NO OP
      }
    });
    assertTrue(optionalIsEmpty.isEmpty());
    //nothing returned and no exception was thrown
    assertDoesNotThrow(() ->
        TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            var x = 1; //NO OP
          }
        }));
    //exception (fatal) thrown with wrap on JVM fatal throwable
    var fatalThrowableLinkageErrorWrap = assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrapCheckedException(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            throw new LinkageError("testyWrap");
          }
        }));
    assertEquals("testyWrap", fatalThrowableLinkageErrorWrap.getMessage());
    //exception (fatal) thrown with wrapOrThrow on JVM fatal throwable
    var fatalThrowableLinkageErrorWrapOrThrow = assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            throw new LinkageError("testyWrapOrThrow");
          }
        }));
    assertEquals("testyWrapOrThrow", fatalThrowableLinkageErrorWrapOrThrow.getMessage());
    //Optional runtime exception returned for the RuntimeException descendant
    var optionalIllegalArgumentException = TryCatchesOps.wrapCheckedException(() -> {
      voidFunctionWithCheckedException();
      if (true) {
        throw new IllegalArgumentException("testyWrap");
      }
    });
    assertFalse(optionalIllegalArgumentException.isEmpty());
    assertEquals(IllegalArgumentException.class, optionalIllegalArgumentException.get().getClass());
    assertEquals("testyWrap", optionalIllegalArgumentException.get().getMessage());
    //exception thrown on the RuntimeException descendant
    var illegalArgumentExceptionRuntimeDescendant = assertThrows(
        IllegalArgumentException.class,
        () ->
            TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
              voidFunctionWithCheckedException();
              if (true) {
                throw new IllegalArgumentException("testyWrapOrThrow");
              }
            }));
    assertEquals("testyWrapOrThrow", illegalArgumentExceptionRuntimeDescendant.getMessage());
    //optional exception returned on being explicitly found within the passed list of exceptions
    var optionalRuntimeExceptionFoundInList = TryCatchesOps.wrapCheckedException(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            throw new IllegalStateException("testyWrap");
          }
        },
        IllegalStateException.class,
        NumberFormatException.class);
    assertFalse(optionalRuntimeExceptionFoundInList.isEmpty());
    assertEquals(IllegalStateException.class, optionalRuntimeExceptionFoundInList.get().getClass());
    assertEquals("testyWrap", optionalRuntimeExceptionFoundInList.get().getMessage());
    //exception thrown on being explicitly found within the passed list of exceptions
    var illegalRuntimeExceptionFoundInList = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
                  voidFunctionWithCheckedException();
                  if (true) {
                    throw new IllegalStateException("testyWrapOrThrow");
                  }
                },
                IllegalStateException.class,
                NumberFormatException.class));
    assertEquals("testyWrapOrThrow", illegalRuntimeExceptionFoundInList.getMessage());
    //exception thrown with wrap on NOT being explicitly found within the passed list of exceptions
    var illegalRuntimeExceptionNotFoundInListWrap = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrapCheckedException(() -> {
                  voidFunctionWithCheckedException();
                  if (true) {
                    throw new IllegalStateException("testyWrap");
                  }
                },
                IllegalArgumentException.class,
                NumberFormatException.class));
    assertEquals("testyWrap", illegalRuntimeExceptionNotFoundInListWrap.getMessage());
    //exception thrown with wrapOrThrow on NOT being explicitly found within the passed list of exceptions
    var illegalRuntimeExceptionNotFoundInListWrapOrThrow = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
                  voidFunctionWithCheckedException();
                  if (true) {
                    throw new IllegalStateException("testyWrapOrThrow");
                  }
                },
                IllegalArgumentException.class,
                NumberFormatException.class));
    assertEquals("testyWrapOrThrow", illegalRuntimeExceptionNotFoundInListWrapOrThrow.getMessage());
    //Optional checked exception returned with wrap on being explicitly sneaky with a checked exception within list of exceptions
    var optionalCheckedExceptionFoundInList = TryCatchesOps.wrapCheckedException(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            throwStrippedCheckedException(new IOException("optional sneaky checked exception"));
          }
        },
        IOException.class,
        NumberFormatException.class);
    assertTrue(optionalCheckedExceptionFoundInList.isPresent());
    assertEquals(IOException.class, optionalCheckedExceptionFoundInList.get().getClass());
    assertEquals("optional sneaky checked exception", optionalCheckedExceptionFoundInList.get().getMessage());
    //wrapped exception thrown with wrapOrThrow on being explicitly sneaky with a checked exception within list of exceptions
    var wrappedCheckedExceptionFoundInList = assertThrows(
        WrappedCheckedException.class,
        () ->
            TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
                  voidFunctionWithCheckedException();
                  if (true) {
                    throwStrippedCheckedException(new IOException("wrapped sneaky wrapOrThrow checked exception"));
                  }
                },
                IOException.class,
                NumberFormatException.class));
    assertEquals("wrapped sneaky wrapOrThrow checked exception", wrappedCheckedExceptionFoundInList.getCause().getMessage());
    //wrapped checked exception throw with wrap on being explicitly sneaky with a checked exception NOT in list of exceptions
    var wrappedCheckedExceptionNotFoundInListWrap = assertThrows(
        WrappedCheckedException.class,
        () ->
            TryCatchesOps.wrapCheckedException(() -> {
                  voidFunctionWithCheckedException();
                  if (true) {
                    throwStrippedCheckedException(new IOException("wrapped sneaky wrap checked exception"));
                  }
                },
                IllegalStateException.class,
                NumberFormatException.class));
    assertEquals(IOException.class, wrappedCheckedExceptionNotFoundInListWrap.getCause().getClass());
    assertEquals("wrapped sneaky wrap checked exception", wrappedCheckedExceptionNotFoundInListWrap.getCause().getMessage());
    //wrapped checked exception thrown with wrapOrThrow on being explicitly sneaky with a checked exception NOT in list of exceptions
    var wrappedCheckedExceptionNotFoundInListWrapOrThrow = assertThrows(
        WrappedCheckedException.class,
        () ->
            TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
                  voidFunctionWithCheckedException();
                  if (true) {
                    throwStrippedCheckedException(new IOException("wrapped sneaky wrapOrThrow checked exception"));
                  }
                },
                IllegalStateException.class,
                NumberFormatException.class));
    assertEquals(IOException.class, wrappedCheckedExceptionNotFoundInListWrapOrThrow.getCause().getClass());
    assertEquals("wrapped sneaky wrapOrThrow checked exception", wrappedCheckedExceptionNotFoundInListWrapOrThrow.getCause().getMessage());
  }

  @Test
  public void testTryCatchWrapCheckedExceptionSupplierCheckedException() {
    //Either right returned with wrap because no exception was thrown
    var eitherRight = TryCatchesOps.wrapCheckedException(() -> {
      voidFunctionWithCheckedException();
      if (true) {
        var x = 1; //NO OP
      }

      return 1;
    });
    assertTrue(eitherRight.isRight());
    assertEquals(1, eitherRight.getRight());
    //result returned with wrapAndThrow because no exception was thrown
    var integer = assertDoesNotThrow(() ->
        TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            var x = 1; //NO OP
          }

          return 1;
        }));
    assertEquals(1, integer);
    //Either right returned with wrap because no exception was thrown
    var eitherRightWithList = TryCatchesOps.wrapCheckedException(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            var x = 1; //NO OP
          }

          return 1;
        },
        IllegalStateException.class,
        NumberFormatException.class);
    assertTrue(eitherRightWithList.isRight());
    assertEquals(1, eitherRightWithList.getRight());
    //result returned with wrapAndThrow because no exception was thrown
    var integerWithList = assertDoesNotThrow(() ->
        TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
              voidFunctionWithCheckedException();
              if (true) {
                var x = 1; //NO OP
              }

              return 1;
            },
            IllegalStateException.class,
            NumberFormatException.class));
    assertEquals(1, integerWithList);
    //exception (fatal) thrown with wrap on JVM fatal throwable
    var fatalThrowableLinkageErrorWrap = assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrapCheckedException(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            throw new LinkageError("testyWrap");
          }

          return 1;
        }));
    assertEquals("testyWrap", fatalThrowableLinkageErrorWrap.getMessage());
    //exception (fatal) thrown with wrapOrThrow on JVM fatal throwable
    var fatalThrowableLinkageErrorWrapOrThrow = assertThrows(
        LinkageError.class,
        () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            throw new LinkageError("testyWrapOrThrow");
          }

          return 1;
        }));
    assertEquals("testyWrapOrThrow", fatalThrowableLinkageErrorWrapOrThrow.getMessage());
    //Either left exception returned for RuntimeException descendant
    var eitherLeft = TryCatchesOps.wrapCheckedException(() -> {
      voidFunctionWithCheckedException();
      if (true) {
        throw new IllegalArgumentException("testyWrap");
      }

      return 1;
    });
    assertTrue(eitherLeft.isLeft());
    assertEquals(IllegalArgumentException.class, eitherLeft.getLeft().getClass());
    assertEquals("testyWrap", eitherLeft.getLeft().getMessage());
    //exception thrown on the RuntimeException descendant
    var illegalArgumentExceptionRuntimeExceptionDescendant = assertThrows(
        IllegalArgumentException.class,
        () ->
            TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
              voidFunctionWithCheckedException();
              if (true) {
                throw new IllegalArgumentException("testyWrapOrThrow");
              }

              return 1;
            }));
    assertEquals("testyWrapOrThrow", illegalArgumentExceptionRuntimeExceptionDescendant.getMessage());
    //Either left exception returned on being explicitly found within the passed list of exceptions
    var eitherLeftFoundInList = TryCatchesOps.wrapCheckedException(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            throw new IllegalStateException("testyWrap");
          }

          return 1;
        },
        IllegalStateException.class,
        NumberFormatException.class);
    assertTrue(eitherLeftFoundInList.isLeft());
    assertEquals(IllegalStateException.class, eitherLeftFoundInList.getLeft().getClass());
    assertEquals("testyWrap", eitherLeftFoundInList.getLeft().getMessage());
    //exception thrown on being explicitly found within the passed list of exceptions
    var illegalStateExceptionFoundInList = assertThrows(
        IllegalStateException.class,
        () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
              voidFunctionWithCheckedException();
              if (true) {
                throw new IllegalStateException("testyWrapOrThrow");
              }

              return 1;
            },
            IllegalStateException.class,
            NumberFormatException.class));
    assertEquals("testyWrapOrThrow", illegalStateExceptionFoundInList.getMessage());
    //exception thrown with wrap on NOT being explicitly found within the passed list of exceptions
    var illegalStateExceptionNotFoundInListWrap = assertThrows(
        IllegalStateException.class,
        () -> TryCatchesOps.wrapCheckedException(() -> {
              voidFunctionWithCheckedException();
              if (true) {
                throw new IllegalStateException("testyWrap");
              }

              return 1;
            },
            IllegalArgumentException.class,
            NumberFormatException.class));
    assertEquals("testyWrap", illegalStateExceptionNotFoundInListWrap.getMessage());
    //exception thrown with wrapOrThrow on NOT being explicitly found within the passed list of exceptions
    var illegalStateExceptionNotFoundInListWrapOrThrow = assertThrows(
        IllegalStateException.class,
        () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
              voidFunctionWithCheckedException();
              if (true) {
                throw new IllegalStateException("testyWrapOrThrow");
              }

              return 1;
            },
            IllegalArgumentException.class,
            NumberFormatException.class));
    assertEquals("testyWrapOrThrow", illegalStateExceptionNotFoundInListWrapOrThrow.getMessage());
    //Either left checked exception returned with wrap on being explicitly sneaky with a checked exception within list of exceptions
    var eitherLeftCheckedException = TryCatchesOps.wrapCheckedException(() -> {
          voidFunctionWithCheckedException();
          if (true) {
            throwStrippedCheckedException(new IOException("optional sneaky wrap checked exception"));
          }

          return 1;
        },
        IOException.class,
        NumberFormatException.class);
    assertTrue(eitherLeftCheckedException.isLeft());
    assertEquals(IOException.class, eitherLeftCheckedException.getLeft().getClass());
    assertEquals("optional sneaky wrap checked exception", eitherLeftCheckedException.getLeft().getMessage());
    //wrapped exception thrown with wrapOrThrow on being explicitly sneaky with a checked exception within list of exceptions
    var wrappedCheckedExceptionFoundInList = assertThrows(
        WrappedCheckedException.class,
        () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
              voidFunctionWithCheckedException();
              if (true) {
                throwStrippedCheckedException(new IOException("optional sneaky wrapOrThrow checked exception"));
              }

              return 1;
            },
            IOException.class,
            NumberFormatException.class));
    assertEquals(IOException.class, wrappedCheckedExceptionFoundInList.getCause().getClass());
    assertEquals("optional sneaky wrapOrThrow checked exception", wrappedCheckedExceptionFoundInList.getCause().getMessage());
    //wrapped checked exception throw with wrap on being explicitly sneaky with a checked exception NOT in list of exceptions
    var wrappedCheckedExceptionNotFoundInListWrap = assertThrows(
        WrappedCheckedException.class,
        () -> TryCatchesOps.wrapCheckedException(() -> {
              voidFunctionWithCheckedException();
              if (true) {
                throwStrippedCheckedException(new IOException("optional sneaky wrap checked exception"));
              }

              return 1;
            },
            IllegalStateException.class,
            NumberFormatException.class));
    assertEquals(IOException.class, wrappedCheckedExceptionNotFoundInListWrap.getCause().getClass());
    assertEquals("optional sneaky wrap checked exception", wrappedCheckedExceptionNotFoundInListWrap.getCause().getMessage());
    var wrappedCheckedExceptionNotFoundInListWrapOrThrow = assertThrows(
        WrappedCheckedException.class,
        () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
              voidFunctionWithCheckedException();
              if (true) {
                throwStrippedCheckedException(new IOException("optional sneaky wrapOrThrow checked exception"));
              }

              return 1;
            },
            IllegalStateException.class,
            NumberFormatException.class));
    assertEquals(IOException.class, wrappedCheckedExceptionNotFoundInListWrapOrThrow.getCause().getClass());
    assertEquals("optional sneaky wrapOrThrow checked exception", wrappedCheckedExceptionNotFoundInListWrapOrThrow.getCause().getMessage());
  }

  @Test
  public void testFatalThrowablesPropagation() {
    // 1. InterruptedException across all 4 supplier categories
    assertThrows(InterruptedException.class, () -> TryCatchesOps.wrap(() -> throwStrippedCheckedException(new InterruptedException("fatal interrupt"))));
    assertThrows(InterruptedException.class, () -> TryCatchesOps.wrapOrThrow(() -> throwStrippedCheckedException(new InterruptedException("fatal interrupt"))));
    assertThrows(InterruptedException.class, () -> TryCatchesOps.wrap(() -> {
      throwStrippedCheckedException(new InterruptedException("fatal interrupt"));
      return 1;
    }));
    assertThrows(InterruptedException.class, () -> TryCatchesOps.wrapOrThrow(() -> {
      throwStrippedCheckedException(new InterruptedException("fatal interrupt"));
      return 1;
    }));
    assertThrows(InterruptedException.class, () -> TryCatchesOps.wrapCheckedException(() -> {
      throw new InterruptedException("fatal interrupt");
    }));
    assertThrows(InterruptedException.class, () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
      throw new InterruptedException("fatal interrupt");
    }));
    assertThrows(InterruptedException.class, () -> TryCatchesOps.wrapCheckedException(() -> {
      if (true) throw new InterruptedException("fatal interrupt");
      return 1;
    }));
    assertThrows(InterruptedException.class, () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
      if (true) throw new InterruptedException("fatal interrupt");
      return 1;
    }));

    // 2. OutOfMemoryError (VirtualMachineError)
    assertThrows(OutOfMemoryError.class, () -> TryCatchesOps.wrap(() -> {
      throw new OutOfMemoryError("fatal oom");
    }));
    assertThrows(OutOfMemoryError.class, () -> TryCatchesOps.wrapOrThrow(() -> {
      throw new OutOfMemoryError("fatal oom");
    }));
    assertThrows(OutOfMemoryError.class, () -> TryCatchesOps.wrap(() -> {
      if (true) throw new OutOfMemoryError("fatal oom");
      return 1;
    }));
    assertThrows(OutOfMemoryError.class, () -> TryCatchesOps.wrapOrThrow(() -> {
      if (true) throw new OutOfMemoryError("fatal oom");
      return 1;
    }));
    assertThrows(OutOfMemoryError.class, () -> TryCatchesOps.wrapCheckedException(() -> {
      throw new OutOfMemoryError("fatal oom");
    }));
    assertThrows(OutOfMemoryError.class, () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
      throw new OutOfMemoryError("fatal oom");
    }));
    assertThrows(OutOfMemoryError.class, () -> TryCatchesOps.wrapCheckedException(() -> {
      if (true) throw new OutOfMemoryError("fatal oom");
      return 1;
    }));
    assertThrows(OutOfMemoryError.class, () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> {
      if (true) throw new OutOfMemoryError("fatal oom");
      return 1;
    }));

    // 3. ControlBreakThrowable
    assertThrows(org.deus_ex_java.lang.ControlBreakThrowable.class, () -> TryCatchesOps.wrap(() -> throwStrippedCheckedException(new org.deus_ex_java.lang.ControlBreakThrowable("fatal control break") {
    })));
    assertThrows(org.deus_ex_java.lang.ControlBreakThrowable.class, () -> TryCatchesOps.wrapOrThrow(() -> throwStrippedCheckedException(new org.deus_ex_java.lang.ControlBreakThrowable("fatal control break") {
    })));
    assertThrows(org.deus_ex_java.lang.ControlBreakThrowable.class, () -> TryCatchesOps.wrapCheckedException(() -> throwStrippedCheckedException(new org.deus_ex_java.lang.ControlBreakThrowable("fatal control break") {
    })));
    assertThrows(org.deus_ex_java.lang.ControlBreakThrowable.class, () -> TryCatchesOps.wrapCheckedExceptionOrThrow(() -> throwStrippedCheckedException(new org.deus_ex_java.lang.ControlBreakThrowable("fatal control break") {
    })));

    // 4. LinkageError
    assertThrows(LinkageError.class, () -> TryCatchesOps.wrap(() -> {
      throw new LinkageError("fatal linkage");
    }));

    // 5. ThreadDeath
    var threadDeath = new ThreadDeath();
    assertThrows(ThreadDeath.class, () -> TryCatchesOps.wrap(() -> {
      throw threadDeath;
    }));
  }

  @Test
  public void testNullParameterValidation() {
    assertThrows(NullPointerException.class, () -> TryCatchesOps.wrap((org.deus_ex_java.util.function.VoidSupplier) null));
    assertThrows(NullPointerException.class, () -> TryCatchesOps.wrap(() -> {
    }, (Class<? extends Throwable>[]) null));
    assertThrows(NullPointerException.class, () -> TryCatchesOps.wrap((java.util.function.Supplier<Object>) null));
    assertThrows(NullPointerException.class, () -> TryCatchesOps.wrapCheckedException((org.deus_ex_java.util.function.VoidSupplierCheckedException) null));
    assertThrows(NullPointerException.class, () -> TryCatchesOps.wrapCheckedException((org.deus_ex_java.util.function.SupplierCheckedException<Object>) null));
  }
}
