package app.common.utils;

import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.RRule;
import app.modules.calendar.Event;

public class ICalUtils
{
	public static VEvent getVEvent(Event event)
	{
		VEvent ve = null;

		try
		{
			if (event.isAllDay())
			{
				ve = new VEvent(new Date(event.getStartDate()), event.getTitle());
			}
			else
			{
				ve = new VEvent(new Date(event.getStartDate()), new Date(event.getEndDate()), event.getTitle());
			}

			if (event.isRepeats() && !StringUtils.isEmpty(event.getRrule()))
			{
				RRule rrule = new RRule(event.getRrule());
				ve.getProperties().add(rrule);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return ve;
	}
}
