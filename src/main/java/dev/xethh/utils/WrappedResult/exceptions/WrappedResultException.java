package dev.xethh.utils.WrappedResult.exceptions;

import dev.xethh.utils.WrappedResult.wrappedResult.WrappedResult;

/**
 * Root Exception thrown for {@link WrappedResult}
 */
public class WrappedResultException extends RuntimeException {
    public WrappedResultException() {
    }

    public WrappedResultException(String message) {
        super(message);
    }

    public WrappedResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrappedResultException(Throwable cause) {
        super(cause);
    }

    public WrappedResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
