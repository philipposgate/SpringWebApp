package app.common.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import app.core.AbstractEntity;
import app.core.user.User;

@Entity
@Table(name = "calendar_list")
public class CalendarList extends AbstractEntity
{
    @ManyToOne(fetch=FetchType.EAGER, optional=false)
	private CalendarDomain calendarDomain;
	
    @ManyToOne(fetch=FetchType.EAGER)
	private User owner;
	
	@Column(nullable = false)
	private Date created;

	@Column
	private Date updated;

	@Column(nullable = false)
	private String title;
	
	@Transient
	private List<Calendar> calendars = new ArrayList<Calendar>();

	public User getOwner()
	{
		return owner;
	}

	public void setOwner(User owner)
	{
		this.owner = owner;
	}

	public Date getCreated()
	{
		return created;
	}

	public void setCreated(Date created)
	{
		this.created = created;
	}

	public Date getUpdated()
	{
		return updated;
	}

	public void setUpdated(Date updated)
	{
		this.updated = updated;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public List<Calendar> getCalendars()
	{
		return calendars;
	}

	public void setCalendars(List<Calendar> calendars)
	{
		this.calendars = calendars;
	}

	public CalendarDomain getCalendarDomain()
	{
		return calendarDomain;
	}

	public void setCalendarDomain(CalendarDomain calendarDomain)
	{
		this.calendarDomain = calendarDomain;
	}

}
