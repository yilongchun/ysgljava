package com.vieking.sys.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookiesUtil {

	public static String getCookies(String name) {
		FacesContext facesContext = javax.faces.context.FacesContext
				.getCurrentInstance();
		if (facesContext == null)
			return null;
		Cookie[] cookies = ((HttpServletRequest) facesContext
				.getExternalContext().getRequest()).getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				String cn = cookies[i].getName();
				if (cn.equals(name)) {

					return cookies[i].getValue();
				}
			}
		}
		return null;
	}

	public static void setCookie(String k, String v) {
		FacesContext facesContext = javax.faces.context.FacesContext
				.getCurrentInstance();
		if (facesContext == null)
			return;
		Cookie cookie = new Cookie(k, v);
		cookie.setMaxAge(31536000);
		cookie.setPath("/");
		((HttpServletResponse) facesContext.getExternalContext().getResponse())
				.addCookie(cookie);
	}

}
