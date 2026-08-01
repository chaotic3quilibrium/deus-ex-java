package org.deus_ex_java.lang.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.Either;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RefinedTests {

  @SuppressWarnings("ConstantValue")
  @Test
  public void testPolymorphicPatternMatchingAndSealedHierarchy() {
    List<Refined<?>> refinedValues = List.of(
        new PosInt(42),
        new NonNegInt(0),
        new NonEmptyString("hello"),
        new NonBlankString("world"),
        new NonEmptyLowerCaseString("deus")
    );

    for (Refined<?> refined : refinedValues) {
      String description;
      if (refined instanceof PosInt p) {
        description = "PosInt:" + p.value();
      } else if (refined instanceof NonNegInt n) {
        description = "NonNegInt:" + n.value();
      } else if (refined instanceof NonEmptyString s) {
        description = "NonEmptyString:" + s.value();
      } else if (refined instanceof NonBlankString b) {
        description = "NonBlankString:" + b.value();
      } else if (refined instanceof NonEmptyLowerCaseString l) {
        description = "NonEmptyLowerCaseString:" + l.value();
      } else {
        fail("Unrecognized subtype of sealed interface Refined: " + refined.getClass().getName());
        description = "";
      }

      assertNotNull(description);
      assertFalse(description.isEmpty());
    }

    // Individual polymorphic reference assertions
    Refined<?> posIntRef = new PosInt(10);
    assertInstanceOf(PosInt.class, posIntRef);
    assertEquals(10, posIntRef.value());

    Refined<String> stringRef = new NonEmptyString("test");
    assertEquals("test", stringRef.value());
  }

  @Test
  public void testUniformValueAccessor() {
    Refined<Integer> posInt = new PosInt(5);
    assertEquals(Integer.valueOf(5), posInt.value());

    Refined<Integer> nonNegInt = new NonNegInt(0);
    assertEquals(Integer.valueOf(0), nonNegInt.value());

    Refined<String> nonEmptyString = new NonEmptyString("abc");
    assertEquals("abc", nonEmptyString.value());

    Refined<String> nonBlankString = new NonBlankString("xyz");
    assertEquals("xyz", nonBlankString.value());

    Refined<String> nonEmptyLowerCaseString = new NonEmptyLowerCaseString("lowercase");
    assertEquals("lowercase", nonEmptyLowerCaseString.value());
  }

  @Test
  public void testPureValidationFlowWithTryCatchesWrap() {
    // Invalid inputs -> Either.Left containing ParametersValidationException
    Either<ParametersValidationException, PosInt> posIntInvalid = PosInt.from(0);
    assertTrue(posIntInvalid.isLeft());

    Either<ParametersValidationException, NonNegInt> nonNegIntInvalid = NonNegInt.from(-1);
    assertTrue(nonNegIntInvalid.isLeft());

    Either<ParametersValidationException, NonEmptyString> nonEmptyStringInvalid = NonEmptyString.from("");
    assertTrue(nonEmptyStringInvalid.isLeft());

    Either<ParametersValidationException, NonBlankString> nonBlankStringInvalid = NonBlankString.from("   ");
    assertTrue(nonBlankStringInvalid.isLeft());

    Either<ParametersValidationException, NonEmptyLowerCaseString> nonEmptyLowerCaseStringInvalid = NonEmptyLowerCaseString.from("UPPER");
    assertTrue(nonEmptyLowerCaseStringInvalid.isLeft());

    // Valid inputs -> Either.Right containing validated instance
    Either<ParametersValidationException, PosInt> posIntValid = PosInt.from(1);
    assertTrue(posIntValid.isRight());
    assertEquals(1, posIntValid.getRight().value());

    Either<ParametersValidationException, NonNegInt> nonNegIntValid = NonNegInt.from(0);
    assertTrue(nonNegIntValid.isRight());
    assertEquals(0, nonNegIntValid.getRight().value());

    Either<ParametersValidationException, NonEmptyString> nonEmptyStringValid = NonEmptyString.from("a");
    assertTrue(nonEmptyStringValid.isRight());
    assertEquals("a", nonEmptyStringValid.getRight().value());

    Either<ParametersValidationException, NonBlankString> nonBlankStringValid = NonBlankString.from("b");
    assertTrue(nonBlankStringValid.isRight());
    assertEquals("b", nonBlankStringValid.getRight().value());

    Either<ParametersValidationException, NonEmptyLowerCaseString> nonEmptyLowerCaseStringValid = NonEmptyLowerCaseString.from("c");
    assertTrue(nonEmptyLowerCaseStringValid.isRight());
    assertEquals("c", nonEmptyLowerCaseStringValid.getRight().value());
  }
}
