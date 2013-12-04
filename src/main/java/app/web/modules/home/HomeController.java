package app.web.modules.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.web.PathElementAbstractController;

@Controller
public class HomeController extends PathElementAbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public ModelAndView displayHome(HttpServletRequest request,
            HttpServletResponse response) {
    	logger.info("displayHome");
        ModelAndView mv = new ModelAndView("home/home");
        mv.addObject("homeNav", "home");
        return mv;
    }

}
