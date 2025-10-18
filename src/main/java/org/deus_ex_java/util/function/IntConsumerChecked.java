package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;

/**
 * Enables the providing of a {@link IntConsumer} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface IntConsumerChecked<E extends Exception> {

  /**
   * Performs this operation on the given argument.
   *
   * @param value the input argument
   */
  void accept(int value) throws E;

  /**
   * Returns a composed {@code IntConsumer} that performs, in sequence, this operation followed by the {@code after}
   * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
   * operation.  If performing this operation throws an exception, the {@code after} operation will not be performed.
   *
   * @param after the operation to perform after this operation
   * @return a composed {@code IntConsumer} that performs in sequence this operation followed by the {@code after}
   *     operation
   */
  @NotNull
  default IntConsumerChecked<E> andThen(
      @NotNull IntConsumerChecked<? extends E> after
  ) {
    return (int t) -> {
      accept(t);
      after.accept(t);
    };
  }
}
