package app.modules.calendar;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.core.rest.AbstractRestController;

@Controller
@RequestMapping(value = "calendar/{domainId}")
public class CalendarRestController extends AbstractRestController
{
	@RequestMapping(value = "calEvents")
	@ResponseBody
	public String calEvents(@PathVariable("domainId") Long domainId, HttpServletRequest request)
	{
		System.out.println("domainId: " + domainId);
		dumpRequestParameters(request);
		
		JSONArray events = new JSONArray();
		return events.toString();
	}

}
