package app.common.calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import app.core.AbstractEntity;

@Entity
@Table(name = "calendar_list_item")
public class CalendarListItem extends AbstractEntity
{
    @ManyToOne(fetch=FetchType.EAGER)
	private CalendarList calendarList;

    @ManyToOne(fetch=FetchType.EAGER)
	private Calendar calendar;

	public CalendarList getCalendarList()
	{
		return calendarList;
	}

	public void setCalendarList(CalendarList calendarList)
	{
		this.calendarList = calendarList;
	}

	public Calendar getCalendar()
	{
		return calendar;
	}

	public void setCalendar(Calendar calendar)
	{
		this.calendar = calendar;
	}
}
