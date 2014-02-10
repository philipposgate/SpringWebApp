package app.common.calendar;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import app.core.AbstractEntity;
import app.core.user.User;

@Entity
@Table(name = "event")
public class Event extends AbstractEntity
{
    @ManyToOne(fetch=FetchType.EAGER)
	private User owner;
	
	@Column(nullable = false)
	private Date created;

	@Column
	private Date updated;
	
	@Column(nullable = false)
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column
	private Date startDate;

	@Column
	private Date endDate;
	
	@Column
	private boolean allDay;

	@Column
	private String location;

	@Transient
	private Calendar calendar;

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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean isAllDay()
	{
		return allDay;
	}

	public void setAllDay(boolean allDay)
	{
		this.allDay = allDay;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public User getOwner()
	{
		return owner;
	}

	public void setOwner(User owner)
	{
		this.owner = owner;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
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
