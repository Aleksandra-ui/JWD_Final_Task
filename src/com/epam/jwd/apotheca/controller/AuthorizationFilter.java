package com.epam.jwd.apotheca.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.model.User;

public class AuthorizationFilter implements Filter {

	public static final String APOTHECA_LOGON_PAGE_JSP = "/apotheca/logonPage.jsp";
	org.slf4j.Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);
	

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		logger.info("entering {}", AuthorizationFilter.class.getName());
		HttpSession session = ((HttpServletRequest) request).getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			((HttpServletResponse) response).sendRedirect(APOTHECA_LOGON_PAGE_JSP);
		}
		logger.info("X");
		logger.error("leaving " );
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
