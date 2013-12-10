package app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;
import org.springframework.web.util.UrlPathHelper;

import app.common.pathElement.PathElement;
import app.common.pathElement.PathElementService;

public abstract class PathElementAbstractController extends MultiActionController
{
	@Autowired
	private PathElementService pathElementService;
	
	public PathElementAbstractController()
	{
		ParameterMethodNameResolver r = new ParameterMethodNameResolver();
		r.setDefaultMethodName("displayHome");
		r.setParamName("action");
		this.setMethodNameResolver(r);
	}
	
	public abstract ModelAndView displayHome(HttpServletRequest request,
            HttpServletResponse response);

	public abstract String getLabel();

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
		return pathElementService.getPathElement(request);
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = super.handleRequestInternal(request, response);
		mv.addObject("pathElement", getPathElement(request));
		return mv;
	}
	
}
