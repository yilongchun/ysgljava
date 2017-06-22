package com.vieking.sys.exception;

import java.text.MessageFormat;

import org.jboss.seam.international.Messages;

/**
 * <p>
 * 异常基类 <a href="Exception.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
public class KeException extends Exception {

	private static final long serialVersionUID = -2480984013918023675L;

	private boolean logged = false;

	private java.lang.String errorgroup;
	private java.lang.String errorkey;
	private java.lang.String info;

	public KeException(String message) {
		super(message);
		this.info = message;
		printStackTrace();
	}

	public KeException(String key, Object... arguments) {
		super(MessageFormat.format(Messages.instance().get(key), arguments));
		this.info = MessageFormat.format(Messages.instance().get(key),
				arguments);
		printStackTrace();
	}

	public KeException(String info, String message) {
		super(message);
		this.info = info;
		printStackTrace();
	}

	public KeException(String info, String errorgroup, String errorkey) {
		super();
		this.errorgroup = errorgroup;
		this.errorkey = errorkey;
		this.info = info;
		printStackTrace();
	}

	public KeException(String info, String errorgroup, String errorkey,
			String message) {
		super(message);
		this.errorgroup = errorgroup;
		this.errorkey = errorkey;
		this.info = info;
		printStackTrace();
	}

	public java.lang.String getErrorgroup() {
		return errorgroup;
	}

	public java.lang.String getErrorkey() {
		return errorkey;
	}

	public java.lang.String getInfo() {
		return info;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setErrorgroup(java.lang.String errorgroup) {
		this.errorgroup = errorgroup;
	}

	public void setErrorkey(java.lang.String errorkey) {
		this.errorkey = errorkey;
	}

	public void setInfo(java.lang.String info) {
		this.info = info;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

}
