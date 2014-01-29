package app.modules.calendar;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import app.common.calendar.Calendar;
import app.common.calendar.CalendarList;
import app.common.calendar.CalendarService;
import app.core.pathElement.PathElementController;
import app.core.user.User;

@Controller
public class CalendarController extends PathElementController<CalendarDomain>
{
	@Autowired
	protected CalendarService calendarService;

	@Override
	public ModelAndView displayHome(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView mv = new ModelAndView("cal/cal_adminHome");

		CalendarDomain d = getDomain(request);
		User userLoggedIn = userService.getUserLoggedIn();

		if (null != userLoggedIn)
		{
			List<CalendarList> calLists = calendarService.getCalendarLists(userLoggedIn);

			if (calLists.isEmpty())
			{
				CalendarList calList = calendarService.createCalendarList(userLoggedIn, "My Calendars");
				Calendar calendar = calendarService.createCalendar(userLoggedIn, "My Calendar");
				calendarService.bind(calList, calendar);
				calendarService.populate(calList);
				calLists.add(calList);
			}
			mv.addObject("calLists", calLists);
		}

		return mv;
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
