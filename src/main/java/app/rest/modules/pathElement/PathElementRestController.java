package app.rest.modules.pathElement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
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
		PathElement rootElement = pathElementService.getRootElement();
		JSONObject rootNode = getTreeNode(rootElement);

		try 
		{
			if (!rootElement.isLeaf())
			{
				rootNode.put("state", "open");

				JSONArray children = new JSONArray();

				for (PathElement child : rootElement.getChildren())
				{
					JSONObject childNode = getTreeNode(child);

					if (!child.isLeaf())
					{
						childNode.put("state", "closed");
					}
					
					children.put(childNode);
				}
				
				rootNode.put("children", children);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		model.addAttribute("rootElement", rootNode);
		return "/pe/pe_restHome";
	}

	@RequestMapping(value = "/pathElementView/{id}")
	public String displayPathElementView(@PathVariable Integer id, Model model)
	{
		PathElement pathElement = pathElementDAO.getById(id);
		pathElementService.populate(pathElement);
		model.addAttribute("pathElement", pathElement);
		return "/pe/pe_restPathElementView";
	}

	@RequestMapping(value = "/pathElementEdit/{id}")
	public String displayPathElementEdit(@PathVariable Integer id, Model model)
	{
		PathElement pathElement = pathElementDAO.getById(id);
		model.addAttribute("pathElement", pathElement);
		model.addAttribute("controllers", pathElementService.getPathElementControllers());
		return "/pe/pe_restPathElementEdit";
	}

	@RequestMapping(value = "/treeNodes", method=RequestMethod.GET)
	@ResponseBody
	public String getTreeNodes(HttpServletRequest request) throws Exception
	{
		PathElement parent = pathElementDAO.getById(request.getParameter("id"));
		
		List<PathElement> children = pathElementDAO.getChildren(parent);
		
		JSONArray nodes = new JSONArray();
		
		for (PathElement child : children) 
		{
			JSONObject childNode = getTreeNode(child);
			
			if (pathElementDAO.hasChildren(child))
			{
				childNode.put("state", "closed");
			}
			
			nodes.put(childNode);
		}
		
		return nodes.toString();
	}
	
	private JSONObject getTreeNode(PathElement pe)
	{
		JSONObject node = new JSONObject();
		try 
		{
			node.put("data", pe.getTitle());
			
			JSONObject attr = new JSONObject();
			attr.put("id", pe.getId());
			node.put("attr", attr);
		} 
		catch (Exception e) 
		{
		}
		return node;
	}
	
	@RequestMapping(value = "/treeNode", method=RequestMethod.POST)
	@ResponseBody
	public String createTreeNode(HttpServletRequest request) throws Exception
	{
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

		return getTreeNode(newElement).toString();
	}
	
	@RequestMapping(value = "/treeNode/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public String deleteTreeNode(@PathVariable Integer id) throws Exception
	{
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
		PathElement pe = pathElementDAO.getById(id);
		
		if (null != pe)
		{
			boolean updated = false;
			
			String title = request.getParameter("title");
			if (!StringUtils.isEmpty(title) && !title.equals(pe.getTitle()))
			{
				pe.setTitle(title);
				updated = true;
			}
			
			String path = request.getParameter("path");
			if (!StringUtils.isEmpty(path) && !path.equals(pe.getPath()))
			{
				pe.setPath(path.replaceAll("\\s+", "").toLowerCase());
				updated = true;
			}
			
			String controller = request.getParameter("controller");
			if (!StringUtils.isEmpty(controller) && !controller.equals(pe.getController()))
			{
				pe.setController(controller);
				updated = true;
			}
			
			if (updated)
			{
				pathElementDAO.update(pe);
				pathElementService.refreshUrlMappings();
			}
			
		}
		
		return getTreeNode(pe).toString();
	}	

}
