package com.vieking.sys.convert;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("calendarConvert")
@Converter(id = "calendar")
@BypassInterceptors
public class CalendarConvert implements javax.faces.convert.Converter,
		Serializable {

	private static final long serialVersionUID = 5572282516432909965L;

	@Logger
	private Log log;

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		log.debug("CalendarConvert value:" + value);
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
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					Date temp = df.parse((String) value);
					result = Calendar.getInstance();
					result.setTime(temp);
				} catch (ParseException e1) {

					try {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Date temp = df.parse((String) value);
						result = Calendar.getInstance();
						result.setTime(temp);
					} catch (ParseException e2) {
						throw new ConverterException(
								"不能解析数据为Calendar类型.Value =" + value, e1);
					}
				}
			}
		}
		return result;
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		String result = null;
		if (value instanceof Calendar) {
			Calendar temp = (Calendar) ((Calendar) value).clone();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
			result = df.format(temp.getTime());
			if ("00:00:00".equals(result.substring(11, 19))) {
				return result.substring(0, 10);
			}
		} else {
			throw new ConverterException("不支持的值转换! Value is: " + value
					+ "\tType is: " + value.getClass());
		}
		return result;
	}
}
