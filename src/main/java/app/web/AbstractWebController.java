package app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;

public abstract class AbstractWebController extends MultiActionController {

	public AbstractWebController()
	{
		ParameterMethodNameResolver r = new ParameterMethodNameResolver();
		r.setDefaultMethodName("displayHome");
		r.setParamName("action");
		this.setMethodNameResolver(r);
	}
	
	public abstract ModelAndView displayHome(HttpServletRequest request,
            HttpServletResponse response) throws Exception;
}
