package dev.xethh.utils.WrappedResult;

import dev.xethh.utils.WrappedResult.exceptions.NoExceptionForError;

import java.util.Optional;

public class WrappedResultImpl<R extends Object> implements WrappedResult<R> {
    private final Optional<R> obj;
    private final Throwable exception;

    protected WrappedResultImpl(R obj) {
        this.obj = Optional.ofNullable(obj);
        this.exception = null;
    }
    protected WrappedResultImpl(Throwable exception) {
        if (exception == null)
            throw new NoExceptionForError();
        this.obj = Optional.empty();
        this.exception = exception;
    }

    @Override
    public Optional<R> resultOpt() {
        return obj;
    }

    @Override
    public Throwable error() {
        return exception;
    }

}
