package app.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

public class PathElementHandlerMapping extends SimpleUrlHandlerMapping {

	@Override
	public void initApplicationContext() throws BeansException {
		Map<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("/home.htm", "homeController");
		hmap.put("/about.htm", "aboutController");
		this.setUrlMap(hmap);
		
		super.initApplicationContext();
		
		hmap.put("/contact.htm", "contactController");
		hmap.put("/pe.htm", "peAdminController");
		registerHandlers(hmap);

	}

}
