package app.common.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
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
		c.setTitle(title);
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

	public Event createQuickEvent(User owner, Calendar calendar, String title)
	{
		Date now = new Date();

		Event e = new Event();
		e.setOwner(owner);
		e.setCreated(now);
		e.setTitle(title);
		e.setAllDay(true);
		e.setStartDate(now);
		e.setEndDate(now);
		getHt().save(e);

		return e;
	}

	public void bind(Calendar calendar, Event event)
	{
		CalendarEvent ce = new CalendarEvent();
		ce.setCalendar(calendar);
		ce.setEvent(event);
		getHt().save(ce);
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
					el.addAll(getEvents(c, start, end));
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
			hql.append(" and ce.event.startDate between '").append(DateUtils.formatDate(start, DateUtils.DB_DATE_FORMAT))
			        .append("' and '").append(DateUtils.formatDate(end, DateUtils.DB_DATE_FORMAT)).append("'");
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

		return getHt().find(hql.toString());
	}

	public Event getEvent(HttpServletRequest request)
    {
	    Event event = null;
	    
	    if (null != request.getAttribute("eventId"))
	    {
	    	event = (Event) getHt().load(Event.class, (Integer)request.getAttribute("eventId"));
	    }
	    else if (StringUtils.isInteger(request.getParameter("eventId")))
	    {
	    	event = (Event) getHt().load(Event.class, new Integer(request.getParameter("eventId")));
	    }
	    
	    return event;
    }

	public void bind(Event event, HttpServletRequest request)
    {
		if (null == event)
		{
			event = new Event();
		}
		event.setTitle(request.getParameter("title"));
    }

	public void save(Event event)
    {
		getHt().saveOrUpdate(event);
    }
}
