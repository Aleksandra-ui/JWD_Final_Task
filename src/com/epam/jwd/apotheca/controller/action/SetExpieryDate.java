package com.epam.jwd.apotheca.controller.action;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.RecipeCart;
import com.epam.jwd.apotheca.controller.validator.DateValidator;
import com.epam.jwd.apotheca.controller.validator.UserIdValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
import com.epam.jwd.apotheca.model.User;

public class SetExpieryDate extends RecipeCartAction {

	private static final Logger logger = LoggerFactory.getLogger(SetExpieryDate.class);
	private static SetExpieryDate instance = new SetExpieryDate();
	
	private SetExpieryDate() {
		getValidators().put("date", new DateValidator("year", "month", "day"));
	}
	
	public static SetExpieryDate getInstance() {
		
		return instance;
		
	}

	@Override
	public String run() {
		
		super.run();
		
		Validator validator = getValidators().get("date");
		validator.setValue(getParams());
		
		if ( validator.validate() ) {
		
			Date date = getCart().getExpieryDate();
			Calendar calendar = new GregorianCalendar();
			if ( date != null ) {
				calendar.setTime(date);
			}
			
			String yearStr = getParams().get("year")[0];
			Integer year = Integer.valueOf(yearStr);
			calendar.set(Calendar.YEAR, year);
			
			String monthStr = getParams().get("month")[0];
			Integer month = Integer.valueOf(monthStr);
			calendar.set(Calendar.MONTH, month);
				
			String dayStr = getParams().get("day")[0];
			Integer day = Integer.valueOf(dayStr);
			calendar.set(Calendar.DAY_OF_MONTH, day);
				
			logger.trace("values {}/{}/{} were set into {}/{}/{}", year, month, day, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			
			getCart().setExpieryDate(new java.sql.Date(calendar.getTime().getTime()));
			
		}
		
		updateCart();
		
		return null;
		
	}

}
