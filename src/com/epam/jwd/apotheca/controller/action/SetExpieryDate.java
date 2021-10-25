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
import com.epam.jwd.apotheca.model.User;

public class SetExpieryDate extends RecipeCartAction {

	private static final Logger logger = LoggerFactory.getLogger(SetExpieryDate.class);
	private static SetExpieryDate instance = new SetExpieryDate();
	
	private SetExpieryDate() {
	}
	
	public static SetExpieryDate getInstance() {
		
		return instance;
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public String run() {
		
		super.run();
		
		updateCart();
		
		Date date = getCart().getExpieryDate();
		Calendar calendar = new GregorianCalendar();
		if ( date != null ) {
			calendar.setTime(date);
		}
		
		//&&  && 
		// + "/" + getParams().get("month")[0] + "/" + getParams().get("day")[0]
		if (  getParams().get("year") != null  ) {
			String yearStr = getParams().get("year")[0];
			Integer year = null;
			try {
				year = Integer.valueOf(yearStr);
				calendar.set(Calendar.YEAR, year);
			} catch (NumberFormatException e) {
				logger.error(Arrays.toString(e.getStackTrace()));
			}
		} 
		if ( getParams().get("month") != null ) {
			String monthStr = getParams().get("month")[0];
			Integer month = null;
			try {
				month = Integer.valueOf(monthStr);
				calendar.set(Calendar.MONTH, month);
			} catch (NumberFormatException e) {
				logger.error(Arrays.toString(e.getStackTrace()));
			}
		}
		if ( getParams().get("day") != null ) {
			String dayStr = getParams().get("day")[0];
			Integer day = null;
			try {
				day = Integer.valueOf(dayStr);
				calendar.set(Calendar.DAY_OF_MONTH, day);
			} catch (NumberFormatException e) {
				logger.error(Arrays.toString(e.getStackTrace()));
			}
		}
		
		getCart().setExpieryDate(new java.sql.Date(calendar.getTime().getTime()));
		
		return null;
		
	}

}
