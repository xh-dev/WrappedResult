package dev.xethh.utils.WrappedResult.tryExtension;

import dev.xethh.utils.WrappedResult.extensions.AnyObjectExtension;
import dev.xethh.utils.WrappedResult.extensions.TryExtension;
import io.vavr.control.Try;
import lombok.experimental.ExtensionMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Flow;

import static org.junit.Assert.assertEquals;

@DisplayName("Test TryAnyExtension")
@ExtensionMethod({
        TryExtension.class
})
public class TryExtensionTest {

    @Nested
    @DisplayName("Simple conversion")
    class SimpleTest {
        @DisplayName("test mapIf")
        @Test
        public void testMapIf() {
            var try1 = Try.success(1);
            Assertions.assertEquals(Integer.valueOf(2), TryExtension.mapIf(try1, it -> it.equals(1), it -> 2).get());
            Assertions.assertEquals(Integer.valueOf(1), TryExtension.mapIf(try1, it -> it.equals(2), it -> 2).get());

            Assertions.assertEquals(Integer.valueOf(3), TryExtension.mapIfNot(try1, it -> it.equals(1), it -> 2).get());
        }

        @DisplayName("test mapCase")
        @Test
        public void testMapCase() {
            var try1 = Try.success(1)
                    .mapCase(Integer.class, String.class, transformer -> {
                        transformer.inCaseValue(1).thenValue("100");
                        transformer.inCaseValue(2).thenValue("200");
                    });
            Assertions.assertEquals("100", try1.get());
            var try2 = Try.success(2)
                    .mapCase(Integer.class, String.class, transformer -> {
                        transformer.inCaseValue(1).thenValue("100");
                        transformer.inCaseValue(2).thenValue("200");
                    });
            Assertions.assertEquals("200", try2.get());
        }

        @DisplayName("test sideEffect")
        @Test
        public void testSideEff(){
            var tryEx = Try.success(1)
                    .sideEffect(it->{
                        throw new Exception("msg");
                    })
                    ;

            Assertions.assertThrows(Exception.class, tryEx::get);
        }

        @DisplayName("test mapIfNot")
        @Test
        public void testMapIfNot() {
            var try1 = Try.success(1);
            Assertions.assertEquals(Integer.valueOf(1), TryExtension.mapIfNot(try1, it -> it.equals(1), it -> 2).get());
            Assertions.assertEquals(Integer.valueOf(2), TryExtension.mapIfNot(try1, it -> it.equals(2), it -> 2).get());
        }
    }
}
