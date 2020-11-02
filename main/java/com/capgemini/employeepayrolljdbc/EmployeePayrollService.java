package com.capgemini.employeepayrolljdbc;

import java.sql.*;
import java.util.*;

import java.sql.Statement;
import java.time.LocalDate;

public class EmployeePayrollService {
	List<EmployeePayrollData> employeePayrollList;
	EmployeePayrollData empDataObj = null;

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

	public void updateSalary(String name, double salary) throws DBServiceException {
		String query = String.format("update Employee_Payroll set salary = %.2f where name = '%s';", salary, name);
		try (Connection con = new JDBC().getConnection()) {
			Statement statement = con.createStatement();
			int result = statement.executeUpdate(query);
			if (result > 0 && empDataObj != null)
				empDataObj.setSalary(salary);
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
