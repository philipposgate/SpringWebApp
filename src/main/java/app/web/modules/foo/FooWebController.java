package app.web.modules.foo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.web.PathElementAbstractController;

@Controller
public class FooWebController extends PathElementAbstractController {

	@Override
	public ModelAndView displayHome(HttpServletRequest request,	HttpServletResponse response) 
	{
		return new ModelAndView("foo/foo_home");
	}

	@Override
	public String getLabel() 
	{
		return "Foo Controller";
	}

}
