package com.capgemini.employeepayrolljdbc;

import java.sql.Date;
import java.time.LocalDate;

public class EmployeePayrollData {
	private int emp_id;
	private String name;
	private double salary;
	private LocalDate start_date;

	public EmployeePayrollData(int emp_id, String name, double salary, LocalDate start) {
		super();
		this.emp_id = emp_id;
		this.name = name;
		this.salary = salary;
		this.start_date = start;
	}

	public int getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData [emp_id=" + emp_id + ", name=" + name + ", salary=" + salary + ", start_date="
				+ start_date + "]";
	}

}
