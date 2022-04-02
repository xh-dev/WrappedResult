package dev.xethh.utils.WrappedResult.extensions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamExtension {
    public static <T> Stream<T> sideEffect(Stream<T> tryObj, Consumer<T> op){
        return tryObj.map(it->{
            op.accept(it);
            return it;
        });
    }
    public static <T> Stream<T> mapIfNot(Stream<T> tryObj, Predicate<T> test, Function<T,T> op){
        return mapIf(tryObj, it->!test.test(it), op);
    }
    public static <T> Stream<T> mapIf(Stream<T> tryObj, Predicate<T> test, Function<T,T> op){
        return tryObj
                .map(it->{
                    if(test.test(it)){
                        return op.apply(it);
                    } else {
                        return it;
                    }
                });

    }
}