package com.vieking.resource.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CalendarAdapter extends XmlAdapter<String, Calendar> {

	@Override
	public String marshal(Calendar c) throws Exception {
		String result = null;
		if (c instanceof Calendar) {
			Calendar temp = (Calendar) c.clone();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result = df.format(temp.getTime());
			if ("00:00:00".equals(result.substring(11, 19))){
				return result.substring(0, 10);
			}
		}
		return result;
	}

	@Override
	public Calendar unmarshal(String value) throws Exception {
		if (value == null) {
			return null;
		}
		Calendar result = null;
		if (value instanceof String && !value.equals("")) {
			try {
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date temp = df.parse((String) value);
				result = Calendar.getInstance();
				result.setTime(temp);
			} catch (ParseException e) {
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date temp = df.parse((String) value);
					result = Calendar.getInstance();
					result.setTime(temp);
				} catch (ParseException e1) {
					throw new Exception("不能解析数据为Calendar类型.", e1);

				}
			}
		}
		return result;
	}
}
