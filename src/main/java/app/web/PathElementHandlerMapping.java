package app.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import app.common.pathElement.PathElementService;

@Component
public class PathElementHandlerMapping extends SimpleUrlHandlerMapping implements InitializingBean {

	@Autowired
	private PathElementService pathElementService;

	@Override
	public void initApplicationContext() throws BeansException {
		
//		Map<String, Object> hmap = new HashMap<String, Object>();
//		hmap.put("/home.htm", "homeController");
//		hmap.put("/about.htm", "aboutController");
//		this.setUrlMap(hmap);
		
		super.initApplicationContext();
		
		Map<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("/home.htm", "homeController");
		hmap.put("/about.htm", "aboutController");
		hmap.put("/contact.htm", "contactController");
		registerHandlers(hmap);

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, Object> hmap = pathElementService.getUrlMap();
		this.setUrlMap(hmap);
		registerHandlers(hmap);

	}

}
