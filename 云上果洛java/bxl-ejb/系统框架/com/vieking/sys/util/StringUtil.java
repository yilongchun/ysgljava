package com.vieking.sys.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 字符串转换 & 编码密码工具
 * 
 */
public class StringUtil {
	// ~ Static fields/initializers
	// =============================================

	private final static Logger log = Logger.getLogger(StringUtil.class);

	public static String replaceStr = "****";

	// 计算平均值
	public static Double avg(List<Double> list, boolean bl) {
		if (list.isEmpty())
			return 0.00;
		Double dd = 0.0;

		if (list.size() > 2) {
			list.remove(Collections.max(list));
			list.remove(Collections.min(list));
		}
		for (Iterator<Double> x = list.iterator(); x.hasNext();) {
			Double d = x.next();
			if (d != null) {
				dd = dd + d;
			}
		}
		if (bl) {
			dd = dd / list.size();
			/** 四舍五入保留两位小数 */
			BigDecimal b = new BigDecimal(dd);
			dd = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return dd;
	}

	/**
	 * 保留小数位 四舍五入
	 * 
	 * @param d
	 *            数值
	 * @param n
	 *            位数
	 * @return
	 * 
	 */
	public static Double dd(Double d, int n) {
		BigDecimal b = new BigDecimal(d);
		return b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 取整
	 * 
	 * @param d
	 *            数值
	 * @param bl
	 *            true为向上取整 false为向下取整
	 * @return
	 * 
	 */
	public static Integer ii(Double d, boolean bl) {
		if (bl)
			return (int) Math.ceil(d);
		else
			return (int) Math.floor(d);
	}

	/**
	 * 字符串日期转换成中文格式日期
	 * 
	 * 
	 * 
	 * @param date
	 *            字符串日期 yyyy-MM-dd
	 * @return yyyy年MM月dd日
	 * @throws Exception
	 */
	public static String dateToCnDate(String date) {
		String result = "";
		String[] cnDate = new String[] { "○", "一", "二", "三", "四", "五", "六",
				"七", "八", "九" };
		String ten = "十";
		String[] dateStr = date.split("-");
		for (int i = 0; i < dateStr.length; i++) {
			for (int j = 0; j < dateStr[i].length(); j++) {
				String charStr = dateStr[i];
				String str = String.valueOf(charStr.charAt(j));
				if (charStr.length() == 2) {
					if (charStr.equals("10")) {
						result += ten;
						break;
					} else {
						if (j == 0) {
							if (charStr.charAt(j) == '1')
								result += ten;
							else if (charStr.charAt(j) == '0')
								result += "";
							else
								result += cnDate[Integer.parseInt(str)] + ten;
						}
						if (j == 1) {
							if (charStr.charAt(j) == '0')
								result += "";
							else
								result += cnDate[Integer.parseInt(str)];
						}
					}
				} else {
					result += cnDate[Integer.parseInt(str)];
				}
			}
			if (i == 0) {
				result += "年";
				continue;
			}
			if (i == 1) {
				result += "月";
				continue;
			}
			if (i == 2) {
				result += "日";
				continue;
			}
		}
		return result;
	}

	public static void _setNvs(String xx, Object... nvs) {
		if (nvs.length == 0) {
			System.out.println("nvs is null");
		} else {
			System.out.println(nvs);
		}
	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 * 
	 * @param s
	 * @param blw
	 *            如果无小数位时补0字符
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	public static boolean contain(String str, String sub) {
		if (str == null || sub == null) {
			return false;
		}
		return str.indexOf(sub) > 0;
	}

	public static boolean isPageUrl(String path) {
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		String regex = "(\\.xhtml|\\.seam|\\.action|\\.htm|\\.do|\\.html|\\.asp|\\.aspx)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(path);
		return matcher.find();
	}

	public static boolean hasExp(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		return str.indexOf("#{") >= 0;
	}

	public static boolean isFullUrl(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		return str.indexOf("http://") == 0 || str.indexOf("https://") == 0;
	}

	public static String uRLEncoder(String str) {
		if (str == null) {
			return str;
		}
		try {
			return java.net.URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/***
	 * 
	 * @param params
	 *            要合并的字符串参数
	 * @return 符分隔的合并字符串
	 */
	public static String app(String... params) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < params.length; i++) {
			if ("*NL*".equals(params[i])) {
				sb.append("\r\n");
			} else {
				sb.append(params[i]);
			}

		}
		return sb.toString();
	}

	/***
	 * 
	 * @param separator
	 *            分隔符
	 * @param params
	 *            要合并的字符串参数
	 * @return 分隔符分隔的合并字符串
	 */
	public static String appSep(String separator, String... params) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < params.length; i++) {
			sb.append(params[i]);
			if (i < params.length - 1)
				sb.append(separator);
		}
		return sb.toString();
	}

	public static List<String> toList(String value, String separator) {
		if (StringUtils.isEmpty(value)) {
			return new ArrayList<String>();
		} else {
			return Arrays.asList(value.split(separator));
		}
	}

	public static String toStr(List<String> ls, String separator) {
		if (ls == null || ls.isEmpty()) {
			return null;
		}
		String result = "";
		for (int i = 0; i < ls.size(); i++) {
			String xx = ls.get(i);
			result = result + xx;
			if (i < ls.size() - 1) {
				if ("*NL*".equals(separator)) {
					result = result + "\r\n";
				} else {
					result = result + separator;
				}
			}
		}
		return result;
	}

	public static String lsToStr(List<Object> ls, String separator) {
		if (ls == null || ls.isEmpty()) {
			return null;
		}
		String result = "";
		for (int i = 0; i < ls.size(); i++) {
			String _str = "";
			Object obj = ls.get(i);
			if (obj == null)
				continue;
			if (obj instanceof Object[]) {
				Object[] oa = (Object[]) obj;
				_str = oa.toString();
			}
			_str = toStr(obj, "");
			result = result + _str;
			if (i < ls.size() - 1) {
				result = result + separator;
			}
		}
		return result;
	}

	public static String toStr(Double o, String fm, String nullStr) {
		DecimalFormat df = new DecimalFormat(fm);
		if (o == null) {
			return nullStr;
		} else {
			return df.format(o);
		}

	}

	public static String toStr(Object o, String nullStr) {
		if (o == null)
			return nullStr;
		if (o instanceof Double) {
			return toStr((Double) o);
		}
		if (o instanceof Calendar) {
			return toStr((Calendar) o);
		}
		if (o instanceof Boolean) {
			return toStr((Boolean) o);
		}
		if (o instanceof String) {
			return toStr((String) o);
		}
		if (o instanceof Integer) {
			return toStr((Integer) o);
		}

		return o.toString();
	}

	public static String toStr(Integer o) {
		return String.valueOf(o);
	}

	public static String toStr(Double o) {
		String result = toStr(o, "#0.000000", "---");
		result = subZeroAndDot(result);
		return result;
	}

	public static String toStr(Calendar o, String nullStr) {
		if (o == null)
			return nullStr;
		return CalendarUtil.convertToString(o);
	}

	public static String toStr(Calendar o) {
		return toStr(o, "****");
	}

	public static String toStr(Boolean o, String[] osa, String nullStr) {
		if (o == null)
			return nullStr;
		if (o) {
			return osa[0];
		} else {
			return osa[1];
		}

	}

	public static String toStr(Boolean o) {
		return toStr(o, new String[] { "真", "假" }, "****");
	}

	public static String toTrueFalse(Boolean o) {
		return toStr(o, new String[] { "true", "false" }, "****");
	}

	public static String toStr(String o) {
		return o;
	}

	// ~ Methods
	// ================================================================

	/**
	 * Encode a string using algorithm specified in web.xml and return the
	 * resulting encrypted password. If exception, the plain credentials string
	 * is returned
	 * 
	 * @param password
	 *            Password or other credentials to use in authenticating this
	 *            username
	 * @param algorithm
	 *            Algorithm used to do the digest
	 * 
	 * @return encypted password based on the algorithm.
	 */
	public static String encodePassword(String password, String algorithm) {
		byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;

		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			log.error("Exception: " + e);

			return password;
		}

		md.reset();

		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);

