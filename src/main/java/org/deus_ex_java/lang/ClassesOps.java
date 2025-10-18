package org.deus_ex_java.lang;

import org.deus_ex_java.util.Either;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Utility class providing static methods to create {@link Class} instances.
 */
public final class ClassesOps {

  private ClassesOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns an {@link Optional} containing the object successfully narrowed by {@code tClass}, otherwise
   * {@link Optional#empty()}.
   *
   * @param object the instance to attempt to narrow
   * @param tClass the narrowing target {@link Class}
   * @param <T>    the type of the narrowing target
   * @return an {@link Optional} containing the object successfully narrowed by {@code tClass}, otherwise
   *     {@link Optional#empty()}
   */
  @NotNull
  public static <T> Optional<T> narrow(
      @NotNull Object object,
      @NotNull Class<T> tClass
  ) {
    return tClass.isInstance(object)
        ? Optional.of(tClass.cast(object))
        : Optional.empty();
  }

  /**
   * Returns an {@link Optional} containing the object successfully narrowed within the {@code supplier}; i.e.
   * {@code supplier.get()} didn't throw a {@link ClassCastException}, otherwise {@link Optional#empty()}.
   * <p>
   * <b>** WARNING:</b>
   * <p>
   * This does not check and catch generic type parameters. IOW, it will not catch the improper cast of
   * {@code List<String>} on an instance of {@code List<Integer>} because the List will resolve, leaving the generic
   * type unchecked.
   *
   * @param supplier the supplier of the instance attempting to be narrowed
   * @param <T>      the type of the narrowing target
   * @return an {@link Optional} containing the object successfully narrowed within the {@code supplier}; i.e.
   *     {@code supplier.get()} didn't throw a {@link ClassCastException}, otherwise {@link Optional#empty()}
   */
  @NotNull
  public static <T> Optional<T> narrow(
      @NotNull Supplier<T> supplier
  ) {
    return Either.tryCatch(
            supplier,
            ClassCastException.class)
        .toOptional();
  }
}
