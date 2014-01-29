package app.modules.calendar;

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
import org.springframework.web.bind.annotation.ResponseBody;

import app.common.calendar.CalendarDomain;
import app.common.calendar.CalendarService;
import app.common.calendar.Event;
import app.common.utils.DateUtils;
import app.core.rest.AbstractRestController;
import app.core.user.User;

@Controller
@RequestMapping(value = "calendar/{domainId}")
public class CalendarRestController extends AbstractRestController
{
	private static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sZ";

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
		event.put("start", DateUtils.formatDate(e.getStartDate(), JSON_DATE_FORMAT));
		event.put("end", DateUtils.formatDate(e.getEndDate(), JSON_DATE_FORMAT));

		return event;
	}

}
