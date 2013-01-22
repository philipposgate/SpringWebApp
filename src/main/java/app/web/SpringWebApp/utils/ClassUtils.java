package app.web.SpringWebApp.utils;

import java.util.HashMap;
import java.util.Map;

public final class ClassUtils
{
	/**
	 * key: class name, value: class
	 */
	private static Map classMap = new HashMap();

	private ClassUtils()
	{
	}

	public static Class loadClass(String className)
			throws ClassNotFoundException
	{
		Class ret = null;
		if (classMap.containsKey(className))
		{
			ret = (Class) classMap.get(className);
		}
		else
		{
			ret = Class.forName(className);
			classMap.put(className, ret);
		}
		return ret;
	}
}
