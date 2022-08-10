package com.lms.persistence;

import com.lms.bean.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
	String pass="wiley";
    @Override
    public Integer addEmployee(Employee employee) {
        int rows = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lms","root",pass);
             PreparedStatement preparedStatement = connection
                     .prepareStatement("INSERT INTO employee VALUES(?,?,?,?,?)");) {

            preparedStatement.setInt(1, employee.getEmployeeId());
            preparedStatement.setString(2, employee.getEmpFirstName());
            preparedStatement.setString(3, employee.getEmpLastName());
            preparedStatement.setString(4, employee.getDesignation());
            preparedStatement.setInt(5, 0);
           

            rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    @Override
    public Integer removeEmployee(int empID) {
        int rows = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lms","root",pass);
             PreparedStatement preparedStatement = connection
                     .prepareStatement("DELETE FROM employee WHERE employeeId = ?")) {

            preparedStatement.setInt(1, empID);

            rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    @Override
    public Employee searchEmployee(int empID) {
        Employee employee = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lms","root",pass);
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT * FROM employee WHERE employeeId = ?");) {

            preparedStatement.setInt(1, empID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("employeeId");
                String first_name = resultSet.getString("employeeFn");
                String last_name = resultSet.getString("employeeLn");
                String designation = resultSet.getString("desg");
                int booksIssue = resultSet.getInt("booksIssued");
                
                employee = new Employee(id, first_name, last_name, designation, booksIssue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> empList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lms","root",pass);
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT * FROM employee");) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("employeeId");
                String first_name = resultSet.getString("employeeFn");
                String last_name = resultSet.getString("employeeLn");
                String designation = resultSet.getString("desg");
                int booksIssue = resultSet.getInt("booksIssued");
                
                empList.add(new Employee(id, first_name, last_name, designation, booksIssue));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empList;
    }

	@Override
	public int getEId(String email,String pass) {
		String qry="Select employeeId,type from user where email=? and pass=?";
		int eid=0;
		boolean type=false;
		 try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lms","root","wiley");
	          PreparedStatement preparedStatement = connection.prepareStatement(qry);) {
			 
			 preparedStatement.setString(1, email);
			 preparedStatement.setString(2, pass);
			 
			 ResultSet resultSet = preparedStatement.executeQuery();
	            while (resultSet.next()) {
	                eid = resultSet.getInt("employeeId");
	                type = resultSet.getBoolean("type");
	               
	            }
			 
		 }catch (SQLException e) {
	            e.printStackTrace();
	        }
		 
		return (eid==0)?-1:((type)?1:0);
	}
	
	
	
	@Override
	public Boolean returnBook(int employeeId,int transactionID, IssuedBooks issuedBooks) {

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/LibraryManagementSystem",
				"root", "wiley");
				PreparedStatement preparedStatement = connection.prepareStatement(
						"UPDATE ISSUEDBOOKS SET returnDate=?,fine=? WHERE employeeId=? AND transactionId=? LIMIT 1");) {

			preparedStatement.setTimestamp(1, Timestamp.valueOf(issuedBooks.getReturnDateTime()));
			preparedStatement.setDouble(2, issuedBooks.getLateFees());
			preparedStatement.setInt(3, empID);
			preparedStatement.setInt(4, transactionID);
			if (preparedStatement.executeUpdate() != 0)
				return true;
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}

		return false;
}
