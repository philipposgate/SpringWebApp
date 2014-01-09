package app.common;

/**
 * A Domain object is used those web-modules that can can have multiple
 * instances deployed in different PathElement's. The Domain object represents
 * the "realm" that the an instance belongs to. The Domain object is typically
 * implemented as a persisted entity, containing domain-specific configurations
 * and data.
 * 
 * @author philip
 */
public interface Domain
{
    public Integer getId();

    public String getDomainName();
    
    public void setDomainName(String domainName);
}
