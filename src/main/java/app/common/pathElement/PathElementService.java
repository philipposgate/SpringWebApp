package app.common.pathElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UrlPathHelper;

import app.common.utils.StringUtils;
import app.web.PathElementHandlerMapping;

@Service
public class PathElementService implements InitializingBean   {

	@Autowired
	private PathElementDAO pathElementDAO;
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();

	private PathElementHandlerMapping pathElementHandlerMapping;
	
	private PathElement rootElement;
	
	private final Map<String, PathElement> pathElementMap = new HashMap<String, PathElement>();
	
	@Override
	@Transactional
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
		
		PathElement loginElement = pathElementDAO.getLoginPathElement();
		
		if (null == loginElement)
		{
			loginElement = new PathElement();
			loginElement.setPath("login");
			loginElement.setController("loginController");
			loginElement.setParent(rootElement);
			loginElement.setTitle("Login");
			loginElement.setActive(true);
			pathElementDAO.create(loginElement);
		}
		
		pathElementDAO.populateChildren(rootElement);
	}
	
	public Map<String, PathElement> getPathElementMap()
	{
		if (pathElementMap.isEmpty())
		{
			PathElement rootElement = getRootElement();
			for (PathElement child : rootElement.getChildren())
			{
				populatePathElementMap(child);
			}
		}
		return Collections.unmodifiableMap(pathElementMap);
	}

	private void populatePathElementMap(PathElement pathElement) 
	{
		pathElementMap.put(pathElement.getFullPath(), pathElement);
		for (PathElement child : pathElement.getChildren())
		{
			populatePathElementMap(child);
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

	public Map<String, Object> getUrlMap() 
	{
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		PathElement root = getRootElement();
		
		if (null != root && null != root.getChildren() && !root.getChildren().isEmpty())
		{
			for (PathElement child : root.getChildren())
			{
				populateUrlMap(map, child);
			}
		}

		return map;
	}

	private void populateUrlMap(Map<String, Object> map, PathElement pathElement) {

		String urlPath = pathElement.getFullPath();
		map.put(urlPath, pathElement.getController());
		
		if (null != pathElement.getChildren() && !pathElement.getChildren().isEmpty())
		{
			for (PathElement child : pathElement.getChildren())
			{
				populateUrlMap(map, child);
			}
		}
	}
	
	public void refreshUrlMappings()
	{
		rootElement = null;
		pathElementMap.clear();
		pathElementHandlerMapping.refreshUrlMappings();
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
		return getPathElementMap().get(urlPathHelper.getLookupPathForRequest(request));
	}
}
