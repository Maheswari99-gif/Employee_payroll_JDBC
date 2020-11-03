package com.capgemini.employeepayrolljdbc;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.capgemini.employeepayrolljdbc.EmployeePayrollService.statementType;

public class EmployeePayroll {

	private static EmployeePayrollService employeePayrollService;
	private static EmployeePayrollService employeePayrollDBService;
	List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
	PreparedStatement preparedStatement;
	public int employeeId;
	public String name;
	public String salary;
	public LocalDate startDate;

	public EmployeePayroll() {
		employeePayrollDBService = employeePayrollDBService.getInstance();
	}

	// To read payroll Data from database
	public List<EmployeePayrollData> readData() throws DBServiceException {
		employeePayrollList = employeePayrollService.viewEmployeePayroll();
		return employeePayrollList;
	}

	// To update data in the database
	public void updateData(String name, double salary, statementType type) throws DBServiceException {
		employeePayrollList = employeePayrollService.viewEmployeePayroll();
		int rowAffected = employeePayrollDBService.updateSalary(name, salary);
		if (rowAffected != 0)
			(getEmployeeByName(employeePayrollList, name)).setSalary(salary);
	}

	private EmployeePayrollData getEmployeeByName(List<EmployeePayrollData> employeePayrollList2, String name) {
		EmployeePayrollData employee = employeePayrollList2.stream()
				.filter(employeeObj -> ((employeeObj.getName()).equals(name))).findFirst().orElse(null);
		return employee;
	}

	public boolean checkEmployeeDataInSyncWithDatabase(String name) throws DBServiceException {
		boolean result = false;
		employeePayrollList = employeePayrollDBService.viewEmployeePayroll();
		EmployeePayrollData employee = employeePayrollDBService.getEmployee(employeePayrollList, name);
		result = (getEmployeeByName(employeePayrollList, name)).equals(employee);
		return result;
	}

	// To get employee data joined after a particular date
	public List<EmployeePayrollData> viewEmployeePayrollByJoinDateRange(LocalDate startDate, LocalDate endDate)
			throws DBServiceException {
		return employeePayrollDBService.viewEmployeePayrollByJoinDateRange(startDate, endDate);
	}

	// To get sum of salaries of male and female employees
	public Map<String, Double> viewEmployeeDataGroupedByGender(String operation, String column)
			throws DBServiceException {
		return employeePayrollDBService.viewEmployeeDataGroupedByGender(operation, column);
	}

}
