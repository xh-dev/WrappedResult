package dev.xethh.utils.WrappedResult.matching;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Intermediate class for creating match case in {@link ItemTransformer}
 * @param <T> the source generic class transform from
 * @param <X> the target generic class transform to
 * @param <Y> temporary generic class for type safe of the {@link CaseCriteria#predicate} and {@link CaseCriteria#then(Function)}
 */
public class CaseCriteria<T, X, Y extends T>{
    /**
     * {@link ItemTransformer} instance used for creating new {@link ItemTransformer}
     */
    private final ItemTransformer<T,X> itemTransformer;
    /**
     * the testing {@link Predicate} for the case matching
     */
    private final Predicate<Y> predicate;

    /**
     * constructor
     * @param itemTransformer parent object of {@link ItemTransformer}
     * @param predicateList the testing {@link Predicate} for the case matching
     */
    protected CaseCriteria(ItemTransformer<T, X> itemTransformer, Predicate<Y> predicateList) {
        this.itemTransformer = itemTransformer;
        this.predicate = predicateList;
    }

    /**
     * except the base test {@link Predicate}, add more testing for the case matching.
     * similar to match guard is scala
     * @param predicate {@link Predicate} for more testing
     * @return new instance of {@link CaseCriteria}
     */
    public CaseCriteria<T, X, Y> also(Predicate<Y> predicate){
        return new CaseCriteria<>(itemTransformer, this.predicate.and(predicate));
    }

    /**
     * provide conversion function if the match case matched
     * @param op conversion function convert {@link T} to {@link X}
     * @return new instance of {@link ItemTransformer}
     */
    public ItemTransformer<T, X> then(Function<Y, X> op){
        return itemTransformer.add((Predicate<T>) predicate, (Function<T, X>) op);
    }

    /**
     * provide value if the match case matched
     * @param value value of type {@link X}
     * @return new instance of {@link ItemTransformer}
     */
    public ItemTransformer<T, X> thenValue(X value){
        return then(it->value);
    }


}
