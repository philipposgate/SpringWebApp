package app.rest.modules.appointments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import app.common.google.GoogleCalendarService;
import app.common.google.GoogleEmailerService;
import app.common.utils.DateUtils;
import app.common.utils.StringUtils;
import app.rest.AbstractRestController;

@Controller
@RequestMapping(value = "appts")
public class AppointmentController extends AbstractRestController
{

	private static final int APPT_LENGTH_MINUTES = 30;
	private static final String LOCATION_CODE_DEFAULT = "defaultLoc";
	private static final String LOCATION_CODE_OTHER = "otherLoc";

	private static final String GOOGLE_CALENDAR_ID = "appointmentGoogleCalendarId";
	private static final String GOOGLE_CALENDAR_NAME = "Spray Tan Appointments";
	private static final String GOOGLE_CALENDAR_LOCATION = "St Catharines, Ontario, Canada";

	@Autowired
	private GoogleEmailerService gmailService;

	@Autowired
	private GoogleCalendarService gcalService;

	@RequestMapping(value = "/")
	public String displayHome(Model model)
	{
		model.addAttribute("apptLengthMinutes", APPT_LENGTH_MINUTES);
		return "/appt/appt_home";
	}

	@RequestMapping(value = "/bookAppt", method = RequestMethod.POST)
	@Transactional
	public String bookAppt(HttpServletRequest request)
	{
		Appointment appt = new Appointment();
		appt.setDateCreated(new Date());
		appt.setCustomerName(request.getParameter("customerName"));
		appt.setCustomerPhone(request.getParameter("customerPhone"));
		appt.setCustomerEmail(request.getParameter("customerEmail"));

		String apptDate = request.getParameter("apptDate");
		String apptTime = request.getParameter("apptTime");
		Date startDate = DateUtils.parseDate(apptDate + " " + apptTime, "MM/dd/yyyy HHmm");
		appt.setApptStart(startDate);

		String unitAmount = request.getParameter("unitAmount");
		appt.setUnitAmount(StringUtils.isInteger(unitAmount) ? new Integer(unitAmount) : 1);

		Date endDate = DateUtils.addMinute(appt.getApptStart(), appt.getUnitAmount() * APPT_LENGTH_MINUTES);
		appt.setApptEnd(endDate);

		appt.setLocationCode(request.getParameter("locationCode"));
		if (LOCATION_CODE_OTHER.equals(appt.getLocationCode()))
		{
			appt.setLocAddress(request.getParameter("locAddress"));
			appt.setLocCity(request.getParameter("locCity"));
		}

		appt.setCustomerMessage(request.getParameter("customerMessage"));

		appt.setConfirmationCode(StringUtils.getRandomAlphaNumeric(10));
		getHt().saveOrUpdate(appt);

		gmailService.sendMail(appt.getCustomerEmail(), "Spray Tan Appointment Confirmation", "thanks!");

		refreshGoogleCalendar();

		return "redirect:/appts/bookingReceived/" + StringUtils.URL_encrypt(appt.getId().toString());
	}

	@RequestMapping(value = "/admin/ajaxRefreshGoogleCalendar/")
	@ResponseBody
	public String ajaxRefreshGoogleCalendar()
	{
		refreshGoogleCalendar();
		return jsonBooleanResponse(SUCCESS_KEY, true);
	}

	private synchronized void refreshGoogleCalendar()
	{
		Calendar calendar = getCalendar();

		System.out.println("\nCalendar ID: " + calendar.getId());
		if (calendar != null)
		{
			List<Appointment> apptList = getHt().find("from Appointment");

			removeDeletedEvents(calendar, apptList);
			addNewEvents(calendar, apptList);
		}
	}

