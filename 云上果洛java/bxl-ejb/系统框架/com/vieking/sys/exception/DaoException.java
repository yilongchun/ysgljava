package com.vieking.sys.exception;

import java.text.MessageFormat;

import javax.ejb.ApplicationException;

import org.jboss.seam.international.Messages;

/**
 * <p>
 * DAO异常 <a href="DaoException.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@ApplicationException(rollback = true)
public class DaoException extends KeException {

	private static final long serialVersionUID = 4450672143709502449L;

	private static boolean serverSide = false;

	public static DaoException instance(String msg, Object... arguments) {
		msg = MessageFormat.format(Messages.instance().get(msg), arguments);
		return new DaoException(msg, msg);
	}

	public static boolean isServerSide() {
		return serverSide;
	}

	public static void setServerSide(boolean serverSide) {
		DaoException.serverSide = serverSide;
	}

	public java.lang.Throwable detail;

	public java.lang.Object params[];

	public DaoException(String message) {
		super(message);
	}

	public DaoException(String info, String message) {
		super(info, message);
	}

	public DaoException(String info, String errorgroup, String errorkey) {
		super(info, errorgroup, errorkey);

	}

	public DaoException(String info, String errorgroup, String errorkey,
			String message) {
		super(info, errorgroup, errorkey, message);
	}

	public java.lang.Throwable getDetail() {
		return detail;
	}

	public java.lang.Object[] getParams() {
		return params;
	}

	public void setDetail(java.lang.Throwable detail) {
		this.detail = detail;
	}

	public void setParams(java.lang.Object[] params) {
		this.params = params;
	}

}
