package app.rest;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.SavedRequest;

import app.AppHelper;
import app.user.User;

public class AuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler
{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException
	{
		User user = (User) authentication.getPrincipal();
		user.setLastLoggedInDate(new Date());
		AppHelper.updateUser(user);

		String redirectUrl = null;
		
        SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
		
        if (savedRequest != null)
        {
        	redirectUrl = savedRequest.getRedirectUrl();
        }
        
        if (null == redirectUrl)
        {
    		Set<String> roles = AuthorityUtils.authorityListToSet(authentication
    				.getAuthorities());

    		if (roles.contains(AppHelper.ROLE_ADMIN))
    		{
            	redirectUrl = "/admin/";
    		}
    		else
    		{
            	redirectUrl = "/user/";
    		}
        }

		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}
}
