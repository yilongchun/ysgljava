package com.vieking.sys.enumerable;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据类型 <br>
 * *
 * 
 * <p>
 * <a href="DataType.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 * 
 */
public enum DataType {
	DT001("浮点型"), DT002("整形(Integer)"), DT003("字符串"), DT004("日期(Calenar)"), DT005(
			"lob"), DT006("布尔型"), DT007("日期(Date)"), DT008("长整形(Long)"), DT100(
			"单选"), DT110("多选"), DT200("子文档"), DT800("对象"), DT900("实体");

	private final String des;

	private DataType(String des) {
		this.des = des;
	}

	public String getDes() {
		return des;
	}

	public static List<DataType> wdylx() {
		List<DataType> ls = new ArrayList<DataType>();
		ls.add(DataType.DT001);
		ls.add(DataType.DT002);
		ls.add(DataType.DT003);
		ls.add(DataType.DT004);
		ls.add(DataType.DT005);
		ls.add(DataType.DT006);
		return ls;
	}

	public static List<DataType> qpdlx() {
		List<DataType> ls = new ArrayList<DataType>();
		ls.add(DataType.DT001);
		ls.add(DataType.DT002);
		ls.add(DataType.DT003);
		ls.add(DataType.DT004);
		ls.add(DataType.DT006);
		ls.add(DataType.DT007);
		ls.add(DataType.DT008);
		ls.add(DataType.DT100);
		ls.add(DataType.DT110);
		ls.add(DataType.DT800);
		return ls;
	}

	public static List<DataType> bgylx() {
		List<DataType> ls = new ArrayList<DataType>();
		ls.add(DataType.DT001);
		ls.add(DataType.DT002);
		ls.add(DataType.DT003);
		ls.add(DataType.DT004);
		ls.add(DataType.DT005);
		ls.add(DataType.DT006);
		ls.add(DataType.DT900);
		return ls;
	}

	public static List<DataType> jzfwlx() {
		List<DataType> ls = new ArrayList<DataType>();
		ls.add(DataType.DT003);
		return ls;
	}

}
