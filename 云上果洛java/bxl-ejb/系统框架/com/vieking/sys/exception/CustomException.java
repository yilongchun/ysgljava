package com.vieking.sys.exception;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author 万俊
 * @version $Revision: $<br>
 *          $Id: $
 */
@XmlRootElement(name = com.vieking.sys.exception.ReConst.QN_CustomException)
public class CustomException implements ReConst, Serializable {

	private static final long serialVersionUID = 2186254500122122720L;

	/** 错误信息 */
	private String info;

	/** 错误分组 */
	private String errorGroup;

	/** 错误代码 */
	private String errorKey;

	/** 错误级别 */
	private int errorLevel = ReConst.ERROR_LEVEL_NORMAL;

	public CustomException() {
		super();
	}

	public CustomException(String info, String errorGroup, String errorKey) {
		super();
		this.info = info;
		this.errorGroup = errorGroup;
		this.errorKey = errorKey;
	}

	public CustomException(String info, String errorGroup, String errorKey,
			int errorLevel) {
		super();
		this.info = info;
		this.errorGroup = errorGroup;
		this.errorKey = errorKey;
		this.errorLevel = errorLevel;
	}

	@XmlAttribute(name = QN_P_ErrorGroup)
	public String getErrorGroup() {
		return errorGroup;
	}

	@XmlAttribute(name = QN_P_ErrorKey)
	public String getErrorKey() {
		return errorKey;
	}

	@XmlAttribute(name = QN_P_ErrorLevel)
	public int getErrorLevel() {
		return errorLevel;
	}

	@XmlAttribute(name = QN_P_Info)
	public String getInfo() {
		return info;
	}

	public void setErrorGroup(String errorGroup) {
		this.errorGroup = errorGroup;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public void setErrorLevel(int errorLevel) {
		this.errorLevel = errorLevel;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
