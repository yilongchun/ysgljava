package com.vieking.sys.util;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.vieking.sys.exception.TypeConversionException;

public class CalendarUtil {

	private static final String[][] foo;
	public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";
	public static final String ISO_EXPANDED_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss,SSSzzz";
	public static final DateFormatSymbols dateFormatSymbles;
	public static final String ISO_DATE_FORMAT = "yyyyMMdd";

	static {
		// override the timezone strings
		foo = new String[0][];
		dateFormatSymbles = new DateFormatSymbols();
		dateFormatSymbles.setZoneStrings(foo);
	}

	/**
	 * 取得月头日期
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getMonthBeginDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		// result = df.format(c.getTime());
		Date d = new Date(c.getTime().getYear(), c.getTime().getMonth(), 1);
		return df.format(d);
	}

	/**
	 * 转化为yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getYyyyMmDd(Calendar c) {
		if (c == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());
	}

	/**
	 * 转化为yyyy-MM
	 * 
	 * @return
	 */
	public static String getYyyyMm(Calendar c) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		return df.format(c.getTime());
	}

	/**
	 * 转化为yyyy年MM月dd日
	 * 
	 * @return
	 */
	public static String getYyyyMmDdZh(Calendar c) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		return df.format(c.getTime());
	}

	
	public static String getyymm(Calendar o) {
		String result = null;
		if (o instanceof Calendar) {
			Calendar temp = (Calendar) o.clone();
			SimpleDateFormat df = new SimpleDateFormat("yyMM");
			result = df.format(temp.getTime());
		}
		return result;
	}
	
	public static String getyymmdd(Calendar o) {
		String result = null;
		if (o instanceof Calendar) {
			Calendar temp = (Calendar) o.clone();
			SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
			result = df.format(temp.getTime());
		}
		return result;
	}

	public static String getMonthmm(Calendar o) {
		String result = null;
		if (o instanceof Calendar) {
			Calendar temp = (Calendar) o.clone();
			SimpleDateFormat df = new SimpleDateFormat("MM");
			result = df.format(temp.getTime());
		}
		return result;
	}

	public static boolean isToday(Calendar o) {
		return clearTime(o, true).equals(
				clearTime(Calendar.getInstance(), false));
	}

	public static String getYearyyyy(Calendar o) {
		String result = null;
		if (o instanceof Calendar) {
			Calendar temp = (Calendar) o.clone();
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			result = df.format(temp.getTime());
		}
		return result;
	}

	public static String getMmdd(Calendar o) {
		String result = null;
		if (o instanceof Calendar) {
			Calendar temp = (Calendar) o.clone();
			SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
			result = df.format(temp.getTime());
		}
		return result;
	}

	public static String getYyMmdd(Calendar o) {
		String result = null;
		if (o instanceof Calendar) {
			Calendar temp = (Calendar) o.clone();
			SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
			result = df.format(temp.getTime());
		}
		return result;
	}

	public static String getTodayYymmdd() {
		return getyymmdd(Calendar.getInstance());
	}

	public static String getTodaymmdd() {
		return getMmdd(getToday());
	}

	public static String getYearyy(Calendar o) {
		String result = null;
		if (o instanceof Calendar) {
			Calendar temp = (Calendar) o.clone();
			SimpleDateFormat df = new SimpleDateFormat("yy");
			result = df.format(temp.getTime());
		}
		return result;
	}

	public static String convertToString(Calendar o) {
		return getYyyyMmDd(o);
	}

	// Timestamp转化为String:
	public static String convertToString(Timestamp o) {
		String result = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
		// Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
		result = df.format(o);
		return result;
	}

	// Timestamp转化为String:
	public static String dataTimeStr(Calendar o) {
		String result = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
		result = df.format(o.getTime());
		return result;
	}

	public static String getDateTime() {
		String result = null;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		result = df.format(c.getTime());
		return result;
	}

	public static String getDate() {
		String result = null;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		result = df.format(c.getTime());
		return result;
	}

	public static Calendar StringToCalendar(String value) {
		if (value == null) {
			return null;
		}
		Calendar result = null;
		if (value instanceof String && !value.equals("")) {
			try {
				Date date = CalendarUtil.strToDateTime(value);
				result = Calendar.getInstance();
				result.setTime(date);
			} catch (TypeConversionException e) {
				throw new TypeConversionException(e);
			}
		}
		return result;
	}

	public static final Date strToDateTime(String dateTimeString) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date result = df.parse((String) dateTimeString);
			return result;

		} catch (ParseException e) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date result = df.parse((String) dateTimeString);
				return result;
			} catch (ParseException e1) {

				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date result = df.parse((String) dateTimeString);
					return result;
				} catch (ParseException e2) {
					throw new TypeConversionException(e2);
				}

			}
		}
	}

	/**
	 * 返回当前的0时0分0秒0毫秒
	 * 
	 * @return
	 */
	public static Calendar getToday() {
		Calendar result = null;
		result = Calendar.getInstance();
		clearTime(result, false);
		return result;
	}

	/**
	 * 清除小时、分钟、秒、毫秒信息
	 * 
	 * @param c
	 *            带时间的日期对象
	 * @param returnClone
	 *            是否返回clone对象的处理结果。
	 * @return 清除小时、分钟、秒、毫秒信息的Calendar
	 */
	public static Calendar clearTime(Calendar c, boolean returnClone) {
		Calendar result = null;
		if (c == null)
			return result;
		if (returnClone) {
			result = (Calendar) c.clone();
		} else {
			result = c;
		}
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		result.set(Calendar.MILLISECOND, 0);
		return result;
	}

	/**
	 * 返回某一天的最后一毫秒时间
	 * 
	 * @param c
	 *            带时间的日期对象
	 * @param returnClone
	 *            是否返回clone对象的处理结果。
	 * @return 返回某一天的最后一毫秒时间
	 */
	public static Calendar getEndTime(Calendar c, boolean returnClone) {
		Calendar result = clearTime(c, returnClone);
		if (result != null) {
			result.add(Calendar.DATE, 1);
			result.add(Calendar.SECOND, -1);
		}
		return result;

	}

	/**
	 * 返回最近的一个星期X
	 * 
	 * @param c
	 *            带时间的日期对象
	 * @param dayOfWeek
	 *            星期X 星期一=2 是否返回clone对象的处理结果。
	 * 
	 * @return 返回某一天的最后一毫秒时间
	 */
	public static Calendar getNextDayOfWeek(Calendar c, int dayOfWeek) {
		Calendar result = clearTime(c, true);
		if (result != null) {
			if (result.get(Calendar.DAY_OF_WEEK) < dayOfWeek) {
				result.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			} else {
				result.set(Calendar.DAY_OF_WEEK, dayOfWeek);
				result.add(Calendar.DATE, 7);
			}
		}
		return result;

	}

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Calendar c) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = clearTime(c, true);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	/***
	 * 
	 * @param c
	 *            日期
	 * @param day
	 *            天数
	 * @return 下次日期
	 */
	public static Calendar getNextDay(Calendar c, int day) {
		Calendar result = clearTime(c, true);
		if (result != null) {
			result.add(Calendar.DATE, day);
		}
		return result;
	}

	/**
	 * 取一个月以前的日期
	 * 
	 * @return
	 */
	public static Calendar get1MonthBefore() {
		Calendar result = getToday();
		result.add(Calendar.MONTH, -1);
		return result;
	}

	/**
	 * 取一个月以后的日期
	 * 
	 * @return
	 */
	public static Calendar get1Monthafter() {
		Calendar result = getToday();
		result.add(Calendar.MONTH, +1);
		return result;
	}

	/**
	 * 取给上月今天后280天的日期
	 * 
	 * @param c
	 *            给定的日期
	 */
	public static Calendar get280DayAfter() {
		Calendar result = get1MonthBefore();
		result.add(Calendar.DATE, 280);
		return result;
	}

	/**
	 * 给定没有时区的时间，加入时区
	 */
	public static Calendar getTimeZoneCalendar(Calendar c) {
		Calendar result = null;
		if (c == null)
			return result;
		if (c.getTimeZone().getID()
				.equals(Calendar.getInstance().getTimeZone().getID())) {
			result = (Calendar) c.clone();
			return result;
		} else {
			// System.out.println(c.getTimeZone().getID());
			result = (Calendar) c.clone();
			return result;
		}
	}

	/**
	 * 给定有时区的时间，除去时区
	 */
	public static Calendar getDelTimeZoneCalendar(Calendar c) {
		Calendar result = null;
		if (c == null)
			return result;
		if (c instanceof GregorianCalendar) {
			result = (Calendar) c.clone();
			// result.add(Calendar.HOUR,-8);

		}
		return result;
	}

	/**
	 * 返会两个日历间的天数
	 * 
	 * @param args
	 */
	public static int getBetweenDayOfCalendar(Calendar cBegin, Calendar cEnd) {
		if (cBegin == null || cEnd == null) {
			return 0;
		}
		int i = 0;
		Calendar tmp = (Calendar) cBegin.clone();
		if (cEnd.compareTo(tmp) >= 0) {
			while (tmp.compareTo(cEnd) <= 0) {
				i++;
				tmp.add(Calendar.DAY_OF_MONTH, 1);
			}
			i--;
		} else {
			while (tmp.compareTo(cEnd) >= 0) {
				i--;
				tmp.add(Calendar.DAY_OF_MONTH, -1);
			}
			i++;
		}
		return i;
	}

	/**
	 * 返会两个日历间的星期差
	 * 
	 * @param args
	 */
	public static int getBetweenWeekOfCalendar(Calendar cBegin, Calendar cEnd) {
		if (cBegin == null || cEnd == null) {
			return 0;
		}
		int i = 0;
		Calendar tmp = (Calendar) cBegin.clone();
		if (cEnd.compareTo(tmp) >= 0) {
			while (tmp.compareTo(cEnd) <= 0) {
				i++;
				tmp.add(Calendar.DATE, 7);
			}
			i--;
		} else {
			while (tmp.compareTo(cEnd) >= 0) {
				i--;
				tmp.add(Calendar.DATE, -7);
			}
			i++;
		}
		return i;
	}

	/**
	 * 返会两个日历间的月份差
	 * 
	 * @param args
	 */
	public static int getBetweenMonthOfCalendar(Calendar cBegin, Calendar cEnd) {
		if (cBegin == null || cEnd == null) {
			return 0;
		}
		int i = 0;
		Calendar tmp = (Calendar) cBegin.clone();
		if (cEnd.compareTo(tmp) >= 0) {
			while (tmp.compareTo(cEnd) <= 0) {
				i++;
				tmp.add(Calendar.MONTH, 1);
			}
			i--;
		} else {
			while (tmp.compareTo(cEnd) >= 0) {
				i--;
				tmp.add(Calendar.MONTH, -1);
			}
			i++;
		}
		return i;
	}

	/**
	 * 返会两个日历间的年份差
	 * 
	 * @param args
	 */
	public static int getBetweenYearOfCalendar(Calendar cBegin, Calendar cEnd) {
		if (cBegin == null || cEnd == null) {
			return 0;
		}
		int i = 0;
		Calendar tmp = (Calendar) cBegin.clone();
		if (cEnd.compareTo(tmp) >= 0) {
			while (tmp.compareTo(cEnd) <= 0) {
				i++;
				tmp.add(Calendar.YEAR, 1);
			}
			i--;
		} else {
			while (tmp.compareTo(cEnd) >= 0) {
				i--;
				tmp.add(Calendar.YEAR, -1);
			}
			i++;
		}
		return i;
	}

	public static void main(String[] args) throws ParseException {
		/*
		 * System.out
		 * .println(CalendarUtil.convertToString(Calendar.getInstance()));
		 * System.out.println(getToday().getTimeZone()); SimpleDateFormat df =
		 * new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); java.util.Date dbegin =
		 * df.parse("2006-04-14 13:31:40"); java.util.Date dend =
		 * df.parse("2006-10-15 13:31:39"); Calendar cbgin =
		 * Calendar.getInstance(); cbgin.setTime(dbegin); Calendar cend =
		 * Calendar.getInstance(); cend.setTime(dend);
		 * System.out.println(getBetweenYearOfCalendar(cbgin, cend));
		 * System.out.println(getBetweenMonthOfCalendar(cbgin, cend));
		 * System.out.println(getBetweenWeekOfCalendar(cbgin, cend));
		 */
		/*
		 * Calendar c = getNextDayOfWeek(CalendarUtil
		 * .StringToCalendar("2007-07-15"), Calendar.MONDAY);
		 * System.out.println(CalendarUtil.convertToString(c));
		 * System.out.println(CalendarUtil.getyymmdd(c));
		 * System.out.println(CalendarUtil.getTodaymmdd());
		 */System.out.println(CalendarUtil.getDate());
		// Calendar temp=CalendarUtil.StringToCalendar("2001-01-01");
		Calendar temp = Calendar.getInstance();
		System.out.println(getWeekOfDate(temp));
		System.out.println(getYyyyMmDdZh(temp));

		String result = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
		result = df.format(temp.getTime());
		if ("00:00:00".equals(result.substring(11, 19))) {
			result = result.substring(0, 10);
		}
		System.out.println("转换日期Calendar" + result);
		Calendar birthday = CalendarUtil.StringToCalendar("2001-2-10");
		// birthday =CalendarUtil.getToday();
		// birthday=Calendar.getInstance();
		System.out.println("dataTimeStr" + CalendarUtil.dataTimeStr(birthday));

		List<Integer> la = new ArrayList<Integer>();
		la.add(1);
		la.add(111);
		la.add(1123);
		Integer[] ia = { 1, 2, 3, 4 };
		System.out.println(Arrays.toString(ia));
		System.out.println(Arrays.toString(la.toArray()));
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, 1);
		System.out.println(c.getTimeInMillis());
		System.out.println(Calendar.getInstance().getTimeInMillis());
		System.out.println(CalendarUtil.getYyyyMmDd(Calendar.getInstance()));
		System.out.println(CalendarUtil.isBetween(
				CalendarUtil.StringToCalendar("2011-11-15"),
				CalendarUtil.StringToCalendar("2011-11-16"), false));
		System.out.println(CalendarUtil.StringToCalendar("2011-11-16 12-12")
				.getTime().getTime());
		System.out.println(CalendarUtil.StringToCalendar("2011-11-16 11:12:50")
				.getTime().getTime());
		System.out.println(Calendar.getInstance().after(
				CalendarUtil.StringToCalendar("2012-03-14 18:20")));

		Calendar sc = Calendar.getInstance();
		List<Calendar> hds = new ArrayList<Calendar>();
		hds.add(StringToCalendar("2012-09-03"));
		hds.add(StringToCalendar("2012-09-05"));
		hds.add(StringToCalendar("2012-09-08"));
		hds.add(StringToCalendar("2012-09-09"));
		Calendar ec = getWorkDayEndDate(sc, 4, hds);
		System.out.println(dataTimeStr(ec));
	}

	/**
	 * 
	 * @param startDate
	 *            开始时间
	 * @param day
	 *            工作日
	 * @param holidayList
	 *            节假日列表
	 * @return 办理时限
	 */
	public static final Calendar getWorkDayEndDate(Calendar startDate, int day,
			List<Calendar> holidayList) {
		if (startDate == null)
			return null;
		if (holidayList == null) {
			holidayList = new ArrayList<Calendar>();
		}
		Calendar tmp = (Calendar) startDate.clone();
		clearTime(tmp, false);
		while (day > 0) {
			tmp.add(Calendar.DATE, 1);
			if (holidayList.contains(tmp)) {
				System.out.println("hod-->" + dataTimeStr(tmp));
				continue;
			} else {
				System.out.println("workDay-->" + dataTimeStr(tmp));
				day--;
			}
		}
		tmp.add(Calendar.HOUR_OF_DAY, startDate.get(Calendar.HOUR_OF_DAY));
		return tmp;
	}

	public static final Date isoToDate(String dateString, boolean expanded)
			throws ParseException {
		SimpleDateFormat formatter;

		if (expanded) {
			formatter = new SimpleDateFormat("yyyy-MM-dd", dateFormatSymbles);
		} else {
			formatter = new SimpleDateFormat(ISO_DATE_FORMAT, dateFormatSymbles);
		}
		return new Date(formatter.parse(dateString).getTime());
	}

	public static final String dateToISO(Date date, boolean expanded) {
		SimpleDateFormat formatter;

		if (expanded) {
			formatter = new SimpleDateFormat(ISO_EXPANDED_DATE_FORMAT,
					dateFormatSymbles);
		} else {
			formatter = new SimpleDateFormat(ISO_DATE_FORMAT, dateFormatSymbles);
		}

		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

		return formatter.format(date);
	}

	public static final String dateTimeToString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	// 处理有额外假日的2个日期返回天数
	/*
	 * Calendar cFrom = Calendar.getInstance(); Calendar cTo =
	 * Calendar.getInstance(); cFrom.set(2010,9,1); //2010-09-01
	 * cTo.set(2010,10,31); //2010-10-31
	 * 
	 * //假日表 ArrayList<Date> al = new ArrayList<Date>(); Calendar c =
	 * Calendar.getInstance(); c.set(2010,9,2); al.add(c.getTime());
	 * c.set(2010,9,3); al.add(c.getTime()); c.set(2010,9,4);
	 * al.add(c.getTime()); c.set(2010,9,5); al.add(c.getTime());
	 * System.out.print("从2010-09-01 到 2010-10-31");
	 * System.out.print(" 有 "+getWorkDays(cFrom.getTime(),cTo.getTime(),al));
	 * System.out.println(" 个工作天");
	 */

	public static final int getWorkDays(Date from, Date to,
			List<Date> holidayList) {
		if (from.getTime() > to.getTime()) {
			return 0;
		}
		Calendar cFrom = Calendar.getInstance();
		cFrom.setTime(from);
		cFrom.set(cFrom.get(Calendar.YEAR), cFrom.get(Calendar.MONTH),
				cFrom.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		Date orgFromDate = cFrom.getTime();
		Calendar cTo = Calendar.getInstance();
		cTo.setTime(to);
		cTo.set(cTo.get(Calendar.YEAR), cTo.get(Calendar.MONTH),
				cTo.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		// 把起始日都修正到星期六
		int sDayofWeek = cFrom.get(Calendar.DAY_OF_WEEK);
		int workdays = 0;
		// 修正到星期六之后，再修正多出來的非假日
		cFrom.add(Calendar.DATE, -(sDayofWeek % 7));
		workdays -= ((sDayofWeek - 2) > 0) ? sDayofWeek - 2 : 0;

		// 计算2个日期之间的天数
		int totalDays = (int) ((cTo.getTimeInMillis() - cFrom.getTimeInMillis()) / (1000 * 60 * 60 * 24)) + 1;
		workdays += (totalDays / 7) * 5;

		// 修正最后剩余天数
		if ((totalDays % 7 - 2) > 0) {
			workdays += (totalDays % 7 - 2);
		}
		if (holidayList != null) {
			for (Date holiday : holidayList) {
				if (holiday.getTime() > orgFromDate.getTime()
						&& holiday.getTime() < cTo.getTimeInMillis()) {
					Calendar cHday = Calendar.getInstance();
					cHday.setTime(holiday);
					if (cHday.get(Calendar.DAY_OF_WEEK) > 1
							&& cHday.get(Calendar.DAY_OF_WEEK) < 7) {
						workdays--;
					}
				}
			}
		}
		return workdays;
	}

	// 没有额外假日 2个日期返回天数
	public static final int getWorkDays(Date from, Date to) {
		return getWorkDays(from, to, null);

	}

	// 日期加天数 week星期 days天数
	public static final Calendar getWorkingDays(Calendar c, int week, int days) {
		Calendar result = c;
		int d = week * 7;// 获取星期天数
		result.add(Calendar.DATE, d);
		int w = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		if (w == 6) {// 星期六加2天
			days = days + 2;
		}
		if (w == 0) {// 星期天加1天
			days = days + 1;
		}
		result.add(Calendar.DATE, days);
		return result;
	}

	/* 当月第一天 */
	public static Calendar firstDay() {
		Calendar calendarF = Calendar.getInstance();
		calendarF.set(Calendar.DAY_OF_MONTH,
				calendarF.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendarF;
	}

	/* 当月最好一天 */
	public static Calendar lastDay() {
		Calendar calendarE = Calendar.getInstance();
		calendarE.set(Calendar.DAY_OF_MONTH,
				calendarE.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendarE;
	}

	/**
	 * 判断当前时间是否在指定时间范围内
	 * 
	 * @param begin
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @param endTime
	 *            结束时间是否取当天最后时刻
	 * @return
	 */
	public static boolean isBetween(Calendar begin, Calendar end,
			boolean endTime) {
		Calendar c = Calendar.getInstance();
		if (begin == null && end == null) {
			return true;
		}
		if (begin == null) {
			return c.before(endTime ? CalendarUtil.getEndTime(end, true) : end);
		}
		if (end == null) {
			return c.after(begin);
		}
		return c.after(begin)
				&& c.before(endTime ? CalendarUtil.getEndTime(end, true) : end);
	}

	/**
	 * 获取两个时间之内的工作日时间（只去掉两个日期之间的周末时间，法定节假日未去掉）
	 * 
	 * @param start
	 *            -起始时间，共有3个重载方法，可以传入long型，Long型，与Date型
	 * @param end
	 *            -结束时间，共有3个重载方法，可以传入long型，Long型，与Date型
	 * @return Long型时间差对象
	 */
	public static Long getWorkdayTimeInMillis(long start, long end) {
		// 如果起始时间大于结束时间，将二者交换
		if (start > end) {
			long temp = start;
			start = end;
			end = temp;
		}
		// 根据参数获取起始时间与结束时间的日历类型对象
		Calendar sdate = Calendar.getInstance();
		Calendar edate = Calendar.getInstance();
		sdate.setTimeInMillis(start);
		edate.setTimeInMillis(end);
		// 如果两个时间在同一周并且都不是周末日期，则直接返回时间差，增加执行效率
		if (sdate.get(Calendar.YEAR) == edate.get(Calendar.YEAR)
				&& sdate.get(Calendar.WEEK_OF_YEAR) == edate
						.get(Calendar.WEEK_OF_YEAR)
				&& sdate.get(Calendar.DAY_OF_WEEK) != 1
				&& sdate.get(Calendar.DAY_OF_WEEK) != 7
				&& edate.get(Calendar.DAY_OF_WEEK) != 1
				&& edate.get(Calendar.DAY_OF_WEEK) != 7) {
			return new Long(end - start);
		}
		// 首先取得起始日期与结束日期的下个周一的日期
		Calendar snextM = getNextMonday(sdate);
		Calendar enextM = getNextMonday(edate);
		// 获取这两个周一之间的实际天数
		int days = getDaysBetween(snextM, enextM);
		// 获取这两个周一之间的工作日数(两个周一之间的天数肯定能被7整除，并且工作日数量占其中的5/7)
		int workdays = days / 7 * 5;
		// 获取开始时间的偏移量
		long scharge = 0;
		if (sdate.get(Calendar.DAY_OF_WEEK) != 1
				&& sdate.get(Calendar.DAY_OF_WEEK) != 7) {
			// 只有在开始时间为非周末的时候才计算偏移量
			scharge += (7 - sdate.get(Calendar.DAY_OF_WEEK)) * 24 * 3600000;
			scharge -= sdate.get(Calendar.HOUR_OF_DAY) * 3600000;
			scharge -= sdate.get(Calendar.MINUTE) * 60000;
			scharge -= sdate.get(Calendar.SECOND) * 1000;
			scharge -= sdate.get(Calendar.MILLISECOND);
		}
		// 获取结束时间的偏移量
		long echarge = 0;
		if (edate.get(Calendar.DAY_OF_WEEK) != 1
				&& edate.get(Calendar.DAY_OF_WEEK) != 7) {
			// 只有在结束时间为非周末的时候才计算偏移量
			echarge += (7 - edate.get(Calendar.DAY_OF_WEEK)) * 24 * 3600000;
			echarge -= edate.get(Calendar.HOUR_OF_DAY) * 3600000;
			echarge -= edate.get(Calendar.MINUTE) * 60000;
			echarge -= edate.get(Calendar.SECOND) * 1000;
			echarge -= edate.get(Calendar.MILLISECOND);
		}
		// 计算最终结果，具体为：workdays加上开始时间的时间偏移量，减去结束时间的时间偏移量
		return workdays * 24 * 3600000 + scharge - echarge;
	}

	public static Long getWorkdayTimeInMillis(Long start, Long end) {
		return getWorkdayTimeInMillis(start.longValue(), end.longValue());
	}

	public static Long getWorkdayTimeInMillis(Date start, Date end) {
		return getWorkdayTimeInMillis(start.getTime(), end.getTime());
	}

	public static Long getWorkdayTimeInMillis(String start, String end,
			String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date sdate;
		Date edate;
		try {
			sdate = sdf.parse(start);
			edate = sdf.parse(end);
			return getWorkdayTimeInMillis(sdate, edate);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Long(0);
		}
	}

	private static Calendar getNextMonday(Calendar cal) {
		int addnum = 9 - cal.get(Calendar.DAY_OF_WEEK);
		if (addnum == 8)
			addnum = 1;// 周日的情况
		cal.add(Calendar.DATE, addnum);
		return cal;
	}

	/**
	 * 获取两个日期之间的实际天数，支持跨年
	 */
	public static int getDaysBetween(Calendar start, Calendar end) {
		if (start.after(end)) {
			Calendar swap = start;
			start = end;
			end = swap;
		}
		int days = end.get(Calendar.DAY_OF_YEAR)
				- start.get(Calendar.DAY_OF_YEAR);
		int y2 = end.get(Calendar.YEAR);
		if (start.get(Calendar.YEAR) != y2) {
			start = (Calendar) start.clone();
			do {
				days += start.getActualMaximum(Calendar.DAY_OF_YEAR);
				start.add(Calendar.YEAR, 1);
			} while (start.get(Calendar.YEAR) != y2);
		}
		return days;
	}
}
