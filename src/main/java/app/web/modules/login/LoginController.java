package app.web.modules.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.web.PathElementAbstractController;

@Controller
public class LoginController extends PathElementAbstractController {

	@Override
	public ModelAndView displayHome(HttpServletRequest request,	HttpServletResponse response) 
	{
		 ModelAndView mv = new ModelAndView("login/login_home");
	     return mv;	
	}

}