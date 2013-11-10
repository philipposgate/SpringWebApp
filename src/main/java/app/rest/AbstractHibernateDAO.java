package app.rest;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

@Transactional
public abstract class AbstractHibernateDAO<T extends AbstractEntity>
{

	@Autowired
	protected SessionFactory sessionFactory;

	public abstract Class<T> getEntityClass();

	/**
	 * Meant to be overridden so that you can include your own custom guards
	 * that check the entity for validness. TODO: we might consider some kind of
	 * return type in the future. for now just throw runtime exceptions when
	 * something is invalid. (i like using google's Preconditions util)
	 */
	public void check(final T entity)
	{
	}

	protected final Session getCurrentSession()
	{
		return this.sessionFactory.getCurrentSession();
	}

	public T getById(final String id)
	{
		return getById(new Integer(id));
	}

	public T getById(final Integer id)
	{
		Preconditions.checkArgument(id != null);
		return (T) this.getCurrentSession().get(getEntityClass(), id);
	}

	public List<T> getAll()
	{
		return this.getCurrentSession()
				.createQuery("from " + getEntityClass().getName()).list();
	}

	public void create(final T entity)
	{
		check(entity);
		Preconditions.checkNotNull(entity);
		Preconditions.checkState(entity.getId() == null);
		this.getCurrentSession().persist(entity);
	}

	public void update(final T entity)
	{
		check(entity);
		Preconditions.checkNotNull(entity.getId());

		this.getCurrentSession().merge(entity);
	}

	public void delete(final T entity)
	{
		Preconditions.checkNotNull(entity);
		this.getCurrentSession().delete(entity);
	}

	public void deleteById(final Integer entityId)
	{
		final T entity = this.getById(entityId);
		Preconditions.checkState(entity != null);
		this.delete(entity);
	}

}