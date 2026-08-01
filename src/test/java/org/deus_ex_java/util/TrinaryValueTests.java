package org.deus_ex_java.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"EqualsWithItself", "SimplifiableAssertion", "AssertBetweenInconvertibleTypes", "DataFlowIssue"})
public class TrinaryValueTests {

  private <E, T> void validatePresent(T expectedValue, TrinaryValue<E, T> trinary) {
    assertTrue(trinary.isPresent());
    assertFalse(trinary.isAbsent());
    assertFalse(trinary.isInvalid());
    assertEquals(expectedValue, trinary.get());

    var ex = assertThrows(NoSuchElementException.class, trinary::getError);
    assertEquals("No error present in state: " + trinary, ex.getMessage());
  }

  private <E, T> void validateAbsent(TrinaryValue<E, T> trinary) {
    assertFalse(trinary.isPresent());
    assertTrue(trinary.isAbsent());
    assertFalse(trinary.isInvalid());

    var exGet = assertThrows(NoSuchElementException.class, trinary::get);
    assertEquals("No value present in state: " + trinary, exGet.getMessage());

    var exErr = assertThrows(NoSuchElementException.class, trinary::getError);
    assertEquals("No error present in state: " + trinary, exErr.getMessage());
  }

  private <E, T> void validateInvalid(E expectedError, TrinaryValue<E, T> trinary) {
    assertFalse(trinary.isPresent());
    assertFalse(trinary.isAbsent());
    assertTrue(trinary.isInvalid());
    assertEquals(expectedError, trinary.getError());

    var ex = assertThrows(NoSuchElementException.class, trinary::get);
    assertEquals("No value present in state: " + trinary, ex.getMessage());
  }

  @Test
  public void testFactoryPresent() {
    TrinaryValue<Integer, String> trinary = TrinaryValue.present("PresentValue");
    validatePresent("PresentValue", trinary);
  }

  @Test
  public void testFactoryAbsent() {
    TrinaryValue<Integer, String> trinary = TrinaryValue.absent();
    validateAbsent(trinary);
    assertSame(TrinaryValue.absent(), TrinaryValue.<Integer, String>absent());
  }

  @Test
  public void testFactoryInvalid() {
    TrinaryValue<Integer, String> trinary = TrinaryValue.invalid(404);
    validateInvalid(404, trinary);
  }

  @Test
  public void testEquals() {
    TrinaryValue<Integer, String> presentA = TrinaryValue.present("A");
    TrinaryValue<Integer, String> presentA2 = TrinaryValue.present("A");
    TrinaryValue<Integer, String> presentB = TrinaryValue.present("B");

    TrinaryValue<Integer, String> absentA = TrinaryValue.absent();
    TrinaryValue<Integer, String> absentB = TrinaryValue.absent();

    TrinaryValue<Integer, String> invalidA = TrinaryValue.invalid(500);
    TrinaryValue<Integer, String> invalidA2 = TrinaryValue.invalid(500);
    TrinaryValue<Integer, String> invalidB = TrinaryValue.invalid(400);

    assertEquals(presentA, presentA);
    assertEquals(presentA, presentA2);
    assertNotEquals(presentA, presentB);
    assertFalse(presentA.equals(new Object()));

    assertEquals(absentA, absentB);

    assertEquals(invalidA, invalidA);
    assertEquals(invalidA, invalidA2);
    assertNotEquals(invalidA, invalidB);

    assertNotEquals(presentA, absentA);
    assertNotEquals(presentA, invalidA);
    assertNotEquals(absentA, invalidA);
  }

  @Test
  public void testHashCode() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("Sample");
    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(500);

    var set = Set.of(present, absent, invalid);

    assertTrue(set.contains(TrinaryValue.present("Sample")));
    assertTrue(set.contains(TrinaryValue.absent()));
    assertTrue(set.contains(TrinaryValue.invalid(500)));

    assertFalse(set.contains(TrinaryValue.present("Other")));
    assertFalse(set.contains(TrinaryValue.invalid(404)));

