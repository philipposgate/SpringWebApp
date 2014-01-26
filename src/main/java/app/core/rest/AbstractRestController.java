package app.core.rest;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import app.core.AppService;
import app.core.ByteArrayDownloadView;
import app.core.PageContent;
import app.core.user.User;
import app.core.user.UserService;

public abstract class AbstractRestController
{
	protected static final String SUCCESS_KEY = "successful";

	@Autowired
	protected AppService appService;

   @Autowired
    protected UserService userService;

	@Autowired
	protected ServletContext servletContext;

	@Autowired
	protected SessionFactory sessionFactory;

	private HibernateTemplate ht;

    public HibernateTemplate getHt()
    {
        if (null == ht)
        {
            ht = new HibernateTemplate(sessionFactory);
        }
        return ht;
    }

	/**
	 * NOTE: don't commit controllers using this method as it'll pollute our log
	 * files in production
	 * 
	 * This is just a debugging tool that will dump HttpServletRequest
	 * parameters to the console.
	 */
	protected void dumpRequestParameters(HttpServletRequest request)
	{
		System.out.println("\n******* REQUEST PARAMETERS @ " + new Date()
				+ " *******");

		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		if (trace.length > 1)
		{

			System.out.println("******* File:" + trace[2].getFileName()
					+ " Method:" + trace[2].getMethodName() + " LineNumber:"
					+ trace[2].getLineNumber() + " *******");
		}

		Enumeration<String> requestParams = request.getParameterNames();
		while (requestParams.hasMoreElements())
		{
			String name = requestParams.nextElement();
			String[] values = request.getParameterValues(name);
			for (int i = 0; i < values.length; i++)
			{
				System.out.println("    " + name + " = " + values[i]);
			}
		}
	}

	protected ServletContext getServletContext()
	{
		return servletContext;
	}

	/**
	 * Returning a "download view" will invoke the client browser's "download
	 * file" dialog.
	 * 
	 * @param mimeType
	 *            - the mime-type of the downloaded file
	 * @param fileName
	 *            - the file name of the downloaded file
	 * @param fileData
	 *            - the contents of the downloaded file
	 */
	protected ModelAndView getDownloadView(String mimeType, String fileName,
			ByteArrayOutputStream fileData)
	{
		return new ModelAndView(new ByteArrayDownloadView(mimeType, fileName,
				fileData));
	}

	protected String jsonBooleanResponse(String key, boolean value)
	{
		JSONObject j = new JSONObject();
		try
		{
			j.put(key, value);
		}
		catch (JSONException e)
		{
		}
		return j.toString();
	}

	protected String jsonObjectResponse(JSONObject object)
	{
		return object.toString();
	}

	protected String getContentPage(String page, HttpServletRequest request,
			Model model, String defaultContent)
	{
		PageContent pageContent = null;

		if (page == null || request == null || model == null)
		{
			throw new RuntimeException("Bad Args");
		}
		
		List<PageContent> pList = getHt()
				.find("from PageContent p where p.page = ?", page);

		if (pList != null && !pList.isEmpty())
		{
			pageContent = pList.get(0);
		}
		else
		{
			pageContent = new PageContent();
			pageContent.setPage(page);
			pageContent.setContent(defaultContent == null ? page
					: defaultContent);
		}

		String pageContentAction = request.getParameter("pageContentAction");
		User userLoggedIn = userService.getUserLoggedIn();

		if (pageContentAction != null && userLoggedIn != null
				&& userService.userHasRole(userLoggedIn, UserService.ROLE_ADMIN))
		{
			if (pageContentAction.equalsIgnoreCase("edit"))
			{
				model.addAttribute("editMode", true);
			}
			else if (pageContentAction.equalsIgnoreCase("save")
					&& request.getParameter("htmlContent") != null)
			{
				pageContent.setContent(request.getParameter("htmlContent"));
				getHt().saveOrUpdate(pageContent);
			}
		}

		model.addAttribute("pageContent", pageContent);
		model.addAttribute("pageURL", request.getRequestURL());

		return page;
	}
}
