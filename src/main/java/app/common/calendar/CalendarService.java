package app.common.calendar;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import app.core.user.User;

@Service
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

	public List<CalendarList> getCalendarLists(User user)
	{
		List<CalendarList> ret = null;

		if (null != user)
		{
			ret = getHt().find("from CalendarList cl where cl.owner=?", user);
			for (CalendarList cl : ret)
			{
				populate(cl);
			}
		}
		return ret;
	}

	public void populate(CalendarList cl)
	{
		cl.getCalendars().addAll(
		        getHt().find("select cli.calendar from CalendarListItem cli where cli.calendarList=?", cl));
	}

	public CalendarList createCalendarList(User owner, String title)
	{
		CalendarList calList = new CalendarList();
		calList.setCreated(new Date());
		calList.setOwner(owner);
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

	public CalendarEvent createQuickEvent(User creator, Calendar calendar, String title)
    {
		CalendarEvent e = new CalendarEvent();
		e.setCreator(creator);
		e.setCalendar(calendar);
		e.setTitle(title);
		e.setAllDay(true);
		e.setStart(new Date());
		
		getHt().save(e);
		
	    return e;
    }
}
