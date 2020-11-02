package com.capgemini.employeepayrolljdbc;

import java.sql.*;
import java.util.*;

import java.sql.Statement;
import java.time.LocalDate;

public class EmployeePayrollService {
	public List<EmployeePayrollData> viewEmployeePayroll() throws DBServiceException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		JDBC obj = new JDBC();
		EmployeePayrollData empDataObj = null;
		String query = "select * from Employee_Payroll";
		try (Connection con = obj.getConnection()) {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				double salary = resultSet.getDouble(3);
				LocalDate start = resultSet.getDate(4).toLocalDate();
				empDataObj = new EmployeePayrollData(id, name, salary, start);
				employeePayrollList.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollList;
	}

}
