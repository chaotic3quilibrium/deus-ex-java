package org.deus_ex_java.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectsOpsTests {

  @Test
  public void testDefaultIfNull() {
    assertEquals("value", ObjectsOps.defaultIfNull("value", "default"));
    assertEquals("default", ObjectsOps.defaultIfNull(null, "default"));
    assertThrows(NullPointerException.class, () -> ObjectsOps.defaultIfNull(null, null));
  }

  @Test
  public void testDefaultIfNullGet() {
    var supplierCalled = new AtomicBoolean(false);
    assertEquals("value", ObjectsOps.defaultIfNullGet("value", () -> {
      supplierCalled.set(true);
      return "default";
    }));
    assertFalse(supplierCalled.get());

    assertEquals("default", ObjectsOps.defaultIfNullGet(null, () -> {
      supplierCalled.set(true);
      return "default";
    }));
    assertTrue(supplierCalled.get());

    assertThrows(NullPointerException.class, () -> ObjectsOps.defaultIfNullGet(null, () -> null));
    assertThrows(NullPointerException.class, () -> ObjectsOps.defaultIfNullGet(null, null));
  }

  @Test
  public void testAllNonNull() {
    assertTrue(ObjectsOps.allNonNull("a", "b", "c"));
    assertFalse(ObjectsOps.allNonNull("a", null, "c"));
    assertFalse(ObjectsOps.allNonNull((Object) null));
    assertTrue(ObjectsOps.allNonNull());
    assertThrows(NullPointerException.class, () -> ObjectsOps.allNonNull((Object[]) null));
  }

  @Test
  public void testAnyNonNull() {
    assertTrue(ObjectsOps.anyNonNull("a", null, "c"));
    assertTrue(ObjectsOps.anyNonNull(null, null, "c"));
    assertFalse(ObjectsOps.anyNonNull(null, null, null));
    assertFalse(ObjectsOps.anyNonNull());
    assertThrows(NullPointerException.class, () -> ObjectsOps.anyNonNull((Object[]) null));
  }

  @Test
  public void testAllNull() {
    assertTrue(ObjectsOps.allNull(null, null, null));
    assertFalse(ObjectsOps.allNull("a", null, "c"));
    assertTrue(ObjectsOps.allNull());
    assertThrows(NullPointerException.class, () -> ObjectsOps.allNull((Object[]) null));
  }

  @Test
  public void testRequireAllNonNullWithMessage() {
    var array = new String[]{"a", "b", "c"};
    assertSame(array, ObjectsOps.requireAllNonNull("Custom message", array));

    var npe = assertThrows(
        NullPointerException.class,
        () -> ObjectsOps.requireAllNonNull("Element must not be null", "a", null, "c")
    );
    assertEquals("Element must not be null", npe.getMessage());

    assertThrows(NullPointerException.class, () -> ObjectsOps.requireAllNonNull("Null array", (String[]) null));
    assertThrows(NullPointerException.class, () -> ObjectsOps.requireAllNonNull((String) null, "a", "b"));
  }

  @Test
  public void testRequireAllNonNullDefaultMessage() {
    var array = new Integer[]{1, 2, 3};
    assertSame(array, ObjectsOps.requireAllNonNull(array));

    var npe = assertThrows(
        NullPointerException.class,
        () -> ObjectsOps.requireAllNonNull(1, null, 3)
    );
    assertEquals("All objects must be non-null", npe.getMessage());
  }
}
