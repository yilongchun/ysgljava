package com.vieking.sys.util;

import org.hibernate.proxy.HibernateProxy;

public class HibernateUtil {

	public static Object relObject(Object obj) {
		Object target = null;
		if (obj instanceof HibernateProxy) {
			target = ((HibernateProxy) obj).getHibernateLazyInitializer()
					.getImplementation();

		} else {
			target = obj;
		}
		return target;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isClass(Object obj, Class clazz) {
		Object o = relObject(obj);
		if (o.getClass() == clazz) {
			return true;
		} else {
			return false;
		}
	}

}
