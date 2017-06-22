package com.vieking.sys.util;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;

public class ExpressUtil {

	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(ExpressUtil.class);

	@SuppressWarnings("rawtypes")
	public static Object elValue(String elStr) {
		elStr = StringUtils.trim(elStr);
		Object value = null;
		try {
			ValueExpression el = Expressions.instance().createValueExpression(
					elStr);
			value = el.getValue();
		} catch (Exception e) {
			e.printStackTrace();
			value = null;
		}
		return value;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object elValue(String elStr, String elVarName, Object obj) {
		elStr = StringUtils.trim(elStr);
		Object value = null;
		try {
			if (!StringUtils.isEmpty(elVarName)) {
				ValueExpression elRowData = Expressions.instance()
						.createValueExpression("#{" + elVarName + "}");
				elRowData.setValue(obj);
			}
			ValueExpression el = Expressions.instance().createValueExpression(
					elStr);
			value = el.getValue();
		} catch (Exception e) {
			e.printStackTrace();
			value = null;
		}
		return value;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object elValue(String elStr, Map<String, Object> elVars) {
		elStr = StringUtils.trim(elStr);
		Object value = null;
		try {
			for (Iterator iterator = elVars.keySet().iterator(); iterator
					.hasNext();) {
				String varName = (String) iterator.next();
				if (!StringUtils.isEmpty(varName)) {
					ValueExpression ve = Expressions.instance()
							.createValueExpression("#{" + varName + "}");
					ve.setValue(elVars.get(varName));
				}
			}
			ValueExpression el = Expressions.instance().createValueExpression(
					elStr);
			value = el.getValue();
			// System.out.println("value class--->"+
			// value.getClass().getName());
			elVars.clear();
			elVars = null;
		} catch (Exception e) {
			e.printStackTrace();
			value = null;
			elVars.clear();
			elVars = null;
		}
		return value;
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return Short 结果
	 */
	public static Short evShort(String elStr, Short isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr);
		if (_v != null && _v instanceof Short) {
			return (Short) _v;
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param elVars
	 *            表达式变量Map
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return Short 计算结果
	 */
	public static Short evShort(String elStr, Map<String, Object> elVars,
			Short isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr, elVars);
		if (_v != null && _v instanceof Short) {
			return (Short) _v;
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return Integer 结果
	 */
	public static Integer evInteger(String elStr, Integer isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr);
		if (_v != null && _v instanceof String) {
			String _sv = (String) _v;
			try {
				return Integer.valueOf(_sv);
			} catch (NumberFormatException e) {
				return isNullReturn;
			}
		}
		if (_v != null && (_v instanceof Integer || _v instanceof Long)) {
			return Integer.valueOf(_v.toString());
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param elVars
	 *            表达式变量Map
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return Integer 计算结果
	 */
	public static Integer evInteger(String elStr, Map<String, Object> elVars,
			Integer isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr, elVars);
		if (_v != null && _v instanceof Short) {
			return (Integer) _v;
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return Double 结果
	 */
	public static Double evDouble(String elStr, Double isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr);
		if (_v != null && _v instanceof Double) {
			return (Double) _v;
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param elVars
	 *            表达式变量Map
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return Double 计算结果
	 */
	public static Double evInteger(String elStr, Map<String, Object> elVars,
			Double isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr, elVars);
		if (_v != null && _v instanceof Double) {
			return (Double) _v;
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return Boolean 结果
	 */
	public static Boolean evb(String elStr, boolean isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr);
		if (_v != null && _v instanceof Boolean) {
			return (Boolean) _v;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param elVarName
	 *            表达式变量名称
	 * @param obj
	 *            变量值
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return Boolean 计算结果
	 */
	public static Boolean evb(String elStr, String elVarName, Object obj,
			boolean isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr, elVarName, obj);
		if (_v instanceof Boolean) {
			return (Boolean) _v;
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param elVars
	 *            表达式变量Map
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return Boolean 计算结果
	 */
	public static Boolean evb(String elStr, Map<String, Object> elVars,
			boolean isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr, elVars);
		if (_v != null && _v instanceof Boolean) {
			return (Boolean) _v;
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return String 结果
	 */
	public static String evs(String elStr, String isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr);
		if (_v instanceof String) {
			return String.valueOf(_v);
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param elVarName
	 *            表达式变量名称
	 * @param obj
	 *            变量值
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return String 计算结果
	 */
	public static String evs(String elStr, String elVarName, Object obj,
			String isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr, elVarName, obj);
		if (_v instanceof String) {
			return String.valueOf(_v);
		} else {
			return isNullReturn;
		}
	}

	/**
	 * 
	 * @param elStr
	 *            表达式字符串
	 * @param elVarName
	 *            表达式变量Map
	 * @param isNullReturn
	 *            el为Null 时返回值
	 * @return String 计算结果
	 */
	public static String evs(String elStr, Map<String, Object> elVars,
			String isNullReturn) {
		if (StringUtils.isEmpty(elStr)) {
			return isNullReturn;
		}
		Object _v = elValue(elStr, elVars);
		if (_v != null && _v instanceof String) {
			return String.valueOf(_v);
		} else {
			return isNullReturn;
		}
	}

}
