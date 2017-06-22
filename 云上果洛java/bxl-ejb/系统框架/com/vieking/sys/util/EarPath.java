package com.vieking.sys.util;

import java.io.File;

import javax.servlet.ServletContext;

import org.jboss.seam.contexts.ServletLifecycle;

public class EarPath {
	private static String ear_path;
	private static String ejb_path;
	private static String war_class_path;

	public static final String downlowds = "downloads/";

	/**
	 * @return /${JBOSS_HOME}/downloads/
	 */
	public static String downloads() {
		return jboss_home() + downlowds;
	}

	final static String user_temp = "temp/";

	/**
	 * @return /${JBOSS_HOME}/downloads/temp/
	 */
	public static String user_temp() {
		String result = downloads() + user_temp;
		return result;
	}

	/**
	 * @return /${JBOSS_HOME}/
	 */
	public static String jboss_home() {
		String jboss_path = ear_home();
		int dirLevel = 2;
		for (int i = 0; i < dirLevel; i++) {
			File file = new File(jboss_path);
			jboss_path = file.getParent();
		}
		jboss_path = jboss_path + "/";
		return jboss_path;
	}

	public static String ejb_home() {
		if (ejb_path == null) {
			ejb_path = EarPath.class.getResource("/.").getPath();
			System.out.println("ejb_Home=" + ejb_path);
			File file = new File(ejb_path);
			ejb_path = file.getPath();
			System.out.println("ejb_Home2=" + ejb_path);
		}
		return ejb_path;
	}

	public static String ear_home() {
		if (ear_path == null) {

			File file = new File(view_home());
			ear_path = file.getParent();
		}
		return ear_path;
	}

	public static String view_home() {
		if (war_class_path == null) {
			final ServletContext servletContext = ServletLifecycle
					.getServletContext();

			if (servletContext != null) {
				war_class_path = ServletLifecycle.getServletContext()
						.getRealPath("/");
			} else {
				throw new IllegalStateException("war_class路径获取错误");
			}
		}
		return war_class_path;
	}

	public static void main(String[] args) {
		System.out.println(EarPath.ear_home());
		System.out.println(EarPath.ejb_home());
		System.out.println(EarPath.jboss_home());
	}
}
