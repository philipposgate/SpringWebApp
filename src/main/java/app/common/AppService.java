package app.common;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class AppService implements ApplicationContextAware 
{
    private static ApplicationContext applicationContext = null;


	@Autowired
	private AppConfigDAO appConfigDAO;

	@Resource(name = "appSettings")
	private final Map<String, String> appSettings = null;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		AppService.applicationContext = applicationContext;
	}

	public String getConfigValue(String key)
	{
		return appConfigDAO.getValue(key);
	}

	public AppConfig getAppConfig(String key)
	{
		return appConfigDAO.getAppConfig(key);
	}

	public void saveConfig(String key, String value)
	{
		appConfigDAO.saveConfig(key, value);
	}

}
