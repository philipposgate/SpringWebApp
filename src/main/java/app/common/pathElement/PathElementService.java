package app.common.pathElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UrlPathHelper;

import app.web.PathElementAbstractController;
import app.web.PathElementHandlerMapping;

@Service
public class PathElementService implements InitializingBean   {

	@Autowired
	private PathElementDAO pathElementDAO;
	
	@Autowired
	private AbstractShiroFilter shiroFilter;
	
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

	private void populateUrlControllerMap(Map<String, Object> map, PathElement pathElement) {

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
		
		PathMatchingFilterChainResolver chainResolver = (PathMatchingFilterChainResolver) this.shiroFilter.getFilterChainResolver();
		DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager)chainResolver.getFilterChainManager();
		
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
			if (e.getValue().isAuthRequired())
			{
				filterChainManager.addToChain(e.getKey(), "authc");
			}
		}
		
		for (String chainName : filterChainManager.getChainNames())
		{
			System.out.println(chainName);
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

}
