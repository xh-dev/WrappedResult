# WrappedResult

### Maven

```xml
<dependency>
    <groupId>dev.xethh.utils</groupId>
    <artifactId>WrappedResult</artifactId>
    <version>1.0.0</version>
</dependency>
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
                .ifNoError(opt->{
                    // Will execute
                    System.out.println("The result contains no error and is occupied");
                })
                .ifNoErrorAndOccupied(i->{
                    // Will execute
                    System.out.println("There result contains no error and is occupied");
                })
                .ifNoErrorButEmpty(()->{
                    // Will not execute
                    System.out.println("There result contains no error but value is null(empty)");
                })
                ;

        System.out.println(wrapped.result());                   // 1
        System.out.println(wrapped.resultOpt());                // Optional[1]
        System.out.println(wrapped.error());                    //null

        System.out.println(wrapped.hasErrorOpt());              //Optional.empty
        System.out.println(wrapped.noErrorOpt());               //Optional[WrappedResult[1]]
        System.out.println(wrapped.noErrorAndOccupiedOpt());    //Optional[1)]

        wrapped = WrappedResult.error(new RuntimeException());

        System.out.println(wrapped.result());                   // null
        System.out.println(wrapped.resultOpt());                // Optional.empty
        System.out.println(wrapped.error().toString());         // RuntimeException

        System.out.println(wrapped.hasErrorOpt());              // Optional[RuntimeException]
        System.out.println(wrapped.noErrorOpt());               // Optional.empty
        System.out.println(wrapped.noErrorAndOccupiedOpt());    // Optional.empty

```
