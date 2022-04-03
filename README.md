# WrappedResult

### Maven

```xml
<dependency>
    <groupId>dev.xethh.utils</groupId>
    <artifactId>WrappedResult</artifactId>
    <version>1.0.5</version>
</dependency>
```

### Gradle
```groovy
implementation 'dev.xethh.utils:WrappedResult:1.0.5'
```


### [Demo](https://github.com/xh-dev/WrappedResult/blob/master/src/test/java/dev/xethh/utils/WrappedResult/Demo.java)
```java
        int resultFromProcess = 1;
        WrappedResult<Integer> wrapped = WrappedResult.of(resultFromProcess);
        System.out.println(wrapped.hasError()); // false
        System.out.println(wrapped.noError()); // true
        System.out.println(wrapped.occupied()); // true
        System.out.println(wrapped.empty()); // false

        wrapped
                .ifError(throwable -> {
                    // Will not execute
                    System.out.println("There is error");
                })
                .ifNoError(opt -> {
                    // Will execute
                    System.out.println("The result contains no error");
                })
                .ifNoErrorAndOccupied(i -> {
                    // Will execute
                    System.out.println("The result contains no error and is occupied");
                })
                .ifNoErrorButEmpty(() -> {
                    // Will not execute
                    System.out.println("The result contains no error but value is null(empty)");
                })
        ;

        System.out.println(wrapped.result());                   // 1
        System.out.println(wrapped.resultOpt());                // Optional[1]
        System.out.println(wrapped.error());                    // null

        System.out.println(wrapped.hasErrorOpt());              // Optional.empty
        System.out.println(wrapped.noErrorOpt());               // Optional[WrappedResultImpl{obj=Optional[1], exception=[null]}]
        System.out.println(wrapped.noErrorAndOccupiedOpt());    // Optional[1)]

        wrapped = WrappedResult.error(new RuntimeException());

        System.out.println(wrapped.result());                   // null
        System.out.println(wrapped.resultOpt());                // Optional.empty
        System.out.println(wrapped.error().toString());         // java.lang.RuntimeException

        System.out.println(wrapped.hasErrorOpt());              // Optional[java.lang.RuntimeException]
        System.out.println(wrapped.noErrorOpt());               // Optional.empty
        System.out.println(wrapped.noErrorAndOccupiedOpt());    // Optional.empty

        // run callable and store result
        wrapped = WrappedResult.call(() -> 10);
        System.out.println(wrapped.noError());                  // true
        System.out.println(wrapped.result());                   // 10

        // run callable and catch exception
        wrapped = WrappedResult.call(() -> {
            throw new RuntimeException("thrown message");
        });
        System.out.println(wrapped.hasError());                  // true
        System.out.println(wrapped.error().getMessage());        // thrown message

        // run callable and store result
        // true
        WrappedResult<Boolean> wrappedB = WrappedResult.run(
                () -> {
                    System.out.println("hello world");           // hello world
                }
        );
        System.out.println(wrappedB.hasError());                 // false

        // run callable and catch exception
        wrappedB = WrappedResult.run(() -> {
            throw new RuntimeException("thrown message 1");
        });
        System.out.println(wrappedB.hasError());                  // true
        System.out.println(wrappedB.error().getMessage());        // thrown message 1


        // execute map if no error
        System.out.println(WrappedResult.of(1).mapTo(i->i+"").result());               // 1
        
        // execute mapOccupiedTo if no error and value not empty
        System.out.println(WrappedResult.of(1).mapOccupiedTo(i->i+"").result());       // 1
        
        // execute mapOccupiedTo if no error and value not empty, the block i->i+" 1" not executed
        System.out.println(WrappedResult.of(null).mapOccupiedTo(i->i+" 1").result());    // null
        
        // execute mapOccupiedTo if no error, the block i->i+" 1" executed
        System.out.println(WrappedResult.of(null).mapTo(i->i+" 1").result());            // null 1


```
