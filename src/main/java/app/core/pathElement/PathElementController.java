package app.core.pathElement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;

import app.core.Domain;
import app.core.user.UserService;

public abstract class PathElementController<D extends Domain> extends MultiActionController
{
	@Autowired
	protected SessionFactory sessionFactory;

	@Autowired
	private PathElementService pathElementService;

	@Autowired
	protected UserService userService;

	private HibernateTemplate ht;

	public abstract ModelAndView displayHome(HttpServletRequest request, HttpServletResponse response);

	public abstract Class<D> getDomainClass();

	public PathElementController()
	{
		ParameterMethodNameResolver r = new ParameterMethodNameResolver();
		r.setDefaultMethodName("displayHome");
		r.setParamName("action");
		this.setMethodNameResolver(r);
	}

	public HibernateTemplate getHt()
	{
		if (null == ht)
		{
			ht = new HibernateTemplate(sessionFactory);
		}
		return ht;
	}

	public String getLabel()
	{
		return this.getClass().getSimpleName();
	}

	public PathElement getPathElement(HttpServletRequest request)
	{
		return pathElementService.getPathElement(request);
	}

	/**
	 * All requests that get handled by this controller will pass through this
	 * method before and after the "action method" gets called. This allows us
	 * to apply certain global behavior to all HTTP requests.
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
	        throws Exception
	{
		ModelAndView mv = null;

		if ("GET".equalsIgnoreCase(request.getMethod()) && !request.getParameterMap().isEmpty())
		{
			// We don't like seeing GET parameters in the browser's address-bar,
			// so when we see a GET that includes parameters, we force the
			// client to resend the request as a POST...
			mv = new ModelAndView("pe/pe_repost");
			mv.addObject("reqParams", request.getParameterMap());
		}
		else
		{
			// Here's where we run the target ACTION...
			mv = super.handleRequestInternal(request, response);
		}

		// Include the PathElement object for the JSP...
		PathElement pathElement = getPathElement(request);
		mv.addObject("pathElement", pathElement);

		// Include the Controller's "Domain" object (if any) for the JSP...
		if (null != getDomainClass())
		{
			D domain = getDomain(pathElement.getDomainId());
			if (null != domain)
			{
				mv.addObject("domain", domain);
			}
			else
			{
				// The PathElement defines a "Domain class" and "Domain ID", but
				// he Domain was not found in the DB. Therefore handle this as
				// an
				// error.
				mv = new ModelAndView("pe/pe_domainError");
				mv.addObject("domainClass", getDomainClass());
			}
		}

		return mv;
	}

	public List<D> getAllDomains()
	{
		return null != getDomainClass() ? getHt().loadAll(getDomainClass()) : null;
	}

	public D getDomain(Integer domainId)
	{
		D domain = null;
		if (null != getDomainClass())
		{
			try
			{
				domain = getHt().load(getDomainClass(), domainId);
				if (null != domain && null == domain.getId())
				{
					domain = null;
				}
			}
			catch (Exception e)
			{
				domain = null;
				e.printStackTrace();
			}
		}
		return domain;
	}

	public D getDomain(HttpServletRequest request)
	{
		D domain = null;

		if (null != getDomainClass())
		{
			domain = getDomain(getPathElement(request).getDomainId());
		}

		return domain;
	}

	public D createNewDomain()
	{
		D domain = null;

		if (null != getDomainClass())
		{
			try
			{
				domain = getDomainClass().newInstance();
				domain.setDomainName(getDomainClass().getSimpleName());
				getHt().save(domain);

				domain.setDomainName(getDomainClass().getSimpleName() + " " + domain.getId());
				getHt().update(domain);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return domain;
	}
}
