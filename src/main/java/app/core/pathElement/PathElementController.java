package app.core.pathElement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
	        throws Exception
	{
		ModelAndView mv = super.handleRequestInternal(request, response);

		PathElement pathElement = getPathElement(request);
		mv.addObject("pathElement", pathElement);

		if (null != getDomainClass())
		{
			D domain = getDomain(pathElement.getDomainId());
			if (null != domain)
			{
				mv.addObject("domain", domain);
			}
			else
			{
				mv = displayDomainError(request);
			}
		}

		return mv;
	}

	private ModelAndView displayDomainError(HttpServletRequest request)
    {
	    ModelAndView mv = new ModelAndView("pe/pe_domainError");
		PathElement pathElement = getPathElement(request);
		mv.addObject("pathElement", pathElement);
		mv.addObject("domainClass", getDomainClass());
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
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return domain;
	}
}
