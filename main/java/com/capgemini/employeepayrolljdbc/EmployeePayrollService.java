package com.capgemini.employeepayrolljdbc;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.LocalDate;

public class EmployeePayrollService {
	List<EmployeePayrollData> employeePayrollList;
	EmployeePayrollData empDataObj = null;

	public enum statementType {
		STATEMENT, PREPARED_STATEMENT
	}
	/**
	 * 
	 * @return
	 * @throws DBServiceException
	 * usecase2
	 */

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
				String gender=resultSet.getString(5);
				empDataObj = new EmployeePayrollData(emp_id, name, salary, start,gender);
				employeePayrollList.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollList;
	}
	/**
	 * 
	 * @param name
	 * @param salary
	 * @return
	 * usecase3
	 */

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
	/**
	 * 
	 * @param name
	 * @param salary
	 * @param preparedStatement
	 * @return
	 * @throws DBServiceException
	 * usecase4
	 */

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
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DBServiceException
	 * usecase5
	 */
	public List<EmployeePayrollData> viewEmployeePayrollByJoinDateRange(LocalDate startDate , LocalDate endDate) throws DBServiceException
	{
		List<EmployeePayrollData> employeePayrollListByStartDate = new ArrayList<>();
		String query = "select * from Employee_Payroll where start_date between ? and  ?";
		try(Connection con = new JDBC().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setDate(1, Date.valueOf(startDate));
			preparedStatement.setDate(2, Date.valueOf(endDate));
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next())
			{
				int emp_id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				double salary = resultSet.getDouble(3);
				LocalDate start = resultSet.getDate(4).toLocalDate();
				String gender=resultSet.getString(5);
				empDataObj = new EmployeePayrollData(emp_id, name,salary,start,gender);
				employeePayrollListByStartDate.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollListByStartDate;
	}
	/**
	 * 
	 * @param column
	 * @param operation
	 * @return
	 * @throws DBServiceException
	 * usecase6
	 */
	public Map<String,Double> viewEmployeeDataGroupedByGender(String column , String operation) throws DBServiceException
	{
		Map<String,Double> empDataByGender = new HashMap<>();
		String query = String.format("select gender , %s(%s) from Employee_Payroll group by gender;" , operation , column);
		try(Connection con = new JDBC().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next())
			{
				empDataByGender.put(resultSet.getString(1), resultSet.getDouble(2));
			}
		}catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return empDataByGender;
	}	
}

