package org.deus_ex_java.lang.refined;

import org.jspecify.annotations.NullMarked;

/**
 * Common sealed root interface for all refined primitive record types.
 *
 * @param <V> the underlying raw scalar value type (e.g., {@link Integer}, {@link String}, etc.)
 */
@NullMarked
public sealed interface Refined<V> permits PosInt, NonNegInt, NonEmptyString, NonBlankString, NonEmptyLowerCaseString {

  /**
   * Returns the underlying raw scalar value wrapped by this refined primitive type.
   *
   * @return the underlying raw value
   */
  V value();
}
