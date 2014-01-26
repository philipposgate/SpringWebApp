package app.modules.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.common.pathElement.PathElementController;

@Controller
public class HomeController extends PathElementController
{
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Override
    public ModelAndView displayHome(HttpServletRequest request, HttpServletResponse response)
    {
        logger.info("displayHome");
        ModelAndView mv = new ModelAndView("home/home");
        return mv;
    }

    @Override
    public String getLabel()
    {
        return "Home Controller";
    }

    @Override
    public Class getDomainClass()
    {
        return null;
    }
}
