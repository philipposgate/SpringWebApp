package app.common.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements InitializingBean
{
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        Role userRole = roleDAO.getRoleByName(ROLE_USER);
        if (userRole == null)
        {
            System.out.println("CREATE ROLE_USER");
            userRole = new Role();
            userRole.setRole(ROLE_USER);
            roleDAO.create(userRole);
        }

        Role adminRole = roleDAO.getRoleByName(ROLE_ADMIN);
        if (adminRole == null)
        {
            System.out.println("CREATE ROLE_ADMIN");
            adminRole = new Role();
            adminRole.setRole(ROLE_ADMIN);
            roleDAO.create(adminRole);
        }

        User adminUser = getUserByUsername("admin");
        if (adminUser == null)
        {
            System.out.println("CREATE ROOT USER");
            adminUser = new User();
            adminUser.setFirstName("admin");
            adminUser.setLastName("admin");
            adminUser.setEmail("admin@admin.com");
            adminUser.setUsername("admin");
            adminUser.setPassword("1");
            adminUser.setAccountNonLocked(true);
            adminUser.setAccountNonExpired(true);
            adminUser.setCredentialsNonExpired(true);
            adminUser.setEnabled(true);
            adminUser.setCreateDate(new Date());

            userDAO.create(adminUser);

            // adminUser.getRoles().add(userRole);
            adminUser.getRoles().add(adminRole);

            userDAO.update(adminUser);
        }

        User regularUser = getUserByUsername("user");
        if (regularUser == null)
        {
            System.out.println("CREATE REGULAR USER");
            regularUser = new User();
            regularUser.setUsername("user");
            regularUser.setPassword("1");
            regularUser.setAccountNonLocked(true);
            regularUser.setAccountNonExpired(true);
            regularUser.setCredentialsNonExpired(true);
            regularUser.setEnabled(true);
            regularUser.setCreateDate(new Date());
            regularUser.setFirstName("user");
            regularUser.setLastName("user");
            regularUser.setEmail("user@user.com");

            userDAO.create(regularUser);

            regularUser.getRoles().add(userRole);

            userDAO.update(regularUser);
        }

    }

    public User getUserByUsername(String username)
    {
        return userDAO.getUserByUsername(username);
    }

    public User getUserById(String id)
    {
        return userDAO.getById(id);
    }

    public void updateUser(User user)
    {
        userDAO.update(user);
    }

    public User getUserLoggedIn()
    {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    public void setUserLoggedIn(User user)
    {
        // Authentication authentication = new
        // UsernamePasswordAuthenticationToken(
        // user, user.getPassword(), user.getAuthorities());
        //
        // SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Role getRole(String roleName)
    {
        Role role = roleDAO.getRoleByName(roleName);

        if (role == null || role.getId() == null)
        {
            role = new Role();
            role.setRole(roleName);
            roleDAO.create(role);
        }
        return role;
    }

    public boolean userHasRole(User user, Role role)
    {
        return userHasRole(user, role.getRole());
    }

    public void toggleUserRole(User user, String roleName, boolean toggle)
    {
        if (toggle)
        {
            if (!userHasRole(user, roleName))
            {
                Role role = getRole(roleName);
                user.getRoles().add(role);
                userDAO.update(user);
            }
        }
        else
        {
            if (userHasRole(user, roleName))
            {
                Role role = getRole(roleName);
                user.getRoles().remove(role);
                userDAO.update(user);
            }
        }
    }

    public boolean currentUserHasAllRoles(List<Role> roles)
    {
        List<String> roleIdentifiers = new ArrayList<String>();
        for (Role role : roles)
        {
            roleIdentifiers.add(role.getRole());
        }
        return SecurityUtils.getSubject().hasAllRoles(roleIdentifiers);
    }

    public boolean userHasAllRoles(User user, List<Role> roles)
    {
        boolean hasRoles = false;
        
        if (null != user && null != user.getRoles() && !user.getRoles().isEmpty())
        {
            hasRoles = null == roles || roles.isEmpty() || user.getRoles().containsAll(roles);
        }
        
        return hasRoles;
    }
    

    public boolean userHasRole(User user, String roleName)
    {
        boolean hasRole = false;
        
        if (null != user && null != user.getRoles() && !user.getRoles().isEmpty())
        {
            for (Role role : user.getRoles())
            {
                if (role.getRole().equals(roleName))
                {
                    hasRole = true;
                    break;
                }
            }
        }
        return hasRole;
    }

    public boolean userHasAnyRoles(User user, List<Role> roles)
    {
        boolean hasRole = false;
        
        if (null != user && null != user.getRoles() && !user.getRoles().isEmpty())
        {
            for (Role role : roles)
            {
                if (user.getRoles().contains(role))
                {
                    hasRole = true;
                    break;
                }
            }
        }
        
        return hasRole;
    }


}
