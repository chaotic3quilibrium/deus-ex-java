package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParametersValidationExceptionTests {
  @Test
  public void testConstructor() {
    var parametersValidationException = new ParametersValidationException();
    assertEquals("Parameters validation failed", parametersValidationException.getMessage());
    assertNull(parametersValidationException.getCause());
    assertEquals(List.of(), parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: Parameters validation failed", parametersValidationException.toString());
  }

  @Test
  public void testConstructorMessage() {
    var message = "Test Message";
    var parametersValidationException = new ParametersValidationException(message);
    assertEquals(message, parametersValidationException.getMessage());
    assertNull(parametersValidationException.getCause());
    assertEquals(List.of(), parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: " + message, parametersValidationException.toString());
  }

  @Test
  public void testConstructorParametersValidationFailureMessages() {
    var messages = List.of("A", "B");
    var parametersValidationException = new ParametersValidationException(messages);
    assertEquals("Parameters validation failed - Parameter Validation Failures: [A|B]", parametersValidationException.getMessage());
    assertNull(parametersValidationException.getCause());
    assertEquals(messages, parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: Parameters validation failed - Parameter Validation Failures: [A|B]", parametersValidationException.toString());
  }

  @Test
  public void testConstructorCause() {
    var cause = new IOException("test");
    var parametersValidationException = new ParametersValidationException(cause);
    assertEquals("Parameters validation failed", parametersValidationException.getMessage());
    assertEquals(cause, parametersValidationException.getCause());
    assertEquals(List.of(), parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: Parameters validation failed", parametersValidationException.toString());
  }

  @Test
  public void testConstructorMessageAndCause() {
    var message = "Test Message";
    var cause = new IOException("test");
    var parametersValidationException = new ParametersValidationException(message, cause);
    assertEquals(message, parametersValidationException.getMessage());
    assertEquals(cause, parametersValidationException.getCause());
    assertEquals(List.of(), parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: Test Message", parametersValidationException.toString());
  }

  @Test
  public void testConstructorMessageAndParametersValidationFailureMessage() {
    var message = "Test Message";
    var messageX1 = "Whoa there nelly!";
    var parametersValidationException = new ParametersValidationException(message, messageX1);
    assertEquals("Test Message - Parameter Validation Failures: [Whoa there nelly!]", parametersValidationException.getMessage());
    assertNull(parametersValidationException.getCause());
    assertEquals(List.of(messageX1), parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: Test Message - Parameter Validation Failures: [Whoa there nelly!]", parametersValidationException.toString());
  }

  @Test
  public void testConstructorMessageAndParametersValidationFailureMessages() {
    var message = "Test Message";
    var messages = List.of("A", "B");
    var parametersValidationException = new ParametersValidationException(message, messages);
    assertEquals("Test Message - Parameter Validation Failures: [A|B]", parametersValidationException.getMessage());
    assertNull(parametersValidationException.getCause());
    assertEquals(messages, parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: Test Message - Parameter Validation Failures: [A|B]", parametersValidationException.toString());
  }

  @Test
  public void testConstructorCauseAndParametersValidationFailureMessages() {
    var cause = new IOException("test");
    var messages = List.of("A", "B");
    var parametersValidationException = new ParametersValidationException(cause, messages);
    assertEquals("Parameters validation failed - Parameter Validation Failures: [A|B]", parametersValidationException.getMessage());
    assertEquals(cause, parametersValidationException.getCause());
    assertEquals(messages, parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: Parameters validation failed - Parameter Validation Failures: [A|B]", parametersValidationException.toString());
  }

  @Test
  public void testConstructorMessageAndCauseAndParametersValidationFailureMessage() {
    var message = "Test Message";
    var cause = new IOException("test");
    var messageX1 = "Whoa there nelly!";
    var parametersValidationException = new ParametersValidationException(message, cause, messageX1);
    assertEquals("Test Message - Parameter Validation Failures: [Whoa there nelly!]", parametersValidationException.getMessage());
    assertEquals(cause, parametersValidationException.getCause());
    assertEquals(List.of(messageX1), parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: Test Message - Parameter Validation Failures: [Whoa there nelly!]", parametersValidationException.toString());
  }

  @Test
  public void testConstructorMessageAndCauseAndParametersValidationFailureMessages() {
    var message = "Test Message";
    var cause = new IOException("test");
    var messages = List.of("A", "B");
    var parametersValidationException = new ParametersValidationException(message, cause, messages);
    assertEquals("Test Message - Parameter Validation Failures: [A|B]", parametersValidationException.getMessage());
    assertEquals(cause, parametersValidationException.getCause());
    assertEquals(messages, parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: Test Message - Parameter Validation Failures: [A|B]", parametersValidationException.toString());
  }

  @Test
  public void testConstructorAll() {
    var cause = new IOException("test");
    var messages = List.of(
        "A",
        "B");
    var parametersValidationException = new ParametersValidationException(
        "test",
        cause,
        false,
        false,
        messages);
    assertEquals("test - Parameter Validation Failures: [A|B]", parametersValidationException.getMessage());
    assertEquals(cause, parametersValidationException.getCause());
    assertEquals(messages, parametersValidationException.getParametersValidationFailureMessages());
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: test - Parameter Validation Failures: [A|B]", parametersValidationException.toString());
    var parametersValidationException2 = new ParametersValidationException(
        "test",
        cause,
        true,
        true,
        messages);
    //equals ignores the two boolean parameters
    assertEquals(parametersValidationException, parametersValidationException2);
    var parametersValidationException3 = new ParametersValidationException(
        "test1",
        cause,
        messages);
    assertNotEquals(parametersValidationException, parametersValidationException3);
    var cause4 = new IOException("test4");
    var parametersValidationException4 = new ParametersValidationException(
        "test",
        cause4,
        messages);
    assertNotEquals(parametersValidationException, parametersValidationException4);
    assertNotEquals(parametersValidationException2, parametersValidationException4);
    assertNotEquals(parametersValidationException3, parametersValidationException4);
    var messages5 = List.of("A", "B", "C");
    var parametersValidationException5 = new ParametersValidationException(
        "test",
        cause,
        messages5);
    assertNotEquals(parametersValidationException, parametersValidationException5);
    assertNotEquals(parametersValidationException2, parametersValidationException5);
    assertNotEquals(parametersValidationException3, parametersValidationException5);
    assertNotEquals(parametersValidationException4, parametersValidationException5);
    assertEquals("org.deus_ex_java.lang.ParametersValidationException: test - Parameter Validation Failures: [A|B|C]", parametersValidationException5.toString());
  }

  @Test
  public void testEqualsAndHashCode() {
    var cause = new IOException("test");
    var messages = List.of(
        "A",
        "B");
    var parametersValidationExceptionA = new ParametersValidationException(
        "test",
        cause,
        false,
        false,
        messages);
    var parametersValidationExceptionB = new ParametersValidationException(
        "test",
        cause,
        false,
        false,
        messages);
    //noinspection SimplifiableAssertion,EqualsWithItself
    assertTrue(parametersValidationExceptionA.equals(parametersValidationExceptionA));
    assertEquals(parametersValidationExceptionA, parametersValidationExceptionB);
    assertEquals(parametersValidationExceptionB, parametersValidationExceptionA);
    assertEquals(parametersValidationExceptionA.hashCode(), parametersValidationExceptionB.hashCode());
  }
}
