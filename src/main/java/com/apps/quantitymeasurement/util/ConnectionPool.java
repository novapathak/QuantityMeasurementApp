package com.apps.quantitymeasurement.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionPool {

	static {
		try {
			Class.forName(ApplicationConfig.get("db.driver"));
		} catch (Exception e) {
			throw new RuntimeException("Driver load failed", e);
		}
	}

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(
					ApplicationConfig.get("db.url"),
					ApplicationConfig.get("db.username"),
					ApplicationConfig.get("db.password")
					);
		} catch (Exception e) {
			throw new RuntimeException("Connection failed", e);
		}
	}
}