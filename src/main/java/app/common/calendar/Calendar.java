package app.common.calendar;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import app.core.AbstractEntity;
import app.core.user.User;

@Entity
@Table(name = "calendar")
public class Calendar extends AbstractEntity
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
	private String location;

	@Column
	private String colorForeground;

	@Column
	private String colorBackground;

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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getColorForeground()
	{
		return colorForeground;
	}

	public void setColorForeground(String colorForeground)
	{
		this.colorForeground = colorForeground;
	}

	public String getColorBackground()
	{
		return colorBackground;
	}

	public void setColorBackground(String colorBackground)
	{
		this.colorBackground = colorBackground;
	}
}
