package org.deus_ex_java.lang;

import org.deus_ex_java.util.TryCatchesOps;
import org.deus_ex_java.util.Using;
import org.deus_ex_java.util.UsingCheckedException;
import org.deus_ex_java.util.function.FunctionsOps;
import org.deus_ex_java.util.function.FunctionsPrimitivesOps;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * A package-private utility class to detect ({@link #isFatalThrowable(Throwable)}) and support tunneling <em>fatal</em>
 * throwable exceptions through the various try/catch guard patterns; i.e., preventing these <em>fatal</em> exceptions
 * from being successfully "caught".
 * <p>
 * The primary value is captured in the unchecked exception method,
 * {@link #requireNonFatalThrowableOrElseThrowFatalThrowable(Throwable)}. This encapsulates the checked exception
 * pathway such that it does not leak into the method's signature, thereby forcing the clients to handle the checked
 * exception.
 * <p>
 * This class is used extensively by:
 * <ul>
 * <li>{@link TryCatchesOps}</li>
 * <li>{@link WrappedCheckedException}</li>
 * </ul>
 * And indirectly by:
 * <ul>
 * <li>{@link FunctionsOps}</li>
 * <li>{@link FunctionsPrimitivesOps}</li>
 * <li>{@link Using}</li>
 * <li>{@link UsingCheckedException}</li>
 * </ul>
 */
@NullMarked
final class ForcedFatalThrowable {

  private ForcedFatalThrowable() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns {@code true} if the {@code throwable} (or any exception in its cause chain) matches an instance, or
   * descendant, of any fatal exception type registered in {@link WrappedCheckedException#FATAL_THROWABLE_TYPES}.
   * <p>
   * This strategy is inspired by the one used in Scala 2.13's <a
   * href="https://github.com/scala/scala/blob/v2.13.16/src/library/scala/util/control/NonFatal.scala#L17">NonFatal</a>
   * Object.
   * <p>
   * <b>Nullness Contract:</b> Under {@link NullMarked}, the {@code throwable} parameter is strictly non-null.
   * Passing {@code null} as a {@link Throwable} represents an illegal caller state under VOP architecture principles;
   * nullness validation must occur at caller boundaries rather than polluting core domain utilities with defensive
   * null-checks.
   *
   * @param throwable instance of an exception against which to test for Fatal
   * @return {@code true} if {@code throwable} or any cause in its cause chain matches a fatal type or descendant
   */
  static boolean isFatalThrowable(
      Throwable throwable
  ) {
    Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());
    Throwable current = throwable;
    while (current != null && visited.add(current)) {
      if (WrappedCheckedException.isFatalType(current.getClass())) {
        return true;
      }
      current = current.getCause();
    }

    return false;
  }

  /**
   * Returns the passed {@code throwable} if it is non-fatal, i.e. returns {@code false} from
   * {@link ForcedFatalThrowable#isFatalThrowable}, otherwise throws the throwable, even if it is a checked exception,
   * because there is no {@code throws} clause in this method's signature.
   *
   * @param throwable instance of an exception against which to test {@link ForcedFatalThrowable#isFatalThrowable}
   * @return the passed {@code throwable} if it is non-fatal, i.e. returns {@code false} from
   *     {@link ForcedFatalThrowable#isFatalThrowable}, otherwise throws the throwable, even if it is a checked
   *     exception, because there is no {@code throws} clause in this method's signature
   */
  static Throwable requireNonFatalThrowableOrElseThrowFatalThrowable(Throwable throwable) {
    if (isFatalThrowable(throwable)) {
      throwFatalThrowable(throwable);
    }

    return throwable;
  }

  /**
   * Iteratively inspects the exception cause chain to determine if an {@link InterruptedException} is present. Uses
   * reference-equality set tracking via {@link IdentityHashMap} to prevent {@link StackOverflowError} on cyclic
   * exception chains.
   *
   * @param throwable root throwable to inspect
   * @return {@code true} if an {@link InterruptedException} is found within the cause chain; {@code false} otherwise
   */
  private static boolean hasInterruptedException(Throwable throwable) {
    Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());
    Throwable current = throwable;
    while (current != null && visited.add(current)) {
      if (current instanceof InterruptedException) {
        return true;
      }
      current = current.getCause();
    }

    return false;
  }

  /**
   * Internal sneaky throws primitive allowing fatal checked exceptions (e.g., {@link InterruptedException}) to be
   * re-thrown without requiring declared {@code throws} clauses on surrounding method signatures.
   * <p>
   * <b>Architectural Rationale:</b> Sneaky throws is intentionally employed here specifically for fatal exception
   * tunneling. Wrapping fatal checked exceptions in {@link RuntimeException} would allow generic catch blocks to
   * accidentally catch and suppress fatal signals. Sneaky throws guarantees that raw fatal exceptions propagate
   * unhindered to top-level thread boundaries.
   *
   * @param throwable fatal exception to rethrow
   * @param <T>       implicit unchecked exception type parameter used for erasure casting
   * @throws T the fatal throwable instance, rethrown without compile-time signature checks
   */
  @SuppressWarnings("unchecked")
  private static <T extends Throwable> void throwFatalThrowableAsUncheckedException(
      Throwable throwable
  ) throws T {
    // Rely on Java type erasure to bypass compiler checked exception enforcement
    throw (T) throwable;
  }

  private static void throwFatalThrowable(Throwable throwable) {
    if (hasInterruptedException(throwable)) {
      Thread.currentThread().interrupt();
    }
    throwFatalThrowableAsUncheckedException(throwable);
  }
}
