package app.core;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * View that downloads a ByteArrayOutputStream to the client
 */
public class ByteArrayDownloadView implements DownloadView
{
	private final String contentType;

	private final String filename;

	private final ByteArrayOutputStream baos;

	public ByteArrayDownloadView(String contentType, String filename,
			ByteArrayOutputStream baos)
	{
		this.contentType = contentType;
		this.filename = filename;
		this.baos = baos;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (baos == null)
		{
			throw new RuntimeException("baos is null");
		}
		else
		{
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ filename + "\"");
			response.setContentType(contentType);
			response.setContentLength(baos.size());
			baos.writeTo(response.getOutputStream());
			try
			{
				response.getOutputStream().flush();
			}
			catch (Exception e)
			{
			}
		}
	}

	@Override
	public String getContentType()
	{
		return contentType;
	}

}
