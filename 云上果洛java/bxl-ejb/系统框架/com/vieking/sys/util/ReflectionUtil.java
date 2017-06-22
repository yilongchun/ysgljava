package com.vieking.sys.util;

import java.lang.reflect.Method;

public class ReflectionUtil {
	/**
	 * 利用递归找一个类的指定方法，如果找不到，去父亲里面找直到最上层Object对象为止。
	 * 
	 * @param clazz
	 *            目标类
	 * @param methodName
	 *            方法名
	 * @param classes
	 *            方法参数类型数组
	 * @return 方法对象
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Method getMethod(Class clazz, String methodName,
			final Class[] classes) throws Exception {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName, classes);
		} catch (NoSuchMethodException e) {
			try {
				method = clazz.getMethod(methodName, classes);
			} catch (NoSuchMethodException ex) {
				if (clazz.getSuperclass() == null) {
					return method;
				} else {
					method = getMethod(clazz.getSuperclass(), methodName,
							classes);
				}
			}
		}
		return method;
	}

	/**
	 * 
	 * @param obj
	 *            调整方法的对象
	 * @param methodName
	 *            方法名
	 * @param classes
	 *            参数类型数组
	 * @param objects
	 *            参数数组
	 * @return 方法的返回值
	 */
	@SuppressWarnings("rawtypes")
	public static Object invoke(final Object obj, final String methodName,
			final Class[] classes, final Object[] objects) {
		try {
			Method method = getMethod(obj.getClass(), methodName, classes);
			method.setAccessible(true);// 调用private方法的关键一句话
			return method.invoke(obj, objects);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Object invoke(final Object obj, final String methodName,
			final Class[] classes) {
		return invoke(obj, methodName, classes, new Object[] {});
	}

	public static Object invoke(final Object obj, final String methodName) {
		return invoke(obj, methodName, new Class[] {}, new Object[] {});
	}

	@SuppressWarnings("rawtypes")
	public static Object invokeStatic(Class clazz, final String methodName,
			final Class[] classes) {
		try {
			Method method = getMethod(clazz, methodName, classes);
			return method.invoke(clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Object invokeStatic(Class clazz, final String methodName) {
		try {
			Method method = getMethod(clazz, methodName, new Class[] {});
			return method.invoke(clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
