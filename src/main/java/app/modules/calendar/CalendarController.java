package app.modules.calendar;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import app.common.calendar.Calendar;
import app.common.calendar.CalendarDomain;
import app.common.calendar.CalendarList;
import app.common.calendar.CalendarService;
import app.common.calendar.Event;
import app.common.utils.DateUtils;
import app.core.pathElement.PathElementController;
import app.core.user.User;

@Controller
public class CalendarController extends PathElementController<CalendarDomain>
{
	public static final String DATE_FORMAT = "yyyy/MM/dd";
	public static final String TIME_FORMAT = "h:mm a";
	public static final String DATETIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

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
				
				Date now = new Date();
				calendarService.createEvent(userLoggedIn, calendar, "My Event", now, now, true);
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
		calendarService.populate(event);
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
		event.setLocation(request.getParameter("location"));
		event.setAllDay(null != request.getParameter("allDay"));
		
		Date startDate = null;
		String startDay = request.getParameter("startDay");
		String startTime = request.getParameter("startTime");
		
		Date endDate = null;
		String endDay = request.getParameter("endDay");
		String endTime = request.getParameter("endTime");

		if (event.isAllDay())
		{
			startDate = DateUtils.parseDate(startDay, DATE_FORMAT);
			endDate = DateUtils.parseDate(endDay, DATE_FORMAT);
		}
		else
		{
			startDate = DateUtils.parseDate(startDay + " " + startTime, DATETIME_FORMAT);
			endDate = DateUtils.parseDate(endDay + " " + endTime, DATETIME_FORMAT);
		}

		if (DateUtils.isBefore(endDate, startDate))
		{
			endDate = startDate;
		}
		
		event.setStartDate(startDate);
		event.setEndDate(endDate);

		calendarService.save(event);
		logger.info(event.toString());
		
		RedirectView rv = new RedirectView(getPathElement(request).getFullPath());
		rv.addStaticAttribute("action", "displayEventEdit");
		rv.addStaticAttribute("eventId", event.getId());
		rv.addStaticAttribute("successMessage", "Event Saved");

		return new ModelAndView(rv);
	}
}
