package com.vieking.sys.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("ssu")
@BypassInterceptors
@AutoCreate
public class StringSeamUtil {

	public String value(String elStr, String isNullReturn) {
		return ExpressUtil.evs(elStr, isNullReturn);
	}

	public boolean isPageUrl(String url) {
		return StringUtil.isPageUrl(url);
	}

	public List<String> toList(String str, String separator) {
		return StringUtil.toList(str, separator);
	}

	/**
	 * 返回指定数量空格
	 * 
	 * @param repeat
	 *            重复次数
	 * @return
	 */
	public String fcs(int repeat) {
		return StringUtils.repeat("　", repeat);
	}

	/**
	 * 返回重复字符串
	 * 
	 * @param repeat
	 *            重复次数
	 * @param str
	 *            字符串
	 * @return
	 */
	public String fc(int repeat, String str) {
		return StringUtils.repeat(str, repeat);
	}

	/***
	 * 
	 * @param params
	 *            要合并的字符串参数
	 * @return 符分隔的合并字符串
	 */
	public String app(String... params) {
		return StringUtil.app(params);
	}

	/***
	 * 
	 * @param separator
	 *            分隔符
	 * @param params
	 *            要合并的字符串参数
	 * @return 分隔符分隔的合并字符串
	 */
	public String appSep(String separator, String... params) {
		return StringUtil.appSep(separator, params);
	}

	/**
	 * 给定字符串及需从右侧取得的长度,取字符串右边的子串
	 * 
	 * @param s
	 *            字符串,
	 * @param pos
	 *            需取得的长度
	 * @return 子串
	 */
	public String right(String s, int pos) {
		return StringUtil.right(s, pos);

	}

	/**
	 * 给定字符串及需从右侧取得的长度,取字符串右边的子串
	 * 
	 * @param s
	 *            字符串,
	 * @param pos
	 *            需取得的长度
	 * @return 子串
	 */
	public String left(String s, int pos) {
		if (StringUtils.isEmpty(s))
			return null;
		// 反转字符串后取数据
		return s.substring(0, pos);
	}

	public String trans2RMB(String str) {
		return Trans2RMB.trans(str);
	}

	public String trans2RMB(Double dou) {
		return Trans2RMB.trans(dou);
	}

	public String getYyMmdd(Calendar cal) {
		if (cal == null)
			return "    年   月  日";
		return CalendarUtil.getYyMmdd(cal);
	}

	public String calToCnDate(Calendar cal) {
		if (cal == null)
			return "    年   月  日";
		return StringUtil.dateToCnDate(CalendarUtil.getYyyyMmDd(cal));
	}

	public String getYyMmdd() {
		return CalendarUtil.getYyMmdd(Calendar.getInstance());
	}

	public String calToStr(Calendar cal) {
		if (cal == null)
			return null;
		String result = null;
		Calendar temp = (Calendar) cal.clone();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
		result = df.format(temp.getTime());
		if ("00:00:00".equals(result.substring(11, 19))) {
			return result.substring(0, 10);
		}
		return result;
	}

	public String calToStrHHmmss(Calendar cal) {
		if (cal == null)
			return null;
		String result = null;
		Calendar temp = (Calendar) cal.clone();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// 定义格式，不显示毫秒
		result = df.format(temp.getTime());
		return result;
	}

	public String calToStrCN(Calendar cal) {
		if (cal == null)
			return null;
		String result = null;
		Calendar temp = (Calendar) cal.clone();
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");// 定义格式，不显示毫秒
		result = df.format(temp.getTime());
		if ("00时00分00秒".equals(result.substring(12, 21))) {
			return result.substring(0, 11);
		}
		return result;
	}

	/** 取到crc码补齐4位长，并作字符顺序混淆，先13和24互换，然后23互换 */
	public static String ConfusionStr(String s) {
		String crc = StringUtil.crcTable(s);
		String s1 = "0000" + crc;
		s1 = s1.substring(s1.length() - 4, s1.length());
		String[] s2 = { String.valueOf(s1.charAt(0)),
				String.valueOf(s1.charAt(1)), String.valueOf(s1.charAt(2)),
				String.valueOf(s1.charAt(3)) };
		return s2[2] + s2[0] + s2[3] + s2[1];
	}

	/** 取到crc码补齐4位长 */
	public static String crc(String s) {
		String crc = StringUtil.crcTable(s);
		String s1 = "0000" + crc;
		return s1.substring(s1.length() - 4, s1.length());
	}

	public String uRLEncoder(String str) {
		return StringUtil.uRLEncoder(str);
	}

	public String toUrlStr(Object obj) {

		return uRLEncoder(StringUtil.toStr(obj, ""));
	}

	public String toStr(Object obj) {
		return StringUtil.toStr(obj, "");
	}

	public String toStr(Object obj, String nullStr) {
		return StringUtil.toStr(obj, nullStr);
	}

}
