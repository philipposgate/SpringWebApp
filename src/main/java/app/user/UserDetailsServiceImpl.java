package app.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import app.rest.AppHelper;

public class UserDetailsServiceImpl implements UserDetailsService
{
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException
	{
		User user = AppHelper.getUserByUsername(username);

		if (user == null)
		{
			throw new UsernameNotFoundException("User not found.");
		}

		return user;
	}

}
