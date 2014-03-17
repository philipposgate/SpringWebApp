package app.common.calendar.ical;

import app.common.calendar.Event;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.Version;


public class ICalHelper
{
	public static Calendar getICalendar()
    {
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		return calendar;
    }

	public static VEvent getVEvent(Event event) throws Exception
	{
		VEvent vevent = null;
		
		if (event.isAllDay())
		{
			vevent = new VEvent(new Date(event.getStartDate()), event.getTitle());
		}
		else
		{
			vevent = new VEvent(new Date(event.getStartDate()), new Date(event.getEndDate()), event.getTitle());
		}
		
		if (event.isRepeats())
		{
			RRule rrule = new RRule(event.getRrule());
			vevent.getProperties().add(rrule);
		}
		
		return vevent;
	}
}
