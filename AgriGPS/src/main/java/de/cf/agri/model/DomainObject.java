package de.cf.agri.model;

import java.util.Date;

/**
 * Created by cfrank on 04.01.14.
 */
public class DomainObject {

    private Long id;
    private String comment;
    private Date erfdat;
    private Date aendat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getErfdat() {
        return erfdat;
    }

    public void setErfdat(Date erfdat) {
        this.erfdat = erfdat;
    }

    public Date getAendat() {
        return aendat;
    }

    public void setAendat(Date aendat) {
        this.aendat = aendat;
    }
}
