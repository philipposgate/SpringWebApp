package app.common.calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import app.core.AbstractEntity;

@Entity
@Table(name = "calendar_event")
public class CalendarEvent extends AbstractEntity
{
    @ManyToOne(fetch=FetchType.EAGER)
	private Calendar calendar;

    @ManyToOne(fetch=FetchType.EAGER)
	private Event event;

	public Calendar getCalendar()
	{
		return calendar;
	}

	public void setCalendar(Calendar calendar)
	{
		this.calendar = calendar;
	}

	public Event getEvent()
	{
		return event;
	}

	public void setEvent(Event event)
	{
		this.event = event;
	}
}
