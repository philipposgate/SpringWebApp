package app.modules.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.common.utils.DateUtils;
import app.common.utils.StringUtils;
import app.core.user.User;

@Service
@Transactional
public class CalendarService
{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	public static final String WHITE = "#FFFFFF";
	public static final String BLACK = "#000000";

	public static enum COLOR_THEME
	{
		GREEN("#228B22", WHITE), PINK("#FFA6C9", BLACK), AF_BLUE("#5D8AA8", WHITE), AMETHYST("#9966CC", WHITE), CAPRI(
		        "#00BFFF", BLACK), RED("#FF0038", WHITE), CHROME_YELLOW("#FFA700", WHITE), CYBER_YELLOW("#FFD300",
		        BLACK), DARK_KHAKI("#BDB76B", BLACK), KEPPEL("#3AB09E", WHITE), TAUPE("#B38B6D", WHITE), SAE("#FF7E00",
		        BLACK);

		private String background;
		private String foreground;

		private COLOR_THEME(String background, String foreground)
		{
			this.background = background;
			this.foreground = foreground;
		}

		public String getBackground()
		{
			return this.background;
		}

		public String getForeground()
		{
			return this.foreground;
		}
	}

	@Autowired
	protected SessionFactory sessionFactory;

	private HibernateTemplate ht;

	public HibernateTemplate getHt()
	{
		if (null == ht)
		{
			ht = new HibernateTemplate(sessionFactory);
		}
		return ht;
	}

	public List<CalendarList> getCalendarLists(CalendarDomain calendarDomain, User owner)
	{
		List<CalendarList> ret = null;

		if (null != calendarDomain && null != owner)
		{
			ret = getHt().find("from CalendarList cl where cl.calendarDomain=? and cl.owner=?",
			        new Object[] { calendarDomain, owner });
			for (CalendarList cl : ret)
			{
				populate(cl);
			}
		}
		return ret;
	}

	public List<Calendar> getAllUserCalendars(CalendarDomain calendarDomain, User owner)
	{
		List<Calendar> ret = null;

		if (null != calendarDomain && null != owner)
		{
			ret = getHt().find("from Calendar c where c.calendarList.calendarDomain=? and c.calendarList.owner=?",
			        new Object[] { calendarDomain, owner });
		}
		return ret;
	}

	public void populate(CalendarList cl)
	{
		cl.getCalendars().addAll(getCalendars(cl));
	}

	private List<Calendar> getCalendars(CalendarList cl)
	{
		return getHt().find("from Calendar c where c.calendarList=?", cl);
	}

	public CalendarList createCalendarList(CalendarDomain calendarDomain, User owner, String title)
	{
		CalendarList calList = new CalendarList();
		calList.setCalendarDomain(calendarDomain);
		calList.setOwner(owner);
		calList.setCreated(new Date());
		calList.setTitle(title);
		getHt().save(calList);

		return calList;
	}

	public Calendar createCalendar(CalendarList calendarList, String title)
	{
		Calendar c = new Calendar();
		c.setCreated(new Date());
		c.setCalendarList(calendarList);
		c.setTitle(StringUtils.isEmpty(title) ? "(no title)" : title);
		c.setColorBackground("#3a87ad");
		c.setColorForeground("#fff");
		c.setVisible(true);
		getHt().save(c);

		return c;
	}

	public Event createEvent(User owner, Calendar calendar, String title, Date startDate, Date endDate, boolean allDay)
	{
		Event e = null;

		if (null != owner && null != calendar && null != startDate && null != endDate)
		{
			e = new Event();
			e.setCalendar(calendar);
			e.setCreated(new Date());
			e.setTitle(StringUtils.isEmpty(title) ? "(no title)" : title);
			e.setAllDay(allDay);
			e.setStartDate(startDate);
			e.setEndDate(endDate);

			getHt().save(e);

			logger.info(e.toString());
		}

		return e;
	}

	public List<Event> getNonRepeatingEvents(CalendarDomain calendarDomain, User owner, Date start, Date end)
	{
		List<Event> events = new ArrayList<Event>();

		try
		{
			StringBuilder hql = new StringBuilder();

			hql.append(
			        "from Event e where e.repeats=0 and e.calendar.visible=1 and e.calendar.calendarList.calendarDomain.id=")
			        .append(calendarDomain.getId()).append(" and e.calendar.calendarList.owner.id=")
			        .append(owner.getId());

			if (null != start && null != end)
			{
				hql.append(" and e.startDate between '").append(DateUtils.formatDate(start, DateUtils.DB_DATE_FORMAT))
				        .append("' and '").append(DateUtils.formatDate(end, DateUtils.DB_DATE_FORMAT)).append("'");
			}
			else if (null != start && null == end)
			{
				hql.append(" and e.startDate >= '").append(DateUtils.formatDate(start, DateUtils.DB_DATE_FORMAT))
				        .append("'");
			}
			else if (null == start && null != end)
			{
				hql.append(" and e.startDate <= '").append(DateUtils.formatDate(end, DateUtils.DB_DATE_FORMAT))
				        .append("'");
			}

			events = getHt().find(hql.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return events;
	}

	public List<Event> getRepeatingEvents(CalendarDomain calendarDomain, User owner)
	{
		List<Event> events = new ArrayList<Event>();

		try
		{
			StringBuilder hql = new StringBuilder();
			hql.append(
			        "from Event e where e.repeats=1 and e.calendar.visible=1 and e.calendar.calendarList.calendarDomain.id=")
			        .append(calendarDomain.getId()).append(" and e.calendar.calendarList.owner.id=")
			        .append(owner.getId());

			events = getHt().find(hql.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return events;
	}

	public Event getEvent(HttpServletRequest request)
	{
		Event event = null;

		if (null != request.getAttribute("eventId"))
		{
			event = getHt().load(Event.class, (Integer) request.getAttribute("eventId"));
		}
		else if (StringUtils.isInteger(request.getParameter("eventId")))
		{
			event = getHt().load(Event.class, new Integer(request.getParameter("eventId")));
		}

		return event;
	}

	public void save(Event event)
	{
		event.setUpdated(new Date());
		getHt().saveOrUpdate(event);
	}

	public Calendar getCalendar(HttpServletRequest request)
	{
		Calendar calendar = null;

		if (null != request.getAttribute("calendarId"))
		{
			calendar = getHt().load(Calendar.class, (Integer) request.getAttribute("calendarId"));
		}
		else if (StringUtils.isInteger(request.getParameter("calendarId")))
		{
			calendar = getHt().load(Calendar.class, new Integer(request.getParameter("calendarId")));
		}

		return calendar;
	}

	public void delete(Event event)
	{
		getHt().delete(event);
	}

	public void save(Calendar cal)
	{
		cal.setUpdated(new Date());
		getHt().saveOrUpdate(cal);
	}

	public CalendarList getCalendarList(HttpServletRequest request)
	{
		CalendarList calendarList = null;

		if (null != request.getAttribute("calendarListId"))
		{
			calendarList = getHt().load(CalendarList.class, (Integer) request.getAttribute("calendarListId"));
		}
		else if (StringUtils.isInteger(request.getParameter("calendarListId")))
		{
			calendarList = getHt().load(CalendarList.class, new Integer(request.getParameter("calendarListId")));
		}

		return calendarList;
	}
}
