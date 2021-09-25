package com.epam.jwd.apotheca.controller;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.pool.ConnectionPool;

public class MyContextListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(MyContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {

		ConnectionPool.retrieve().destroy();
		logger.info("context destroyed");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext context = event.getServletContext();

		UserManagerService userService = new UserManagerService();
		context.setAttribute("userService", userService);
		RecipeManagerService recipeService = new RecipeManagerService();
		context.setAttribute("recipeService", recipeService);
		OrderManagerService orderService = new OrderManagerService();
		context.setAttribute("orderService", orderService);
		
		Locale.setDefault(Locale.US);
		
		logger.info("context initialized");

	}

}
