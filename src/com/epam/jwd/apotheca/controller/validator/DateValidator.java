package com.epam.jwd.apotheca.controller.validator;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.epam.jwd.apotheca.controller.ControllerFilter;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.model.User;

public class DateValidator implements Validator {

	private Map<String, String[]> params;
	private List<String> messages;
	private static final Logger logger = LoggerFactory.getLogger(DateValidator.class);
	
	public DateValidator(Map<String, String[]> params) {
		this.params = params;
		messages = new ArrayList<String>();
	}
	
	public boolean validate() {
		
		boolean result = true;
		messages.clear();
		
		String yearStr = params.get("year")[0];
		Integer year = Integer.valueOf(yearStr);
		String monthStr = params.get("month")[0];
		Integer month = Integer.valueOf(monthStr);
		String dayStr = params.get("day")[0];
		Integer day = Integer.valueOf(dayStr);
	
		result = validateLogic(year, month, day) && validateDifference(year, month, day);
		
		return result;
		
	}
	
	private boolean validateDifference(Integer year, Integer month, Integer day) {
		
		boolean result = true; 
			
		Calendar calendar = GregorianCalendar.getInstance();
		Calendar toCheck = GregorianCalendar.getInstance();
		toCheck.set(GregorianCalendar.YEAR, year);
		toCheck.set(GregorianCalendar.MONTH, month);
		toCheck.set(GregorianCalendar.DAY_OF_MONTH, day);
		if (calendar.after(toCheck)) {
			messages.add("Specified date is before current.");
			result = false;
		}
		calendar.add(GregorianCalendar.YEAR, 1);
		if (!toCheck.before(calendar)) {
			messages.add("Specified date is more than year after current date.");
			result = false;
		}
		
		return result;
		
	}
	
	private boolean validateLogic(Integer year, Integer month, Integer day) {
		
		boolean result = true;

		String expieryDate = year + "/" + month + "/" + day;
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		format.setLenient(false);
	    try {
			format.parse(expieryDate);
		} catch (ParseException e) {
			result = false;
			messages.add("Incorrect date format, \"YYYY-MM-DD\" is expected.");
			logger.info(Arrays.toString(e.getStackTrace()));
		}
		
		return result;
		
	}

	public List<String> getMessages() {
		return messages;
	}

	@Override
	public void setValue(Object value) {
		this.params = (Map<String, String[]>)value;
	}
	
}
