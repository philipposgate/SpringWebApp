package app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component(value = "peAdminController")
public class PathElementAdminController extends
		PathElementAbstractController {

	@Override
	public ModelAndView displayHome(HttpServletRequest request,
			HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("web/admin/pe/pe_adminHome");
        return mv;
	}

}