	private void addNewEvents(Calendar calendar, List<Appointment> apptList)
	{
		try
		{
			List<Event> events = getAllEvents(calendar);
			List<Appointment> apptsToInsert = new ArrayList<Appointment>();
			boolean found = false;

			for (Appointment appt : apptList)
			{
				found = false;

				for (Event event : events)
				{
					if (null != appt.getGoogleEventId() && appt.getGoogleEventId().equals(event.getId()))
					{
						found = true;
						break;
					}
				}

				if (!found)
				{
					apptsToInsert.add(appt);
				}
			}

			for (Appointment appt : apptsToInsert)
			{
				Event event = new Event();
				// event.setId(appt.getConfirmationCode());
				event.setSummary(appt.getCustomerName() + " x" + appt.getUnitAmount());

				ArrayList<EventAttendee> attendees = new ArrayList<EventAttendee>();
				EventAttendee attendee = new EventAttendee();
				attendee.setEmail(appt.getCustomerEmail());
				attendee.setDisplayName(appt.getCustomerName());
				attendee.setAdditionalGuests(appt.getUnitAmount() - 1);
				attendee.setComment(appt.getCustomerMessage());
				attendees.add(attendee);
				event.setAttendees(attendees);

				DateTime start = new DateTime(appt.getApptStart(), TimeZone.getTimeZone("UTC"));
				event.setStart(new EventDateTime().setDateTime(start));

				DateTime end = new DateTime(appt.getApptEnd(), TimeZone.getTimeZone("UTC"));
				event.setEnd(new EventDateTime().setDateTime(end));

				Event createdEvent = gcalService.getService().events().insert(calendar.getId(), event).execute();
				System.out.println(createdEvent.getId());

				appt.setGoogleEventId(createdEvent.getId());
				getHt().update(appt);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void removeDeletedEvents(Calendar calendar, List<Appointment> apptList)
	{
		try
		{
			List<Event> events = getAllEvents(calendar);
			List<Event> eventsToDelete = new ArrayList<Event>();
			boolean found = false;

			for (Event event : events)
			{
				found = false;

				for (Appointment appt : apptList)
				{
					if (null != appt.getGoogleEventId() && appt.getGoogleEventId().equals(event.getId()))
					{
						found = true;
						break;
					}
				}

				if (!found)
				{
					eventsToDelete.add(event);
				}
			}

			for (Event deleteThis : eventsToDelete)
			{
				gcalService.getService().events().delete(calendar.getId(), deleteThis.getId()).execute();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private List<Event> getAllEvents(Calendar calendar)
	{
		List<Event> ret = new ArrayList<Event>();

		try
		{
			String pageToken = null;
			do
			{
				Events events = gcalService.getService().events().list(calendar.getId()).setPageToken(pageToken)
						.execute();

				ret.addAll(events.getItems());

				pageToken = events.getNextPageToken();
			}
			while (pageToken != null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return ret;
	}

	private Calendar getCalendar()
	{
		Calendar calendar = null;
		
		try
		{
			calendar = gcalService.getService().calendars().get(appService.getConfigValue(GOOGLE_CALENDAR_ID)).execute();
			
			if (calendar == null)
			{
				calendar = initGoogleCalendar();
			}
		}
		catch (Exception e)
		{
			calendar = initGoogleCalendar();
		}
		
		return calendar;
	}

	private Calendar initGoogleCalendar()
	{
		Calendar calendar = null;
		calendar = new Calendar();
		calendar.setSummary(GOOGLE_CALENDAR_NAME);
		calendar.setTimeZone("America/Toronto");
		calendar.setLocation(GOOGLE_CALENDAR_LOCATION);

		try
		{
			calendar = gcalService.getService().calendars().insert(calendar).execute();
			appService.saveConfig(GOOGLE_CALENDAR_ID, calendar.getId());
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		return calendar;
	}

	@RequestMapping(value = "/bookingReceived/{encryptedApptId}")
	@Transactional
	public String displayBookingReceived(@PathVariable String encryptedApptId, Model model)
	{
		Integer apptId = new Integer(StringUtils.URL_decrypt(encryptedApptId));
		Appointment appt = (Appointment) getHt().load(Appointment.class, apptId);
		model.addAttribute("appt", appt);
		return "/appt/appt_bookingReceived";
	}

	@RequestMapping(value = "/admin/adminHome/")
	public String displayAdminHome(Model model)
	{
		model.addAttribute("adminNav", "apptMgt");
		return "/appt/appt_adminHome";
	}

	@RequestMapping(value = "/admin/ajaxLoadAppts")
	@ResponseBody
	public String ajaxLoadAppts(HttpServletRequest request)
	{
		JSONArray appts = new JSONArray();

		try
		{
			Date start = new Date(new Long(request.getParameter("start")) * 1000);
			Date end = new Date(new Long(request.getParameter("end")) * 1000);
			List<Appointment> apptList = getHt().find("from Appointment a where a.apptStart between ? and ?", new Object[]{start, end});

			Date now = new Date();
			for (Appointment a : apptList)
			{
				JSONObject appt = new JSONObject();
				appt.put("id", a.getId());
				appt.put("allDay", false);
				appt.put("title", a.getCustomerName() + " x" + a.getUnitAmount());
				appt.put("start", a.getApptStart().getTime() / 1000);
				appt.put("end", a.getApptEnd().getTime() / 1000);
				appt.put("past", now.after(a.getApptStart()));
				appts.put(appt);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return appts.toString();
	}
	
	@RequestMapping(value = "/admin/adminConfig/")
	public String displayAdminConfig(HttpServletRequest request, Model model)
	{
		model.addAttribute("adminNav", "apptMgt");
		
		if (null != request.getSession().getAttribute("adminConfigSaved"))
		{
			model.addAttribute("successMessage", "Appointment Configuration Saved.");
			request.getSession().removeAttribute("adminConfigSaved");
		}
		
		return "/appt/appt_adminConfig";
	}
	
	@RequestMapping(value = "/admin/adminSaveConfig/")
	public String saveConfig(HttpServletRequest request)
	{
		request.getSession().setAttribute("adminConfigSaved", true);
		return "redirect:/appts/admin/adminConfig/";
	}
	
}
