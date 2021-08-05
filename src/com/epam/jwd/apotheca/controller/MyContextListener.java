package com.epam.jwd.apotheca.controller;

import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.epam.jwd.apotheca.pool.ConnectionPool;

public class MyContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {

		ConnectionPool.retrieve().destroy();

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext context = event.getServletContext();

		UserManagerService userService = new UserManagerService();
		context.setAttribute("userService", userService);
		DrugManagerService drugService = new DrugManagerService();
		context.setAttribute("drugService", drugService);
		RecipeManagerService recipeService = new RecipeManagerService();
		context.setAttribute("recipeService", recipeService);
		OrderManagerService orderService = new OrderManagerService();
		context.setAttribute("orderService", orderService);
		
		Locale.setDefault(Locale.US);

	}

}
