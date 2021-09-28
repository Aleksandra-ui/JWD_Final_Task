package com.epam.jwd.apotheca.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.model.User;
import com.mysql.fabric.Response;

public class ControllerFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(ControllerFilter.class);
	private Map<String, RunCommand> actionMapping; 

    public ControllerFilter() {
    	
    	actionMapping = new HashMap<String, RunCommand>();
    	actionMapping.put("hello", new Hello());
    	actionMapping.put("bye", new Bye());
    	actionMapping.put("drugs", new Drugs());
    	actionMapping.put("recipe", new Recipe());
    	
    }

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String path = ((HttpServletRequest)request).getServletPath();
		String finalPath = path.substring(path.lastIndexOf("/"), path.indexOf(".run")).replace("/", "");
		
		logger.info("path: " + path);
		logger.info("final path: " + finalPath);
		
		if ( actionMapping.keySet().contains(finalPath) ) {
			RunCommand command = actionMapping.get(finalPath);
			command.setParams(request.getParameterMap());
			command.setUser((User)
					((HttpServletRequest)request).getSession().getAttribute("user"));
			String value = command.run();
			((HttpServletRequest)request).setAttribute("action", command);
			logger.info(value);
			request.getRequestDispatcher(command.getView()).forward(request, response);
			return;
		} else {
			logger.error("unknown command");
		}
		 
		chain.doFilter(request, response);
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
