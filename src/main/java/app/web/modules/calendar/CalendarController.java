package app.web.modules.calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.web.PathElementController;

@Controller
public class CalendarController extends PathElementController<CalendarDomain>
{

    @Override
    public ModelAndView displayHome(HttpServletRequest request, HttpServletResponse response)
    {
        CalendarDomain d = getDomain(request);
        return new ModelAndView("cal/cal_adminHome");
    }

    @Override
    public String getLabel()
    {
        return "Calendar Module";
    }

    @Override
    public Class<CalendarDomain> getDomainClass()
    {
        return CalendarDomain.class;
    }

}
