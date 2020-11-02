package com.capgemini.employeepayrolljdbc;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import com.mysql.cj.jdbc.Driver;

public class JDBC {
	public static void main(String[] args) throws SQLException {
		String url = "jdbc:mysql://localhost:3307/employee_payroll?useSSL=false";
		String userName = "root";
		String password = "Mahihari@99";
		Connection con = null;
		try {
			// Driver Loading
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Making the connection to Database
			con = DriverManager.getConnection(url, userName, password);
			System.out.println("Connection Successful");

		}  catch (ClassNotFoundException e) {
			throw new IllegalStateException("cannot find the driver");
		}
		listAllDrivers();

		try {
			System.out.println("Connecting to database" + url);
			con = DriverManager.getConnection(url, userName, password);
			System.out.println("Connected successfully " + con);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*PreparedStatement stmt = con.prepareStatement("select * from employee_payroll");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getInt("id") + " " + rs.getString("name") + " " + rs.getString("gender") + " "
					+ rs.getDouble("salary") + " " + rs.getDate("startdate"));
		}*/
	}

	private static void listAllDrivers() {
		Enumeration<java.sql.Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driver = (Driver) driverList.nextElement();
			System.out.println(" " + driver.getClass().getName());
		}

	}

}
