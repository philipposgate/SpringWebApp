package app.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.json.JSONArray;

/**
 * String related helper methods.
 */
public final class StringUtils
{
	public static final boolean DEFAULT_TRIM = false;

	private static final String ENCRYPTION_KEY = "123456781234567812345678";

	private static final String ENCRYPTION_IV = "asdfasdf";

	private StringUtils()
	{
	}

	public static String limitTo255(String s)
	{
		String ret = s;
		if (ret != null)
		{
			if (ret.length() > 255)
			{
				ret = s.substring(0, 255);
			}
		}
		return ret;
	}

	public static String xmlEncode(String inString)
	{
		String lt = "<";
		String xmlLt = "&lt;";
		String gt = ">";
		String xmlGt = "&gt;";
		String amp = "&";
		String xmlAmp = "&amp;";
		String apos = "'";
		String xmlApos = "&apos;";
		String quote = "\"";
		String xmlQuote = "&quot;";

		String outString = inString;

		outString = outString.replaceAll(lt, xmlLt);
		outString = outString.replaceAll(gt, xmlGt);
		outString = outString.replaceAll(amp, xmlAmp);
		outString = outString.replaceAll(apos, xmlApos);
		outString = outString.replaceAll(quote, xmlQuote);

		return outString;
	}

	/**
	 * Extract XML values from a string based on the key name I.e.
	 * <STUDENT_NUMBER>123456789</STUDENT_NUMBER> can be extracted by specifying
	 * keyname: STUDENT_NUMBER
	 * 
	 * @param ret
	 * @param keyname
	 * @return
	 */
	public static String extractXmlValue(String ret, String keyname)
	{
		int i1 = ret.indexOf("<" + keyname + ">");
		int i2 = ret.indexOf("</" + keyname + ">");
		return (i1 > -1 && i2 > -1) ? ret.substring(i1 + keyname.length() + 2,
				i2) : "";
	}

	/**
	 * Return the first wordCount words from a text where words are separated by
	 * a space.
	 * 
	 * @param text
	 * @param wordCount
	 * @return
	 */
	public static String getWords(String text, int wordCount)
	{
		String ret = "";
		String[] words = text.split(" ");
		int c = 0;

		while (c < wordCount && c < words.length)
		{
			ret = ret + words[c] + " ";
			c++;
		}

		return ret;
	}

	/**
	 * This method gets a contrasting text color for the background color that
	 * is sent to it
	 * 
	 * @param backgroundColor
	 * @return
	 */
	public static String getTextColor(String backgroundColor)
	{
		String textColor = "";
		int redSub = Integer.valueOf(backgroundColor.substring(1, 3), 16);
		int greenSub = Integer.valueOf(backgroundColor.substring(3, 5), 16);
		int blueSub = Integer.valueOf(backgroundColor.substring(5, 7), 16);
		double totalColor = redSub * 0.299 + greenSub * 0.587 + blueSub * 0.114;

		if (totalColor > 128)
		{
			textColor = "black";
		}
		else
		{
			textColor = "white";
		}
		return textColor;
	}

	public static String fixForExcel(String text)
	{
		return (text != null ? text.replaceAll(",", " ").replaceAll("\r", "")
				.replaceAll("\n", "").replaceAll("%0A", "")
				.replaceAll("%0D", "") : "");
	}

	/**
	 * Returns TRUE if specified String is an integer, otherwise returns FALSE.
	 */
	public static boolean isInteger(String s)
	{
		if (isEmpty(s))
			return false;

		try
		{
			Integer.parseInt(s);
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}

		return true;
	}

	/**
	 * Returns TRUE if specified String is a long, otherwise returns FALSE.
	 */
	public static boolean isLong(String s)
	{
		if (isEmpty(s))
			return false;

		try
		{
			Long.parseLong(s);
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}

		return true;
	}

