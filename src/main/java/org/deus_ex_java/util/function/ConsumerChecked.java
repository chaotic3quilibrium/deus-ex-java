package org.deus_ex_java.util.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Enables the providing of a {@link Consumer} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface ConsumerChecked<T, EX extends Exception> {

  /**
   * Performs this operation on the given argument.
   *
   * @param t the input argument
   */
  void accept(T t) throws EX;

  /**
   * Returns a composed {@code Consumer} that performs, in sequence, this operation followed by the {@code after}
   * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
   * operation.  If performing this operation throws an exception, the {@code after} operation will not be performed.
   *
   * @param after the operation to perform after this operation
   * @return a composed {@code Consumer} that performs in sequence this operation followed by the {@code after}
   *     operation
   * @throws NullPointerException if {@code after} is null
   */
  default ConsumerChecked<T, EX> andThen(ConsumerChecked<? super T, ? extends EX> after) {
    Objects.requireNonNull(after);

    return (T t) -> {
      accept(t);
      after.accept(t);
    };
  }
}
