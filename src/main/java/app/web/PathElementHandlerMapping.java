package app.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import app.common.pathElement.PathElementService;

@Component
public class PathElementHandlerMapping extends SimplePathElementHandler {

	@Autowired
	private PathElementService pathElementService;

	private final Map<String, PathElementAbstractController> controllers = new TreeMap<String, PathElementAbstractController>();

	@Override
	public void initApplicationContext() throws BeansException 
	{
		Map<String, Object> hmap = pathElementService.getUrlControllerMap();
		this.setUrlMap(hmap);
		
		super.initApplicationContext();
		
		pathElementService.setPathElementHandlerMapping(this);
	}

	public void refreshUrlMappings() 
	{
		clearHandlers();
		registerHandlers(pathElementService.getUrlControllerMap());
	}

	/**
	 * Returns a Map where key=controller-bean-name, and value=controller-bean-instance
	 */
	public Map<String, PathElementAbstractController> getPathElementControllers()
	{
		if (controllers.isEmpty())
		{
			controllers.putAll(getApplicationContext().getBeansOfType(PathElementAbstractController.class));
		}
		
		return Collections.unmodifiableMap(controllers);
	}
}