	/**
	 * Returns TRUE if specified String is a number, otherwise returns FALSE.
	 */
	public static boolean isNumber(String s)
	{
		try
		{
			Double.parseDouble(s);
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}

	/**
	 * Matches: 12.56 | 0.25 | 156.56 | 10 | 56
	 * 
	 * Non-Matches: -123.45 | 1.023 | 1.2
	 */
	public static boolean isCurrency(String s)
	{
		boolean valid = false;

		if (!isEmpty(s) && NumberUtils.isFloat(s)
				&& s.matches("^(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$"))
		{
			valid = true;
		}

		return valid;
	}

	/**
	 * Added single digit support
	 * 
	 * Matches: 12.56 | 0.25 | 156.56 | 10 | 56 | 1.1
	 * 
	 * Non-Matches: -123.45 | 1.023
	 */
	public static boolean isCurrencyWithSingleDigit(String s)
	{
		boolean valid = false;

		if (!isEmpty(s) && NumberUtils.isFloat(s)
				&& s.matches("^(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{1,2})?$"))
		{
			valid = true;
		}

		return valid;
	}

	public static String[] leftSplice(String[] toSplice, int startingIndex)
	{
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < toSplice.length; i++)
		{
			if (i >= startingIndex)
			{
				ret.add(toSplice[i]);
			}
		}
		return ret.toArray(new String[0]);
	}

