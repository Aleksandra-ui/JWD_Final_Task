package com.epam.jwd.apotheca.pool;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConcurrentConnectionTest {

	private static ConnectionPool cp;

	@BeforeClass
	public static void retrieve() {
		cp = ConnectionPool.retrieve();
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMultiTakeConnection() throws SQLException {
		try {
			for (int i = 0; i < 40; i++) {
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Connection c = cp.takeConnection();
							Thread.sleep(1);
							cp.releaseConnection(c);
						} catch (SQLException | InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				t.start();
			}
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("multi finished");
		}
	}

	@Test
	public void testSingleTakeConnection() {

		System.out.println("single start");
		try {
			for (int i = 0; i < 40; i++) {
				Connection c = cp.takeConnection();
				Thread.sleep(1);
				cp.releaseConnection(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("single finished");
		}
	}

	@AfterClass
	public static void release() {
		cp.destroy();

	}

}
