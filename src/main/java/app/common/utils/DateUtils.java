package app.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Date related methods
 */
public final class DateUtils
{
	/**
	 * The default date format for the "Full Calendar" widget
	 */
	public static final String FULL_CALENDAR_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss z";

	public static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String FULL_CALENDAR_WORD_FORMAT = "MMM-dd-yyyy h:mm a";

	private static final long ONE_HOUR = 60 * 60 * 1000L;

	private static DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

	private static DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private static int daysInMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };

	/** The number of milliseconds in 24 hours. */
	public static long TWENTY_FOUR_HOURS_MS = 1000 * 60 * 60 * 24;

	private DateUtils()
	{
		// this class is non-instantiable
	}

	/**
	 * 
	 * @param businessDaysInAdvance
	 *            (Can be +ve or -ve value, Depending on days you wish to Add to
	 *            Subtract)
	 * @return: date
	 */
	public static Date getBusinessDaysFromDate(Date date,
			int businessDaysInAdvance)
	{
		while (businessDaysInAdvance > 0)
		{
			date = DateUtils.addDays(date, 1);
			if (DateUtils.isWeekday(date))
			{
				businessDaysInAdvance--;
			}
		}

		while (businessDaysInAdvance < 0)
		{
			date = DateUtils.subtractDays(date, 1);
			if (DateUtils.isWeekday(date))
			{
				businessDaysInAdvance++;
			}
		}
		return date;
	}

	/**
	 * Returns an int that's equivalent to Calendar.DAY_OF_WEEK for the
	 * corresponding user-specified 'dayName'
	 * 
	 * @param dayName
	 * @return
	 */
	public static int getCalenderIndexForDayOfWeek(String dayName)
	{
		int dayIndex = -999;

		if (dayName.equalsIgnoreCase("sunday"))
			dayIndex = Calendar.SUNDAY;
		else if (dayName.equalsIgnoreCase("monday"))
			dayIndex = Calendar.MONDAY;
		else if (dayName.equalsIgnoreCase("tuesday"))
			dayIndex = Calendar.TUESDAY;
		else if (dayName.equalsIgnoreCase("wednesday"))
			dayIndex = Calendar.WEDNESDAY;
		else if (dayName.equalsIgnoreCase("thursday"))
			dayIndex = Calendar.THURSDAY;
		else if (dayName.equalsIgnoreCase("friday"))
			dayIndex = Calendar.FRIDAY;
		else if (dayName.equalsIgnoreCase("saturday"))
			dayIndex = Calendar.SATURDAY;

		if (dayIndex == -999)
		{
			throw new RuntimeException(
					"DateUtils.getCalenderIndexForDayOfWeek(): bad day name: "
							+ dayName);
		}

		return dayIndex;
	}

	/**
	 * Add days to a Date
	 * 
	 * @param date
	 *            - date
	 * @param days
	 *            - number
	 * @return the new Date
	 */
	public static Date addDays(Date date, int days)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * Add months to a Date
	 * 
	 * @param date
	 *            - date
	 * @param months
	 *            - number
	 * @return the new Date
	 */
	public static Date addMonth(Date date, int months)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}

	/**
	 * Add minutes to a Date
	 * 
	 * @param date
	 *            - date
	 * @param minute
	 *            - number
	 * @return the new Date
	 */
	public static Date addMinute(Date date, int minutes)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}

	/**
	 * Add hours to a Date
	 * 
	 * @param date
	 *            - date
	 * @param hour
	 *            - number
	 * @return the new Date
	 */
	public static Date addHours(Date date, int hours)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hours);
		return calendar.getTime();
	}

	/**
	 * Subtract months from a Date
	 * 
	 * @param date
	 *            - date
	 * @param months
	 *            - number
	 * @return the new Date
	 */
	public static Date subtractMonth(Date date, int months)
	{
		return addMonth(date, -months);
	}

	/**
	 * Remove hours from a Date
	 * 
	 * @param date
	 *            - date
	 * @param hours
	 *            - number
	 * @return the new Date
	 */
	public static Date subtractHours(Date date, int hours)
	{
		return addHours(date, -hours);
	}

	public static Date subtractMinutes(Date date, int minutes)
	{
		return addMinutes(date, -minutes);
	}

	private static Date addMinutes(Date date, int minutes)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}

	/**
	 * Remove days from a Date
	 * 
	 * @param date
	 *            - date
	 * @param days
	 *            - number
	 * @return the new Date
	 */
	public static Date subtractDays(Date date, int days)
	{
		return addDays(date, -days);
	}

	/**
	 * Add years to a Date
	 * 
	 * @param date
	 *            - date
	 * @param years
	 *            - number
	 * @return the new Date
	 */
	public static Date addYears(Date date, int years)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, years);
		return calendar.getTime();
	}

	/**
	 * Subtract years to a Date
	 * 
	 * @param date
	 *            - date
	 * @param years
	 *            - number
	 * @return the new Date
	 */
	public static Date subtractYears(Date date, int years)
	{
		return addYears(date, -years);
	}

	/**
	 * Returns true if date1 is before date2
	 * 
	 * @param date1
	 * @param date2
	 * @return a boolean
	 */
	public static boolean isBefore(Date date1, Date date2)
	{
		return date1 != null && date2 != null
				&& date1.getTime() < date2.getTime();
	}

	/**
	 * Returns true if date1 is before or equals date2
	 * 
	 * @param date1
	 * @param date2
	 * @return a boolean
	 */
	public static boolean isBeforeOrEquals(Date date1, Date date2)
	{
		return date1 != null && date2 != null
				&& date1.getTime() <= date2.getTime();
	}

	/**
	 * Returns true if date1 is before or same as date2
	 * 
	 * @param date1
	 * @param date2
	 * @return a boolean
	 */
	public static boolean isBeforeOrSame(Date date1, Date date2)
	{
		return date1.getTime() <= date2.getTime();
	}

	/**
	 * Returns today's starting date (which is today at 00:00:00)
	 * 
	 * @return
	 */
	public static Date todayStarts()
	{
		return getStartDate(new Date());
	}

	/**
	 * Returns today's ending date (which is tonight at 23:59:59)
	 * 
	 * @return
	 */
	public static Date todayEnds()
	{
		return getEndDate(new Date());
	}

	/**
	 * Returns the first instant of last monday this week.
	 * 
	 * @return
	 */
	public static Date thisWeekStarts()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date thisWeekendStarts()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Returns the last second of this week.
	 * 
	 * @return
	 */
	public static Date thisWeekEnds()
	{
		Date weekStarts = DateUtils.thisWeekStarts();
		Date ret = DateUtils.addDays(weekStarts, 6);
		ret = DateUtils.getEndDate(ret);
		return ret;
	}

	public static Date yearStarts()
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}

	/**
	 * Returns this month's starting date (which is 1st day of this month at
	 * 00:00:00)
	 * 
	 * @return a Date
	 */
	public static Date thisMonthStarts()
	{
		Date today = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(today);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Returns the first instant for month of the given date. E.g. if the given
	 * date is Fri Aug 11 10:57:09 EDT 2006 we return Tue Aug 01 00:00:00 EDT
	 * 2006.
	 * 
	 * @param date
	 * @return
	 */
	public static Date monthStarts(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		return calendar.getTime();
	}

	/**
	 * Returns the first day of the week the given date. 2006.
	 * 
	 * @param date
	 * @return
	 */
	public static Date weekStarts(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * Returns the LAST day of the week the given date.
	 * 
	 * @param date
	 * @return
	 */
	public static Date weekEnds(Date date)
	{
		Date lastDayOfWeek = addDays(weekStarts(date), 6);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastDayOfWeek);

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);

		return calendar.getTime();
	}

	/**
	 * Returns this month's ending date (which is on the last day of the month
	 * at 23:59:59)
	 * 
	 * @return a Date
	 */
	public static Date thisMonthEnds()
	{
		Date firstOfMonth = thisMonthStarts();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(firstOfMonth);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int dayInMonth = daysInMonth[month];
		dayInMonth = month == 1 && calendar.isLeapYear(year) ? dayInMonth + 1
				: dayInMonth;
		Date endOfMonth = addDays(firstOfMonth, dayInMonth - 1);
		return getEndDate(endOfMonth);
	}

	/**
	 * Returns the given month's ending date (which is on the last day of the
	 * month at 23:59:59)
	 * 
	 * @return a Date
	 */
	public static Date monthEnds(Date date)
	{
		Date firstOfMonth = monthStarts(date);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(firstOfMonth);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int dayInMonth = daysInMonth[month];
		dayInMonth = month == 1 && calendar.isLeapYear(year) ? dayInMonth + 1
				: dayInMonth;
		Date endOfMonth = addDays(firstOfMonth, dayInMonth - 1);
		return getEndDate(endOfMonth);
	}

	/**
	 * Returns the date's ending date (which is at 23:59:59)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndDate(Date date)
	{
		Date ret = null;
		try
		{
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 997); // needs to be 997 to prevent
												// SQL Server from rolling to
												// the next day
			ret = cal.getTime();
		}
		catch (Exception e)
		{
			// e.printStackTrace();
		}
		return ret;

	}

	/**
	 * Returns the date's starting date (which is at 00:00:00)
	 * 
	 * @return a Date
	 */
	public static Date getStartDate(Date date)
	{
		Date ret = null;
		try
		{
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			ret = cal.getTime();

		}
		catch (Exception e)
		{
			// e.printStackTrace()
		}
		return ret;
	}

	/**
	 * Formats a date to a specific style
	 * 
	 * @param date
	 * @param formatString
	 * @return
	 */
	public static String formatDate(Date date, String formatString)
	{
		String ret = null;
		if (date != null && formatString != null)
		{
			DateFormat df = new SimpleDateFormat(formatString);
			ret = df.format(date);
		}
		return ret;
	}

	/**
	 * Parses a date string according to formatString
	 * 
	 * @param date
	 * @param formatString
	 * @return a Date or null if unparseable
	 */
	public static Date parseDate(String date, String formatString)
	{
		DateFormat df = new SimpleDateFormat(formatString);
		Date ret = null;
		try
		{
			ret = df.parse(date);
		}
		catch (Exception e)
		{
		}
		return ret;
	}

	/**
	 * Returns true if date falls in between beginning of day startDate and end
	 * of day endDate
	 * 
	 * @param startDate
	 * @param endDate
	 * @param date
	 * @return
	 */
	public static boolean isBetween(Date startDate, Date endDate, Date date)
	{
		startDate = getStartDate(startDate);
		endDate = getEndDate(endDate);
		return isBefore(startDate, date) && isBefore(date, endDate);
	}

	/**
	 * Returns true if date falls in between beginning of day startDate and end
	 * of day endDate or equals to either
	 * 
	 * @param startDate
	 * @param endDate
	 * @param date
	 * @return
	 */
	public static boolean isBetweenOrEquals(Date startDate, Date endDate,
			Date date)
	{
		startDate = getStartDate(startDate);
		endDate = getEndDate(endDate);
		return isBeforeOrEquals(startDate, date)
				&& isBeforeOrEquals(date, endDate);
	}

	/**
	 * Returns true if date falls in between startDate and endDate
	 * 
	 * @param startDate
	 * @param endDate
	 * @param date
	 * @return
	 */
	public static boolean isBetweenExact(Date startDate, Date endDate, Date date)
	{
		return isBeforeOrSame(startDate, date) && isBeforeOrSame(date, endDate);
	}

	/**
	 * Returns true if the min (minute of day) falls between startMin and endMin
	 * 
	 * @param startMin
	 * @param endMin
	 * @param min
	 * @return
	 */
	public static boolean isBetween(int startMin, int endMin, int min)
	{
		return startMin <= min && endMin >= min;
	}

	/**
	 * Returns true if the period defined by date1From and date1To falls between
	 * the period defined by beginning of day date2From and end of day date2To
	 * 
	 * @param date1From
	 * @param date1To
	 * @param date2From
	 * @param date2To
	 * @return
	 */
	public static boolean periodFallsWithin(Date date1From, Date date1To,
			Date date2From, Date date2To)
	{
		return isBetween(date2From, date2To, date1From)
				&& isBetween(date2From, date2To, date1To);
	}

	/**
	 * Returns true if the period defined by date1From and date1To falls between
	 * the period defined by date2From and date2To
	 * 
	 * @param date1From
	 * @param date1To
	 * @param date2From
	 * @param date2To
	 * @return
	 */
	public static boolean periodFallsWithinExact(Date date1From, Date date1To,
			Date date2From, Date date2To)
	{
		return isBetweenExact(date2From, date2To, date1From)
				&& isBetweenExact(date2From, date2To, date1To);
	}

	/**
	 * Returns the current year (e.g. 2006)
	 * 
	 * @return
	 */
	public static int getCurrentYear()
	{
		return getYear(new Date());
	}

	/**
	 * Returns the year (e.g. 2006) of the specified date
	 * 
	 * @return
	 */
	public static int getYear(Date date)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static Date getYear(int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);

		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * Returns the minute of day of the specified date
	 * 
	 * @return
	 */
	public static int getMinuteOfDay(Date date)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY) * 60
				+ calendar.get(Calendar.MINUTE);
	}

	public static int getHourOfDay(Date date)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Returns the current month (1-12)
	 * 
	 * @return
	 */
	public static int getCurrentMonth()
	{
		return getMonth(new Date());
	}

	/**
	 * Returns the month (1-12) of the specified date
	 * 
	 * @return
	 */
	public static int getMonth(Date date)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static String getMonthName(int monthNum)
	{
		String[] monthName = { "", "January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October",
				"November", "December" };
		return monthName[monthNum];
	}

	public static int getDayOfMonth(Date date)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns true if the date specified is today
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date)
	{
		return DateUtils.isBetween(DateUtils.todayStarts(),
				DateUtils.todayEnds(), date);
	}

	/**
	 * Returns how many days there are in the month specified in the date
	 * 
	 * @param date
	 * @return
	 */
	public static int getDaysInMonth(Date date)
	{
		Date firstOfMonth = monthStarts(date);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(firstOfMonth);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int dayInMonth = daysInMonth[month];
		dayInMonth = month == 1 && calendar.isLeapYear(year) ? dayInMonth + 1
				: dayInMonth;
		return dayInMonth;
	}

	/**
	 * Returns which weekday a given date falls on.
	 * 
	 * @param date
	 * @return 1 - Sunday, 2 - Monday, 3 - Tuesday ... 7 - Saturday
	 */
	public static int getDayOfWeek(Date date)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Returns true if the date supplied is a weekday
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isWeekday(Date date)
	{
		int dow = DateUtils.getDayOfWeek(date);
		return (dow > 1) && (dow < 7);
	}

	/**
	 * Returns true if the time period date1From-date1To cross-sects the period
	 * date2From-date2To
	 * 
	 * @param date1From
	 * @param date1To
	 * @param date2From
	 * @param date2To
	 * @return
	 */
	public static boolean isConflicting(Date date1From, Date date1To,
			Date date2From, Date date2To)
	{
		boolean ret = false;
		if (date1From != null && date1To != null && date2From != null
				&& date2To != null)
		{
			if (date2To.getTime() > date1From.getTime()
					&& date2To.getTime() < date1To.getTime())
			{
				ret = true;
			}
			if (date2From.getTime() > date1From.getTime()
					&& date2From.getTime() < date1To.getTime())
			{
				ret = true;
			}
			if (date1To.getTime() > date2From.getTime()
					&& date1To.getTime() < date2To.getTime())
			{
				ret = true;
			}
			if (date1From.getTime() > date2From.getTime()
					&& date1From.getTime() < date2To.getTime())
			{
				ret = true;
			}
			if (date1From.getTime() == date2From.getTime()
					&& date1To.getTime() == date2To.getTime())
			{
				ret = true;
			}
		}
		return ret;
	}

	public static boolean areOnSameDay(Date date1, Date date2)
	{
		return (DateUtils.getYear(date1) == DateUtils.getYear(date2))
				&& (DateUtils.getMonth(date1) == DateUtils.getMonth(date2))
				&& (DateUtils.getDayOfMonth(date1) == DateUtils
						.getDayOfMonth(date2));
	}

	public static long getDifferenceInDays(Date d1, Date d2)
	{
		return ((d2.getTime() - d1.getTime() + ONE_HOUR) / (ONE_HOUR * 24));
	}

	public static long getDifferenceInMinutes(Date d1, Date d2)
	{
		return (d2.getTime() - d1.getTime()) / (1000 * 60);
	}

	public static long getDifferenceInWeeks(Date d1, Date d2)
	{
		return ((d2.getTime() - d1.getTime() + ONE_HOUR) / (ONE_HOUR * 24 * 7));
	}

	/**
	 * Returns a random date that falls between a MIN and MAX date.
	 */
	public static Date getRandomDate(Date minDate, Date maxDate)
	{
		Random r = new Random();

		long randomTS = (long) (r.nextDouble() * (maxDate.getTime() - minDate
				.getTime())) + minDate.getTime();

		return new Date(randomTS);
	}

	public static Date getYearStart(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(calendar.SECOND, 0);

		return calendar.getTime();
	}

	public static Date getYearEnd(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.HOUR, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(calendar.SECOND, 59);

		return calendar.getTime();
	}
}
