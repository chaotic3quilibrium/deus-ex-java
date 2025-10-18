package org.deus_ex_java.util.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.Objects;

import static java.util.Map.entry;

//TODO: x4 missing javadocs
public class TuplesOps {
  private TuplesOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  @NotNull
  public static <K, V> Tuple2<K, V> from(
      @NotNull Entry<K, V> entry
  ) {
    return new Tuple2<>(
        Objects.requireNonNull(entry.getKey()),
        Objects.requireNonNull(entry.getValue()));
  }

  @NotNull
  public static <K, V> Entry<K, V> to(
      @NotNull Tuple2<K, V> tuple
  ) {
    return entry(
        tuple._1(),
        tuple._2());
  }

  @NotNull
  public static <K, V> Tuple2<K, V> from(
      @NotNull SimpleImmutableEntry<K, V> simpleImmutableEntry
  ) {
    return new Tuple2<>(
        Objects.requireNonNull(simpleImmutableEntry.getKey()),
        Objects.requireNonNull(simpleImmutableEntry.getValue()));
  }

  @NotNull
  public static <K, V> SimpleImmutableEntry<K, V> toSimpleImmutableEntry(
      @NotNull Tuple2<K, V> tuple
  ) {
    return new SimpleImmutableEntry<>(
        tuple._1(),
        tuple._2());
  }
}
