package com.epam.jwd.apotheca.controller;

import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.epam.jwd.apotheca.pool.ConnectionPool;

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
		UserManagerService userService = new UserManagerService();
		context.setAttribute("userService", userService);
		DrugManagerService drugService = new DrugManagerService();
		context.setAttribute("drugService", drugService);
		RecipeManagerService recipeService = new RecipeManagerService();
		context.setAttribute("recipeService", recipeService);
		OrderManagerService orderService = new OrderManagerService();
		context.setAttribute("orderService", orderService);
		
		// ConnectionPool.retrieve();
		Locale.setDefault(Locale.US);
	
	}

}
