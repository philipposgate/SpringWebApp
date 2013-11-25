package app.common.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.AppService;

@Service(value="userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService
{
//	@Autowired
	private AppService appService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException
	{
		User user = appService.getUserByUsername(username);

		if (user == null)
		{
			throw new UsernameNotFoundException("User not found.");
		}

		return user;
	}

}
