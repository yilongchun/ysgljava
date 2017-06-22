package com.vieking.sys.convert;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;

@Name("numberConvert")
@Converter(id = "number")
@BypassInterceptors
public class NumberConvert implements javax.faces.convert.Converter,
		Serializable {

	private static final long serialVersionUID = 5572282516432909965L;

	@Logger
	private Log log;

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		log.debug("NumberConvert value:" + value);
		Double result = null;
		if (value instanceof String && !value.equals("")) {
			try {
				result = Double.valueOf(value);
			} catch (NumberFormatException e) {
				throw new ConverterException("不能解析数据为Double类型.Value =" + value,
						e);
			}
		}
		return result;
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		String result = null;
		if (value instanceof Double) {
			DecimalFormat df = new DecimalFormat("#0.00"); // 保留两位小数且不用科学计数法
			result = df.format(value);
		} else {
			throw new ConverterException("不支持的值转换! Value is: " + value
					+ "\tType is: " + value.getClass());
		}
		return result;
	}
}
