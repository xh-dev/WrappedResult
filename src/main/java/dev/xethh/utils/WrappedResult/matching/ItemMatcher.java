package dev.xethh.utils.WrappedResult.matching;

import io.vavr.Tuple;
import io.vavr.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemMatcher<T, X> {
    private final Supplier<X> defaultValue;
    private final List<Tuple2<Predicate<T>, Function<T, X>>> matchList;

    public ItemMatcher(Supplier<X> defaultValue, List<Tuple2<Predicate<T>, Function<T, X>>> list) {
        this.defaultValue = defaultValue;
        this.matchList = list;
    }

    public CaseCriteria<T, X> inCase(Predicate<T> predicate) {
        return new CaseCriteria<>(this, predicate);
    }

    public CaseCriteria<T, X> inCase(Supplier<Predicate<T>> predicate) {
        return new CaseCriteria<>(this, predicate.get());
    }

    public ItemMatcher<T,X> defaultValue(Supplier<X> supplier){
        return new ItemMatcher<>(supplier, matchList);
    }

    public ItemMatcher<T,X> defaultValue(X defaultValue){
        return new ItemMatcher<>(()->defaultValue, matchList);
    }


    protected ItemMatcher<T, X> add(Predicate<T> predicate, Function<T, X> op) {
        matchList.add(Tuple.of(predicate, op));
        return this;
    }


    public static <T, X> ItemMatcher<T, X> build() {
        return new ItemMatcher<>(null, new ArrayList<>());
    }

    public X matches(T t) {
        for (Tuple2<Predicate<T>, Function<T, X>> item : matchList) {
            if (item._1.test(t)) {
                return item._2.apply(t);
            }
        }
        if (defaultValue != null) {
            return defaultValue.get();
        } else {
            throw new NoMatchFoundException();
        }
    }

}
