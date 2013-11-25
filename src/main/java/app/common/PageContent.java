package app.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "page_content")
public class PageContent extends AbstractEntity
{
	@Column(unique = true, nullable = false)
	private String page;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Override
	public String toString()
	{
		return content;
	}

	public String getPage()
	{
		return page;
	}

	public void setPage(String page)
	{
		this.page = page;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

}
