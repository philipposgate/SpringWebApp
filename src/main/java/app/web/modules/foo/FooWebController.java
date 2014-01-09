package app.web.modules.foo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.web.PathElementController;

@Controller
public class FooWebController extends PathElementController
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
