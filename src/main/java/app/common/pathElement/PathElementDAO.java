package app.common.pathElement;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.common.AbstractHibernateDAO;

import com.google.common.base.Preconditions;

@Repository
@Transactional
public class PathElementDAO extends AbstractHibernateDAO<PathElement>
{
	@Override
	public Class<PathElement> getEntityClass()
	{
		return PathElement.class;
	}

	@Transactional
	public PathElement getRootPathElement()
	{
		PathElement root = null;
		
		List<PathElement> roots =  sessionFactory.getCurrentSession()
				.createQuery("from PathElement pe where pe.parent is null").list();

		if (!roots.isEmpty() && roots.size() == 1)
		{
			root = roots.get(0);
			populateChildren(root);
		}
		
		return root;
	}

	private void populateChildren(PathElement parent) {
		List<PathElement> children =  sessionFactory.getCurrentSession()
				.createQuery("from PathElement pe where pe.parent=?").setParameter(0, parent).list();
		
		for (PathElement child : children) {
			populateChildren(child);
		}
		
		parent.setChildren(children);
	}

	@Override
	public void check(PathElement pathElement)
	{
		Preconditions.checkNotNull(pathElement);
	}

}
