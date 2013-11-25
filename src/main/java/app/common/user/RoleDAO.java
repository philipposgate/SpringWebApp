package app.common.user;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.AbstractHibernateDAO;

import com.google.common.base.Preconditions;

@Repository
@Transactional
public class RoleDAO extends AbstractHibernateDAO<Role>
{

	@Override
	public Class<Role> getEntityClass()
	{
		return Role.class;
	}

	public Role getRoleByName(String roleName)
	{
		Role role = null;

		List<Role> roles = sessionFactory.getCurrentSession()
				.createQuery("from Role r where r.role=?")
				.setParameter(0, roleName).list();

		if (!roles.isEmpty() && roles.size() == 1)
		{
			role = roles.get(0);
		}

		return role;

	}

	@Override
	public void check(Role role)
	{
		Preconditions.checkNotNull(role);
		Preconditions.checkNotNull(role.getRole());
	}

}
