package org.deus_ex_java.util;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Utility class providing static helper methods for object lifecycle, null-safety, and validation.
 */
@NullMarked
public final class ObjectsOps {

  private ObjectsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns the given {@code object} if it is non-null, otherwise returns {@code defaultValue}.
   *
   * @param object       the object to evaluate
   * @param defaultValue the default value to return if {@code object} is null
   * @param <T>          the type of object
   * @return {@code object} if non-null, otherwise {@code defaultValue}
   * @throws NullPointerException if {@code defaultValue} is null
   */
  public static <T> T defaultIfNull(@Nullable T object, T defaultValue) {
    Objects.requireNonNull(defaultValue, "defaultValue cannot be null");
    return object != null ? object : defaultValue;
  }

  /**
   * Returns the given {@code object} if it is non-null, otherwise evaluates and returns the value supplied by
   * {@code defaultValueSupplier}.
   *
   * @param object               the object to evaluate
   * @param defaultValueSupplier the supplier for default value if {@code object} is null
   * @param <T>                  the type of object
   * @return {@code object} if non-null, otherwise supplied default value
   * @throws NullPointerException if {@code defaultValueSupplier} is null or returns null
   */
  public static <T> T defaultIfNullGet(@Nullable T object, Supplier<T> defaultValueSupplier) {
    Objects.requireNonNull(defaultValueSupplier, "defaultValueSupplier cannot be null");
    return object != null ? object : Objects.requireNonNull(defaultValueSupplier.get(), "defaultValueSupplier returned null");
  }

  /**
   * Returns {@code true} if all specified {@code objects} are non-null.
   *
   * @param objects the objects to evaluate
   * @return {@code true} if every object is non-null; {@code false} otherwise
   * @throws NullPointerException if {@code objects} array itself is null
   */
  public static boolean allNonNull(Object... objects) {
    Objects.requireNonNull(objects, "objects cannot be null");
    return Arrays.stream(objects).allMatch(Objects::nonNull);
  }

  /**
   * Returns {@code true} if at least one of the specified {@code objects} is non-null.
   *
   * @param objects the objects to evaluate
   * @return {@code true} if any object is non-null; {@code false} otherwise
   * @throws NullPointerException if {@code objects} array itself is null
   */
  public static boolean anyNonNull(Object... objects) {
    Objects.requireNonNull(objects, "objects cannot be null");
    return Arrays.stream(objects).anyMatch(Objects::nonNull);
  }

  /**
   * Returns {@code true} if all specified {@code objects} are null.
   *
   * @param objects the objects to evaluate
   * @return {@code true} if every object is null; {@code false} otherwise
   * @throws NullPointerException if {@code objects} array itself is null
   */
  public static boolean allNull(Object... objects) {
    Objects.requireNonNull(objects, "objects cannot be null");
    return Arrays.stream(objects).allMatch(Objects::isNull);
  }

  /**
   * Validates that every object in {@code objects} is non-null.
   *
   * @param message the detail message for thrown NullPointerException if validation fails
   * @param objects the objects to validate
   * @param <T>     the component type of objects array
   * @return the verified {@code objects} array
   * @throws NullPointerException if {@code objects} is null or contains any null element
   */
  @SafeVarargs
  public static <T> T[] requireAllNonNull(String message, T... objects) {
    Objects.requireNonNull(message, "message cannot be null");
    Objects.requireNonNull(objects, message);
    for (T obj : objects) {
      Objects.requireNonNull(obj, message);
    }
    return objects;
  }

  /**
   * Validates that every object in {@code objects} is non-null using a default exception message.
   *
   * @param objects the objects to validate
   * @param <T>     the component type of objects array
   * @return the verified {@code objects} array
   * @throws NullPointerException if {@code objects} is null or contains any null element
   */
  @SafeVarargs
  public static <T> T[] requireAllNonNull(T... objects) {
    return requireAllNonNull("All objects must be non-null", objects);
  }
}
