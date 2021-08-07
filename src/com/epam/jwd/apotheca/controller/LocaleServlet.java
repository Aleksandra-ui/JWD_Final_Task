package com.epam.jwd.apotheca.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LocaleServlet extends HttpServlet {

	/****/
	private static final long serialVersionUID = 6151651810369784194L;
	
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
//		PrintWriter out = resp.getWriter();
//		out.println("!!!!!");
		
//		Cookie cookie = new Cookie("locale", req.getParameter("locale"));
//		resp.addCookie(cookie);
//		resp.setContentType("text/html");
//	 
//	    Locale locale = null;
//	    
//	    if ("en".equals(req.getParameter("locale"))) {
//		    locale = new Locale("en", "US");
//	    } else {
//	    	locale = new Locale("zh", "CHINESE");
//	    }
//	    ResourceBundle rb = ResourceBundle.getBundle("Drugs", locale);
//	    out.println(rb.getString("drugs.welcome"));
	    
		
	}

}
