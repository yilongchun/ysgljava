package com.vieking.sys.util;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 4012056850996044264L;

	private String title;

	private String info;

	public Message(String title, String info) {
		this.title = title;
		this.info = info;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
