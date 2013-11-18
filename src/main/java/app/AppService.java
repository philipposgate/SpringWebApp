package app;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.user.Role;
import app.user.RoleDAO;
import app.user.User;
import app.user.UserDAO;

@Service(value="appService")
public class AppService implements InitializingBean
{
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private AppConfigDAO appConfigDAO;

	@Resource(name = "appSettings")
	private final Map<String, String> appSettings = null;

	@Override
	public void afterPropertiesSet() throws Exception
	{
		Role userRole = roleDAO.getRoleByName(ROLE_USER);
		if (userRole == null)
		{
			System.out.println("CREATE ROLE_USER");
			userRole = new Role();
			userRole.setRole(ROLE_USER);
			roleDAO.create(userRole);
		}

		Role adminRole = roleDAO.getRoleByName(ROLE_ADMIN);
		if (adminRole == null)
		{
			System.out.println("CREATE ROLE_ADMIN");
			adminRole = new Role();
			adminRole.setRole(ROLE_ADMIN);
			roleDAO.create(adminRole);
		}

		User adminUser = getUserByUsername("admin");
		if (adminUser == null)
		{
			System.out.println("CREATE ROOT USER");
			adminUser = new User();
			adminUser.setFirstName("admin");
			adminUser.setLastName("admin");
			adminUser.setEmail("admin@admin.com");
			adminUser.setUsername("admin");
			adminUser.setPassword("1");
			adminUser.setAccountNonLocked(true);
			adminUser.setAccountNonExpired(true);
			adminUser.setCredentialsNonExpired(true);
			adminUser.setEnabled(true);
			adminUser.setCreateDate(new Date());

			userDAO.create(adminUser);

			adminUser.getRoles().add(userRole);
			adminUser.getRoles().add(adminRole);

			userDAO.update(adminUser);
		}

		User regularUser = getUserByUsername("user");
		if (regularUser == null)
		{
			System.out.println("CREATE REGULAR USER");
			regularUser = new User();
			regularUser.setUsername("user");
			regularUser.setPassword("1");
			regularUser.setAccountNonLocked(true);
			regularUser.setAccountNonExpired(true);
			regularUser.setCredentialsNonExpired(true);
			regularUser.setEnabled(true);
			regularUser.setCreateDate(new Date());
			regularUser.setFirstName("user");
			regularUser.setLastName("user");
			regularUser.setEmail("user@user.com");

			userDAO.create(regularUser);

			regularUser.getRoles().add(userRole);

			userDAO.update(regularUser);
		}

	}

	public User getUserByUsername(String username)
	{
		return userDAO.getUserByUsername(username);
	}

	public User getUserById(String id)
	{
		return userDAO.getById(id);
	}

	public static boolean isInteger(String s)
	{
		boolean ret = false;
		try
		{
			Integer.parseInt(s);
			ret = true;
		}
		catch (Exception e)
		{
		}
		return ret;
	}

	@Transactional
	public void updateUser(User user)
	{
		userDAO.update(user);
	}

	public User getUserLoggedIn()
	{
		User user = null;
		Authentication authentic = SecurityContextHolder.getContext()
				.getAuthentication();

		if (authentic != null)
		{
			user = getUserByUsername(authentic.getName());
		}

		return user;
	}

	public void setUserLoggedIn(User user)
	{
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				user, user.getPassword(), user.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public Role getRole(String roleName)
	{
		Role role = roleDAO.getRoleByName(roleName);

		if (role == null || role.getId() == null)
		{
			role = new Role();
			role.setRole(roleName);
			roleDAO.create(role);
		}
		return role;
	}

	public boolean userHasRole(User user, Role role)
	{
		return user.getAuthorities().contains(role);
	}

	public String getConfigValue(String key)
	{
		return appConfigDAO.getValue(key);
	}

	public AppConfig getAppConfig(String key)
	{
		return appConfigDAO.getAppConfig(key);
	}

	public void saveConfig(String key, String value)
	{
		appConfigDAO.saveConfig(key, value);
	}
	
	public void toggleRole(User user, String roleName, boolean toggle)
	{
		if (toggle)
		{
			if (!user.hasRole(roleName))
			{
				Role role = getRole(roleName);
				user.getRoles().add(role);
				userDAO.update(user);
			}
		}
		else
		{
			if (user.hasRole(roleName))
			{
				Role role = getRole(roleName);
				user.getRoles().remove(role);
				userDAO.update(user);
			}
		}

	}

}
