package com.vieking.sys.exception;

import javax.ejb.ApplicationException;

/**
 * <p>
 * 业务处理异常 <a href="BusinessException.java.html"><i>查看源文件</i></a>
 * </p>
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@ApplicationException(rollback = true)
public class BusinessException extends KeException {

	private static final long serialVersionUID = 2505753240246022264L;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String info, String errorgroup, String errorkey,
			String message) {
		super(info, errorgroup, errorkey, message);
	}

	public BusinessException(String info, String errorgroup, String errorkey) {
		super(info, errorgroup, errorkey);
	}

	public BusinessException(String info, String message) {
		super(info, message);
	}

}
