package dev.xethh.utils.WrappedResult.extensions;

import dev.xethh.utils.WrappedResult.checkedWrapper.CheckWrappingException;
import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;

import java.util.function.Consumer;
import java.util.function.Function;

public class AnyObjectExtension {
    public static <T,X> X let(T t, CheckedFunction1<T,X> op){
        return Try.of( ()->op.apply(t)).getOrElseThrow(AnyObjectExtension::toCheckedException);
    }

    public static <T> T also(T t, CheckedConsumer<T> op){
        return Try.of(()->{
            op.accept(t);
            return t;
        }).getOrElseThrow(AnyObjectExtension::toCheckedException);
    }

    public static CheckWrappingException toCheckedException(Throwable t){
        return new CheckWrappingException(t);
    }
    public static <T> Try<T> toTry(T t){
        return Try.success(t);
    }
    public static <T> Try<T> toFail(Throwable t){
        return Try.failure(t);
    }
}
