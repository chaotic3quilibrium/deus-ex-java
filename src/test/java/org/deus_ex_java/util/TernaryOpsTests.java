package org.deus_ex_java.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TernaryOpsTests {

  @Test
  public void testGet() {
    assertEquals(
        1,
        TernaryOps.get(
            true,
            () -> 1,
            () -> 2));
    assertEquals(
        2,
        TernaryOps.get(
            false,
            () -> 1,
            () -> 2));
  }

  @Test
  public void testOfSupplier() {
    assertEquals(
        1,
        TernaryOps.of(
                () -> true,
                () -> 1,
                () -> 2)
            .get());
    assertEquals(
        2,
        TernaryOps.of(
                () -> false,
                () -> 1,
                () -> 2)
            .get());
  }

  @Test
  public void testExecute() {
    var value = new int[]{0};
    TernaryOps.execute(
        true,
        () -> value[0] = 1,
        () -> value[0] = 2);
    assertEquals(1, value[0]);
    TernaryOps.execute(
        false,
        () -> value[0] = 1,
        () -> value[0] = 2);
    assertEquals(2, value[0]);
  }

  @Test
  public void testOfVoidSupplier() {
    var value = new int[]{0};
    TernaryOps.of(
            () -> true,
            () -> {
              value[0] = 1;
            },
            () -> {
              value[0] = 2;
            })
        .execute();
    assertEquals(1, value[0]);
    TernaryOps.of(
            () -> false,
            () -> {
              value[0] = 1;
            },
            () -> {
              value[0] = 2;
            })
        .execute();
    assertEquals(2, value[0]);
  }
}
