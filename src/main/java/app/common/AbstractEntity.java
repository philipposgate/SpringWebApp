package app.common;

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
	public boolean equals(Object obj) {
		boolean equal = false;
		
		if (null != obj 
				&& obj.getClass().isAssignableFrom(this.getClass()) 
				&& obj instanceof AbstractEntity 
				&& ((AbstractEntity)obj).getId() != null
				&& ((AbstractEntity)obj).getId().equals(this.getId()))
		{
			equal = true;
		}
		
		return equal;
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + " [id=" + id + "]";
	}

}