	public static String[] rightSplice(String[] toSplice, int amtToRemove)
	{
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < toSplice.length; i++)
		{
			if (i < toSplice.length - amtToRemove)
			{
				ret.add(toSplice[i]);
			}
		}
		return ret.toArray(new String[0]);
	}

	public static Integer toInteger(String s)
	{
		if (!isInteger(s))
			return null;

		return new Integer(s);
	}

	/**
	 * Capitalize the first letter of the string, lowercase all others
	 * 
	 * @param str
	 * @return
	 */
	public static String sentenceCase(String str)
	{
		if (str == null)
			return "";

		str = str.toLowerCase();
		str = org.apache.commons.lang.StringUtils.capitalise(str);
		return str;
	}

	/**
	 * Capitalize the first letter of the string, lowercase all others
	 * 
	 * @param str
	 * @return
	 */
	public static String titleCase(String str)
	{
		String result = "";

		if (str == null || StringUtils.isEmpty(str))
			return result;

		str = str.toLowerCase();

		String[] words = str.split(" ");

		for (int i = 0; i < words.length; i++)
		{
			result += org.apache.commons.lang.StringUtils.capitalise(words[i])
					+ " ";
		}

		result = result.trim();

		String r2 = "";

		for (int i = 0; i < (result.length() - 1); i++)
		{
			r2 = r2.concat(result.charAt(i) + "");
			if (result.charAt(i) == '-')
			{
				r2 = r2.concat(org.apache.commons.lang.StringUtils
						.capitalise(result.charAt(i + 1) + ""));
				i++;
			}
		}
		r2 = r2.concat(result.charAt(result.length() - 1) + "");

		try
		{
			r2 = r2.substring(0, r2.indexOf("'") + 1)
					+ org.apache.commons.lang.StringUtils.capitalise(r2
							.charAt(r2.indexOf("'") + 1)
							+ r2.substring(r2.indexOf("'") + 2));
			if (r2.startsWith("Mc"))
			{
				r2 = "Mc"
						+ org.apache.commons.lang.StringUtils
								.capitalise(("" + r2.charAt(2)))
						+ r2.substring(3);
			}
			if (r2.startsWith("Mac"))
			{
				r2 = "Mac"
						+ org.apache.commons.lang.StringUtils
								.capitalise(("" + r2.charAt(3)))
						+ r2.substring(4);
			}
		}
		catch (RuntimeException e)
		{
		}

		return r2;
	}

	/**
	 * 
	 * @param format
	 * @param data
	 * @return formated String by filling in the placeholders within format with
	 *         the items in data[] Ex: fillInPlaceholders("[1] [0] ([2])", new
	 *         String[]{"Costa", "Nathan", "000017593"}) returns:
	 *         "Nathan Costa (000017593)"
	 */
	public static String fillInPlaceholders(String format, Object[] data)
	{
		String ret = "";

		String[] splitFormat = format.split("\\[");

		for (int i = 0; i < splitFormat.length; i++)
		{
			if (!StringUtils.isEmpty(splitFormat[i]))
			{
				if (splitFormat[i].contains("]")
						&& splitFormat[i].split("\\]").length > 0
						&& StringUtils.isNumber(splitFormat[i].split("\\]")[0])
						&& new Integer(splitFormat[i].split("\\]")[0])
								.intValue() < data.length
						&& new Integer(splitFormat[i].split("\\]")[0])
								.intValue() >= 0)
				{
					int index = new Integer(splitFormat[i].split("\\]")[0])
							.intValue();
					ret += data[index]
							+ (splitFormat[i].split("\\]").length > 1 ? splitFormat[i]
									.split("\\]")[1] : "");
				}
				else
				{
					if (i == 0 && !format.startsWith("["))
					{
						ret += splitFormat[i];
					}
					else
					{
						ret += "[" + splitFormat[i];
					}
				}
			}
			else if (i != 0)
			{
				ret += "[";
			}
		}

		return ret;
	}

	public static void main(String[] args)
	{
		System.out
				.println(StringUtils
						.fillInPlaceholders(
								"hello [ [0] - [2] - [1] - [3] - [9] - f,mhas,fkasjhnfre random :]]][[]]",
								new Object[] { "beginingAndEnd", "left",
										"right", Boolean.class }));
	}

	/**
	 * Check the string is not empty
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str)
	{
		if (str == null || str.trim().length() == 0
				|| str.trim().equalsIgnoreCase(""))
			return true;

		return false;
	}

	public static String convertToHex(byte[] data)
	{
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++)
		{
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do
			{
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String MD5(String text) throws NoSuchAlgorithmException,
			UnsupportedEncodingException
	{
		MessageDigest md;
		md = MessageDigest.getInstance("MD5");
		md.update(text.getBytes());

		return convertToHex(md.digest());
	}

	public static String setDefaultIfEmpty(String setVal, String defaultVal)
	{
		return isEmpty(setVal) ? defaultVal : setVal;
	}

	public static List getEmailList(String emails)
	{
		List ret = new ArrayList();
		if (!StringUtils.isEmpty(emails))
		{
			String[] temp = emails.replaceAll(" ", "").split(",");
			for (int i = 0; i < temp.length; i++)
			{
				String email = temp[i];
				ret.add(email);
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param toJoin
	 *            The List that will be converted to a single string
	 * @param delimiter
	 *            This will separate each element in the array within the string
	 * @return String containing all elements within the received array
	 *         separated by the received delimiter
	 */
	public static String join(List<String> toJoin, String delimiter)
	{
		String ret = "";
		for (String item : toJoin)
		{
			ret += item.toString() + delimiter;
		}

		return ret.substring(0, ret.length() - (delimiter.length()));
	}

	/**
	 * 
	 * @param toJoin
	 *            The String Array that will be converted to a single string
	 * @param delimiter
	 *            This will separate each element in the array within the string
	 * @return String containing all elements within the received array
	 *         separated by the received delimiter
	 */
	public static String join(String[] toJoin, String delimiter)
	{
		String ret = "";
		if (toJoin != null)
		{
			for (int i = 0; i < toJoin.length; i++)
			{
				ret += toJoin[i] + delimiter;
			}

			if (ret.length() != 0)
			{
				ret = ret.substring(0, ret.length() - (delimiter.length()));
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param toList
	 *            String that will be converted to a list
	 * @param delimiter
	 *            String that will determine each list item
	 * @return List that contains all elements within the received toList string
	 *         split by the received delimiter
	 */
	public static List<String> listify(String toList, String delimiter)
	{
		List<String> ret = new ArrayList<String>();
		if (toList != null)
		{
			String[] temp = toList.split(delimiter);
			for (int i = 1; i <= temp.length; i += 1)
			{
				if (temp[i - 1].trim().length() != 0)
					ret.add(temp[i - 1]);
			}
		}
		return ret;
	}

	public static List<String> listify(String[] toList)
	{
		List<String> ret = new ArrayList<String>();
		if (toList != null)
		{
			for (int i = 0; i < toList.length; i++)
			{
				ret.add(toList[i]);
			}
		}
		return ret;
	}

	public static LinkedHashMap mapify(List keys, List values)
	{
		LinkedHashMap ret = new LinkedHashMap();
		int index = 0;

		for (Object o : keys)
		{
			ret.put(o, values.get(index));
			index++;
		}

		return ret;
	}

	public static Map mapify(Object[] keys, Object[] values)
	{
		Map ret = new LinkedHashMap();

		for (int i = 0; i < keys.length; i++)
		{
			ret.put(keys[i], values[i]);
		}

		return ret;
	}

	public static JSONArray convertListToJSONArray(List list)
	{
		JSONArray array = new JSONArray();

		for (Object o : list)
		{
			array.put(o);
		}

		return array;
	}

	/**
	 * Returns a randomly generated alpha-numerical String.
	 */
	public static String getRandomString()
	{
		return ("" + Math.random()).substring(2)
				+ ("" + Math.random()).substring(2);
	}

	/**
	 * If the specified value is wrapped in double-quotes, this function will
	 * return the value without them.
	 */
	public static String trimQuotes(String value)
	{
		if (value == null)
			return value;

		value = value.trim();

		if (value.startsWith("\"") && value.endsWith("\""))
			return value.substring(1, value.length() - 1);

		return value;
	}

	/**
	 * Returns the given exception's stacktrace as a String
	 */
	public static String getStackTraceAsString(Exception e)
	{
		String ret = "";

		if (e != null)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			ret = sw.toString();
		}

		return ret;
	}

	/**
	 * Returns the current thread's stacktrace as a String
	 */
	public static String getStackTraceAsString()
	{
		StringBuilder sb = new StringBuilder();

		StackTraceElement[] elements = Thread.currentThread().getStackTrace();

		for (int i = 0; i < elements.length; i++)
		{
			sb.append(elements[i].toString()).append("\n");
		}

		return sb.toString();
	}

	public static String DES_encrypt(String s)
	{
		String result = null;

		try
		{
			DESedeKeySpec keyspec = new DESedeKeySpec(ENCRYPTION_KEY.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey key = keyFactory.generateSecret(keyspec);
			IvParameterSpec iv = new IvParameterSpec(ENCRYPTION_IV.getBytes());
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);

			result = new sun.misc.BASE64Encoder().encode(cipher.doFinal(s
					.getBytes()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

	public static String DES_decrypt(String s)
	{
		String result = null;

		try
		{
			DESedeKeySpec keyspec = new DESedeKeySpec(ENCRYPTION_KEY.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey key = keyFactory.generateSecret(keyspec);
			IvParameterSpec iv = new IvParameterSpec(ENCRYPTION_IV.getBytes());
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);

			byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(s);

			result = new String(cipher.doFinal(dec));
		}
		catch (Exception e)
		{
		}

		return result;
	}

	public static String URL_encrypt(String s)
	{
		String result = null;

		try
		{
			result = URLEncoder.encode(DES_encrypt(s), "UTF-8");
		}
		catch (Exception e)
		{
			// e.printStackTrace();
		}
		return result;
	}

	public static String URL_decrypt(String s)
	{
		String result = null;

		try
		{
			result = DES_decrypt(URLDecoder.decode(s, "UTF-8"));
		}
		catch (Exception e)
		{
			// e.printStackTrace();
		}
		return result;
	}

	public static String getRandomAlphaNumeric(int length)
	{
	    String charPool = "ABCD1E2F3GOPQ4R5S6TUVWXYZH7I8J9K0LMN";
	    
	    StringBuilder rs = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < length; i++)
	    {
	        rs.append(charPool.charAt((int)(random.nextDouble() * charPool.length())));
	    }
	    return rs.toString();
	}
}
