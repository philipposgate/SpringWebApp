package app.modules.calendar;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import app.common.calendar.Calendar;
import app.common.calendar.CalendarDomain;
import app.common.calendar.CalendarService;
import app.common.calendar.Event;
import app.common.utils.DateUtils;
import app.common.utils.StringUtils;
import app.core.rest.AbstractRestController;
import app.core.user.User;

@Controller
@RequestMapping(value = "calendar/{domainId}")
public class CalendarRestController extends AbstractRestController
{
	private static final String FULLCALENDAR_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sZ";

	@Autowired
	protected CalendarService calendarService;

	@RequestMapping(value = "userEvents")
	@ResponseBody
	public String userEvents(@PathVariable("domainId") Integer domainId, HttpServletRequest request)
	{
		Date start = null;
		Date end = null;

		try
		{
			start = new Date(Long.parseLong(request.getParameter("start")) * 1000);
		}
		catch (Exception e)
		{
		}

		try
		{
			end = new Date(Long.parseLong(request.getParameter("end")) * 1000);
		}
		catch (Exception e)
		{
		}

		CalendarDomain d = getHt().load(CalendarDomain.class, domainId);
		User userLoggedIn = userService.getUserLoggedIn();

		JSONArray events = new JSONArray();

		List<Event> evts = calendarService.getEvents(d, userLoggedIn, start, end);
		for (Event e : evts)
		{
			try
			{
				events.put(getEventJSON(e));
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}

		return events.toString();
	}

	private JSONObject getEventJSON(Event e) throws Exception
	{
		JSONObject event = new JSONObject();
		event.put("id", e.getId());
		event.put("title", e.getTitle());
		event.put("allDay", e.isAllDay());
		event.put("start", DateUtils.formatDate(e.getStartDate(), FULLCALENDAR_DATE_FORMAT));
		event.put("end", DateUtils.formatDate(e.getEndDate(), FULLCALENDAR_DATE_FORMAT));
		
		if (!StringUtils.isEmpty(e.getLocation()))
		{
			event.put("location", e.getLocation());
		}

		if (null != e.getCalendar())
		{
			event.put("calendar", getCalendarJSON(e.getCalendar()));
		}

		return event;
	}

	private JSONObject getCalendarJSON(Calendar c) throws Exception
	{
		JSONObject calendar = new JSONObject();
		calendar.put("id", c.getId());
		calendar.put("title", c.getTitle());
		return calendar;
	}

	@RequestMapping(value = "createEvent", method = RequestMethod.POST)
	@ResponseBody
	public String createEvent(@PathVariable("domainId") Integer domainId, HttpServletRequest request) throws Exception
	{
		User userLoggedIn = userService.getUserLoggedIn();
		String title = request.getParameter("title");
		Calendar calendar = calendarService.getCalendar(request);
		Date startDate = DateUtils.parseDate(request.getParameter("startDate"), "yyyy-MM-dd HH:mm");
		Date endDate = DateUtils.parseDate(request.getParameter("endDate"), "yyyy-MM-dd HH:mm");
		boolean allDay = "true".equalsIgnoreCase(request.getParameter("allDay"));

		// CREATE EVENT...
		Event event = calendarService.createEvent(userLoggedIn, calendar, title, startDate, endDate, allDay);

		return getEventJSON(event).toString();
	}

	@RequestMapping(value = "deleteEvent", method = RequestMethod.POST)
	@ResponseBody
	public String deleteEvent(@PathVariable("domainId") Integer domainId, HttpServletRequest request) throws Exception
	{
		User userLoggedIn = userService.getUserLoggedIn();
		Event event = calendarService.getEvent(request);

		if (userLoggedIn.getId().equals(event.getOwner().getId()))
		{
			calendarService.delete(event);
		}

		return getEventJSON(event).toString();
	}
	
	@RequestMapping(value = "updateEventDateTime", method = RequestMethod.POST)
	@ResponseBody
	public String updateEventDateTime(@PathVariable("domainId") Integer domainId, HttpServletRequest request) throws Exception
	{
		User userLoggedIn = userService.getUserLoggedIn();
		Event event = calendarService.getEvent(request);
		
		if (userLoggedIn.getId().equals(event.getOwner().getId()))
		{
			Date startDate = DateUtils.parseDate(request.getParameter("startDate"), "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(request.getParameter("endDate"), "yyyy-MM-dd HH:mm");
			if (null == endDate || DateUtils.isBefore(endDate, startDate))
			{
				endDate = startDate;
			}
			event.setStartDate(startDate);
			event.setEndDate(endDate);

			event.setAllDay("true".equalsIgnoreCase(request.getParameter("allDay")));

			calendarService.save(event);
			
			logger.info(event.toString());
		}
		
		return getEventJSON(event).toString();
	}
}
