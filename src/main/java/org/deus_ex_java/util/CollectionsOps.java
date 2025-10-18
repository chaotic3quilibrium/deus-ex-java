package org.deus_ex_java.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Utility class providing static methods to create and work with {@link Collection} instances.
 */
public final class CollectionsOps {

  private CollectionsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns {@code true} if the {@link Collection} throws an {@link UnsupportedOperationException} when calling
   * {@link Collection#addAll} with {@link Collections#emptyList}, false otherwise.
   *
   * @param collection instance being tested for being unmodifiable
   * @return {@code true} if the {@link Collection} throws an {@link UnsupportedOperationException} when calling
   *     {@link Collection#addAll} with {@link Collections#emptyList}, false otherwise
   */
  public static boolean isUnmodifiable(@NotNull Collection<?> collection) {
    try {
      collection.addAll(Collections.emptyList());

      return false;
    } catch (UnsupportedOperationException UnsupportedOperationException) {
      return true;
    }
  }

  /**
   * Returns {@code true} if the {@link Map} throws an {@link UnsupportedOperationException} when calling
   * {@link Map#putAll} with an empty {@link Map#of}, false otherwise.
   *
   * @param map instance being tested for being unmodifiable
   * @return {@code true} if the {@link Map} throws an {@link UnsupportedOperationException} when calling
   *     {@link Map#putAll} with an empty {@link Map#of}, false otherwise
   */
  public static boolean isUnmodifiable(@NotNull Map<?, ?> map) {
    try {
      map.putAll(Map.of());

      return false;
    } catch (UnsupportedOperationException UnsupportedOperationException) {
      return true;
    }
  }

}
