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

import app.common.AppService;
import app.common.menu.MenuItem;
import app.common.pathElement.PathElement;
import app.common.pathElement.PathElementService;
import app.common.user.User;
import app.common.user.UserService;

@Component
public class BaseTemplateViewPreparer implements ViewPreparer
{
    @Autowired
    private UserService userService;

    @Autowired
    private PathElementService pathElementService;

    @Override
    public void execute(Request request, AttributeContext context)
    {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();

        User userLoggedIn = userService.getUserLoggedIn();
        PathElement root = pathElementService.getRootElement();
        PathElement currentPathElement = (PathElement) request.getContext("request").get("pathElement");
        for (PathElement pathElement : root.getChildren())
        {
            if (!pathElement.isHideNavWhenUnauthorized() || pathElementService.isUserAllowed(userLoggedIn, pathElement))
            {
                MenuItem mi = new MenuItem();
                mi.setName(pathElement.getTitle());
                mi.setUrl(pathElement.getFullPath());
                mi.setChildren(new ArrayList<MenuItem>());
                populateMenuItems(mi, pathElement, currentPathElement, userLoggedIn);
                menuItems.add(mi);
            }
        }

        request.getContext("request").put("menuItems", menuItems);
    }

    private void populateMenuItems(MenuItem menuItem, PathElement pathElement, PathElement currentPathElement, User userLoggedIn)
    {
        if (null != currentPathElement && pathElement.equals(currentPathElement))
        {
            activate(menuItem);
        }

        for (PathElement child : pathElement.getChildren())
        {
            if (!child.isHideNavWhenUnauthorized() || pathElementService.isUserAllowed(userLoggedIn, child))
            {
                MenuItem subMI = new MenuItem();
                subMI.setParent(menuItem);
                subMI.setName(child.getTitle());
                subMI.setUrl(child.getFullPath());
                subMI.setChildren(new ArrayList<MenuItem>());
                populateMenuItems(subMI, child, currentPathElement, userLoggedIn);
                menuItem.getChildren().add(subMI);
            }
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
