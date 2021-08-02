package com.epam.jwd.apotheca.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserManager extends HttpServlet {

	/***/
	private static final long serialVersionUID = 5923930100531435210L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("logonPage.jsp").forward(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

}
