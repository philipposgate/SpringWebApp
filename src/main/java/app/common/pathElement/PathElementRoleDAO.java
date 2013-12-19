package app.common.pathElement;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.common.AbstractHibernateDAO;
import app.common.user.Role;

@Repository
@Transactional
public class PathElementRoleDAO extends AbstractHibernateDAO<PathElementRole>
{
    @Override
    public Class<PathElementRole> getEntityClass()
    {
        return PathElementRole.class;
    }
    
    public List<Role> getRoles(PathElement pathElement)
    {
        List<Role> roles =  sessionFactory.getCurrentSession()
                .createQuery("select pr.role from PathElementRole pr where pr.pathElement=?").setParameter(0, pathElement).list();
        return roles;
    }

    public List<PathElementRole> getPathElementRoles(PathElement pathElement)
    {
        List<PathElementRole> prs =  sessionFactory.getCurrentSession()
                .createQuery("from PathElementRole pr where pr.pathElement=?").setParameter(0, pathElement).list();
        return prs;
    }
}
