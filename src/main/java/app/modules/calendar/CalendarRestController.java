package app.modules.calendar;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.component.VEvent;

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
import app.common.calendar.ical.ICalHelper;
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

		populateNonRepeatingFullCalendarEvents(events, d, userLoggedIn, start, end);
		populateRepeatingFullCalendarEvents(events, d, userLoggedIn, start, end);

		return events.toString();
	}

	private void populateNonRepeatingFullCalendarEvents(JSONArray events, CalendarDomain d, User user, Date start,
	        Date end)
	{
		List<Event> evts = calendarService.getNonRepeatingEvents(d, user, start, end);
		for (Event e : evts)
		{
			try
			{
				events.put(getFullCalendarEvent(e));
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}

	private void populateRepeatingFullCalendarEvents(JSONArray events, CalendarDomain d, User user, Date start, Date end)
	{
		List<Event> revts = calendarService.getRepeatingEvents(d, user);
		Period period = new Period(new DateTime(start), new DateTime(end));
		for (Event e : revts)
		{
			try
			{
				VEvent ve = ICalHelper.getVEvent(e);
				PeriodList list = ve.calculateRecurrenceSet(period);

				for (Object po : list)
				{
					Period p = (Period) po;
					
					JSONObject event = new JSONObject();
					event.put("id", e.getId());
					event.put("title", e.getTitle());
					event.put("allDay", e.isAllDay());
					event.put("start", DateUtils.formatDate(p.getStart(), FULLCALENDAR_DATE_FORMAT));
					event.put("end", DateUtils.formatDate(p.getEnd(), FULLCALENDAR_DATE_FORMAT));

					if (!StringUtils.isEmpty(e.getLocation()))
					{
						event.put("location", e.getLocation());
					}

					if (null != e.getCalendar())
					{
						Calendar calendar = e.getCalendar();

						if (!StringUtils.isEmpty(calendar.getColorBackground())
						        && !StringUtils.isEmpty(calendar.getColorForeground()))
						{
							event.put("color", calendar.getColorBackground());
							event.put("textColor", calendar.getColorForeground());
						}

						JSONObject c = new JSONObject();
						c.put("id", calendar.getId());
						c.put("title", calendar.getTitle());
						event.put("calendar", c);
					}
					
					events.put(event);
				}
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}

	private JSONObject getFullCalendarEvent(Event e) throws Exception
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
			Calendar calendar = e.getCalendar();

			if (!StringUtils.isEmpty(calendar.getColorBackground())
			        && !StringUtils.isEmpty(calendar.getColorForeground()))
			{
				event.put("color", calendar.getColorBackground());
				event.put("textColor", calendar.getColorForeground());
			}

			JSONObject c = new JSONObject();
			c.put("id", calendar.getId());
			c.put("title", calendar.getTitle());
			event.put("calendar", c);
		}

		return event;
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

		return getFullCalendarEvent(event).toString();
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

		return getFullCalendarEvent(event).toString();
	}

	@RequestMapping(value = "updateEventDateTime", method = RequestMethod.POST)
	@ResponseBody
	public String updateEventDateTime(@PathVariable("domainId") Integer domainId, HttpServletRequest request)
	        throws Exception
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

		return getFullCalendarEvent(event).toString();
	}

	@RequestMapping(value = "updateCalendarViz/{calendarId}", method = RequestMethod.PUT)
	@ResponseBody
	public String updateCalendarViz(@PathVariable("domainId") Integer domainId, @PathVariable String calendarId,
	        HttpServletRequest request)
	{
		Calendar cal = getHt().load(Calendar.class, new Integer(calendarId));
		cal.setVisible("true".equals(request.getParameter("visible")));
		calendarService.save(cal);
		return "{}";
	}
}
