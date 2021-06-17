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

public class UserManager extends HttpServlet {

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
		HttpSession session = req.getSession();
		
		/*
		 * Integer visitsSession = (Integer) session.getAttribute("visits"); if
		 * (visitsSession == null) { visitsSession = 1; } pw.println("Page visited: " +
		 * visits + " times"); pw.println("Session page visited: " + visitsSession +
		 * " times"); visits++; visitsSession++; session.setAttribute("visits",
		 * visitsSession);
		 */
		 
		pw.println("<html>");
		pw.println("<head><title>Apotheca</title></head>");
		pw.println("<body>");
		pw.println(req.getAttribute("name"));
		pw.println("<br>");
		pw.println(req.getAttribute("pass"));
		pw.println("<br>");
		String userName = req.getParameter("name");
		String userPass = req.getParameter("pass");
		if (userName != null && userPass != null) {
			pw.println("logged user: " + userName + "<br/>password: " + userPass + "<br/>");
		}
		User user = (User) session.getAttribute("user");
		if (user == null) {
			pw.println("<p>please login</p>");
			pw.println(
					"<form action=\"/apotheca/\" method=\"POST\">name: <input type=\"text\" id=\"name\"></input><br/>password: <input type=\"password\" id=\"pass\"></input><br><input type=\"submit\"></input></form>");
		}
		pw.println("</body></html>");
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
