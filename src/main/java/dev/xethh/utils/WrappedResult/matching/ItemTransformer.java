package dev.xethh.utils.WrappedResult.matching;

import dev.xethh.utils.WrappedResult.scope.Scope;
import io.vavr.Tuple;
import io.vavr.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * {@link ItemTransformer} is helper class that help to transform value from one type ({@link T}) to the other ({@link X}) based on cases.
 * Its just like the switch syntax in java or match in scala. The {@link ItemTransformer} is by design immutable, adding case match will crate new instance.
 * e.g.
 * <pre>
 *     Integer target = ItemTransformer.transfer(String.class, Integer.class)
 *                                    .inCaseValue("A").thenValue(80)
 *                                    .inCaseValue("B").thenValue(60)
 *                                    .defaultValue(40)
 *                                    .matches("A")
 *     // target == 80
 * </pre>
 *
 * @param <T> the source generic class transform from
 * @param <X> the target generic class transform to
 */
public class ItemTransformer<T, X> {
    /**
     * tag class indicate the source type of the transformation
     */
    private final Class<T> from;
    /**
     * tag class indicate the target type of the transformation
     */
    private final Class<X> to;
    /**
     * default value if none of the case in {@link ItemTransformer#matchList} is found.
     * If defaultValue is null, the {@link NoMatchFoundException} is thrown
     */
    private final Function<T, X> defaultValue;

    /**
     * A list of case to be matched and the transformation function
     */
    private final List<Tuple2<Predicate<T>, Function<T, X>>> matchList;


    /**
     * Constructor for {@link ItemTransformer}, the class is marked as protected to prevent direct accessing class.
     * The construction this class should be made by {@link ItemTransformer#transfer(Class, Class)}
     *
     * @param from         the source class ({@link ItemTransformer#from})
     * @param to           the target class ({@link ItemTransformer#to})
     * @param defaultValue the default value for no matching ({@link ItemTransformer#defaultValue})
     * @param matchList    the list of matching case ({@link ItemTransformer#matchList})
     */
    protected ItemTransformer(Class<T> from, Class<X> to, Function<T, X> defaultValue, List<Tuple2<Predicate<T>, Function<T, X>>> matchList) {
        this.from = from;
        this.to = to;
        this.defaultValue = defaultValue;
        this.matchList = matchList;
    }

    /**
     * Adding case to be matched
     *
     * @param predicate the {@link Predicate} to test if item match
     * @return {@link CaseCriteria} as intermediate instance
     */
    public CaseCriteria<T, X> inCase(Predicate<T> predicate) {
        return new CaseCriteria<>(this, predicate);
    }

    /**
     * Adding case to be matched
     *
     * @param value to be test with {@link Object#equals(Object)}
     * @return {@link CaseCriteria} as intermediate instance
     */
    public CaseCriteria<T, X> inCaseValue(T value) {
        return inCase((t) -> t.equals(value));
    }

    /**
     * Adding case for input value is null
     *
     * @return {@link CaseCriteria} as intermediate instance
     */
    public CaseCriteria<T, X> isNull() {
        return inCase(Objects::isNull);
    }

    /**
     * Adding case for input value is not null
     *
     * @return {@link CaseCriteria} as intermediate instance
     */
    public CaseCriteria<T, X> isNonNull() {
        return inCase(Objects::nonNull);
    }

    /**
     * Adding case for input is sub class of specific class
     *
     * @param clazz the class to be tested
     * @return {@link CaseCriteria} as intermediate instance
     */
    public CaseCriteria<T, X> isSubClassOf(Class<? extends T> clazz) {
        return inCase(it -> clazz != null && clazz.isInstance(it));
    }

    /**
     * Adding case for input is exact the specific class
     *
     * @param clazz the class to be tested
     * @return {@link CaseCriteria} as intermediate instance
     */
    public CaseCriteria<T, X> isExactClassOf(Class<? extends T> clazz) {
        return inCase(it -> it != null && it.getClass() == clazz);
    }

    /**
     * Default value if none of the match case in {@link ItemTransformer#matchList}.
     * If missing the default(value is null), {@link NoMatchFoundException} is thrown
     *
     * @param supplier {@link Supplier} of X
     * @return new instance of {@link ItemTransformer}
     */
    public ItemTransformer<T, X> defaultValueSupplier(Supplier<X> supplier) {
        return new ItemTransformer<>(from, to, it -> supplier.get(), matchList);
    }

    /**
     * Default value if none of the match case in {@link ItemTransformer#matchList}.
     * If missing the default(value is null), {@link NoMatchFoundException} is thrown
     *
     * @param defaultValue value of X when no match case found
     * @return new instance of {@link ItemTransformer}
     */
    public ItemTransformer<T, X> defaultValue(X defaultValue) {
        return new ItemTransformer<>(from, to, it -> defaultValue, matchList);
    }

    /**
     * Default value if none of the match case in {@link ItemTransformer#matchList}.
     * If missing the default(value is null), {@link NoMatchFoundException} is thrown
     *
     * @param op {@link Function} for convert source instance to target instance
     * @return new instance of {@link ItemTransformer}
     */
    public ItemTransformer<T, X> defaultValueTransform(Function<T,X> op){
        return new ItemTransformer<>(from, to, op, matchList);
    }


    /**
     * Internal method designed to be used by {@link CaseCriteria}.
     * It is the method for {@link CaseCriteria} to add the case matching to create new {@link ItemTransformer}
     *
     * @param predicate the matching criteria
     * @param op        operation to be performed for transfer from {@link T} to {@link X}
     * @return the {@link ItemTransformer} then added the criteria
     */
    protected ItemTransformer<T, X> add(Predicate<T> predicate, Function<T, X> op) {
        return new ItemTransformer<>(from, to, defaultValue,
                Scope.of(matchList).apply(it -> it.add(Tuple.of(predicate, op))).unscoped()
        );
    }


    /**
     * static method for creating {@link ItemTransformer} by providing Source class and Target class
     *
     * @param fromClass source class
     * @param toClass   target class
     * @param <T>       generic type of source class
     * @param <X>       generic type of target class
     * @return new instance of {@link ItemTransformer}
     */
    public static <T, X> ItemTransformer<T, X> transfer(Class<T> fromClass, Class<X> toClass) {
        return new ItemTransformer<>(fromClass, toClass, null, new ArrayList<>());
    }

    /**
     * Match logic for ItemTransformer.
     * loop through all the matching case, if any matched case, transform base the conversion function.
     * if no matched case, check if the {@link ItemTransformer#defaultValue} is null or not.
     * if {@link ItemTransformer#defaultValue} is not null, return result base on the default value supplier
     * if {@link ItemTransformer#defaultValue} is null, throw exception of {@link NoMatchFoundException}
     *
     * @param t source value to be tested
     * @return target value
     */
    public X matches(T t) {
        for (Tuple2<Predicate<T>, Function<T, X>> item : matchList) {
            if (item._1.test(t)) {
                return item._2.apply(t);
            }
        }
        if (defaultValue != null) {
            return defaultValue.apply(t);
        } else {
            throw new NoMatchFoundException();
        }
    }

}
