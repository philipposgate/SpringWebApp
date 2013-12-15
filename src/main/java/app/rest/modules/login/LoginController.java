package app.rest.modules.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import app.rest.AbstractRestController;

@Controller
public class LoginController extends AbstractRestController 
{
	@RequestMapping(value = "/login")
	public String login() {
		return "/app/login";
	}
	
	@RequestMapping(value = "/accessDenied")
	public String accessDenied() {
		return "/app/accessDenied";
	}
}
