package app.common.menu;

import java.util.List;

public class MenuItem
{
    private String name;
    private String url;
    private boolean active;
    private MenuItem parent;
    private List<MenuItem> children;
    

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public List<MenuItem> getChildren()
    {
        return children;
    }

    public void setChildren(List<MenuItem> children)
    {
        this.children = children;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public MenuItem getParent()
    {
        return parent;
    }

    public void setParent(MenuItem parent)
    {
        this.parent = parent;
    }

    @Override
    public String toString()
    {
        return "MenuItem [url=" + url + ", active=" + active + "]";
    }
}
