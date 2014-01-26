package app.modules.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "test/{domainId}")
public class TestRestController {

	@RequestMapping(value = "test1")
	@ResponseBody
	public String test1(@PathVariable("domainId") Long domainId)
	{
	    System.out.println(domainId);
		return "test1";
	}
	
	@RequestMapping(value = "testRedirect")
	public String testRedirect() {
		return "redirect:/rest/user";
	}

}
