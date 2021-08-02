package com.epam.jwd.apotheca.pool;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {

	Connection takeConnection() throws SQLException;

	void releaseConnection(Connection connection);

	void init() throws CouldNotInitializeConnectionPoolException;

	void destroy();

	static ConnectionPool retrieve() {
		return ConcurrentConnectionPool.INSTANCE;
	}

}