package app.web.modules;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.web.PathElementAbstractController;

@Controller
public class DefaultController extends PathElementAbstractController {

	@Override
	public ModelAndView displayHome(HttpServletRequest request,
			HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("default/default_home");
        return mv;
	}

	@Override
	public String getLabel() 
	{
		return "Default Controller";
	}

}
