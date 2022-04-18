package dev.xethh.utils.WrappedResult.extensions;

import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;

import java.util.Optional;

public class AnyObjectExtension {
    public static <A> Optional<A> optOfNullable(A a){
        return Optional.ofNullable(a);
    }

    public static <A> Optional<A> optOf(A a){
        return Optional.of(a);
    }

    public static <A> Try<A> toTry(A a){
        return Try.of(()->a);
    }

    public static <T,X> Try<X> tryMap(T t, CheckedFunction1<T,X> op){
        return Try.of(()->op.apply(t));
    }

    public static <T> Try<T> tryUpdate(T t, CheckedConsumer<T> op){
        return Try.of(()->{
            op.accept(t);
            return t;
        });
    }
}
