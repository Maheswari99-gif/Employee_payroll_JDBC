package com.capgemini.employeepayrolljdbc;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeePayrollTest {
	static EmployeePayrollService serviceObj;
	@BeforeClass
	public static void setUp()  {
		serviceObj = new EmployeePayrollService();
	}

	@Test
	public void givenEmpPayrollDB_WhenRetrieved_ShouldMatchEmpCount() throws DBServiceException{
		List<EmployeePayrollData> empPayrollList = serviceObj.viewEmployeePayroll();
		assertEquals(3, empPayrollList.size());
	}

}