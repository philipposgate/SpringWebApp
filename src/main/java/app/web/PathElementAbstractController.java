package app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;

import app.common.pathElement.PathElement;

public abstract class PathElementAbstractController extends MultiActionController {

	public PathElementAbstractController()
	{
		ParameterMethodNameResolver r = new ParameterMethodNameResolver();
		r.setDefaultMethodName("displayHome");
		r.setParamName("action");
		this.setMethodNameResolver(r);
	}
	
	public abstract ModelAndView displayHome(HttpServletRequest request,
            HttpServletResponse response);
	
	/**
	 * To be called when a PathElement gets bound to this controller instance.  
	 * This method provides an opportunity to associate (bind) the path element 
	 * to something in this controller's data-model.
	 */
	public void bindToPathElement(PathElement pathElement) {
		// override this method to bind the pathElement to whatever object, as necessary...
	}
	
	public PathElement getPathElement(HttpServletRequest request)
	{
		PathElement ret = null;
		return ret;
	}
}
