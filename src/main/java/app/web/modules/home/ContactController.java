package app.web.modules.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import app.web.PathElement;
import app.web.PathElementAbstractController;

@Component
public class ContactController extends PathElementAbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    public ModelAndView displayHome(HttpServletRequest request,
            HttpServletResponse response) {
    	logger.info("displayHome");
        ModelAndView mv = new ModelAndView("contact");
        mv.addObject("homeNav", "contact");
        return mv;
    }

}
