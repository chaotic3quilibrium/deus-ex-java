package org.deus_ex_java.util;

import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Utility class providing static methods to create and work with {@link Collection} instances.
 */
@NullMarked
public final class CollectionsOps {

  private CollectionsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns {@code true} if the {@link Collection} is identified as unmodifiable via internal class type or behavioral
   * probing.
   * <p>
   * Behavior probing check to see if an {@link UnsupportedOperationException} is thrown when calling
   * {@link Collection#addAll} with an {@link Collections#emptyList}, false otherwise.
   *
   * @param collection instance being tested for being unmodifiable
   * @return {@code true} if the {@link Collection} throws an {@link UnsupportedOperationException} when calling
   *     {@link Collection#addAll} with an {@link Collections#emptyList}, false otherwise
   */
  public static boolean isUnmodifiable(Collection<?> collection) {
    // 1. Fast path: Check known JDK internal types (Java 17+)
    var className = collection.getClass().getName();
    if (className.contains("Unmodifiable") || className.contains("ImmutableCollections")) {
      return true;
    }

    // 2. Slow path: Your existing behavioral probe
    try {
      collection.addAll(Collections.emptyList());

      return false;
    } catch (UnsupportedOperationException UnsupportedOperationException) {
      return true;
    }
  }

  /**
   * Returns {@code true} if the {@link Map} is identified as unmodifiable via internal class type or behavioral
   * probing.
   * <p>
   * Behavior probing check to see if an {@link UnsupportedOperationException} is thrown when calling {@link Map#putAll}
   * with an {@link Collections#emptyList}, false otherwise.
   *
   * @param map instance being tested for being unmodifiable
   * @return {@code true} if the {@link Map} is identified as unmodifiable via internal class type or behavioral probing
   */
  public static boolean isUnmodifiable(Map<?, ?> map) {
    // 1. Fast path: Check for known JDK immutable/unmodifiable types
    var className = map.getClass().getName();
    if (className.contains("Unmodifiable") || className.contains("ImmutableCollections")) {
      return true;
    }

    // 2. Slow path: Behavioral probe for custom or unknown implementations
    try {
      map.putAll(Map.of());

      return false;
    } catch (UnsupportedOperationException UnsupportedOperationException) {
      return true;
    }
  }
}
