package app.web.SpringWebApp.home;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.web.SpringWebApp.AbstractController;

/**
 * Handles custom requests for the application .
 */
@Controller
public class HomeController extends AbstractController
{

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	private static final String defaultContent = "<div><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis id velit metus. Aenean lobortis adipiscing lacus, nec condimentum ligula blandit ac. Ut at odio a velit ullamcorper ullamcorper a vel sapien. Suspendisse mattis mauris eu neque consectetur at pretium turpis lobortis. Nunc id eros urna, at fringilla metus. Donec quis magna augue, et pharetra nunc. Quisque a sapien quis mi venenatis ullamcorper. Donec scelerisque, enim quis venenatis hendrerit, elit ante iaculis justo, vitae lobortis tortor urna et ante. Donec mollis posuere nisl, at porta nibh porttitor consectetur. Ut felis metus, pretium non fringilla ut, pellentesque tempor nulla. Ut a libero odio, eu tempor nulla. Sed faucibus, neque cursus elementum molestie, leo libero tincidunt tortor, sit amet volutpat purus sem non nisi. Pellentesque urna nisl, tincidunt non iaculis nec, feugiat in arcu. Praesent elementum, sapien tincidunt blandit sollicitudin, tortor est convallis lectus, eu eleifend purus eros in quam. Nunc vel sapien quis libero feugiat interdum.</p><p>Cras id tristique quam. Sed eget lorem leo, ac malesuada augue. Suspendisse leo neque, mattis sed convallis molestie, faucibus a massa. Nullam aliquam lacus ut mauris sollicitudin non scelerisque sapien auctor. Morbi nec eros libero, nec pulvinar risus. Pellentesque in elit eu nulla feugiat elementum a nec enim. Vivamus turpis ligula, pretium pulvinar aliquet et, viverra vel lectus. Sed sit amet convallis libero. Nunc imperdiet nisi a lacus lobortis ultrices. Donec volutpat suscipit pretium. Phasellus in velit eget augue molestie vulputate ut ut mauris.</p><p>Vivamus in ligula in felis faucibus lacinia. Sed pretium placerat dapibus. Morbi nec turpis egestas diam dictum imperdiet. Vestibulum vulputate eleifend turpis, a placerat ipsum tincidunt at. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam aliquet, nisl eget pharetra ornare, ipsum metus ornare purus, id porttitor sapien dui non elit. Integer adipiscing rutrum leo at cursus.</p><p>Nam purus felis, eleifend eleifend tincidunt a, fermentum non odio. Cras gravida justo in tellus vulputate varius. In feugiat, dui nec volutpat ultricies, libero erat tempor urna, ac fringilla augue enim a felis. Proin accumsan neque nec nulla faucibus cursus. Curabitur adipiscing tincidunt lacus a iaculis. Cras nec libero arcu, mollis laoreet orci. Praesent tellus enim, mattis id porta vel, sodales quis nunc. Vestibulum eu nisl in est accumsan faucibus. Proin vehicula, lectus sed commodo posuere, elit leo vestibulum diam, quis tempus mi enim vel nisi.</p><p>Nunc quam libero, tincidunt sit amet tincidunt vel, sollicitudin vel diam. Ut non ligula sapien. Mauris scelerisque nisi ut ligula luctus eget volutpat velit aliquam. Integer aliquet consectetur justo vel dignissim. Vivamus eu turpis massa, eu eleifend purus. Nam id semper elit. Suspendisse eget nulla sapien. Nunc aliquam bibendum quam, tempor ultricies ligula auctor cursus. Ut tellus enim, blandit ac mattis nec, venenatis mattis nisi. In sed felis risus, ut consectetur nisi. Suspendisse et congue nulla. In lacinia, nisi eget fermentum ornare, risus dolor sollicitudin orci, id iaculis velit enim molestie odio.</p></div>";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model)
	{
		model.addAttribute("homeNav", "home");
		return "home";
	}

	@RequestMapping(value = "/about")
	@Transactional
	public String about(HttpServletRequest request, Model model)
	{
		model.addAttribute("homeNav", "about");
		return getContentPage("about", request, model, defaultContent);
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact(Locale locale, Model model)
	{
		model.addAttribute("homeNav", "contact");
		return "contact";
	}

}
