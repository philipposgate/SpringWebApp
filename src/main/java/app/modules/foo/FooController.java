package app.modules.foo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.core.pathElement.PathElementController;

@Controller
public class FooController extends PathElementController
{

    @Override
    public ModelAndView displayHome(HttpServletRequest request, HttpServletResponse response)
    {
        return new ModelAndView("foo/foo_home");
    }

    @Override
    public String getLabel()
    {
        return "Foo Controller";
    }

    @Override
    public Class getDomainClass()
    {
        return null;
    }
}
