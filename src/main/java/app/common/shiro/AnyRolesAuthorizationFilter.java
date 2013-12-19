package app.common.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.springframework.stereotype.Component;

/**
 * This is a custom "Shiro AuthorizationFilter" that authorizes access if the
 * user has "any" of the roles specified in the filter-chain (as opposed to the
 * regular "RolesAuthorizationFilter" which authorizes access when the user has
 * "all" of the roles specified)
 * 
 * @author philip
 */
@Component
public class AnyRolesAuthorizationFilter extends RolesAuthorizationFilter
{
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException
    {

        final Subject subject = getSubject(request, response);
        final String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0)
        {
            // no roles specified, so nothing to check - allow access.
            return true;
        }

        for (String roleName : rolesArray)
        {
            if (subject.hasRole(roleName))
            {
                return true;
            }
        }

        return false;
    }

}
