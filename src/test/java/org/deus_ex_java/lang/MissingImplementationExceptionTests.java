package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MissingImplementationExceptionTests {
  @Test
  public void testConstructorEmpty() {
    var missingImplementationException = new MissingImplementationException();
    assertNull(missingImplementationException.getMessage());
  }

  @Test
  public void testConstructorMessage() {
    var missingImplementationException = new MissingImplementationException("test");
    assertEquals("test", missingImplementationException.getMessage());
  }

  @Test
  public void testConstructorAll() {
    var cause = new IOException("test");
    var missingImplementationException = new MissingImplementationException("test", cause, false, false);
    assertEquals("test", missingImplementationException.getMessage());
    assertEquals(cause, missingImplementationException.getCause());
  }
}
