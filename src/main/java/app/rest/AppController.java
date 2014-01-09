package app.rest;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import app.common.AppService;
import app.common.google.GoogleEmailerService;
import app.common.user.User;
import app.common.user.UserDAO;
import app.common.user.UserService;

@Controller
public class AppController extends AbstractRestController
{

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private GoogleEmailerService gmailService;

    @RequestMapping(value = "user/{userId}", method = RequestMethod.POST)
    public String userUpdate(@PathVariable String userId, Model model, HttpServletRequest request)
    {
        User userLoggedIn = userService.getUserLoggedIn();

        if (userLoggedIn != null && userLoggedIn.getId().equals(new Integer(userId)))
        {
            User user = userService.getUserById(userId);
            bindUser(request, user);

            userDAO.update(user);

            userService.setUserLoggedIn(user);

            model.addAttribute("saved", true);
        }
        return userHome(model);
    }

    @RequestMapping(value = "user")
    public String userHome(Model model)
    {
        return "app/user/userHome";
    }

    @RequestMapping(value = "admin")
    public String adminHome(Model model)
    {
        return "app/admin/admin_home";
    }

    @RequestMapping(value = "admin/endPoints")
    public String endPoints(Model model)
    {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.handlerMapping.getHandlerMethods();
        model.addAttribute("handlerMethods", handlerMethods);
        model.addAttribute("adminNav", "endPoints");

        return "app/admin/admin_endPoints";
    }

    @RequestMapping(value = "admin/users")
    public String adminUsrMgt(Model model)
    {
        model.addAttribute("users", userDAO.getAll());
        return "app/admin/admin_usrMgt";
    }

    @RequestMapping(value = "admin/users/{userId}", method = RequestMethod.GET)
    public String adminUsrLoad(@PathVariable String userId, Model model)
    {
        User user = userDAO.getById(userId);
        model.addAttribute("user", user);
        return "app/admin/admin_ajaxUserDetails";
    }

    @RequestMapping(value = "admin/users/{userId}", method = RequestMethod.POST)
    public String adminUsrSave(@PathVariable String userId, Model model, HttpServletRequest request)
    {
        User user = userDAO.getById(userId);

        bindUser(request, user);
        user.setAccountNonExpired(request.getParameter("accountNonExpired") == null);
        user.setAccountNonLocked(request.getParameter("accountNonLocked") == null);
        user.setCredentialsNonExpired(request.getParameter("credentialsNonExpired") == null);
        user.setEnabled(request.getParameter("enabled") != null);

        userDAO.update(user);

        System.out.println(user);
        model.addAttribute("userUpdated", true);
        return adminUsrMgt(model);
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.GET)
    public String registerUserView()
    {
        return "/app/registerUser";
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public String registerUserSave(HttpServletRequest request)
    {
        User user = new User();
        user.setCreateDate(new Date());
        user.setEnabled(true);
        bindUser(request, user);

        userDAO.create(user);

        user.getRoles().add(userService.getRole(UserService.ROLE_USER));

        userDAO.update(user);

        userService.setUserLoggedIn(user);

        return "redirect:/rest/user/";
    }

    @RequestMapping(value = "/checkUsername", method = RequestMethod.GET)
    @ResponseBody
    public String checkUsername(@RequestParam("username") String username)
    {
        boolean ok = false;

        if (username != null)
        {
            User userLoggedIn = userService.getUserLoggedIn();

            if (userLoggedIn == null)
            {
                ok = userService.getUserByUsername(username) == null;
            }
            else
            {
                ok = getHt().find("from User u where u.id != ? and u.username=?", new Object[]{userLoggedIn.getId(), username}).isEmpty();
            }
        }

        System.out.println("checkUsername: " + username + (ok ? " [ok]" : " [not ok]"));

        return String.valueOf(ok);
    }

    @RequestMapping(value = "/checkUsername/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public String checkUsername(@PathVariable String userId, @RequestParam("username") String username)
    {
        boolean ok = false;

        if (username != null)
        {
            User user = userDAO.getById(userId);

            if (user == null)
            {
                ok = userService.getUserByUsername(username) == null;
            }
            else
            {
                ok = getHt().find("from User u where u.id != ? and u.username=?", new Object[]{user.getId(), username}).isEmpty();;
            }
        }

        System.out.println("checkUsername: " + username + (ok ? " [ok]" : " [not ok]"));

        return String.valueOf(ok);
    }

    @RequestMapping(value = "/sessionExpired")
    public String sessionExpired()
    {
        System.out.println("session expired!");
        return "redirect:/login";
    }

    private void bindUser(HttpServletRequest request, User user)
    {
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));
        user.setUsername(request.getParameter("username"));

        String password = request.getParameter("password");
        if (!password.equals(request.getParameter("confirmPassword")))
        {
            throw new IllegalArgumentException("password mismatch!");
        }
        user.setPassword(password);
    }

    @RequestMapping(value = "admin/emailMgt")
    public String adminEmailMgt(Model model)
    {
        model.addAttribute("adminNav", "emailMgt");
        model.addAttribute("gmailUsername", appService.getConfigValue("gmailUsername"));
        model.addAttribute("gmailPassword", appService.getConfigValue("gmailPassword"));
        return "app/admin/admin_emailMgt";
    }

    @RequestMapping(value = "admin/saveEmailSettings", method = RequestMethod.POST)
    public String saveEmailSettings(HttpServletRequest request)
    {
        appService.saveConfig("gmailUsername", request.getParameter("gmailUsername"));
        appService.saveConfig("gmailPassword", request.getParameter("gmailPassword"));
        gmailService.resetUsernameAndPassword();
        return "redirect:/rest/admin/emailMgt";
    }
}
