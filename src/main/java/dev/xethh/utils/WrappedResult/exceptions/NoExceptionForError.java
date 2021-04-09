package dev.xethh.utils.WrappedResult.exceptions;

/**
 * Exception thrown when creating {@link dev.xethh.utils.WrappedResult.WrappedResult} with method {@link dev.xethh.utils.WrappedResult.WrappedResult#error(Throwable)}, but the throwable passed in is null.
 */
public class NoExceptionForError extends WrappedResultException {
    public NoExceptionForError() {
        super("No exception passed in constructor when creating WrappedResult.");
    }
}
