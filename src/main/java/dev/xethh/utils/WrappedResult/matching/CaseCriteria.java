package dev.xethh.utils.WrappedResult.matching;

import lombok.Builder;

import java.util.function.Function;
import java.util.function.Predicate;

@Builder
public class CaseCriteria<T, X>{
    private final ItemMatcher<T,X> m;
    private final Predicate<T> predicate;

    public CaseCriteria(ItemMatcher<T, X> m, Predicate<T> predicateList) {
        this.m = m;
        this.predicate = predicateList;
    }

    public ItemMatcher<T, X> then(Function<T, X> op){
        return m.add(predicate, op);
    }


}
