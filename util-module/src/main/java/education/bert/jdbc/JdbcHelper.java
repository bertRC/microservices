package education.bert.jdbc;

import education.bert.jdbc.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class that makes working with databases easier.
 */
public class JdbcHelper {

    /**
     * The wrapper for java.sql.Statement.executeUpdate with connection establishment and statement creation.
     *
     * @param url a database url.
     * @param sql an SQL Data Manipulation Language (DML) statement.
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements
     * that return nothing.
     */
    public static int executeUpdate(String url, String sql) {
        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
        ) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * The wrapper for java.sql.PreparedStatement.executeUpdate with connection establishment and preparedStatement
     * creation.
     *
     * @param url    a database url.
     * @param sql    an SQL Data Manipulation Language (DML) statement.
     * @param setter a PreparedStatementSetter functional interface implementation that sets the PreparedStatement.
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements
     * that return nothing.
     */
    public static int executeUpdate(String url, String sql, PreparedStatementSetter setter) {
        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement preparedStatement = setter.set(connection.prepareStatement(sql));
        ) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * The wrapper for java.sql.PreparedStatement.executeUpdate with connection establishment and preparedStatement
     * creation that returns object containing the auto-generated key.
     *
     * @param url    a database url.
     * @param sql    an SQL Data Manipulation Language (DML) statement.
     * @param setter a PreparedStatementSetter functional interface implementation that sets the PreparedStatement.
     * @param <T>    the type of object containing the auto-generated key.
     * @return the object containing the auto-generated key.
     */
    public static <T> T executeUpdateWithId(String url, String sql, PreparedStatementSetter setter) {
        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement preparedStatement = setter.set(connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS));
        ) {
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return (T) generatedKeys.getObject(1);
            }
            throw new SQLException("No keys generated");
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * The wrapper for java.sql.PreparedStatement.executeQuery returning single object with connection establishment,
     * preparedStatement and resultSet creation.
     *
     * @param url    a database url.
     * @param sql    an SQL Data Manipulation Language (DML) statement.
     * @param setter a PreparedStatementSetter functional interface implementation that sets the PreparedStatement.
     * @param mapper a RowMapper functional interface implementation that maps ResultSet to desired object.
     * @param <T>    the type of object queried from the database.
     * @return Optional container for the object queried from the database.
     */
    public static <T> Optional<T> executeQueryForObject(String url, String sql, PreparedStatementSetter setter, RowMapper<T> mapper) {
        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement preparedStatement = setter.set(connection.prepareStatement(sql));
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (resultSet.next()) {
                return Optional.of(mapper.map(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * The wrapper for java.sql.PreparedStatement.executeQuery returning the list of objects with connection
     * establishment, statement and resultSet creation.
     *
     * @param url    a database url.
     * @param sql    an SQL Data Manipulation Language (DML) statement.
     * @param mapper a RowMapper functional interface implementation that maps ResultSet to desired object.
     * @param <T>    the type of object queried from the database.
     * @return List for objects queried from the database.
     */
    public static <T> List<T> executeQueryForList(String url, String sql, RowMapper<T> mapper) {
        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(mapper.map(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * The wrapper for java.sql.PreparedStatement.executeQuery returning the list of objects with connection
     * establishment, preparedStatement and resultSet creation.
     *
     * @param url    a database url.
     * @param sql    an SQL Data Manipulation Language (DML) statement.
     * @param setter a PreparedStatementSetter functional interface implementation that sets the PreparedStatement.
     * @param mapper a RowMapper functional interface implementation that maps ResultSet to desired object.
     * @param <T>    the type of object queried from the database.
     * @return List for objects queried from the database.
     */
    public static <T> List<T> executeQueryForList(String url, String sql, PreparedStatementSetter setter, RowMapper<T> mapper) {
        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement preparedStatement = setter.set(connection.prepareStatement(sql));
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(mapper.map(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
