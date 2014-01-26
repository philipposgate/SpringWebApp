package app.modules.defalt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.core.pathElement.PathElementController;

@Controller
public class DefaultController extends PathElementController
{
    @Override
    public ModelAndView displayHome(HttpServletRequest request, HttpServletResponse response)
    {
        return new ModelAndView("default/default_home");
    }

    @Override
    public String getLabel()
    {
        return "Default Controller";
    }

    @Override
    public Class getDomainClass()
    {
        return null;
    }

}
