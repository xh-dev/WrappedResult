package dev.xethh.utils.WrappedResult.exceptions;

import dev.xethh.utils.WrappedResult.wrappedResult.WrappedResult;

/**
 * Exception thrown when creating {@link WrappedResult} with method {@link WrappedResult#error(Throwable)}, but the throwable passed in is null.
 */
public class NoExceptionForError extends WrappedResultException {
    public NoExceptionForError() {
        super("No exception passed in constructor when creating WrappedResult.");
    }
}
