package app.web.SpringWebApp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "app_config")
public class AppConfig extends AbstractEntity
{
	@Column(name = "keyName")
	private String key;

	@Column(columnDefinition = "TEXT")
	private String value;

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
