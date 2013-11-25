package app.common.utils;

import java.util.HashMap;
import java.util.Map;

public final class ClassUtils
{
	/**
	 * key: class name, value: class
	 */
	private static Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();

	private ClassUtils()
	{
	}

	public static Class<?> loadClass(String className)
			throws ClassNotFoundException
	{
		Class<?> ret = null;
		if (classMap.containsKey(className))
		{
			ret = classMap.get(className);
		}
		else
		{
			ret = Class.forName(className);
			classMap.put(className, ret);
		}
		return ret;
	}
}
