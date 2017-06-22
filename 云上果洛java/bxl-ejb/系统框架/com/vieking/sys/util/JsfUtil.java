package com.vieking.sys.util;

import com.vieking.sys.exception.KeException;

public class JsfUtil {
	public static void setPopMsg(Message message) {
		FacesUtil.getExternalContext().getRequestMap().put("_pop", message);
	}

	public static void clearPopMsg() {
		FacesUtil.getExternalContext().getRequestMap().remove("_pop");
	}

	public static void setPopMsg(KeException kfe) {
		String title = kfe.getErrorgroup() != null ? "出错提示！" : "出错提示【"
				+ kfe.getErrorgroup() + "】！";
		FacesUtil.getExternalContext().getRequestMap()
				.put("_pop", new Message(title, kfe.getInfo()));
	}
}
