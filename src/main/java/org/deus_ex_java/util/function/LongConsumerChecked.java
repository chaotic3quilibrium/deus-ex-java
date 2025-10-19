package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.LongConsumer;

/**
 * Enables the providing of a {@link LongConsumer} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface LongConsumerChecked<EX extends Exception> {

  /**
   * Performs this operation on the given argument.
   *
   * @param value the input argument
   */
  void accept(long value) throws EX;

  /**
   * Returns a composed {@code LongConsumer} that performs, in sequence, this operation followed by the {@code after}
   * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
   * operation.  If performing this operation throws an exception, the {@code after} operation will not be performed.
   *
   * @param after the operation to perform after this operation
   * @return a composed {@code LongConsumer} that performs in sequence this operation followed by the {@code after}
   *     operation
   */
  @NotNull
  default LongConsumerChecked<EX> andThen(
      @NotNull LongConsumerChecked<? extends EX> after
  ) {
    return (long t) -> {
      accept(t);
      after.accept(t);
    };
  }
}
