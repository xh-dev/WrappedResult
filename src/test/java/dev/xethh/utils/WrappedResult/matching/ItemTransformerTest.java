package dev.xethh.utils.WrappedResult.matching;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test ItemTransformer")
public class ItemTransformerTest {


    @Nested
    @DisplayName("Basic test")
    class ValueBased{
        @DisplayName("Test value base")
        @Test
        public void testValueBased(){
            ItemTransformer<Integer, String> matcher = ItemTransformer.transfer(Integer.class, String.class)
                    .inCaseValue(10).thenValue("B")
                    .inCaseValue(20).thenValue("C");
            assertEquals( "B", matcher.matches(10));
            assertEquals( "C", matcher.matches(20));
            assertEquals( "D", matcher.defaultValue("D").matches(40));
            assertThrows(NoMatchFoundException.class,()->matcher.matches(40));
        }

        @DisplayName("Test lambda base")
        @Test
        public void testLambdaBased(){
            ItemTransformer<Integer, String> matcher = ItemTransformer.transfer(Integer.class, String.class)
                    .inCase(it->it.equals(10)).then(it->"B")
                    .inCase(it->it.equals(20)).then(it->"C");
            assertEquals( "B", matcher.matches(10));
            assertEquals( "C", matcher.matches(20));
            assertEquals( "D", matcher.defaultValueSupplier(()->"D").matches(40));
            assertEquals( "D", matcher.defaultValueTransform(it->"D").matches(40));
            assertThrows(NoMatchFoundException.class,()->matcher.matches(40));
        }


        @DisplayName("Test is sub class ")
        @Test
        public void testIsSubClassOf(){
            ItemTransformer<Throwable, Boolean> matcher = ItemTransformer.transfer(Throwable.class, Boolean.class)
                    .isSubClassOf(RuntimeException.class).thenValue(true)
                    .defaultValue(false);
            assertFalse(()->matcher.matches(null));
            assertTrue(()->matcher.matches(new AException()));
            assertTrue(()->matcher.matches(new RuntimeException()));
            assertFalse(()->matcher.matches(new BNotException()));

        }

        @DisplayName("Test is exact class")
        @Test
        public void testIsExactClassOf(){
            ItemTransformer<Throwable, Boolean> matcher = ItemTransformer.transfer(Throwable.class, Boolean.class)
                    .isExactClassOf(RuntimeException.class).thenValue(true)
                    .defaultValue(false);
            assertFalse(()->matcher.matches(null));
            assertFalse(()->matcher.matches(new AException()));
            assertTrue(()->matcher.matches(new RuntimeException()));
            assertFalse(()->matcher.matches(new BNotException()));
        }

        @DisplayName("Test is input is null")
        @Test
        public void testIsNull(){
            ItemTransformer<Throwable, Boolean> matcher = ItemTransformer.transfer(Throwable.class, Boolean.class)
                    .isNull().thenValue(true)
                    .defaultValue(false);
            assertTrue(()->matcher.matches(null));
            assertFalse(()->matcher.matches(new AException()));
        }

        @DisplayName("Test is input is not null")
        @Test
        public void testIsNonNull(){
            ItemTransformer<Throwable, Boolean> matcher = ItemTransformer.transfer(Throwable.class, Boolean.class)
                    .isNonNull().thenValue(true)
                    .defaultValue(false);
            assertFalse(()->matcher.matches(null));
            assertTrue(()->matcher.matches(new AException()));
        }
    }
    public static class AException extends RuntimeException{}
    public static class BNotException extends Exception{}
}
