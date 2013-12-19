package app.common.pathElement;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UrlPathHelper;

import app.common.shiro.AnyRolesAuthorizationFilter;
import app.common.user.Role;
import app.common.user.RoleDAO;
import app.web.PathElementAbstractController;
import app.web.PathElementHandlerMapping;
import app.web.modules.home.HomeController;

@Service
public class PathElementService implements InitializingBean
{
    private static final Logger logger = LoggerFactory.getLogger(PathElementService.class);

    @Autowired
    private PathElementDAO pathElementDAO;

    @Autowired
    private PathElementRoleDAO pathElementRoleDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private AbstractShiroFilter shiroFilter;
    
    @Autowired
    private AnyRolesAuthorizationFilter anyRoles;

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    private PathElementHandlerMapping pathElementHandlerMapping;

    private PathElement rootElement;

    private final Map<String, PathElement> pathElementMap = new HashMap<String, PathElement>();

    @Override
    public void afterPropertiesSet() throws Exception
    {
        rootElement = pathElementDAO.getRootPathElement();

        if (null == rootElement)
        {
            rootElement = new PathElement();
            rootElement.setPath("");
            rootElement.setController("");
            rootElement.setParent(null);
            rootElement.setTitle("Spring Web App");
            rootElement.setActive(true);
            pathElementDAO.create(rootElement);
        }

        PathElement homeElement = pathElementDAO.getHomePathElement();

        if (null == homeElement)
        {
            homeElement = new PathElement();
            homeElement.setPath("index");
            homeElement.setController("homeController");
            homeElement.setParent(rootElement);
            homeElement.setTitle("Home");
            homeElement.setActive(true);
            pathElementDAO.create(homeElement);
        }

        pathElementDAO.populateChildren(rootElement);

        resetSecurityFilters();
    }

    public Map<String, PathElement> getUrlPathElementMap()
    {
        if (pathElementMap.isEmpty())
        {
            PathElement rootElement = getRootElement();
            for (PathElement child : rootElement.getChildren())
            {
                populateUrlPathElementMap(child);
            }
        }
        return Collections.unmodifiableMap(pathElementMap);
    }

    private void populateUrlPathElementMap(PathElement pathElement)
    {
        pathElementMap.put(pathElement.getFullPath(), pathElement);
        for (PathElement child : pathElement.getChildren())
        {
            populateUrlPathElementMap(child);
        }
    }

    public PathElement getRootElement()
    {
        if (null == rootElement)
        {
            rootElement = pathElementDAO.getRootPathElement();
        }
        return rootElement;
    }

    public Map<String, Object> getUrlControllerMap()
    {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        PathElement root = getRootElement();

        if (null != root && null != root.getChildren() && !root.getChildren().isEmpty())
        {
            for (PathElement child : root.getChildren())
            {
                populateUrlControllerMap(map, child);
            }
        }

        return map;
    }

    private void populateUrlControllerMap(Map<String, Object> map, PathElement pathElement)
    {

        String urlPath = pathElement.getFullPath();
        map.put(urlPath, pathElement.getController());

        if (null != pathElement.getChildren() && !pathElement.getChildren().isEmpty())
        {
            for (PathElement child : pathElement.getChildren())
            {
                populateUrlControllerMap(map, child);
            }
        }
    }

    public void refreshUrlMappings()
    {
        rootElement = null;
        pathElementMap.clear();
        pathElementHandlerMapping.refreshUrlMappings();
        resetSecurityFilters();
    }

    private void resetSecurityFilters()
    {
        Map<String, NamedFilterList> newfilterChains = new LinkedHashMap<String, NamedFilterList>();

        PathMatchingFilterChainResolver chainResolver = (PathMatchingFilterChainResolver) this.shiroFilter
                .getFilterChainResolver();
        DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager) chainResolver
                .getFilterChainManager();

        for (String chainName : filterChainManager.getChainNames())
        {
            if (!chainName.endsWith(".htm"))
            {
                newfilterChains.put(chainName, filterChainManager.getChain(chainName));
            }
        }

        filterChainManager.setFilterChains(newfilterChains);

