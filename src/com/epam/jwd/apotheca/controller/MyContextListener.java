package com.epam.jwd.apotheca.controller;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.epam.jwd.apotheca.dao.ConnectionPool;
import com.epam.jwd.apotheca.dao.CouldNotInitializeConnectionPoolException;

public class MyContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		ConnectionPool.retrieve().destroy();

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext context = event.getServletContext();
		Enumeration<String> params = context.getInitParameterNames();
		String name = params.nextElement();
		while (params.hasMoreElements()) {
			context.log("parameter: " + name + ", value: " + context.getInitParameter(name));
			name = params.nextElement();
		}
		context.setAttribute("xxx", "yyy");
		context.setAttribute("abc", "def");
		UserManagerService service = new UserManagerService();
		context.setAttribute("service", service);
		// ConnectionPool.retrieve();

	}

}
