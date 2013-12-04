package app.rest.modules.pathElement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@RequestMapping(value = "/")
	public String displayHome(Model model)
	{
		return "/pe/pe_restHome";
	}

	@RequestMapping(value = "/json")
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
			JSONObject node = new JSONObject();
			node.put("id", child.getId());
			node.put("data", child.getTitle());
			nodes.put(node);
		}
		
		return nodes.toString();
	}
}
