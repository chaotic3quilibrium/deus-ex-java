package org.deus_ex_java.util.function;

/**
 * Represents an action to execute without having to unnecessarily specify an input argument, and unnecessarily supply a
 * return value; i.e. exists entirely to computes an optionally side-effecting result, or throws a checked exception if unable
 * to do so.
 *
 * <p>There is no requirement that a new or distinct action occur, or new or distinct side-effects be generated, each
 * time it is invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #execute()}.
 */
@FunctionalInterface
public interface VoidSupplierChecked<E extends Exception> {

  /**
   * Computes an optionally side-effecting result, or throws a checked exception if unable to do so.
   *
   * @throws E if unable to compute a side-effecting result
   */
  void execute() throws E;
}