    assertEquals(present.hashCode(), TrinaryValue.present("Sample").hashCode());
    assertEquals(absent.hashCode(), TrinaryValue.absent().hashCode());
    assertEquals(invalid.hashCode(), TrinaryValue.invalid(500).hashCode());
  }

  @Test
  public void testToOptional() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("Value");
    assertTrue(present.toOptional().isPresent());
    assertEquals("Value", present.toOptional().get());

    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    assertTrue(absent.toOptional().isEmpty());

    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(500);
    var ex = assertThrows(IllegalStateException.class, invalid::toOptional);
    assertEquals("Cannot convert Invalid state to Optional. Error context: 500", ex.getMessage());
  }

  @Test
  public void testToStream() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("Value");
    assertEquals(List.of("Value"), present.stream().toList());

    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    assertTrue(absent.stream().toList().isEmpty());

    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(500);
    var ex = assertThrows(IllegalStateException.class, () -> invalid.stream().toList());
    assertEquals("Cannot convert Invalid state to Optional. Error context: 500", ex.getMessage());
  }

  @Test
  public void testToEither() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("Success");
    Either<Integer, String> eitherFromPresent = present.toEither(() -> 999);
    assertTrue(eitherFromPresent.isRight());
    assertEquals("Success", eitherFromPresent.getRight());

    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    Either<Integer, String> eitherFromAbsent = absent.toEither(() -> 404);
    assertTrue(eitherFromAbsent.isLeft());
    assertEquals(404, eitherFromAbsent.getLeft());

    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(500);
    var supplierCalled = new boolean[1];
    Either<Integer, String> eitherFromInvalid = invalid.toEither(() -> {
      supplierCalled[0] = true;
      return 999;
    });
    assertTrue(eitherFromInvalid.isLeft());
    assertEquals(500, eitherFromInvalid.getLeft());
    assertFalse(supplierCalled[0]);
  }

  @Test
  public void testRequired() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("Value");
    assertSame(present, present.required(() -> 404));

    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    TrinaryValue<Integer, String> escalated = absent.required(() -> 404);
    validateInvalid(404, escalated);

    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(500);
    assertSame(invalid, invalid.required(() -> 404));
  }

  @Test
  public void testOrElseGet() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("Value");
    assertSame(present, present.orElseGet(() -> TrinaryValue.present("Fallback")));

    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    TrinaryValue<Integer, String> fallback = absent.orElseGet(() -> TrinaryValue.present("Fallback"));
    validatePresent("Fallback", fallback);

    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(500);
    assertSame(invalid, invalid.orElseGet(() -> TrinaryValue.present("Fallback")));
  }

  @Test
  public void testFilter() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("Valid");
    TrinaryValue<Integer, String> passed = present.filter(s -> s.length() == 5, () -> 400);
    assertSame(present, passed);

    TrinaryValue<Integer, String> failed = present.filter(String::isEmpty, () -> 400);
    validateInvalid(400, failed);

    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    assertSame(absent, absent.filter(s -> true, () -> 400));

    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(500);
    assertSame(invalid, invalid.filter(s -> true, () -> 400));
  }

  @Test
  public void testMap() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("100");
    TrinaryValue<Integer, Integer> mappedPresent = present.map(Integer::parseInt);
    validatePresent(100, mappedPresent);

    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    TrinaryValue<Integer, Integer> mappedAbsent = absent.map(Integer::parseInt);
    validateAbsent(mappedAbsent);

    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(500);
    TrinaryValue<Integer, Integer> mappedInvalid = invalid.map(Integer::parseInt);
    validateInvalid(500, mappedInvalid);

    assertThrows(NullPointerException.class, () -> present.map(s -> null));
  }

  @Test
  public void testFlatMap() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("200");

    TrinaryValue<Integer, Integer> flatMappedPresent = present.flatMap(s -> TrinaryValue.present(Integer.parseInt(s)));
    validatePresent(200, flatMappedPresent);

    TrinaryValue<Integer, Integer> flatMappedToAbsent = present.flatMap(s -> TrinaryValue.absent());
    validateAbsent(flatMappedToAbsent);

    TrinaryValue<Integer, Integer> flatMappedToInvalid = present.flatMap(s -> TrinaryValue.invalid(400));
    validateInvalid(400, flatMappedToInvalid);

    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    assertSame(absent, absent.flatMap(s -> TrinaryValue.present(1)));

    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(500);
    assertSame(invalid, invalid.flatMap(s -> TrinaryValue.present(1)));

    assertThrows(NullPointerException.class, () -> present.flatMap(s -> null));
  }

  @Test
  public void testMapError() {
    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(404);
    TrinaryValue<String, String> mappedInvalid = invalid.mapError(err -> "HTTP " + err);
    validateInvalid("HTTP 404", mappedInvalid);

    TrinaryValue<Integer, String> present = TrinaryValue.present("Value");
    TrinaryValue<String, String> mappedPresent = present.mapError(err -> "HTTP " + err);
    validatePresent("Value", mappedPresent);

    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    TrinaryValue<String, String> mappedAbsent = absent.mapError(err -> "HTTP " + err);
    validateAbsent(mappedAbsent);

    assertThrows(NullPointerException.class, () -> invalid.mapError(err -> null));
  }

  @Test
  public void testFold() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("Val");
    String presentRes = present.fold(
        p -> "P:" + p,
        () -> "A",
        i -> "I:" + i
    );
    assertEquals("P:Val", presentRes);

    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    String absentRes = absent.fold(
        p -> "P:" + p,
        () -> "A",
        i -> "I:" + i
    );
    assertEquals("A", absentRes);

    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(500);
    String invalidRes = invalid.fold(
        p -> "P:" + p,
        () -> "A",
        i -> "I:" + i
    );
    assertEquals("I:500", invalidRes);
  }

  @Test
  public void testFlatMapGenericsCovariance() {
    TrinaryValue<Number, String> present = TrinaryValue.present("Success");
    TrinaryValue<Integer, Boolean> specificInvalid = TrinaryValue.invalid(404);

    TrinaryValue<Number, Boolean> result = present.flatMap(str -> specificInvalid);
    validateInvalid(404, result);
  }

  @Test
  public void testMapErrorGenericsCovariance() {
    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(404);
    TrinaryValue<Number, String> result = invalid.mapError(err -> err);
    validateInvalid(404, result);
  }

  @SuppressWarnings("UnnecessaryBoxing")
  @Test
  public void testToEitherGenericsCovariance() {
    TrinaryValue<Number, String> absent = TrinaryValue.absent();
    Either<Number, String> result = absent.toEither(() -> Integer.valueOf(404));
    assertTrue(result.isLeft());
    assertEquals(404, result.getLeft());
  }

  @Test
  public void testToEitherInvalidCovariance() {
    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(404);

    // Explicitly widen error channel from Integer to Number before reduction
    Either<Number, String> result = invalid
        .<Number>mapError(err -> err)
        .toEither(() -> 999);

    assertTrue(result.isLeft());
    assertEquals(404, result.getLeft());
  }

  @Test
  public void testPatternMatchingInstanceof() {
    TrinaryValue<Integer, String> present = TrinaryValue.present("Present");
    TrinaryValue<Integer, String> absent = TrinaryValue.absent();
    TrinaryValue<Integer, String> invalid = TrinaryValue.invalid(404);

    if (present instanceof TrinaryValue.Present<Integer, String> p) {
      assertEquals("Present", p.value());
    } else {
      fail("Expected Present variant");
    }

    if (absent instanceof TrinaryValue.Absent<Integer, String>) {
      assertTrue(absent.isAbsent());
    } else {
      fail("Expected Absent variant");
    }

    if (invalid instanceof TrinaryValue.Invalid<Integer, String> i) {
      assertEquals(404, i.error());
    } else {
      fail("Expected Invalid variant");
    }
  }

  @Test
  public void testNonNullInvariants() {
    assertThrows(NullPointerException.class, () -> new TrinaryValue.Present<>(null));
    assertThrows(NullPointerException.class, () -> new TrinaryValue.Invalid<>(null));
    assertThrows(NullPointerException.class, () -> TrinaryValue.present(null));
    assertThrows(NullPointerException.class, () -> TrinaryValue.invalid(null));
  }
}