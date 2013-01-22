package app.web.SpringWebApp.utils;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

public class NumberUtils
{

	public static double getMedian(List doubles)
	{
		double ret = 0;
		if (doubles.size() > 0)
		{
			if (doubles.size() % 2 == 0)
			{
				// even
				int index = doubles.size() / 2 - 1;
				int index2 = doubles.size() / 2;
				Double val1 = (Double) doubles.get(index);
				Double val2 = (Double) doubles.get(index2);
				ret = (val1.doubleValue() + val2.doubleValue()) / 2;
			}
			else
			{
				// odd
				int index = doubles.size() / 2;
				ret = (Double) doubles.get(index);
			}
		}
		return ret;
	}

	public static float getAverage(List integers)
	{
		float ret = 0;

		for (Iterator iterator = integers.iterator(); iterator.hasNext();)
		{
			Integer i = (Integer) iterator.next();
			ret += i.intValue();
		}

		return integers.size() > 0 ? ret / integers.size() : 0;
	}

	public static Long asLong(String str)
	{
		if (StringUtils.isEmpty(str))
		{
			return null;
		}

		Long n = null;
		try
		{
			n = Long.valueOf(str);
		}
		catch (NumberFormatException e)
		{
			// leave number as null
		}
		return n;
	}

	/**
	 * Converts a String into an Integer. We should replace this with
	 * [http://jakarta
	 * .apache.org/commons/lang/api-2.0/org/apache/commons/lang/math
	 * /NumberUtils.html#createInteger(java.lang.String)] once we upgrade to
	 * Commons Lang version 2.
	 * 
	 * @param str
	 * @return Integer or null if <code>str</code> could not be converted into
	 *         an Integer.
	 */
	public static Integer asInteger(String str)
	{
		if (StringUtils.isEmpty(str))
		{
			return null;
		}

		Integer n = null;
		try
		{
			n = Integer.valueOf(str);
		}
		catch (NumberFormatException e)
		{
			// leave number as null
		}
		return n;
	}

	public static float primitive(Float f)
	{
		return (f == null ? 0 : f.floatValue());
	}

	/**
	 * Round a double value to a specified number of decimal places.
	 * 
	 * @param val
	 *            the value to be rounded.
	 * @param places
	 *            the number of decimal places to round to.
	 * @return val rounded to places decimal places.
	 */
	public static double round(double val, int places)
	{
		long factor = (long) Math.pow(10, places);

		// Shift the decimal the correct number of places
		// to the right.
		val = val * factor;

		// Round to the nearest integer.
		long tmp = Math.round(val);

		// Shift the decimal the correct number of places
		// back to the left.
		return (double) tmp / factor;
	}

	/**
	 * Round a float value to a specified number of decimal places.
	 * 
	 * @param val
	 *            the value to be rounded.
	 * @param places
	 *            the number of decimal places to round to.
	 * @return val rounded to places decimal places.
	 */
	public static float round(float val, int places)
	{
		return (float) round((double) val, places);
	}

	/**
	 * List of from-to pairs in the form of Object[]{Integer,Integer}
	 * 
	 * @param ranges
	 * @return true if from <= to for each row, and both from and to are either
	 *         zero on non-zero and there are no overlapping from-to ranges
	 *         between the rows
	 */
	public static boolean areValidRanges(List ranges)
	{
		boolean ret = true;
		for (Iterator iterator = ranges.iterator(); iterator.hasNext();)
		{
			Object[] o = (Object[]) iterator.next();
			Integer a = (Integer) o[0];
			Integer b = (Integer) o[1];
			if (a.intValue() > b.intValue()
					|| (a.intValue() == 0 && b.intValue() != 0)
					|| (b.intValue() == 0 && a.intValue() != 0))
			{
				ret = false;
				break;
			}
			else if (a.intValue() > 0 && b.intValue() > 0)
			{
				if (getNumberOfRowsIntersectingRange(ranges, a.intValue(),
						b.intValue()) > 1)
				{
					ret = false;
					break;
				}
			}
		}
		return ret;
	}

	private static int getNumberOfRowsIntersectingRange(List ranges, int from,
			int to)
	{
		int ret = 0;
		for (Iterator iterator = ranges.iterator(); iterator.hasNext();)
		{
			Object[] o = (Object[]) iterator.next();
			Integer a = (Integer) o[0];
			Integer b = (Integer) o[1];
			if ((a.intValue() <= from && b.intValue() >= from)
					|| (a.intValue() <= to && b.intValue() >= to))
			{
				ret++;
			}
		}
		return ret;
	}

	public static boolean isInteger(String input)
	{
		boolean ret = true;
		try
		{
			Integer.parseInt(input);
		}
		catch (NumberFormatException e)
		{
			ret = false;
		}
		return ret;
	}

	public static boolean isFloat(String input)
	{
		boolean ret = false;

		if (input != null)
		{
			try
			{
				Float.parseFloat(input);
				ret = true;
			}
			catch (NumberFormatException e)
			{
				ret = false;
			}
		}

		return ret;
	}

	public static Number parseDecimal(String number, String formatString)
	{
		DecimalFormat df = new DecimalFormat(formatString);
		df.setDecimalSeparatorAlwaysShown(false);
		Number ret = null;
		try
		{
			ret = df.parse(number);
		}
		catch (Exception e)
		{
			// System.out.println("error");
		}
		return ret;
	}

	public static String formatDecimal(float number, String formatString)
	{
		String ret = null;
		if (formatString != null)
		{
			DecimalFormat df = new DecimalFormat(formatString);
			ret = df.format(number);
		}
		return ret;
	}
}
