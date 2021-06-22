package com.epam.jwd.apotheca.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epam.jwd.apotheca.model.User;

public class DrugManager extends HttpServlet {

	/***/
	private static final long serialVersionUID = 5923930100531435210L;
	private static int visits = 1;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doGet(req, resp);
		resp.setCharacterEncoding("UTF-8");
		PrintWriter pw = resp.getWriter();
		/*
		 * for ( String a : req.getParameterMap().keySet() ) { pw.println(a); }
		 * pw.println("ёу!");
		 */
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}

}
