package app.modules.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "test")
public class TestController {

	@RequestMapping(value = "test1/")
	@ResponseBody
	public String test1()
	{
		return "test1";
	}
	
	@RequestMapping(value = "/testRedirect")
	public String testRedirect() {
		return "redirect:/user/";
	}

}
