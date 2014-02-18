package app.common.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    
    public static enum COLOR_THEME {
    	GREEN("#008000", WHITE),
    	AF_BLUE("#5D8AA8", WHITE),
    	AMETHYST("#008000", WHITE),
    	SAE("#FF7E00", BLACK);
    	
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

	public void populate(CalendarList cl)
	{
		cl.getCalendars().addAll(getCalendars(cl));
	}

	private List<Calendar> getCalendars(CalendarList cl)
	{
		return getHt().find("select cli.calendar from CalendarListItem cli where cli.calendarList=?", cl);
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

	public Calendar createCalendar(User owner, String title)
	{
		Calendar c = new Calendar();
		c.setCreated(new Date());
		c.setOwner(owner);
		c.setTitle(StringUtils.isEmpty(title) ? "(no title)" : title);
		c.setColorBackground("#3a87ad");
		c.setColorForeground("#fff");
		c.setVisible(true);
		getHt().save(c);

		return c;
	}

	public void bind(CalendarList calList, Calendar calendar)
	{
		CalendarListItem cli = new CalendarListItem();
		cli.setCalendarList(calList);
		cli.setCalendar(calendar);
		getHt().save(cli);
	}

	public Event createEvent(User owner, Calendar calendar, String title, Date startDate, Date endDate, boolean allDay)
	{
		Event e = null;

		if (null != owner && null != calendar && null != startDate && null != endDate)
		{
			e = new Event();
			e.setOwner(owner);
			e.setCreated(new Date());
			e.setTitle(StringUtils.isEmpty(title) ? "(no title)" : title);
			e.setAllDay(allDay);
			e.setStartDate(startDate);
			e.setEndDate(endDate);
			
			getHt().save(e);
			
			logger.info(e.toString());
			
			bind(calendar, e);
		}

		return e;
	}

	public void bind(Calendar calendar, Event event)
	{
		CalendarEvent ce = new CalendarEvent();
		ce.setCalendar(calendar);
		ce.setEvent(event);
		getHt().save(ce);
		
		event.setCalendar(calendar);
	}

	public List<Event> getEvents(CalendarDomain calendarDomain, User owner, Date start, Date end)
	{
		List<Event> el = new ArrayList<Event>();

		try
		{
			List<CalendarList> calLists = getCalendarLists(calendarDomain, owner);
			for (CalendarList cl : calLists)
			{
				List<Calendar> cals = getCalendars(cl);
				for (Calendar c : cals)
				{
					if (c.isVisible())
					{
						el.addAll(getEvents(c, start, end));
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return el;
	}

	private List<Event> getEvents(Calendar c, Date start, Date end)
	{
		StringBuilder hql = new StringBuilder();

		hql.append("select ce.event from CalendarEvent ce where ce.calendar.id=").append(c.getId());

		if (null != start && null != end)
		{
			hql.append(" and ce.event.startDate between '")
			        .append(DateUtils.formatDate(start, DateUtils.DB_DATE_FORMAT)).append("' and '")
			        .append(DateUtils.formatDate(end, DateUtils.DB_DATE_FORMAT)).append("'");
		}
		else if (null != start && null == end)
		{
			hql.append(" and ce.event.startDate >= '").append(DateUtils.formatDate(start, DateUtils.DB_DATE_FORMAT))
			        .append("'");
		}
		else if (null == start && null != end)
		{
			hql.append(" and ce.event.startDate <= '").append(DateUtils.formatDate(end, DateUtils.DB_DATE_FORMAT))
			        .append("'");
		}

		List<Event> events = getHt().find(hql.toString());

		for (Event event : events)
		{
			event.setCalendar(c);
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

	public void populate(Event event)
	{
		List<Calendar> ces = getHt().find("select ce.calendar from CalendarEvent ce where ce.event=?", event);

		if (!ces.isEmpty())
		{
			Calendar c = ces.get(0);
			event.setCalendar(c);
		}
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
	    List<CalendarEvent> ces = getHt().find("from CalendarEvent ce where ce.event=?", event);
	    for (CalendarEvent ce : ces)
        {
	        getHt().delete(ce);
        }
	    
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
