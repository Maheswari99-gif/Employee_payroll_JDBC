package com.capgemini.employeepayrolljdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeePayrollTest {
	static EmployeePayrollService serviceObj;
	private List<EmployeePayrollData> employeeList = new ArrayList<>();

	@BeforeClass
	public static void setUp() {
		serviceObj = new EmployeePayrollService();
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

}