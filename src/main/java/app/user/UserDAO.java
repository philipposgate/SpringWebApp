package app.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import app.AbstractHibernateDAO;

import com.google.common.base.Preconditions;

@Repository(value = "userDAO")
public class UserDAO extends AbstractHibernateDAO<User>
{
	@Override
	public Class<User> getEntityClass()
	{
		return User.class;
	}

	public User getUserByUsername(String username)
	{
		User user = null;

		if (username != null)
		{
			List<User> users = sessionFactory.getCurrentSession()
					.createQuery("from User u where u.username=?")
					.setParameter(0, username).list();

			if (!users.isEmpty() && users.size() == 1)
			{
				user = users.get(0);
			}
		}

		return user;
	}

	@Override
	public void check(User user)
	{
		Preconditions.checkNotNull(user);
		Preconditions.checkNotNull(user.getCreateDate());
		Preconditions.checkNotNull(user.getFirstName());
		Preconditions.checkNotNull(user.getLastName());
		Preconditions.checkNotNull(user.getEmail());
		Preconditions.checkNotNull(user.getUsername());
		Preconditions.checkNotNull(user.getPassword());
		Preconditions.checkState(user.getUsername().length() >= 1);
		Preconditions.checkState(user.getPassword().length() >= 1);
	}

}
