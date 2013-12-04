package app.web;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import app.common.pathElement.PathElementService;

@Component
public class PathElementHandlerMapping extends SimpleUrlHandlerMapping {

	@Autowired
	private PathElementService pathElementService;

	@Override
	public void initApplicationContext() throws BeansException {

		Map<String, Object> hmap = pathElementService.getUrlMap();
		this.setUrlMap(hmap);
		
		super.initApplicationContext();
	}

}
