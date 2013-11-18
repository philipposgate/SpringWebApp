package app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component(value = "pathElementAdminController")
public class PathElementAdminController extends
		PathElementAbstractController {

	@Autowired
	private PathElementService pathElementService;
	
	@Override
	public ModelAndView displayHome(HttpServletRequest request,
			HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("web/admin/pe/pe_adminHome");
        mv.addObject("root", pathElementService.getRootElement());
        return mv;
	}

}
