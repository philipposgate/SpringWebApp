package app.web.modules.pathElement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.common.pathElement.PathElementService;
import app.web.PathElementAbstractController;

@Controller
public class PathElementAdminController extends
		PathElementAbstractController {

	@Autowired
	private PathElementService pathElementService;
	
	@Override
	public ModelAndView displayHome(HttpServletRequest request,
			HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("pe/pe_adminHome");
        mv.addObject("root", pathElementService.getRootElement());
        return mv;
	}


	@Override
	public String getLabel() 
	{
		return "Path Element Admin Controller";
	}

}
