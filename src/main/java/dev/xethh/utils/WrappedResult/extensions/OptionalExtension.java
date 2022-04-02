package dev.xethh.utils.WrappedResult.extensions;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class OptionalExtension {
    public static <T> Optional<T> sideEffect(Optional<T> tryObj, Consumer<T> op){
        return tryObj.map(it->{
            op.accept(it);
            return it;
        });
    }
    public static <T> Optional<T> mapIfNot(Optional<T> tryObj, Predicate<T> test, Function<T,T> op){
        return mapIf(tryObj, it->!test.test(it), op);
    }
    public static <T> Optional<T> mapIf(Optional<T> tryObj, Predicate<T> test, Function<T,T> op){
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