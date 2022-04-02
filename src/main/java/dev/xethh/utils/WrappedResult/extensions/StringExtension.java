package dev.xethh.utils.WrappedResult.extensions;

import lombok.experimental.ExtensionMethod;

public class StringExtension {
    public static String pad(String source, String toBePadded){
        return source + toBePadded;
    }
}
