package app.rest;

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
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import app.user.User;

public abstract class AbstractController
{
	public static final String SUCCESS_KEY = "successful";

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Session getCurrentSession()
	{
		return sessionFactory.getCurrentSession();
	}

	public Session getHt()
	{
		return getCurrentSession();
	}

	/**
	 * NOTE: don't commit controllers using this method as it'll pollute our log
	 * files in production
	 * 
	 * This is just a debugging tool that will dump HttpServletRequest
	 * parameters to the console.
	 */
	public void dumpRequestParameters(HttpServletRequest request)
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

		Enumeration requestParams = request.getParameterNames();
		while (requestParams.hasMoreElements())
		{
			String name = (String) requestParams.nextElement();
			String[] values = request.getParameterValues(name);
			for (int i = 0; i < values.length; i++)
			{
				System.out.println("    " + name + " = " + values[i]);
			}
		}
	}

	public ServletContext getServletContext()
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
	public ModelAndView getDownloadView(String mimeType, String fileName,
			ByteArrayOutputStream fileData)
	{
		return new ModelAndView(new ByteArrayDownloadView(mimeType, fileName,
				fileData));
	}

	public String jsonBooleanResponse(String key, boolean value)
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

	public String jsonObjectResponse(JSONObject object)
	{
		return object.toString();
	}

	public String getContentPage(String page, HttpServletRequest request,
			Model model, String defaultContent)
	{
		PageContent pageContent = null;

		if (page == null || request == null || model == null)
		{
			throw new RuntimeException("Bad Args");
		}

		List<PageContent> pList = getHt()
				.createQuery("from PageContent p where p.page = ?")
				.setParameter(0, page).list();

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
		User userLoggedIn = AppHelper.getUserLoggedIn();

		if (pageContentAction != null && userLoggedIn != null
				&& userLoggedIn.hasRole(AppHelper.ROLE_ADMIN))
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
