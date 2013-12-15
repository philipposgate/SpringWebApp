package app.common.shiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.common.user.Role;
import app.common.user.RoleDAO;
import app.common.user.User;
import app.common.user.UserDAO;

@Component
public class AppRealm extends AuthorizingRealm {

	@Autowired
	private UserDAO userDAO;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException 
	{
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		
		User user = userDAO.getUserByUsername(upToken.getUsername());

		if (user == null) 
		{
			throw new AuthenticationException("Login name [" + upToken.getUsername() + "] not found!");
		}
		
		return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) 
	{
		Set<String> roles = new HashSet<String>();
		Set<Permission>	permissions	= new HashSet<Permission>();
		Collection<User> principalsList	= principals.byType(User.class);

		for (User userPrincipal : principalsList) 
		{
			try 
			{
				User user = userDAO.getById(userPrincipal.getId());
				Set<Role> userRoles = user.getRoles();
				for (Role role : userRoles)
				{
					roles.add(role.getRole());
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
		info.setRoles(roles);
		info.setObjectPermissions(permissions);

		return info;
	}

}
