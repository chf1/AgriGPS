package de.cf.agri.model;

/**
 * Created by cfrank on 04.01.14.
 */
public class Partner extends DomainObject {

    private String name;
    private String contactId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
