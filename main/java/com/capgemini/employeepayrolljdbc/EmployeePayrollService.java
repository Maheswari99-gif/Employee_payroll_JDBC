package com.capgemini.employeepayrolljdbc;

import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeePayrollService {
	List<EmployeePayrollData> employeePayrollList;
	EmployeePayrollData empDataObj = null;

	public enum statementType {
		STATEMENT, PREPARED_STATEMENT
	}

	public List<EmployeePayrollData> viewEmployeePayroll() throws DBServiceException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		JDBC obj = new JDBC();
		String query = "select * from Employee_Payroll";
		try (Connection con = obj.getConnection()) {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int emp_id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				double salary = resultSet.getDouble(3);
				LocalDate start = resultSet.getDate(4).toLocalDate();
				/*
				 * double basic_pay = resultSet.getDouble(6); double deductions =
				 * resultSet.getDouble(7); double taxable_pay = resultSet.getDouble(8); double
				 * tax = resultSet.getDouble(9); double net_pay = resultSet.getDouble(10); int
				 * comp_id = resultSet.getInt(11); String phn_no = resultSet.getString(12);
				 * String address = resultSet.getString(13);
				 */
				empDataObj = new EmployeePayrollData(emp_id, name, salary, start);
				employeePayrollList.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollList;
	}

	public int updateSalary(String name, Double salary) {
		String sqlQuery = String.format("UPDATE employee_payroll SET salary = %.2f WHERE NAME = '%s';", salary, name);
		try (Connection connection = JDBC.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateSalaryUsingPreparedStatement(String name, double salary, statementType preparedStatement)
			throws DBServiceException {
		String query = "UPDATE employee_payroll SET salary = ? WHERE name = ?";
		try (Connection con = new JDBC().getConnection()) {
			PreparedStatement statement = con.prepareStatement(query);
			statement.setDouble(1, salary);
			statement.setString(2, name);
			return statement.executeUpdate();
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
	}

	public boolean check(List<EmployeePayrollData> employeeList, String name, double salary) throws DBServiceException {
		// TODO Auto-generated method stub
		EmployeePayrollData employeeObj = getEmployee(employeeList, name);
		employeeObj.setSalary(salary);
		return employeeObj.equals(getEmployee(viewEmployeePayroll(), name));

	}

	private EmployeePayrollData getEmployee(List<EmployeePayrollData> employeeList, String name) {
		EmployeePayrollData employee = employeeList.stream()
				.filter(employeeObj -> ((employeeObj.getName()).equals(name))).findFirst().orElse(null);
		return employee;
	}

}
