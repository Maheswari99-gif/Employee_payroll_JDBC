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
	 * @throws DBServiceException usecase2
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
				String gender = resultSet.getString(5);
				empDataObj = new EmployeePayrollData(emp_id, name, salary, start, gender);
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
	 * @return usecase3
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
	 * @throws DBServiceException usecase4
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
	 * @throws DBServiceException usecase5
	 */
	public List<EmployeePayrollData> viewEmployeePayrollByJoinDateRange(LocalDate startDate, LocalDate endDate)
			throws DBServiceException {
		List<EmployeePayrollData> employeePayrollListByStartDate = new ArrayList<>();
		String query = "select * from Employee_Payroll where start_date between ? and  ?";
		try (Connection con = new JDBC().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setDate(1, Date.valueOf(startDate));
			preparedStatement.setDate(2, Date.valueOf(endDate));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int emp_id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				double salary = resultSet.getDouble(3);
				LocalDate start = resultSet.getDate(4).toLocalDate();
				String gender = resultSet.getString(5);
				empDataObj = new EmployeePayrollData(emp_id, name, salary, start, gender);
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
	 * @throws DBServiceException usecase6
	 */
	public Map<String, Double> viewEmployeeDataGroupedByGender(String column, String operation)
			throws DBServiceException {
		Map<String, Double> empDataByGender = new HashMap<>();
		String query = String.format("select gender , %s(%s) from Employee_Payroll group by gender;", operation,
				column);
		try (Connection con = new JDBC().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				empDataByGender.put(resultSet.getString(1), resultSet.getDouble(2));
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return empDataByGender;
	}

	/**
	 * 
	 * @param name
	 * @param gender
	 * @param salary
	 * @param start_date
	 * @throws DBServiceException
	 */

	public void addNewEmployeeToDB(String name, String gender, double salary, LocalDate start_date)
			throws DBServiceException {
		String query = "insert into Employee_Payroll ( name , gender, salary , start_date) values (?,?,?,?)";
		try (Connection con = new JDBC().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, gender);
			preparedStatement.setDouble(3, salary);
			preparedStatement.setDate(4, Date.valueOf(start_date));
			preparedStatement.executeUpdate();
			empDataObj = new EmployeePayrollData(name, gender, salary, start_date);
			viewEmployeePayroll().add(empDataObj);
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
	}

	/**
	 * usecase8
	 * 
	 * @param name
	 * @param salary
	 * @param startDate
	 * @param gender
	 * @return
	 * @throws DBServiceException
	 */
	public EmployeePayrollData addEmployeeToEmployeeAndPayroll(String name, double salary, LocalDate startDate,
			String gender) throws DBServiceException {
		int emp_id = -1;
		Connection connection = null;
		EmployeePayrollData employeePayrollData = null;
		connection = new JDBC().getConnection();
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO employee_payroll(name,gender,salary,startDate) VALUES ('%s','%s','%s','%s')", name,
					gender, salary, Date.valueOf(startDate));
			int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					emp_id = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format(
					"INSERT INTO payroll_details(employee_id,basic_pay,deductions,taxable_pay,tax,net_pay)VALUES (%s,%s,%s,%s,%s,%s)",
					emp_id, salary, deductions, taxablePay, tax, netPay);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 1) {
				employeePayrollData = new EmployeePayrollData(emp_id, name, gender, salary, startDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return employeePayrollData;
	}
}
