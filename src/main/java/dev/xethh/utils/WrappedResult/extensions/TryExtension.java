package dev.xethh.utils.WrappedResult.extensions;

import dev.xethh.utils.WrappedResult.checkedWrapper.CheckWrappingException;
import io.vavr.control.Try;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TryExtension {
    public static <T> Try<T> toTry(T a){
        return Try.of(()->a);
    }
    public static <T> Try<T> apply(Try<T> tryObj, Consumer<T> consumer){
        try{
            return tryObj.map(it->{
                consumer.accept(it);
                return it;
            });
        } catch (Exception ex){
            throw new CheckWrappingException(ex);
        }
    }
    public static <T> Try<T> sideEffect(Try<T> tryObj, Consumer<T> op){
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
}