package com.epam.jwd.apotheca.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JSPAccessServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6941321309511598109L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = ((HttpServletRequest)request).getServletPath();
		if (! "/index.jsp".equalsIgnoreCase(path)) {
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
		}
//		String path = ((HttpServletRequest)request).getServletPath();
//		if (path.endsWith(".jsp") && ! "/index.jsp".equalsIgnoreCase(path)) {
//			response.sendRedirect("index.jsp");
//		}
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
