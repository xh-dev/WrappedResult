package dev.xethh.utils.WrappedResult.extensions;

import dev.xethh.utils.WrappedResult.matching.ItemTransformer;
import io.vavr.CheckedConsumer;
import io.vavr.control.Try;

import java.util.function.Function;
import java.util.function.Predicate;

public class TryExtension {
    public static <T> Try<T> sideEffect(Try<T> tryObj, CheckedConsumer<T> op){
        return tryObj.mapTry(it->{
            op.accept(it);
            return it;
        });
    }
    public static <T> Try<T> mapIfNot(Try<T> tryObj, Predicate<T> test, Function<T,T> op){
        return mapIf(tryObj, it->!test.test(it), op);
    }
    public static <T> Try<T> mapIf(Try<T> tryObj, Predicate<T> test, Function<T,T> op){
        return tryObj
                .map(it->{
                    if(test.test(it)){
                        return op.apply(it);
                    } else {
                        return it;
                    }
                });
    }
    public static <T, U> Try<U> mapCase(Try<T> tryObj, Class<T> from, Class<U> to, CheckedConsumer<ItemTransformer<T, U>> operation){
        return tryObj.mapTry(it->{
            var transformer = ItemTransformer.transfer(from, to);
            operation.accept(transformer);
            return transformer.matches(it);
        });
    }
}