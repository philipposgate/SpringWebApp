package app.web.SpringWebApp.appointments;

import java.util.Date;
import java.util.List;

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

import app.web.SpringWebApp.AbstractController;
import app.web.SpringWebApp.AppGMailer;
import app.web.SpringWebApp.utils.DateUtils;
import app.web.SpringWebApp.utils.StringUtils;

@Controller
@RequestMapping(value = "appts")
public class AppointmentController extends AbstractController {

	private static final int APPT_LENGTH_MINUTES = 30;
	private static final String LOCATION_CODE_DEFAULT = "defaultLoc";
	private static final String LOCATION_CODE_OTHER = "otherLoc";
	
	@Autowired
	private AppGMailer gmailService;
	
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
		
		return "redirect:/appts/bookingReceived/" + StringUtils.URL_encrypt(appt.getId().toString());
	}

	
	
	@RequestMapping(value = "/bookingReceived/{encryptedApptId}")
	@Transactional
	public String bookingReceived(@PathVariable String encryptedApptId, Model model)
	{
		Integer apptId = new Integer(StringUtils.URL_decrypt(encryptedApptId));
		Appointment appt = (Appointment) getHt().load(Appointment.class, apptId);
		model.addAttribute("appt", appt);
		return "/appt/appt_bookingReceived";
	}
	
	@RequestMapping(value = "/admin/adminHome/")
	public String adminHome(Model model)
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
			List<Appointment> apptList = getHt()
					.createQuery("from Appointment a where a.apptStart between ? and ?").setParameter(0, start).setParameter(1, end).list();
			
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
}
