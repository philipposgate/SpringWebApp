package app.common.utils;

import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.RRule;
import app.common.calendar.Event;

public class ICalUtils
{
	public static VEvent getVEvent(Event event) throws Exception
	{
		VEvent ve = null;

		if (event.isAllDay())
		{
			ve = new VEvent(new Date(event.getStartDate()), event.getTitle());
		}
		else
		{
			ve = new VEvent(new Date(event.getStartDate()), new Date(event.getEndDate()), event.getTitle());
		}

		if (event.isRepeats())
		{
			RRule rrule = new RRule(event.getRrule());
			ve.getProperties().add(rrule);
		}

		return ve;
	}
}
