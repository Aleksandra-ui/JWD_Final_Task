package com.epam.jwd.apotheca.pool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.epam.jwd.apotheca.exception.CouldNotInitializeConnectionPoolException;

public class ConcurrentConnectionPool implements ConnectionPool {
	public static final ConcurrentConnectionPool INSTANCE = new ConcurrentConnectionPool();

	private static final int INIT_CONNECTIONS_AMOUNT = 8;
	private static final int MAX_CONNECTIONS_AMOUNT = 32;
	private static final int CONNECTIONS_GROW_FACTOR = 4;

	private final AtomicBoolean initialized;
	private final Queue<ProxyConnection> availableConnections;// only unused connections
	private AtomicInteger connectionsOpened;// all connections' amount
	private final Lock lock;

	private ConcurrentConnectionPool() {
		initialized = new AtomicBoolean(false);
		availableConnections = new ArrayDeque<>();
		connectionsOpened = new AtomicInteger(0);
		lock = new ReentrantLock();
	}

	@Override
	public Connection takeConnection() throws SQLException {

		lock.lock();
		Connection connection = null;
		try {
			connection = availableConnections.poll();
			final int currentOpenedConnections = connectionsOpened.get();
			if (availableConnections.size() <= currentOpenedConnections / CONNECTIONS_GROW_FACTOR
					&& currentOpenedConnections < MAX_CONNECTIONS_AMOUNT) {
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
				final ProxyConnection proxyConnection = new ProxyConnection(connection);
				availableConnections.add(proxyConnection);
				connectionsOpened.getAndIncrement();
				System.out.println("connection added, available connections: " + availableConnections.size()
						+ ", connections opened: " + connectionsOpened.get());

			} else if (availableConnections.size() == 0 && connection == null) {
				System.out.println("no available connections");
			} else {
				System.out.println("available connections: " + availableConnections.size() + ", connections opened: "
						+ connectionsOpened.get());
			}
		} finally {
			lock.unlock();
		}

		return connection;
	}

	@Override
	public void releaseConnection(Connection connection) {

		lock.lock();
		try {
			if (connection != null) {
				if (connection instanceof ProxyConnection) {
					availableConnections.add((ProxyConnection) connection);
				} else {
					System.out.println("can't release connection");
				}
			}
		} finally {
			lock.unlock();
		}

	}

	@Override
	public void init() throws CouldNotInitializeConnectionPoolException {
		lock.lock();
		try {
			if (initialized.compareAndSet(false, true)) {
				registerDrivers();
				try {
					for (int i = 0; i < INIT_CONNECTIONS_AMOUNT; i++) {
						final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb",
								"root", "root");
						final ProxyConnection proxyConnection = new ProxyConnection(connection);
						availableConnections.add(proxyConnection);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					initialized.set(false);
					throw new CouldNotInitializeConnectionPoolException("failed to open connection", e);
				}
				connectionsOpened.set(INIT_CONNECTIONS_AMOUNT);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void destroy() {
		lock.lock();
		try {
			if (initialized.compareAndSet(true, false)) {
				for (ProxyConnection conn : availableConnections) {
					try {
						conn.realClose();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				// close not available too
				deregisterDrivers();
			}
		} finally {
			lock.unlock();
		}
	}

	private void registerDrivers() throws CouldNotInitializeConnectionPoolException {
		System.out.println("sql drivers registration start...");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.registerDriver(DriverManager.getDriver("jdbc:mysql://localhost:3306/mydb"));
			System.out.println("registration successful");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("registration unsuccessful");
			e.printStackTrace();
			initialized.set(false);
			throw new CouldNotInitializeConnectionPoolException("driver registration failed", e);
		}
	}

	private void deregisterDrivers() {
		System.out.println("sql drivers unregistering start...");
		final Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			try {
				DriverManager.deregisterDriver(drivers.nextElement());
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("unregistering drivers failed");
			}
		}
	}

	public static void main(String[] args) throws CouldNotInitializeConnectionPoolException {
		ConnectionPool.retrieve().init();

	}
}