package com.epam.jwd.apotheca.controller.action;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.validator.DateValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;

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
		
		if ( !validator.validate() ) {
			
				logger.trace("attempt to change date from {} to {} failed", getFormattedDate(getCart().getExpieryDate()), getParams().get("year") + "/" +
						getParams().get("month") + "/" +
						getParams().get("day"));
				for ( String m : validator.getMessages() ) {
					logger.trace(m);
				}
				getErrors().put("expiery", "wrong expiery date (yyyy/mm/dd): " +
											getParams().get("year") + "/" +
											getParams().get("month") + "/" +
											getParams().get("day"));
			
			
		}
		Integer day = null, month=null, year = null;
		Date date = getCart().getExpieryDate();
		Calendar calendar = new GregorianCalendar();
		if ( date != null ) {
			calendar.setTime(date);
		}
		
		if (  getParams().get("year") != null ) {
			String yearStr = getParams().get("year")[0];
			
			try {
				year = Integer.valueOf(yearStr);
				calendar.set(Calendar.YEAR, year);
			} catch ( NumberFormatException e ) {
				logger.error(Arrays.toString(e.getStackTrace()));
			}
			
		}
		if (  getParams().get("month") != null ) {
			String monthStr = getParams().get("month")[0];
			
			try {
				month = Integer.valueOf(monthStr);
				calendar.set(Calendar.MONTH, month - 1);
			} catch ( NumberFormatException e ) {
				logger.error(Arrays.toString(e.getStackTrace()));
			}
			
		}
		if (  getParams().get("day") != null ) {
			String dayStr = getParams().get("day")[0];
			
			try {
				day = Integer.valueOf(dayStr);
				calendar.set(Calendar.DAY_OF_MONTH, day);
			} catch ( NumberFormatException e ) {
				logger.error(Arrays.toString(e.getStackTrace()));
			}
			
		}

			
		logger.trace("values {}/{}/{} were set into {}/{}/{}", year, month, day, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		
		getCart().setExpieryDate(new java.sql.Date(calendar.getTime().getTime()));
			
		
		
		
		updateCart();
		
		return null;
		
	}
	
	public static String getFormattedDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String stringDate = null;
		
		try {
			stringDate = format.format(new java.util.Date(date.getTime()));
		} catch (Exception e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		
		return stringDate;
	}

}
