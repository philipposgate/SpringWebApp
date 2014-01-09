package app.common.pathElement;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import app.common.AbstractEntity;
import app.common.Domain;
import app.common.user.Role;
import app.web.PathElementController;

@Entity
@Table(name = "path_element")
public class PathElement extends AbstractEntity {

	@ManyToOne
	private PathElement parent;
	
	@Column(nullable = false)
	private String path;
	
	@Column(nullable = false)
	private String controllerBeanName;
	
    @Column
    private Integer domainId;

	@Column
	private String title;

	@Column
	private boolean active;

	@Column
	private boolean authRequired;

    @Column
    private boolean allRolesRequired;

    @Column
    private boolean hideNavWhenUnauthorized;
    
    @Column
    private int position;
    
    @Transient
    private List<Role> roles;
    
	@Transient
	private List<PathElement> children;

	@Transient
	private PathElementController controller;
	
    @Override
	public String toString()
    {
        return super.toString() + " " + getFullPath();
    }
	
	@Transient
	public boolean isRoot()
	{
		return null == parent;
	}
	
	@Transient
	public String getFullPath() 
	{
		String fullPath = null;
		
		if (!isRoot())
		{
			fullPath = buildPath() + ".htm";
		}
		else
		{
			fullPath = "/";
		}
		
		return fullPath;
	}
	
	private String buildPath()
	{
		String ret = "";
		
		if (null != parent && !parent.isRoot())
		{
			ret = parent.buildPath();
		}
		
		ret += "/" + path;
		
		return ret;
	}

	@Transient
	public boolean isLeaf()
	{
		return null == children || children.isEmpty();
	}
	

	public PathElement getParent() {
		return parent;
	}

	public void setParent(PathElement parent) {
		this.parent = parent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getControllerBeanName() {
		return controllerBeanName;
	}

	public void setControllerBeanName(String controllerBeanName) {
		this.controllerBeanName = controllerBeanName;
	}

	public List<PathElement> getChildren() {
		return children;
	}

	public void setChildren(List<PathElement> children) {
		this.children = children;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public PathElementController getController() {
		return controller;
	}

	public void setController(PathElementController controller) {
		this.controller = controller;
	}

	public boolean isAuthRequired() {
		return authRequired;
	}

	public void setAuthRequired(boolean authRequired) {
		this.authRequired = authRequired;
	}

    public boolean isAllRolesRequired()
    {
        return allRolesRequired;
    }

    public void setAllRolesRequired(boolean allRolesRequired)
    {
        this.allRolesRequired = allRolesRequired;
    }

    public boolean isHideNavWhenUnauthorized()
    {
        return hideNavWhenUnauthorized;
    }

    public void setHideNavWhenUnauthorized(boolean hideNavWhenUnauthorized)
    {
        this.hideNavWhenUnauthorized = hideNavWhenUnauthorized;
    }

    public List<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(List<Role> roles)
    {
        this.roles = roles;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public Integer getDomainId()
    {
        return domainId;
    }

    public void setDomainId(Integer domainId)
    {
        this.domainId = domainId;
    }
    
    public Domain getControllerDomain()
    {
        Domain domain = null;
        
        if (null != controller && null != controller.getDomainClass() && null != domainId)
        {
            domain = controller.getDomain(domainId);
        }
        
        return domain;
    }
}
