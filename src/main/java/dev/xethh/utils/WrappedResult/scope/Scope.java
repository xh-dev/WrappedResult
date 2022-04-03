package dev.xethh.utils.WrappedResult.scope;

import dev.xethh.utils.WrappedResult.extensions.AnyObjectExtension;
import io.vavr.control.Try;
import lombok.experimental.ExtensionMethod;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Scope function provide temporary scope for object initializing or transformation
 * similar as apply and let function in kotlin.
 * @param <T> T is the scoped object, which could be unscoped to get the value back.
 */
@ExtensionMethod({
        AnyObjectExtension.class
})
public class Scope<T> {
    private final T t;

    private Scope(T t) {
        this.t = t;
    }

    public static <T> Scope<T> of(T t) {
        return new Scope<>(t);
    }

    public Scope<T> apply(Consumer<T> appliedOperation) {
        appliedOperation.accept(t);
        return this;
    }

    public <X> Scope<X> let(Function<T, X> letOperation) {
        return Scope.of(letOperation.apply(t));
    }

    public T unscoped() {
        return t;
    }

    public Try<T> toTry(){
        return Try.of(()->t);
    }

    public Try<T> toOptional(){
        return Try.of(()->t);
    }
}
