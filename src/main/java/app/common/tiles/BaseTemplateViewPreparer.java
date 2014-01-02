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
        PathElement currentPathElement = (PathElement) request.getContext("request").get("pathElement");
        List<MenuItem> menuItems = pathElementService.getMenuItems(currentPathElement);
        request.getContext("request").put("menuItems", menuItems);
    }

}
