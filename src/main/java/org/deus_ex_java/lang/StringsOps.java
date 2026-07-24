package org.deus_ex_java.lang;

import org.deus_ex_java.lang.refined.NonBlankString;
import org.deus_ex_java.lang.refined.NonEmptyString;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Utility class providing static methods to create {@link String} instances.
 */
@NullMarked
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
  public static String nullToEmpty(@Nullable String string) {
    return string != null
        ? string
        : "";
  }

  /**
   * Returns the index of the first occurrence (left-to-right scan) of the specified substring using the Unicode aware
   * {@link String#regionMatches(int, String, int, int)}, or 0 if the substring is empty, otherwise -1 if there is no
   * such occurrence.
   * <p>
   * This method avoids using the more common {@link String#toLowerCase} and/or {@link String#toUpperCase} methods, as
   * both of these methods have been shown to behave inconsistently, or even incorrectly, on languages like Turkish.
   *
   * @param source    the string within the left-to-right {@link String#regionMatches(int, String, int, int)} search
   *                  occurs
   * @param subString the target string to find
   * @return the index of the first occurrence (left-to-right scan) of the specified substring using the Unicode aware
   *     {@link String#regionMatches(int, String, int, int)}, or 0 if the substring is empty, otherwise -1 if there is
   *     no such occurrence
   */
  public static int indexOfIgnoreCase(
      String source,
      String subString
  ) {
    //caching subString's length
    var subStringLength = subString.length();
    if (subStringLength == 0)
      //empty string is always found by convention at index 0
      return 0;

    return IntStream.rangeClosed(0, source.length() - subStringLength)
        .filter(i -> source.regionMatches(true, i, subString, 0, subStringLength))
        .findFirst()
        .orElse(-1);
  }

  /**
   * Returns the index of the last occurrence (right-to-left scan) of the specified substring using the Unicode aware
   * {@link String#regionMatches(int, String, int, int)}, or 0 if the substring is empty, otherwise -1 if there is no
   * such occurrence.
   * <p>
   * This method avoids using the more common {@link String#toLowerCase} and/or {@link String#toUpperCase} methods, as
   * both of these methods have been shown to behave inconsistently, or even incorrectly, on languages like Turkish.
   *
   * @param source    the string within which the right-to-left {@link String#regionMatches(int, String, int, int)}
   *                  search occurs
   * @param subString the target string to find
   * @return the index of the last occurrence (right-to-left scan) of the specified substring using the Unicode aware
   *     {@link String#regionMatches(int, String, int, int)}, or 0 if the substring is empty, otherwise -1 if there is
   *     no such occurrence
   */
  public static int lastIndexOfIgnoreCase(
      String source,
      String subString
  ) {
    //caching subString's length
    var subStringLength = subString.length();
    if (subStringLength == 0)
      //empty string is always found by convention at index source.length()
      return source.length();

    return IntStream.iterate(source.length() - subStringLength, i -> i >= 0, i -> i - 1)
        .filter(i -> source.regionMatches(true, i, subString, 0, subStringLength))
        .findFirst()
        .orElse(-1);
  }

  /**
   * Returns {@code true} if the two strings are equal in length, and using the Unicode case-insensitive aware function,
   * {@link #indexOfIgnoreCase(String, String)}, the first occurrence of the second string is at index 0 of the first
   * string, otherwise {@code false}.
   * <p>
   * This method avoids using the more common {@link String#toLowerCase} and/or {@link String#toUpperCase} methods, as
   * both of these methods have been shown to behave inconsistently, or even incorrectly, on languages like Turkish.
   *
   * @param stringA the first string
   * @param stringB the second string
   * @return {@code true} if the two strings are equal in length, and using the Unicode case-insensitive aware function,
   *     {@link #indexOfIgnoreCase(String, String)}, the first occurrence of the second string is at index 0 of the
   *     first string, otherwise {@code false}
   */
  public static boolean equalsIgnoreCase(
      String stringA,
      String stringB
  ) {
    return (stringA.length() == stringB.length()) &&
        indexOfIgnoreCase(stringA, stringB) != -1;
  }

  /**
   * Returns {@code true} if both {@code stringA} and {@code stringB} are {@code null}, or if the Unicode
   * case-insensitive aware function, {@code equalsIgnoreCase(stringA, stringB)}, returns {@code true}, otherwise
   * {@code false}.
   *
   * @param stringA possibly null string
   * @param stringB possibly null string
   * @return {@code true} if both {@code stringA} and {@code stringB} are {@code null}, or if the Unicode
   *     case-insensitive aware function, {@code equalsIgnoreCase(stringA, stringB)}, returns {@code true}, otherwise
   *     {@code false}
   */
  public static boolean equalsIgnoreCaseNullable(
      @Nullable String stringA,
      @Nullable String stringB
  ) {
    if (stringA == null || stringB == null) {
      return Objects.equals(stringA, stringB);
    }

    return equalsIgnoreCase(stringA, stringB);
  }

}
