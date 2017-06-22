package com.vieking.sys.enumerable;

/**
 * 信息登记审核状态<br>
 * *
 * 
 * <p>
 * <a href="RegistrationState.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 * 
 */
public enum RegistrationState {

	填报中("填报中", 20), 待审核("待审核", 30), 已审核("已审核", 40);

	private final String desc;

	private final int state;

	public boolean readOnly() {
		if (state == 30  || state == 40 ) {
			return true;
		} else {
			return false;
		}
	}

	public static RegistrationState getByState(int state) {
		for (int i = 0; i < RegistrationState.values().length; i++) {
			if (state == (RegistrationState.values()[i].state)) {
				return RegistrationState.values()[i];
			}
		}
		return null;
	}

	private RegistrationState(String desc, int state) {
		this.state = state;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public int getState() {
		return state;
	}

}
