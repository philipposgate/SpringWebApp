package app.modules.calendar;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import app.common.calendar.Calendar;
import app.common.calendar.CalendarDomain;
import app.common.calendar.CalendarList;
import app.common.calendar.CalendarService;
import app.common.calendar.Event;
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

		CalendarDomain calendarDomain = getDomain(request);
		User userLoggedIn = userService.getUserLoggedIn();

		if (null != calendarDomain && null != userLoggedIn)
		{
			List<CalendarList> calLists = calendarService.getCalendarLists(calendarDomain, userLoggedIn);

			if (calLists.isEmpty())
			{
				CalendarList calList = calendarService.createCalendarList(calendarDomain, userLoggedIn, "My Calendars");
				Calendar calendar = calendarService.createCalendar(userLoggedIn, "My Calendar");
				calendarService.bind(calList, calendar);
				calendarService.populate(calList);
				calLists.add(calList);
				
				Event event = calendarService.createQuickEvent(userLoggedIn, calendar, "My Event");
				calendarService.bind(calendar, event);
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

	public ModelAndView displayEventEdit(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView mv = new ModelAndView("cal/cal_eventEdit");
		Event event = calendarService.getEvent(request);
		mv.addObject("event", event);
		return mv;
	}
	
	public ModelAndView saveEvent(HttpServletRequest request, HttpServletResponse response)
	{
		Event event = calendarService.getEvent(request);
		
		if (null == event)
		{
			event = new Event();
		}
		event.setTitle(request.getParameter("title"));
		getHt().saveOrUpdate(event);
		
		RedirectView rv = new RedirectView(getPathElement(request).getFullPath());
		rv.addStaticAttribute("action", "displayEventEdit");
		rv.addStaticAttribute("eventId", event.getId());
		ModelAndView mv = new ModelAndView(rv);
		return mv;
	}
}
