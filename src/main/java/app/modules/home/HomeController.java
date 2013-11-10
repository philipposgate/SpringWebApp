package app.modules.home;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import app.web.AbstractWebController;

@Component(value = "homeController")
public class HomeController extends AbstractWebController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public ModelAndView displayHome(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	logger.info("displayHome");
        ModelAndView mv = new ModelAndView("home");
        mv.addObject("homeNav", "home");
        return mv;
    }
    


}
