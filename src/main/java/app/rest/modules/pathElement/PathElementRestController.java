package app.rest.modules.pathElement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import app.common.pathElement.PathElement;
import app.common.pathElement.PathElementDAO;
import app.common.pathElement.PathElementService;
import app.common.utils.StringUtils;
import app.rest.AbstractRestController;

@Controller
@RequestMapping(value = "pe")
public class PathElementRestController extends AbstractRestController
{
	@Autowired
	private PathElementDAO pathElementDAO;
	
	@Autowired
	private PathElementService pathElementService;
	
	@RequestMapping(value = "/")
	public String displayHome(Model model)
	{
		model.addAttribute("rootElement", getNode(pathElementDAO.getRootPathElement()));
		return "/pe/pe_restHome";
	}

	@RequestMapping(value = "/json", method=RequestMethod.GET)
	@ResponseBody
	public String getJson(HttpServletRequest request) throws Exception
	{
		dumpRequestParameters(request); 
		PathElement pe = null;
		String id = request.getParameter("id");
		
		if (!StringUtils.isInteger(id) || new Integer(id) <= 0)
		{
			pe = pathElementDAO.getRootPathElement();
		}
		else
		{
			pe = pathElementDAO.getById(id);	
		}
		
		List<PathElement> children = pathElementDAO.getChildren(pe);
		
		JSONArray nodes = new JSONArray();
		
		for (PathElement child : children) 
		{
			nodes.put(getNode(child));
		}
		
		return nodes.toString();
	}
	
	private JSONObject getNode(PathElement pe)
	{
		JSONObject node = new JSONObject();
		try {
			node.put("data", pe.getTitle());
			node.put("state", "closed");
			
			JSONObject attr = new JSONObject();
			attr.put("id", pe.getId());
			node.put("attr", attr);
		} catch (Exception e) {
		}
		return node;
	}
	
	@RequestMapping(value = "/pathElement", method=RequestMethod.POST)
	@ResponseBody
	public String createPathElement(HttpServletRequest request) throws Exception
	{
		dumpRequestParameters(request);
		
		PathElement parent = pathElementDAO.getById(request.getParameter("parentId"));
		PathElement newElement = null;
		
		if (null != parent)
		{
			String name = StringUtils.isEmpty(request.getParameter("name")) ? "New Path Element" : request.getParameter("name");
			newElement = new PathElement();
			newElement.setPath(name.replaceAll("\\s+", "").toLowerCase());
			newElement.setController("defaultController");
			newElement.setParent(parent);
			newElement.setTitle(name);
			newElement.setActive(true);
			pathElementDAO.create(newElement);
			
			pathElementService.refreshUrlMappings();
		}
		
		JSONObject node = getNode(newElement);

		return node.toString();
	}
	
	@RequestMapping(value = "/pathElement/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public String deletePathElement(@PathVariable Integer id) throws Exception
	{
		System.out.println("deletePathElement: " + id);
		
		PathElement pe = pathElementDAO.getById(id);
		pe.setActive(false);
		pathElementDAO.update(pe);
		
		pathElementService.refreshUrlMappings();
		
		return "success";
	}
	
	@RequestMapping(value = "/pathElement/{id}", method=RequestMethod.PUT)
	@ResponseBody
	public String updatePathElement(HttpServletRequest request, @PathVariable Integer id) throws Exception
	{
		dumpRequestParameters(request);
		
		PathElement pe = pathElementDAO.getById(id);
		String name = StringUtils.isEmpty(request.getParameter("name")) ? "Path Element " + id : request.getParameter("name");
		pe.setTitle(name);
		pathElementDAO.update(pe);
		
		pathElementService.refreshUrlMappings();

		return "success";
	}	

}
