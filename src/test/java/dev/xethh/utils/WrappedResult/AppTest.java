package dev.xethh.utils.WrappedResult;

import dev.xethh.utils.WrappedResult.exceptions.NoExceptionForError;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void test() {
        WrappedResult<Integer> xx = WrappedResult.of(1);
        assertFalse(xx.hasError());
        assertTrue(xx.noError());
        assertFalse(xx.empty());
        assertTrue(xx.occupied());

        xx = WrappedResult.of(null);
        assertFalse(xx.hasError());
        assertTrue(xx.noError());
        assertFalse(xx.occupied());
        assertTrue(xx.empty());

        xx = WrappedResult.error(new RuntimeException());
        assertTrue(xx.hasError());
        assertFalse(xx.noError());
        assertFalse(xx.occupied());
        assertTrue(xx.empty());

        assertThrows(NoExceptionForError.class, () -> WrappedResult.error(null));

        WrappedResult<Boolean> yy = WrappedResult.run(() -> {
            System.out.println("test");
        });
        assertFalse(yy.hasError());
        assertTrue(yy.noError());
        assertTrue(yy.occupied());
        assertFalse(yy.empty());


        yy = WrappedResult.run(() -> {
            throw new RuntimeException();
        });
        assertTrue(yy.hasError());
        assertFalse(yy.noError());
        assertFalse(yy.occupied());
        assertTrue(yy.empty());

        yy = WrappedResult.call(() -> true);
        assertFalse(yy.hasError());
        assertTrue(yy.noError());
        assertTrue(yy.occupied());
        assertFalse(yy.empty());

        yy = WrappedResult.call(() -> null);
        assertFalse(yy.hasError());
        assertTrue(yy.noError());
        assertFalse(yy.occupied());
        assertTrue(yy.empty());

        yy = WrappedResult.call(() -> {
            throw new RuntimeException();
        });
        assertTrue(yy.hasError());
        assertFalse(yy.noError());
        assertFalse(yy.occupied());
        assertTrue(yy.empty());

        AtomicInteger i = new AtomicInteger(0);
        WrappedResult.of(1)
                .ifError(t -> {
                    i.addAndGet(1);
                })
                .ifNoError(it -> {
                    i.addAndGet(2);
                })
                .ifNoErrorAndOccupied(it -> {
                    i.addAndGet(4);
                })
                .ifNoErrorButEmpty(() -> {
                    i.addAndGet(8);
                })
                .ifErrorOrEmpty(it ->
                        i.addAndGet(16)
                )
        ;
        assertEquals(6, i.get());

        i.set(0);
        WrappedResult.of(null)
                .ifError(t -> {
                    i.addAndGet(1);
                })
                .ifNoError(it -> {
                    i.addAndGet(2);
                })
                .ifNoErrorAndOccupied(it -> {
                    i.addAndGet(4);
                })
                .ifNoErrorButEmpty(() -> {
                    i.addAndGet(8);
                })
                .ifErrorOrEmpty(it ->
                        i.addAndGet(16)
                )
        ;
        assertEquals(26, i.get());

        i.set(0);
        WrappedResult.of(new Throwable())
                .ifError(t -> {
                    i.addAndGet(1);
                })
                .ifNoError(it -> {
                    i.addAndGet(2);
                })
                .ifNoErrorAndOccupied(it -> {
                    i.addAndGet(4);
                })
                .ifNoErrorButEmpty(() -> {
                    i.addAndGet(8);
                })
                .ifErrorOrEmpty(it ->
                        i.addAndGet(16)
                )
        ;
        assertEquals(6, i.get());

        i.set(0);
        WrappedResult.error(new Throwable())
                .ifError(t -> {
                    i.addAndGet(1);
                })
                .ifNoError(it -> {
                    i.addAndGet(2);
                })
                .ifNoErrorAndOccupied(it -> {
                    i.addAndGet(4);
                })
                .ifNoErrorButEmpty(() -> {
                    i.addAndGet(8);
                })
                .ifErrorOrEmpty(it ->
                        i.addAndGet(16)
                )
        ;
        assertEquals(17, i.get());

        assertFalse(WrappedResult.of(1).hasErrorOpt().isPresent());
        assertFalse(WrappedResult.of(null).hasErrorOpt().isPresent());
        assertTrue(WrappedResult.error(new Throwable()).hasErrorOpt().isPresent());

        assertTrue(WrappedResult.of(1).noErrorOpt().isPresent());
        assertTrue(WrappedResult.of(null).noErrorOpt().isPresent());
        assertFalse(WrappedResult.error(new Throwable()).noErrorOpt().isPresent());

        assertTrue(WrappedResult.of(1).noErrorAndOccupiedOpt().isPresent());
        assertFalse(WrappedResult.of(null).noErrorAndOccupiedOpt().isPresent());
        assertFalse(WrappedResult.error(new Throwable()).noErrorAndOccupiedOpt().isPresent());

        assertEquals("1", WrappedResult.of(1).mapTo(x -> x + "").result());
        assertEquals("null", WrappedResult.of(null).mapTo(x -> x + "").result());
        assertNull(WrappedResult.of(null).mapOccupiedTo(x -> x + "").result());


    }

}
