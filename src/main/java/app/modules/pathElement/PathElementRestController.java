package app.modules.pathElement;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import app.core.Domain;
import app.common.menu.MenuItem;
import app.core.pathElement.PathElement;
import app.core.pathElement.PathElementDAO;
import app.core.pathElement.PathElementService;
import app.core.rest.AbstractRestController;
import app.common.utils.StringUtils;

@Controller
@RequestMapping(value = "admin/pe")
public class PathElementRestController extends AbstractRestController
{
    private static final Logger logger = LoggerFactory.getLogger(PathElementRestController.class);

    @Autowired
    private PathElementDAO pathElementDAO;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private PathElementService pathElementService;

    @RequestMapping()
    public String displayHome(Model model)
    {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.handlerMapping.getHandlerMethods();
        model.addAttribute("handlerMethods", handlerMethods);
        model.addAttribute("adminNav", "endPoints");

        return "/pe/pe_restHome";
    }

    @RequestMapping(value = "treeNodes", method = RequestMethod.GET)
    @ResponseBody
    public String getTreeNodes(HttpServletRequest request) throws Exception
    {
        JSONArray nodes = new JSONArray();

        Integer id = new Integer(request.getParameter("id"));

        if (id > 0)
        {
            // Return children of node...
            PathElement node = pathElementDAO.getById(id);

            List<PathElement> children = pathElementDAO.getChildren(node);

            for (PathElement child : children)
            {
                JSONObject childNode = getTreeNode(child);

                if (pathElementDAO.hasChildren(child))
                {
                    childNode.put("state", "closed");
                }

                nodes.put(childNode);
            }
        }
        else
        {
            // Return root-node plus root's children...
            PathElement rootElement = pathElementService.getRootElement();
            JSONObject rootNode = getTreeNode(rootElement);

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

            nodes.put(rootNode);
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

    @RequestMapping(value = "treeNode", method = RequestMethod.POST)
    @ResponseBody
    public String createTreeNode(HttpServletRequest request) throws Exception
    {
        PathElement parent = pathElementDAO.getById(request.getParameter("parentId"));
        PathElement newElement = null;

        if (null != parent)
        {
            String name = request.getParameter("name");
            name = StringUtils.isEmpty(name) ? "New Path Element" : name;

            newElement = new PathElement();
            newElement.setPath(name.replaceAll("\\s+", "").toLowerCase());
            newElement.setControllerBeanName("defaultController");
            newElement.setParent(parent);
            newElement.setTitle(name);
            newElement.setActive(true);
            newElement.setPosition(pathElementDAO.getMaxPosition(parent) + 1);
            pathElementDAO.create(newElement);

            pathElementService.refreshUrlMappings();
        }

        return getTreeNode(newElement).toString();
    }

    @RequestMapping(value = "treeNode/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteTreeNode(@PathVariable Integer id) throws Exception
    {
        PathElement pe = pathElementDAO.getById(id);
        pe.setActive(false);
        pathElementDAO.update(pe);
        pathElementService.refreshUrlMappings();
        return "success";
    }

    @RequestMapping(value = "moveTreeNode", method = RequestMethod.POST)
    @ResponseBody
    public String moveTreeNode(HttpServletRequest request) throws Exception
    {
        String newParentId = request.getParameter("newParentId");
        String newChildren[] = request.getParameterValues("newChildren[]");
        String prevParentId = request.getParameter("prevParentId");
        String prevChildren[] = request.getParameterValues("prevChildren[]");
        
        if (newParentId.equals(prevParentId))
        {
            for (int i = 0; i < newChildren.length; i++)
            {
                PathElement pe = pathElementDAO.getById(newChildren[i]);
                pe.setPosition(i + 1);
                pathElementDAO.update(pe);
            }
        }
        else
        {
            PathElement newParent = pathElementDAO.getById(newParentId);
            for (int i = 0; i < newChildren.length; i++)
            {
                PathElement pe = pathElementDAO.getById(newChildren[i]);
                pe.setParent(newParent);
                pe.setPosition(i + 1);
                pathElementDAO.update(pe);
            }

            PathElement prevParent = pathElementDAO.getById(prevParentId);
            if (null != prevChildren)
            {
                for (int i = 0; i < prevChildren.length; i++)
                {
                    PathElement pe = pathElementDAO.getById(prevChildren[i]);
                    pe.setParent(prevParent);
                    pe.setPosition(i + 1);
                    pathElementDAO.update(pe);
                }
            }
        }

        pathElementService.refreshUrlMappings();

        return "success";
    }
    
    
    @RequestMapping(value = "reloadNav")
    public String reloadNav(Model model) throws Exception
    {
        List<MenuItem> menuItems = pathElementService.getMenuItems(null);
        model.addAttribute("menuItems", menuItems);
        return "/templates/baseHeaderMenuItems";
    }

    @RequestMapping(value = "pathElement/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updatePathElement(HttpServletRequest request, @PathVariable Integer id) throws Exception
    {
        PathElement pe = pathElementDAO.getById(id);

        if (null != pe)
        {
            String title = request.getParameter("title");
            String path = request.getParameter("path");
            String controller = request.getParameter("controller");
            String domainName = request.getParameter("domainName");
            boolean authRequired = null != request.getParameter("authRequired");
            boolean allRolesRequired = "true".equalsIgnoreCase(request.getParameter("allRolesRequired"));
            boolean hideNavWhenUnauthorized = null != request.getParameter("hideNavWhenUnauthorized");
            String roleIds[] = request.getParameterValues("roleId");
            
            pe.setTitle(title);
            
            if (!pe.isRoot())
            {
                pe.setPath(path.replaceAll("\\s+", "").toLowerCase());
                pe.setControllerBeanName(controller);
            }

            pe.setAuthRequired(authRequired);
            pe.setAllRolesRequired(allRolesRequired);
            pe.setHideNavWhenUnauthorized(hideNavWhenUnauthorized);

            pathElementDAO.update(pe);
            
            if (null != domainName)
            {
                try
                {
                    pathElementService.populate(pe);
                    Domain domain = pe.getController().getDomain(pe.getDomainId());
                    domain.setDomainName(domainName);
                    getHt().update(domain);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            
            pathElementService.updatePathElementRoles(pe, roleIds);
            pathElementService.refreshUrlMappings();
        }

        return getTreeNode(pe).toString();
    }

    @RequestMapping(value = "pathElementView/{id}")
    public String displayPathElementView(@PathVariable Integer id, Model model)
    {
        PathElement pathElement = pathElementDAO.getById(id);
        pathElementService.populate(pathElement);
        model.addAttribute("pathElement", pathElement);
        return "/pe/pe_restPathElementView";
    }

    @RequestMapping(value = "pathElementEdit/{id}")
    public String displayPathElementEdit(@PathVariable Integer id, Model model)
    {
        PathElement pathElement = pathElementDAO.getById(id);
        pathElementService.populate(pathElement);
        model.addAttribute("pathElement", pathElement);
        model.addAttribute("controllers", pathElementService.getPathElementControllers());
        model.addAttribute("roleMap", pathElementService.getPathElementRoleMap(pathElement));
        return "/pe/pe_restPathElementEdit";
    }

    @RequestMapping(value = "{id}/createNewDomain")
    @ResponseBody
    public String createNewDomain(@PathVariable Integer id)
    {
        PathElement pathElement = pathElementDAO.getById(id);
        pathElementService.populate(pathElement);
        Domain domain = pathElement.getController().createNewDomain();
        pathElement.setDomainId(domain.getId());
        getHt().update(pathElement);
        pathElementService.refreshUrlMappings();
        logger.info("New Domain \"" + domain.getDomainName() + "\" created for PathElement " + pathElement.getFullPath());
        return "success";
    }
}
