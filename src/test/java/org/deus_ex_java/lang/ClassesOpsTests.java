package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassesOpsTests {
  @Test
  public void testNarrow() {
    var optionalString = ClassesOps.narrow("X", String.class);
    assertTrue(optionalString.isPresent());
    assertEquals(Optional.of("X"), optionalString);
    var optionalInteger = ClassesOps.narrow(Integer.valueOf("1"), String.class);
    assertTrue(optionalInteger.isEmpty());
    assertEquals(Optional.empty(), optionalInteger);
  }

  @Test
  public void testNarrowWithSupplier() {
    var optionalString = ClassesOps.narrow(() ->
        (String) "X");
    assertTrue(optionalString.isPresent());
    assertEquals(Optional.of("X"), optionalString);
    @SuppressWarnings("DataFlowIssue")
    var optionalInteger = ClassesOps.narrow(() ->
        (String) ((Object) Integer.valueOf("1")));
    assertTrue(optionalInteger.isEmpty());
    assertEquals(Optional.empty(), optionalInteger);

  }
}
