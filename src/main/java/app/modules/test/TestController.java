package app.modules.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.common.pathElement.PathElementController;


@Controller
public class TestController extends PathElementController
{

	@Override
    public ModelAndView displayHome(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mv = new ModelAndView("test/test_home");
        return mv;
    }

	@Override
    public Class getDomainClass()
    {
	    return null;
    }

}
