package dev.xethh.utils.WrappedResult.extensions;

import dev.xethh.utils.WrappedResult.checkedWrapper.CheckWrappingException;
import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JDBCExtension {
    public static PreparedStatement prepare(Connection connection, String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (Exception ex) {
            throw new CheckWrappingException(ex);
        }
    }

    public static PreparedStatement config(PreparedStatement preparedStatement, CheckedConsumer<PreparedStatement> preparing) {
        try {
            preparing.accept(preparedStatement);
            return preparedStatement;
        } catch (Throwable e) {
            throw new CheckWrappingException(e);
        }
    }

    public static <T> List<T> processAndConvert(PreparedStatement preparedStatement, CheckedFunction1<ResultSet, T> conversion) {
        try{
            return convertTo(preparedStatement.executeQuery(), conversion);
        } catch (Throwable ex){
            throw new CheckWrappingException(ex);
        }
    }
    public static <T> List<T> convertTo(ResultSet resultSet, CheckedFunction1<ResultSet, T> conversion) {
        try{
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(conversion.apply(resultSet));
            }
            return list;
        } catch (Throwable ex){
            throw new CheckWrappingException(ex);
        }
    }
}
