package dev.xethh.utils.WrappedResult;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An container for wrapping operation result inside.
 * When process operation, there are only two types of result generally
 * 1. <b>Complete with operation result</b>
 * 2. <b>Complete with error</b>
 * By wrapping either <b>result{@link WrappedResult#result()}</b> or <b>error{@link WrappedResult#error()}</b> into {@link WrappedResult}
 * @param <R> the result returned from process
 */
public interface WrappedResult<R> {
    /**
     * result in optional form
     * @return {@link Optional} of result object
     */
    Optional<R> resultOpt();

    /**
     * return result of the {@link WrappedResult}
     * @return result object
     */
    default R result(){
        return resultOpt().orElse(null);
    }

    /**
     * error value of the {@link WrappedResult}
     * @return {@link Throwable}
     */
    Throwable error();

    /**
     * static method create {@link WrappedResult} of success result and contain <b>result</b>
     * @param obj result object
     * @param <R> type of result object
     * @return {@link WrappedResult}
     */
    static <R> WrappedResult<R> of(R obj) {
        return new WrappedResultImpl<>(obj);
    }

    /**
     * static method create {@link WrappedResult} of error result and contain <b>error</b>
     * @param throwable exception thrown
     * @param <R> type of result object
     * @return {@link WrappedResult}
     */
    static <R> WrappedResult<R> error(Throwable throwable) {
        return new WrappedResultImpl<>(throwable);
    }

    /**
     * static method create {@link WrappedResult} from callable
     * @param callable {@link Callable} return type <b>R</b>
     * @param <R> type of result object
     * @return {@link WrappedResult}
     */
    static <R> WrappedResult<R> call(Callable<R> callable) {
        try {
            return of(callable.call());
        } catch (Throwable t) {
            return error(t);
        }
    }

    /**
     * static method create {@link WrappedResult} from runnable
     * @param runnable {@link Runnable} return void
     * @return {@link WrappedResult}
     */
    static WrappedResult<Boolean> run(Runnable runnable) {
        try {
            runnable.run();
            return of(true);
        } catch (Throwable t) {
            return error(t);
        }
    }

    /**
     * Test if the result is empty
     * The will be empty if
     * 1. has error
     * 2. no error but result is null
     * @return {@link Boolean}
     */
    default boolean empty() {
        return null == result();
    }

    /**
     * Test if the result is occupied
     * The will be occupied if
     * 1. no error
     * @return {@link Boolean}
     */
    default boolean occupied() {
        return !empty();
    }

    /**
     * Test if the result has error
     * @return {@link Boolean}
     */
    default boolean hasError() {
        return error() != null;
    }

    /**
     * Test if the result has no error
     * @return {@link Boolean}
     */
    default boolean noError() {
        return !hasError();
    }

    /**
     * Do operation when
     * 1. result has error
     * @param operation further process operation if match condition
     * @return {@link WrappedResult}
     */
    default WrappedResult<R> ifError(Consumer<Throwable> operation) {
        if (hasError()) {
            operation.accept(error());
        }
        return this;
    }

    /**
     * Do operation when
     * 1. no error
     * @param operation further process operation if match condition
     * @return {@link WrappedResult}
     */
    default WrappedResult<R> ifNoError(Consumer<Optional<R>> operation) {
        if (noError()) {
            operation.accept(resultOpt());
        }
        return this;
    }

    /**
     * Do operation when
     * 1. no error and result {@link WrappedResult#occupied()}
     * @param operation further process operation if match condition
     * @return {@link WrappedResult}
     */
    default WrappedResult<R> ifNoErrorAndOccupied(Consumer<R> operation) {
        if (noError() && occupied()) {
            operation.accept(result());
        }
        return this;
    }

    /**
     * Do operation when
     * 1. no error and result {@link WrappedResult#empty()}
     * @param operation further process operation if match condition, the process takes no argument
     * @return {@link WrappedResult}
     */
    default WrappedResult<R> ifNoErrorButEmpty(Runnable operation) {
        if (noError() && empty()) {
            operation.run();
        }
        return this;
    }

    /**
     * if {@link WrappedResult} has error, return {@link Optional} of the {@link Throwable}
     * @return {@link Throwable}
     */
    default Optional<Throwable> hasErrorOpt() {
        return hasError() ? Optional.of(error()) : Optional.empty();
    }

    /**
     * if {@link WrappedResult} has no error, return {@link Optional} of {@link WrappedResult}
     * It could be further detected if it is {@link WrappedResult#occupied()} or {@link WrappedResult#empty()}
     * @return {@link Optional} of {@link WrappedResult} of result object
     */
    default Optional<WrappedResult<R>> noErrorOpt() {
        return Optional.of(this)
                .filter(WrappedResult::noError);
    }

    /**
     * if {@link WrappedResult} has no error and is occupied, return {@link Optional} of result object
     * @return {@link Optional} of result object
     */
    default Optional<R> noErrorAndOccupiedOpt() {
        return Optional.of(this)
                .filter(WrappedResult::noError)
                /*
                 Redundant operation, same effect applied in map operation below.
                 if wrapped result is empty, will return Optional.empty
                 */
                // .filter(WrappedResult::occupied)
                .map(WrappedResult::result)
                ;
    }
}