		// now calculate the hash
		byte[] encodedPassword = md.digest();

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}

		return buf.toString();
	}

	/**
	 * Encode a string using Base64 encoding. Used when storing passwords as
	 * cookies.
	 * 
	 * This is weak encoding in that anyone can use the decodeString routine to
	 * reverse the encoding.
	 * 
	 * @param str
	 * @return String
	 */
	public static String encodeString(String str) {
		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		try {
			return encoder.encodeBuffer(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Decode a string using Base64 encoding.
	 * 
	 * @param str
	 * @return String
	 */
	public static String decodeString(String str) {
		sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
		try {
			byte[] b = dec.decodeBuffer(str);
			return new String(b, "UTF-8");
			// return new String(dec.decodeBuffer(str));
		} catch (IOException io) {
			throw new RuntimeException(io.getMessage(), io.getCause());
		}
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
	public static String right(String s, int pos) {
		return StringUtil.right(s, pos);
	}

	/**
	 * 给定字符串及需从左侧取得的长度,从字符串左边的取子串
	 * 
	 * @param s
	 *            字符串,
	 * @param pos
	 *            需取得的长度
	 * @return 子串
	 */
	public static String left(String s, int pos) {
		// 反转字符串后取数据
		String dido = new StringBuffer(s).substring(0, pos);
		String didoleo = new String(dido);
		return didoleo;

	}

	/**
	 * 计算字符串的SHA数字摘要，以byte[]形式返回
	 */
	@SuppressWarnings("unused")
	public static byte[] MdigestSHA(String source) {
		byte[] nullreturn = { 0 };
		try {
			MessageDigest thisMD = MessageDigest.getInstance("SHA");
			byte[] digest = thisMD.digest(source.getBytes("UTF8"));
			return digest;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算byte[]的SHA数字摘要，以byte[]形式返回
	 */
	@SuppressWarnings("unused")
	public static byte[] MdigestSHA(byte[] source) {
		byte[] nullreturn = { 0 };
		try {
			MessageDigest thisMD = MessageDigest.getInstance("SHA");
			byte[] digest = thisMD.digest(source);
			return digest;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算字符串的MD5数字摘要，以byte[]形式返回
	 */
	@SuppressWarnings("unused")
	public static byte[] MdigestMD5(String source) {
		byte[] nullreturn = { 0 };
		try {
			MessageDigest thisMD = MessageDigest.getInstance("MD5");
			byte[] digest = thisMD.digest(source.getBytes("UTF8"));
			return digest;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算byte[]的数MD5字摘要，以byte[]形式返回
	 */
	@SuppressWarnings("unused")
	public static byte[] MdigestMD5(byte[] source) {
		byte[] nullreturn = { 0 };
		try {
			MessageDigest thisMD = MessageDigest.getInstance("MD5");
			byte[] digest = thisMD.digest(source);
			return digest;
		} catch (Exception e) {
			return null;
		}
	}

	public static String makePassword(String givepasswd) {
		if (StringUtils.isEmpty(givepasswd))
			return "";
		byte[] t1 = MdigestMD5(givepasswd);
		sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder();
		String tempPassword = baseEncoder.encode(t1);
		// for (int i = 0; i < t1.length; i++) {
		// tempPassword += Integer.toString(Math.abs(t1[i]));
		// }
		return tempPassword;
	}

	/**
	 * 
	 * 将字符串List使用指定的分隔符合并成一个字符串。
	 * 
	 * @param list
	 *            List
	 * 
	 * @param delim
	 *            String
	 * 
	 * @return String
	 * 
	 */

	@SuppressWarnings("rawtypes")
	public static String combineList(List list, String delim) {
		if (list == null || list.size() == 0) {
			return "";
		} else {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < list.size() - 1; i++) {
				result.append(list.get(i));
				result.append(delim);
			}
			result.append(list.get(list.size() - 1));
			return result.toString();
		}
	}

	public String buildLikePattern(String search) {
		if (search == null || search.trim().length() == 0) {
			return "%";
		} else {
			return "%".concat(search.replace('*', '%').toLowerCase()).concat(
					"%");
		}
	}

	public static String toString(Object[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "empty";
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < a.length; i++) {
			if (i == 0)
				buf.append("");
			else
				buf.append(", ");

			buf.append(String.valueOf(a[i]));
		}
		return buf.toString();
	}

	/** CRC16循环冗余校验码对照表 */
	public static String crcTable(String s) {
		int[] table = { 0x0000, 0x1189, 0x2312, 0x329b, 0x4624, 0x57ad, 0x6536,
				0x74bf, 0x8c48, 0x9dc1, 0xaf5a, 0xbed3, 0xca6c, 0xdbe5, 0xe97e,
				0xf8f7, 0x1081, 0x0108, 0x3393, 0x221a, 0x56a5, 0x472c, 0x75b7,
				0x643e, 0x9cc9, 0x8d40, 0xbfdb, 0xae52, 0xdaed, 0xcb64, 0xf9ff,
				0xe876, 0x2102, 0x308b, 0x0210, 0x1399, 0x6726, 0x76af, 0x4434,
				0x55bd, 0xad4a, 0xbcc3, 0x8e58, 0x9fd1, 0xeb6e, 0xfae7, 0xc87c,
				0xd9f5, 0x3183, 0x200a, 0x1291, 0x0318, 0x77a7, 0x662e, 0x54b5,
				0x453c, 0xbdcb, 0xac42, 0x9ed9, 0x8f50, 0xfbef, 0xea66, 0xd8fd,
				0xc974, 0x4204, 0x538d, 0x6116, 0x709f, 0x0420, 0x15a9, 0x2732,
				0x36bb, 0xce4c, 0xdfc5, 0xed5e, 0xfcd7, 0x8868, 0x99e1, 0xab7a,
				0xbaf3, 0x5285, 0x430c, 0x7197, 0x601e, 0x14a1, 0x0528, 0x37b3,
				0x263a, 0xdecd, 0xcf44, 0xfddf, 0xec56, 0x98e9, 0x8960, 0xbbfb,
				0xaa72, 0x6306, 0x728f, 0x4014, 0x519d, 0x2522, 0x34ab, 0x0630,
				0x17b9, 0xef4e, 0xfec7, 0xcc5c, 0xddd5, 0xa96a, 0xb8e3, 0x8a78,
				0x9bf1, 0x7387, 0x620e, 0x5095, 0x411c, 0x35a3, 0x242a, 0x16b1,
				0x0738, 0xffcf, 0xee46, 0xdcdd, 0xcd54, 0xb9eb, 0xa862, 0x9af9,
				0x8b70, 0x8408, 0x9581, 0xa71a, 0xb693, 0xc22c, 0xd3a5, 0xe13e,
				0xf0b7, 0x0840, 0x19c9, 0x2b52, 0x3adb, 0x4e64, 0x5fed, 0x6d76,
				0x7cff, 0x9489, 0x8500, 0xb79b, 0xa612, 0xd2ad, 0xc324, 0xf1bf,
				0xe036, 0x18c1, 0x0948, 0x3bd3, 0x2a5a, 0x5ee5, 0x4f6c, 0x7df7,
				0x6c7e, 0xa50a, 0xb483, 0x8618, 0x9791, 0xe32e, 0xf2a7, 0xc03c,
				0xd1b5, 0x2942, 0x38cb, 0x0a50, 0x1bd9, 0x6f66, 0x7eef, 0x4c74,
				0x5dfd, 0xb58b, 0xa402, 0x9699, 0x8710, 0xf3af, 0xe226, 0xd0bd,
				0xc134, 0x39c3, 0x284a, 0x1ad1, 0x0b58, 0x7fe7, 0x6e6e, 0x5cf5,
				0x4d7c, 0xc60c, 0xd785, 0xe51e, 0xf497, 0x8028, 0x91a1, 0xa33a,
				0xb2b3, 0x4a44, 0x5bcd, 0x6956, 0x78df, 0x0c60, 0x1de9, 0x2f72,
				0x3efb, 0xd68d, 0xc704, 0xf59f, 0xe416, 0x90a9, 0x8120, 0xb3bb,
				0xa232, 0x5ac5, 0x4b4c, 0x79d7, 0x685e, 0x1ce1, 0x0d68, 0x3ff3,
				0x2e7a, 0xe70e, 0xf687, 0xc41c, 0xd595, 0xa12a, 0xb0a3, 0x8238,
				0x93b1, 0x6b46, 0x7acf, 0x4854, 0x59dd, 0x2d62, 0x3ceb, 0x0e70,
				0x1ff9, 0xf78f, 0xe606, 0xd49d, 0xc514, 0xb1ab, 0xa022, 0x92b9,
				0x8330, 0x7bc7, 0x6a4e, 0x58d5, 0x495c, 0x3de3, 0x2c6a, 0x1ef1,
				0x0f78 };

		int crc = 0;
		for (int i = 0; i < s.length(); i++) {
			int asc = s.charAt(i);
			/*
			 * System.out.println(i + 1 + " ccc1: " + (crc >> 8));
			 * System.out.println(i + 1 + " ccc2: " + ((byte) (crc >> 8) &
			 * 0xff)); System.out.println(i + 1 + " ccc3: " + (asc & 0xff));
			 * System.out.println(i + 1 + " table: " + (table[((byte) (crc >> 8)
			 * & 0xff) ^ (asc & 0xff)])); System.out.println("-----------");
			 */
			crc = (crc >> 8) ^ table[((byte) (crc >> 8) & 0xff) ^ (asc & 0xff)];
		}
		return Integer.toHexString(crc);
	}

	public static String getReplaceStr() {
		return replaceStr;
	}

	public static void setReplaceStr(String replaceStr) {
		StringUtil.replaceStr = replaceStr;
	}

}
