package dev.xethh.utils.WrappedResult;

import dev.xethh.utils.WrappedResult.exceptions.NoExceptionForError;

import java.util.Optional;

public class WrappedResultImpl<I extends Object> implements WrappedResult<I> {
    private final Optional<I> obj;
    private final Throwable exception;

    protected WrappedResultImpl(I obj) {
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
    public Optional<I> resultOpt() {
        return obj;
    }

    @Override
    public Throwable error() {
        return exception;
    }

    @Override
    public String toString() {
        return "WrappedResultImpl{" +
                "obj=" + obj +
                ", exception=" + (exception==null ? "[null]" : "["+exception.getMessage()+"]" ) +
                '}';
    }
}
