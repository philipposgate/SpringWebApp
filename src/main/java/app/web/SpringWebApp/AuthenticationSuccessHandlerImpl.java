package app.web.SpringWebApp;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import app.web.SpringWebApp.user.User;

public class AuthenticationSuccessHandlerImpl implements
		AuthenticationSuccessHandler
{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException
	{
		User user = (User) authentication.getPrincipal();
		user.setLastLoggedInDate(new Date());
		AppHelper.updateUser(user);

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication
				.getAuthorities());

		if (roles.contains(AppHelper.ROLE_ADMIN))
		{
			response.sendRedirect("/app/admin/");
		}
		else
		{
			response.sendRedirect("/app/user/");
		}
	}
}
