package pl.sda.database;

import java.sql.*;

public class DatabaseManager {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306?serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASS = "***";

    private final Connection connection;

    public DatabaseManager() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void createDatabaseIfNotPresent(String databaseName) {
        try (Statement statement = this.connection.createStatement()) {
            if (!databaseExist(databaseName)) {
                String sql = "CREATE DATABASE " + databaseName;
                statement.executeUpdate(sql);
            } else {
                System.out.println("Baza danych juz istnieje");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTableIfNotPresent(String tableName, String sql) {
        try (Statement statement = this.connection.createStatement()) {
            if (!tableExists(tableName)) {
                statement.executeUpdate(sql);
            } else {
                System.out.println("Tabela juz istnieje");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    private boolean databaseExist(String databaseName) {
        try (ResultSet resultSet = this.connection.getMetaData().getCatalogs()) {
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(databaseName.toUpperCase())) {
                    resultSet.close();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean tableExists(String tableName) {
        try(ResultSet tables = this.connection.getMetaData().getTables(null, null, tableName, null)){
            return tables.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
