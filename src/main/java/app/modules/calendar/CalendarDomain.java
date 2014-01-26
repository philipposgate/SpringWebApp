package app.modules.calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import app.core.AbstractEntity;
import app.core.Domain;

@Entity
@Table(name = "calendar_domain")
public class CalendarDomain extends AbstractEntity implements Domain
{
    @Column(nullable = false)
    private String domainName; 

    @Override
    public String getDomainName()
    {
        return domainName;
    }

    @Override
    public void setDomainName(String domainName)
    {
        this.domainName = domainName;
    }

}
