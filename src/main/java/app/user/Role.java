package app.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import app.AbstractEntity;

@Entity
@Table(name = "role")
public class Role extends AbstractEntity implements GrantedAuthority
{
	@Column(unique = true, nullable = false)
	private String role;

	@Override
	public String getAuthority()
	{
		return this.role;
	}

	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

}
