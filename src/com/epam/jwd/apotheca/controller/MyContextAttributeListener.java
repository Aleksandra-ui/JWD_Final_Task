package com.epam.jwd.apotheca.controller;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyContextAttributeListener implements ServletContextAttributeListener {


	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		if ( "def".equalsIgnoreCase(event.getName() )){
			event.getServletContext().setAttribute("zzz", "add");
			System.out.println("parameter: " + event.getName() + ", value: " + event.getValue());
		}
		
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {

		if ( "def".equalsIgnoreCase(event.getName() )){
			event.getServletContext().setAttribute("zzz", "replaced");
			System.out.println("parameter: " + event.getName() + ", value: " + event.getValue());
		}
		
	}

}
