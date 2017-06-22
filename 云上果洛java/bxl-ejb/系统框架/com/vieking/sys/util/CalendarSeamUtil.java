package com.vieking.sys.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("csu")
@BypassInterceptors
@AutoCreate
public class CalendarSeamUtil {

	/***
	 * 
	 * @param value
	 *            字符串时间
	 * @return
	 */
	public Calendar strToCal(String value) {
		return CalendarUtil.StringToCalendar(value);
	}

	public Calendar date(String value, int day) {
		Calendar c = CalendarUtil.StringToCalendar(value);
		return CalendarUtil.getNextDay(c, day);
	}

	public Calendar gcnow() {
		return GregorianCalendar.getInstance();
	}

	public Calendar now() {
		return Calendar.getInstance();
	}

	public Calendar today() {
		return CalendarUtil.getToday();
	}
}
