package com.capgemini.employeepayrolljdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeePayrollTest {
	static EmployeePayrollService serviceObj;
	private List<EmployeePayrollData> employeeList = new ArrayList<>();
	static Map<String, Double> empDataByGender;
	static List<EmployeePayrollData> empPayrollList;

	@BeforeClass
	public static void setUp() {
		serviceObj = new EmployeePayrollService();
		empDataByGender = new HashMap<>();
		empPayrollList = new ArrayList<>();
	}

	@Test
	public void givenEmpPayrollDB_WhenRetrieved_ShouldMatchEmpCount() throws DBServiceException {
		List<EmployeePayrollData> empPayrollList = serviceObj.viewEmployeePayroll();
		assertEquals(3, empPayrollList.size());
	}

	@Test
	public void givenUpdatedSalary_WhenRetrieved_ShouldBeSyncedWithDB() throws DBServiceException {
		serviceObj.updateSalary("Ambani", 3000000.00);
		boolean isSynced = serviceObj.check(employeeList, "Ambani", 3000000.00);
		assertTrue(isSynced);
	}

	@Test
	public void givenUpdatedSalaryWhenUpdatedUsingPreparedStatementShouldSyncWithDatabase() throws DBServiceException {
		employeeList = serviceObj.viewEmployeePayroll();
		serviceObj.updateSalaryUsingPreparedStatement("Ambani", 2000000.00,
				EmployeePayrollService.statementType.PREPARED_STATEMENT);
		boolean result = serviceObj.check(employeeList, "Ambani", 2000000.00);
		assertTrue(result);
	}

	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmpCount() throws DBServiceException {
		List<EmployeePayrollData> empPayrollList = serviceObj
				.viewEmployeePayrollByJoinDateRange(LocalDate.of(2018, 02, 01), LocalDate.now());
		assertEquals(3, empPayrollList.size());
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedSum_ShouldReturnSumGroupedByGender() throws DBServiceException {
		empDataByGender = serviceObj.viewEmployeeDataGroupedByGender("salary", "sum");
		assertEquals(1550000, empDataByGender.get("M"), 0.0);
		assertEquals(300000, empDataByGender.get("F"), 0.0);
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedAvg_ShouldReturnAvgByGroupedGender() throws DBServiceException {
		empDataByGender = serviceObj.viewEmployeeDataGroupedByGender("salary", "avg");
		assertEquals(3100000, empDataByGender.get("M"), 0.0);
		assertEquals(300000, empDataByGender.get("F"), 0.0);
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedMax_ShouldReturnMaxGroupedByGender() throws DBServiceException {
		empDataByGender = serviceObj.viewEmployeeDataGroupedByGender("salary", "max");
		assertEquals(3000000, empDataByGender.get("M"), 0.0);
		assertEquals(300000, empDataByGender.get("F"), 0.0);
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedMin_ShouldReturnMinGroupedByGender() throws DBServiceException {
		empDataByGender = serviceObj.viewEmployeeDataGroupedByGender("salary", "min");
		assertEquals(100000, empDataByGender.get("M"), 0.0);
		assertEquals(300000, empDataByGender.get("F"), 0.0);
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedCount_ShouldReturnCountGroupedByGender() throws DBServiceException {
		empDataByGender = serviceObj.viewEmployeeDataGroupedByGender("salary", "count");
		assertEquals(2, empDataByGender.get("M"), 0.0);
		assertEquals(1, empDataByGender.get("F"), 0.0);
	}

	@Test
	public void addNewEmployee_WhenRetrieved_ShouldBeSyncedWithDB() throws DBServiceException {
		serviceObj.addNewEmployeeToDB("Mahesh", "M", 6000000.0, LocalDate.now());
		boolean isSynced = serviceObj.check(employeeList, "Mahesh", 6000000.00);
		assertTrue(isSynced);
	}
}
