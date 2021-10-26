package com.epam.jwd.apotheca.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateValidator implements Validator {

	private Map<String, String[]> params;
	private List<String> messages;
	private static final Logger logger = LoggerFactory.getLogger(DateValidator.class);
	private String yearParamName;
	private String monthParamName;
	private String dayParamName;
	
	public DateValidator(String yearParamName, String monthParamName, String dayParamName) {
		this.yearParamName = yearParamName;
		this.monthParamName = monthParamName;
		this.dayParamName = dayParamName;
		messages = new ArrayList<String>();
	}
	
	public boolean validate() {
		
		boolean result = true;
		Integer year = null;
		Integer month = null;
		Integer day = null;
		messages.clear();
		
		String yearStr = params.get(yearParamName) == null ? null : params.get(yearParamName)[0];
		String monthStr = params.get(monthParamName) == null ? null : params.get(monthParamName)[0];
		String dayStr = params.get(dayParamName) == null ? null : params.get(dayParamName)[0];
		
		if ( yearStr == null || monthStr == null || dayStr == null ) {
			messages.add("Expiery date isn't specified.");
			logger.warn("expiery date isn't specified");
			result = false;
		}
		
		if ( result ) {
			try {
				year = Integer.valueOf(yearStr);
				month = Integer.valueOf(monthStr);
				day = Integer.valueOf(dayStr);
			} catch ( NumberFormatException e ) {
				messages.add("Illegal format of expiery date!");
				logger.error("illegal format of expiery date!");
				logger.error(Arrays.toString(e.getStackTrace()));
				result = false;
			}
		}
	
		if ( result ) {
		
			result = validateLogic(year, month, day) && validateDifference(year, month, day);
			
		}
		
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

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.params = (Map<String, String[]>)value;
	}
	
}
