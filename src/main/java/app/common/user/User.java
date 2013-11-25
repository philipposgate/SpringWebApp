package app.common.user;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import app.AbstractEntity;

@Entity
@Table(name = "user")
public class User extends AbstractEntity implements UserDetails
{
	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();

	@Column
	private boolean accountNonExpired;

	@Column
	private boolean accountNonLocked;

	@Column
	private boolean credentialsNonExpired;

	@Column
	private boolean enabled;

	@Column
	private Date createDate;

	@Column
	private Date lastLoggedInDate;

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column
	private String email;

	public String getFullName()
	{
		return this.firstName + " " + this.lastName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return this.roles;
	}

	@Override
	public String getPassword()
	{
		return this.password;
	}

	@Override
	public String getUsername()
	{
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled()
	{
		return this.enabled;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setAccountNonExpired(boolean accountNonExpired)
	{
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked)
	{
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired)
	{
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	@Override
	public String toString()
	{
		return "User [id=" + getId() + ", username=" + username + "]";
	}

	public Date getLastLoggedInDate()
	{
		return lastLoggedInDate;
	}

	public void setLastLoggedInDate(Date lastLoggedInDate)
	{
		this.lastLoggedInDate = lastLoggedInDate;
	}

	public boolean hasRole(String roleName)
	{
		boolean hasRole = false;

		if (roles != null && !roles.isEmpty())
		{
			for (Role role : roles)
			{
				if (role.getRole().equals(roleName))
				{
					hasRole = true;
					break;
				}
			}
		}
		return hasRole;
	}

}
