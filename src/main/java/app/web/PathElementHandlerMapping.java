package app.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import app.common.pathElement.PathElementService;

@Component
public class PathElementHandlerMapping extends SimplePathElementHandler implements InitializingBean
{

    @Autowired
    private PathElementService pathElementService;

    private final Map<String, PathElementController> controllers = new TreeMap<String, PathElementController>();

    @Override
    public void initApplicationContext() throws BeansException
    {
        Map<String, Object> hmap = pathElementService.getUrlControllerMap();
        this.setUrlMap(hmap);

        super.initApplicationContext();
    }

    public void refreshUrlMappings()
    {
        clearHandlers();
        registerHandlers(pathElementService.getUrlControllerMap());
    }

    /**
     * Returns a Map where key=controller-bean-name, and
     * value=controller-bean-instance
     */
    public Map<String, PathElementController> getPathElementControllers()
    {
        if (controllers.isEmpty())
        {
            controllers.putAll(getApplicationContext().getBeansOfType(PathElementController.class));
        }

        return Collections.unmodifiableMap(controllers);
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        pathElementService.setPathElementHandlerMapping(this);
    }
}
