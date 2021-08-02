package com.epam.jwd.apotheca.controller;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

public class MyContextAttributeListener implements ServletContextAttributeListener {

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		if ("def".equalsIgnoreCase(event.getName())) {
			event.getServletContext().setAttribute("zzz", "add");
			System.out.println("parameter: " + event.getName() + ", value: " + event.getValue());
		}

	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {

	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {

		if ("def".equalsIgnoreCase(event.getName())) {
			event.getServletContext().setAttribute("zzz", "replaced");
			System.out.println("parameter: " + event.getName() + ", value: " + event.getValue());
		}

	}

}
