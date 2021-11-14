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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.action.AddToCart;
import com.epam.jwd.apotheca.controller.action.AddToRecipeCart;
import com.epam.jwd.apotheca.controller.action.ChangeUserRole;
import com.epam.jwd.apotheca.controller.action.CreateDrug;
import com.epam.jwd.apotheca.controller.action.CreateRecipe;
import com.epam.jwd.apotheca.controller.action.DeleteUser;
import com.epam.jwd.apotheca.controller.action.DisplayCart;
import com.epam.jwd.apotheca.controller.action.DisplayRecipeCart;
import com.epam.jwd.apotheca.controller.action.Drugs;
import com.epam.jwd.apotheca.controller.action.DrugsBill;
import com.epam.jwd.apotheca.controller.action.Logon;
import com.epam.jwd.apotheca.controller.action.Orders;
import com.epam.jwd.apotheca.controller.action.PrescribedRecipes;
import com.epam.jwd.apotheca.controller.action.RecipeCartAware;
import com.epam.jwd.apotheca.controller.action.RecipeDrugs;
import com.epam.jwd.apotheca.controller.action.RemoveFromCart;
import com.epam.jwd.apotheca.controller.action.RemoveFromRecipeCart;
import com.epam.jwd.apotheca.controller.action.RunCommand;
import com.epam.jwd.apotheca.controller.action.ShoppingCartAware;
import com.epam.jwd.apotheca.controller.action.SetClientName;
import com.epam.jwd.apotheca.controller.action.SetExpieryDate;
import com.epam.jwd.apotheca.controller.action.UserManagement;
import com.epam.jwd.apotheca.model.User;

import ch.qos.logback.classic.Level;

public class ControllerFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(ControllerFilter.class);
	private Map<String, RunCommand> actionMapping; 

    public ControllerFilter() {
    	
    	actionMapping = new HashMap<String, RunCommand>();
    	actionMapping.put("drugs", Drugs.getInstance());
    	actionMapping.put("recipe", RecipeDrugs.getInstance());
    	actionMapping.put("createRecipe", CreateRecipe.getInstance());
    	actionMapping.put("prescribedRecipes", PrescribedRecipes.getInstance());
    	actionMapping.put("logon", Logon.getInstance());
    	actionMapping.put("orders", Orders.getInstance());
    	actionMapping.put("createDrug", CreateDrug.getInstance());
    	actionMapping.put("drugsBill", DrugsBill.getInstance());
    	actionMapping.put("addToCart", new AddToCart());
    	actionMapping.put("removeFromCart", new RemoveFromCart());
    	actionMapping.put("displayCart", new DisplayCart());
    	actionMapping.put("addToRecipeCart", new AddToRecipeCart());    
    	actionMapping.put("removeFromRecipeCart", new RemoveFromRecipeCart());
    	actionMapping.put("displayRecipeCart", new DisplayRecipeCart());
    	actionMapping.put("deleteUser", new DeleteUser());
    	actionMapping.put("changeUserRole", ChangeUserRole.getInstance());
    	actionMapping.put("setClientName", SetClientName.getInstance());
    	actionMapping.put("setExpieryDate", SetExpieryDate.getInstance());
    	actionMapping.put("userManagement", UserManagement.getInstance());
    	
    	ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.TRACE);
    	
    }

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String path = ((HttpServletRequest)request).getServletPath();
		String finalPath = path.substring(path.lastIndexOf("/"), path.indexOf(".run")).replace("/", "");
		
		logger.info("path: " + path);
		logger.info("final path: " + finalPath);
		
		if ( actionMapping.keySet().contains(finalPath) ) {
			RunCommand command = actionMapping.get(finalPath);
			
			if ( command instanceof ShoppingCartAware ) {
				ShoppingCart cart = (ShoppingCart)((HttpServletRequest)request).getSession().getAttribute("cart");
				if ( cart == null ) {
					cart = new ShoppingCart();
					((HttpServletRequest)request).getSession().setAttribute("cart", cart);
				}
				((ShoppingCartAware)command).setCart(cart);
			} else if ( command instanceof RecipeCartAware ) {
				RecipeCart cart = (RecipeCart)((HttpServletRequest)request).getSession().getAttribute("recipeCart");
				if ( cart == null ) {
					cart = new RecipeCart();
					((HttpServletRequest)request).getSession().setAttribute("recipeCart", cart);
				}
				((RecipeCartAware)command).setCart(cart);
			}
			
			User user = (User)((HttpServletRequest)request).getSession().getAttribute("user");
			if ( command.isSecure() && user == null ) {
				command = actionMapping.get("logon");
			}
			
			command.setParams(request.getParameterMap());
			command.setUser(user);	
			String value = command.run();
			if (command instanceof Logon) {
				if (command.getUser() == null) {
					((HttpServletRequest)request).getSession().removeAttribute("user");
				} else {
					((HttpServletRequest)request).getSession().setAttribute("user", command.getUser());
				}
				((Logon) command).setSession(((HttpServletRequest)request).getSession());
			}
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
