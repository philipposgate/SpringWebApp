package app.common.pathElement;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import app.common.AbstractEntity;

@Entity
@Table(name = "path_element")
public class PathElement extends AbstractEntity {

	@ManyToOne
	private PathElement parent;
	
	@Column(nullable = false)
	private String path;
	
	@Column(nullable = false)
	private String controller;

	@Column
	private String title;

	@Column
	private  boolean active;

	@Transient
	private List<PathElement> children;

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

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
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
}
