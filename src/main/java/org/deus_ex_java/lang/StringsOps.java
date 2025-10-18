package org.deus_ex_java.lang;

import org.deus_ex_java.lang.refined.NonBlankString;
import org.deus_ex_java.lang.refined.NonEmptyString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Utility class providing static methods to create {@link String} instances.
 */
public final class StringsOps {

  private StringsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns an empty {@link String} of {@code ""}, if {@code string} is {@code null}, otherwise returns
   * {@code string}.
   * <p>
   * Note: If the requirement is to both avoid a {@code null} value, and an empty (or even blank) {@link String}, the
   * refined classes of {@link NonEmptyString} and {@link NonBlankString} enable <i>compile-time enforcement</i> of said
   * contract requirements.
   *
   * @param string possibly {@code null} {@link String} to reify to make {@code null} safe
   * @return an empty {@link String} of {@code ""}, if {@code string} is {@code null}, otherwise returns {@code string}
   */
  @NotNull
  public static String nullToEmpty(@Nullable String string) {
    return string != null
        ? string
        : "";
  }

  /**
   * Returns {@code true} if both {@code stringA} and {@code stringB} are {@code null}, or if
   * {@code stringA.equalsIgnoreCase(stringB)} returns {@code true}, otherwise {@code false}.
   *
   * @param stringA possibly null string
   * @param stringB possibly null string
   * @return {@code true} if both {@code stringA} and {@code stringB} are {@code null}, or if
   *     {@code stringA.equalsIgnoreCase(stringB)} returns {@code true}, otherwise {@code false}
   */
  public static boolean equalsIgnoreCaseNullable(
      @Nullable String stringA,
      @Nullable String stringB
  ) {
    var isStringANull = Objects.isNull(stringA);

    return (isStringANull == Objects.isNull(stringB)) &&
        (isStringANull || stringA.equalsIgnoreCase(stringB));
  }
}
