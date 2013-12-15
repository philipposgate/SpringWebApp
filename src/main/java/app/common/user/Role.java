package app.common.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


import app.common.AbstractEntity;

@Entity
@Table(name = "role")
public class Role extends AbstractEntity
{
	@Column(unique = true, nullable = false)
	private String role;

	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

}
