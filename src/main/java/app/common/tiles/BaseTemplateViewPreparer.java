package app.common.tiles;

import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.common.menu.MenuItem;
import app.core.pathElement.PathElement;
import app.core.pathElement.PathElementService;
import app.core.user.UserService;

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
        PathElement currentPathElement = (PathElement) request.getContext("request").get("pathElement");
        List<MenuItem> menuItems = pathElementService.getMenuItems(currentPathElement);
        request.getContext("request").put("menuItems", menuItems);
    }

}
