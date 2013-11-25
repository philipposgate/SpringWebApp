package app.common.pathElement;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathElementService implements InitializingBean {

	@Autowired
	private PathElementDAO pathElementDAO;
	
	private PathElement rootElement;
	
	@Override
	public void afterPropertiesSet() throws Exception 
	{
		rootElement = pathElementDAO.getRootPathElement();
		
		if (null == rootElement)
		{
			rootElement = new PathElement();
			rootElement.setPath("index");
			rootElement.setController("pathElementAdminController");
			rootElement.setParent(null);
			pathElementDAO.create(rootElement);
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
		populateUrlMap(map, getRootElement());
		return map;
	}

	private void populateUrlMap(Map<String, Object> map, PathElement pathElement) {

		String urlPath = pathElement.getFullPath() + ".htm";
		map.put(urlPath, pathElement.getController());
		
		if (null != pathElement.getChildren() && !pathElement.getChildren().isEmpty())
		{
			for (PathElement child : pathElement.getChildren())
			{
				populateUrlMap(map, child);
			}
		}
	}

}
