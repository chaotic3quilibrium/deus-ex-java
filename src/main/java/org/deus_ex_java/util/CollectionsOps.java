package org.deus_ex_java.util;

import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Utility class providing static methods to inspect and manipulate {@link Collection} and {@link Map} instances.
 */
@NullMarked
public final class CollectionsOps {

  private CollectionsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns {@code true} if the specified {@link Collection} is identified as unmodifiable or immutable.
   * <p>
   * This method uses safe, non-mutating type inspection to verify unmodifiability without triggering side effects or
   * {@link java.util.ConcurrentModificationException} on live collections.
   * <p>
   * <b>Nullness Contract:</b> Under {@link NullMarked}, the {@code collection} argument is strictly non-null.
   * Passing {@code null} represents an illegal caller state in Value-Oriented Programming (VOP).
   *
   * @param collection non-null {@link Collection} instance to inspect
   * @return {@code true} if the collection is unmodifiable; {@code false} otherwise
   */
  public static boolean isUnmodifiable(Collection<?> collection) {
    return isUnmodifiableType(collection.getClass());
  }

  /**
   * Returns {@code true} if the specified {@link Map} is identified as unmodifiable or immutable.
   * <p>
   * This method uses safe, non-mutating type inspection to verify unmodifiability without triggering side effects or
   * {@link java.util.ConcurrentModificationException} on live maps.
   * <p>
   * <b>Nullness Contract:</b> Under {@link NullMarked}, the {@code map} argument is strictly non-null.
   * Passing {@code null} represents an illegal caller state in Value-Oriented Programming (VOP).
   *
   * @param map non-null {@link Map} instance to inspect
   * @return {@code true} if the map is unmodifiable; {@code false} otherwise
   */
  public static boolean isUnmodifiable(Map<?, ?> map) {
    return isUnmodifiableType(map.getClass());
  }

  private static final List<String> UNMODIFIABLE_CLASS_PREFIXES = List.of(
      "java.util.ImmutableCollections$",
      "java.util.Collections$Unmodifiable",
      "java.util.Collections$Empty",
      "java.util.Collections$Singleton"
  );

  private static boolean isUnmodifiableType(Class<?> clazz) {
    var className = clazz.getName();

    return UNMODIFIABLE_CLASS_PREFIXES.stream().anyMatch(className::startsWith)
        || className.contains("Unmodifiable")
        || className.contains("Immutable");
  }
}

