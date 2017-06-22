package com.vieking.sys.enumerable;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据状态 <br>
 * *
 * 
 * <p>
 * <a href="DataState.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 * 
 */
public enum DataState {
	正常("100", ""), 停用("200", "停用"), 作废("210", "作废"), 注销("220", "注销"), 锁定("230",
			"锁定"), 冻结("240", "冻结");

	private final String desc;

	private final String code;

	public boolean readOnly() {
		if ("1".equals(code.substring(0, 1))) {
			return false;
		} else {
			return true;
		}
	}

	public boolean canModify() {
		return !readOnly();
	}

	public static List<DataState> zfls() {
		List<DataState> ls = new ArrayList<DataState>();
		ls.add(DataState.正常);
		ls.add(DataState.作废);
		return ls;
	}

	/***
	 * 正常、停用
	 * 
	 * @return
	 */
	public static List<DataState> nsls() {
		List<DataState> ls = new ArrayList<DataState>();
		ls.add(DataState.正常);
		ls.add(DataState.停用);
		return ls;
	}

	public boolean canDelete() {
		if ("1".equals(code.substring(0, 1))) {
			return true;
		} else {
			return false;
		}
	}

	private DataState(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public String getCode() {
		return code;
	}

}