        for (Map.Entry<String, PathElement> e : getUrlPathElementMap().entrySet())
        {
            String peURL = e.getKey();
            PathElement pathElement = e.getValue();

            if (pathElement.isAuthRequired())
            {
                filterChainManager.addToChain(peURL, "authc");
            }

            List<Role> roles = getPathElementRoleDAO().getRoles(pathElement);
            if (!roles.isEmpty())
            {
                String rString = "";
                for (Iterator<Role> i = roles.iterator(); i.hasNext();)
                {
                    rString += i.next().getRole();
                    if (i.hasNext())
                    {
                        rString += ",";
                    }
                }
                filterChainManager.addToChain(peURL, "anyRoles", rString);
            }
        }

        // log filter-chains...
        logFilterChains(filterChainManager);
    }

    private void logFilterChains(DefaultFilterChainManager filterChainManager)
    {
        for (String chainName : filterChainManager.getChainNames())
        {
            StringBuilder sb = new StringBuilder();
            sb.append(chainName).append(" = ");
            NamedFilterList nfl = filterChainManager.getChain(chainName);
            for (Iterator<Filter> i = nfl.iterator(); i.hasNext();)
            {
                Filter f = i.next();
                sb.append(f.toString());

                if (f instanceof RolesAuthorizationFilter)
                {
                    try
                    {
                        Field field = PathMatchingFilter.class.getDeclaredField("appliedPaths");
                        field.setAccessible(true);
                        Map map = (Map)field.get(f);
                        String[] roles = (String[])map.get(chainName);
                        sb.append("[");
                        for (int j = 0; j < roles.length; j++)
                        {
                            sb.append(roles[j].toString());
                            if (j < roles.length -1)
                            {
                                sb.append(", ");
                            }
                        }
                        sb.append("]");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                
                if (i.hasNext())
                {
                    sb.append(", ");
                }
            }
            logger.info("resetSecurityFilters(): " + sb.toString());
        }
    }

    public void setPathElementHandlerMapping(PathElementHandlerMapping pathElementHandlerMapping)
    {
        if (null == this.pathElementHandlerMapping)
        {
            this.pathElementHandlerMapping = pathElementHandlerMapping;
        }
    }

    public PathElement getPathElement(HttpServletRequest request)
    {
        return getUrlPathElementMap().get(urlPathHelper.getLookupPathForRequest(request));
    }

    public Map<String, PathElementAbstractController> getPathElementControllers()
    {
        return pathElementHandlerMapping.getPathElementControllers();
    }

    public void populate(PathElement pathElement)
    {
        PathElementAbstractController c = getPathElementControllers().get(pathElement.getController());
        if (null != c)
        {
            pathElement.setControllerLabel(c.getLabel());
        }
        else
        {
            pathElement.setControllerLabel(pathElement.getController());
        }
    }

    public Map<Role, Boolean> getPathElementRoleMap(PathElement pathElement)
    {
        Map<Role, Boolean> map = new LinkedHashMap<Role, Boolean>();

        List<Role> roles = roleDAO.getAll();
        for (Role role : roles)
        {
            map.put(role, Boolean.FALSE);
        }

        roles = pathElementRoleDAO.getRoles(pathElement);
        for (Role role : roles)
        {
            map.put(role, Boolean.TRUE);
        }

        return map;
    }

    public void updatePathElementRoles(PathElement pe, String[] roleIds)
    {
        List<PathElementRole> prs = pathElementRoleDAO.getPathElementRoles(pe);
        for (PathElementRole pr : prs)
        {
            pathElementRoleDAO.delete(pr);
        }

        if (null != roleIds)
        {
            for (int i = 0; i < roleIds.length; i++)
            {
                Role role = roleDAO.getById(roleIds[i]);
                PathElementRole peRole = new PathElementRole();
                peRole.setPathElement(pe);
                peRole.setRole(role);
                pathElementRoleDAO.create(peRole);
            }
        }
    }

    public PathElementRoleDAO getPathElementRoleDAO()
    {
        return pathElementRoleDAO;
    }

}
