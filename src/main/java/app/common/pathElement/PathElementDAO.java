package app.common.pathElement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.common.AbstractHibernateDAO;

import com.google.common.base.Preconditions;

@Repository
@Transactional
public class PathElementDAO extends AbstractHibernateDAO<PathElement>
{

    @Autowired
    private PathElementRoleDAO pathElementRoleDAO;

    @Override
    public Class<PathElement> getEntityClass()
    {
        return PathElement.class;
    }

    public PathElement getRootPathElement()
    {
        PathElement root = null;

        List<PathElement> roots = sessionFactory.getCurrentSession()
                .createQuery("from PathElement pe where pe.parent is null").list();

        if (!roots.isEmpty() && roots.size() == 1)
        {
            root = roots.get(0);
            populateChildren(root);
            populateRoles(root);
        }

        return root;
    }

    private void populateRoles(PathElement pathElement)
    {
        pathElement.setRoles(pathElementRoleDAO.getRoles(pathElement));

        if (null != pathElement.getChildren() && !pathElement.getChildren().isEmpty())
        {
            for (PathElement child : pathElement.getChildren())
            {
                populateRoles(child);
            }
        }
    }

    public List<PathElement> getChildren(PathElement parent)
    {
        List<PathElement> children = sessionFactory.getCurrentSession()
                .createQuery("from PathElement pe where pe.parent=? and pe.active=1 order by pe.position")
                .setParameter(0, parent).list();
        return children;
    }

    public boolean hasChildren(PathElement pathElement)
    {
        long count = (Long) sessionFactory.getCurrentSession()
                .createQuery("select count(pe.id) from PathElement pe where pe.parent=? and pe.active=1")
                .setParameter(0, pathElement).list().get(0);

        return count > 0;
    }

    public void populateChildren(PathElement parent)
    {
        List<PathElement> children = getChildren(parent);

        for (PathElement child : children)
        {
            populateChildren(child);
        }

        parent.setChildren(children);
    }

    @Override
    public void check(PathElement pathElement)
    {
        Preconditions.checkNotNull(pathElement);
    }

    public PathElement getHomePathElement()
    {
        PathElement home = null;

        PathElement root = getRootPathElement();

        if (null != root)
        {
            List<PathElement> children = sessionFactory.getCurrentSession()
                    .createQuery("from PathElement pe where pe.parent=? and path='index'").setParameter(0, root).list();

            if (children.size() == 1)
            {
                home = children.get(0);
            }
        }

        return home;
    }

    public int getMaxPosition(PathElement parent)
    {
        Integer max = (Integer) sessionFactory.getCurrentSession()
                .createQuery("select MAX(pe.position) from PathElement pe where pe.parent=?").setParameter(0, parent)
                .list().get(0);
        
        if (null == max)
            return 0;
        else
            return max;
    }

}
