package app.common.pathElement;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import app.common.AbstractEntity;
import app.common.user.Role;

@Entity
@Table(name = "path_element_role")
public class PathElementRole extends AbstractEntity
{
    @ManyToOne(fetch=FetchType.EAGER)
    private PathElement pathElement;
    
    @ManyToOne(fetch=FetchType.EAGER)
    private Role role;

    public PathElement getPathElement()
    {
        return pathElement;
    }

    public void setPathElement(PathElement pathElement)
    {
        this.pathElement = pathElement;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }
}
