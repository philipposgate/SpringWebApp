package app.web.SpringWebApp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity
{
	@Id
	@GeneratedValue
	@Column(nullable = false)
	private Integer id;

	public Integer getId()
	{
		return id;
	}

	@Override
	public int hashCode()
	{
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + " [id=" + id + "]";
	}

}
