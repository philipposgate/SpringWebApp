package app.web.SpringWebApp;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository(value = "appConfigDAO")
public class AppConfigDAO extends AbstractHibernateDAO<AppConfig>
{
	@Override
	public Class<AppConfig> getEntityClass()
	{
		return AppConfig.class;
	}

	public String getValue(String key)
	{
		String value = null;

		if (key != null)
		{
			List<String> list = getCurrentSession()
					.createQuery("select value from AppConfig where key=?")
					.setParameter(0, key).list();

			if (!list.isEmpty())
			{
				value = list.get(0);
			}
		}
		return value;
	}

	public AppConfig getAppConfig(String key)
	{
		AppConfig appConfig = null;

		if (key != null)
		{
			List<AppConfig> list = getCurrentSession()
					.createQuery("from AppConfig where key=?")
					.setParameter(0, key).list();

			if (!list.isEmpty())
			{
				appConfig = list.get(0);
			}
		}
		return appConfig;
	}
	
	public void saveConfig(String key, String value)
	{
		AppConfig appConfig = getAppConfig(key);
		
		if (null == appConfig)
		{
			appConfig = new AppConfig();
			appConfig.setKey(key);
		}
		
		appConfig.setValue(value);
		getCurrentSession().saveOrUpdate(appConfig);
	}
}
