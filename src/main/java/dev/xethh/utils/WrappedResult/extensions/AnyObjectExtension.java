package dev.xethh.utils.WrappedResult.extensions;

import java.util.function.Consumer;
import java.util.function.Function;

public class AnyObjectExtension {
    public static <T,X> X let(T t, Function<T,X> op){
        return op.apply(t);
    }

    public static <T> T also(T t, Consumer<T> op){
        op.accept(t);
        return t;
    }
}
