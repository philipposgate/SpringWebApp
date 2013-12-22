package app.common.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.common.menu.MenuItem;
import app.common.pathElement.PathElement;
import app.common.pathElement.PathElementService;

@Component
public class BaseTemplateViewPreparer implements ViewPreparer
{
    private static final Logger logger = LoggerFactory.getLogger(BaseTemplateViewPreparer.class);

    @Autowired
    private PathElementService pathElementService;
    
    @Override
    public void execute(Request request, AttributeContext context)
    {
        PathElement root = pathElementService.getRootElement();
        PathElement currentPathElement = (PathElement) request.getContext("request").get("pathElement");
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        for (PathElement pathElement : root.getChildren())
        {
            MenuItem mi = new MenuItem();
            mi.setName(pathElement.getTitle());
            mi.setUrl(pathElement.getFullPath());
            mi.setChildren(new ArrayList<MenuItem>());
            populateMenuItems(mi, pathElement, currentPathElement);
            menuItems.add(mi);
        }
            
        request.getContext("request").put("menuItems", menuItems);
    }

    private void populateMenuItems(MenuItem mi, PathElement pathElement, PathElement currentPathElement)
    {
        if (null != currentPathElement && pathElement.equals(currentPathElement))
        {
            activate(mi);
        }

        for (PathElement child : pathElement.getChildren())
        {
            MenuItem subMI = new MenuItem();
            subMI.setParent(mi);
            subMI.setName(child.getTitle());
            subMI.setUrl(child.getFullPath());
            subMI.setChildren(new ArrayList<MenuItem>());
            populateMenuItems(subMI, child, currentPathElement);
            mi.getChildren().add(subMI);
        }
    }

    private void activate(MenuItem mi)
    {
        mi.setActive(true);
        if (null != mi.getParent())
        {
            activate(mi.getParent());
        }
    }
}
