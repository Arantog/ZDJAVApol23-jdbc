package pl.sda;

import pl.sda.dao.EmployeeDao;
import pl.sda.database.DatabaseManager;

import java.sql.SQLException;


public class App {

    public static void main(String[] args) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.createDatabaseIfNotPresent("EMPLOYEES");
            String createEmployeeTableSql = "CREATE TABLE EMPLOYEES.EMPLOYEE " +
                "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                " name VARCHAR(255), " +
                " department VARCHAR(255), " +
                " PRIMARY KEY ( id ))";
            databaseManager.createTableIfNotPresent("EMPLOYEES.EMPLOYEE", createEmployeeTableSql);
            EmployeeDao employeeDao = new EmployeeDao(databaseManager.getConnection());
            employeeDao.add("Cezary", "IT");
            employeeDao.add("Arkadiusz", "TEST");

            employeeDao.getAllEmployees().forEach(employee -> System.out.println(employee.toString()));

            employeeDao.updateNameById(1, "Dawid");

            employeeDao.getAllEmployees().forEach(employee -> System.out.println(employee.toString()));

            databaseManager.closeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
