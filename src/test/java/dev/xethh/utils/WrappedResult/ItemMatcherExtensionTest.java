package dev.xethh.utils.WrappedResult;

import dev.xethh.utils.WrappedResult.matching.ItemMatcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemMatcherExtensionTest {
    @Test
    public void testBasic() {
        ItemMatcher<String, String> matcher = ItemMatcher.<String, String>build()
                .defaultValue(() -> "USAC")
                .inCase(it -> it.equals("USA")).then(it -> "USAA")
                .inCase(it -> it.equals("USB")).then(it -> "USAB");

        String x = matcher.matches("x");
        assertEquals("USAC", x);
        x = matcher.matches("USA");
        assertEquals("USAA", x);
        x = matcher.matches("USB");
        assertEquals("USAB", x);

    }
}
