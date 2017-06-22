package com.vieking.seam.ext;

import static org.jboss.seam.ScopeType.SESSION;
import static org.jboss.seam.annotations.Install.APPLICATION;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;

import com.vieking.sys.enumerable.UserType;

@Name("org.jboss.seam.security.credentials")
@Scope(SESSION)
@Install(precedence = APPLICATION)
@BypassInterceptors
public class UsbKeyCredentials extends Credentials {

	private static final long serialVersionUID = 2406218390306977380L;

	@Logger
	protected Log log;
	private String keyId;

	private UserType userType = UserType.管理用户;
	private SKINTYPE skinType = SKINTYPE.classic;

	private java.lang.Integer screenWidth;

	private java.lang.Integer screenHeight;

	public java.lang.Integer getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(java.lang.Integer screenWidth) {
		this.screenWidth = screenWidth;
	}

	public java.lang.Integer getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(java.lang.Integer screenHeight) {
		this.screenHeight = screenHeight;
	}

	public SKINTYPE getSkinType() {
		return skinType;
	}

	public void setSkinType(SKINTYPE skinType) {
		this.skinType = skinType;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(String type) {
		this.userType = UserType.valueOf(type);
	}

}
