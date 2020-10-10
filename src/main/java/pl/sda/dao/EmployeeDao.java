package pl.sda.dao;

import pl.sda.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeDao {

    private final Connection connection;

    public EmployeeDao(Connection connection) {
        this.connection = connection;
    }

    public void add(String name, String department) {
        String insertSql = "INSERT INTO EMPLOYEES.EMPLOYEE (NAME, DEPARTMENT) VALUES (?,?)";
        try(PreparedStatement statement = connection.prepareStatement(insertSql,Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, department);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    System.out.println("Dodano pracownika z id: " + generatedKeys.getInt(1));
                }
            }
        }catch (SQLException e) {
            System.out.println("Nie mozna utworzyc pracownika");
        }
    }

    public void updateNameById(int id, String name) {
        String updateNameSql = "UPDATE EMPLOYEES.EMPLOYEE SET NAME = ? WHERE ID = ?";
        try(PreparedStatement statement = connection.prepareStatement(updateNameSql)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Nie mozna utworzyc pracownika");
        }
    }

    public List<Employee> getAllEmployees() {
        String selectSql = "SELECT * FROM EMPLOYEES.EMPLOYEE";
        try (ResultSet resultSet = this.connection.createStatement().executeQuery(selectSql)) {
            List<Employee> employees = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String department = resultSet.getString("department");
                employees.add(new Employee(id, name, department));
            }
            return employees;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}
