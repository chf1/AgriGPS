package de.cf.agri.model;

import java.math.BigDecimal;

/**
 * Created by cfrank on 04.01.14.
 */
public class Vertrag extends DomainObject {

    public static enum Typ {
        PACHT
    }

    public static enum Rolle {
        PAECHTER, VERPAECHTER
    }

    //private Flur flur;
    private Typ typ;
    private Rolle rolle;
    private Partner partner;
    private BigDecimal preisHa;
    private BigDecimal getPreisAbs;

    public Typ getTyp() {
        return typ;
    }

    public void setTyp(Typ typ) {
        this.typ = typ;
    }

    public Rolle getRolle() {
        return rolle;
    }

    public void setRolle(Rolle rolle) {
        this.rolle = rolle;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public BigDecimal getPreisHa() {
        return preisHa;
    }

    public void setPreisHa(BigDecimal preisHa) {
        this.preisHa = preisHa;
    }

    public BigDecimal getGetPreisAbs() {
        return getPreisAbs;
    }

    public void setGetPreisAbs(BigDecimal getPreisAbs) {
        this.getPreisAbs = getPreisAbs;
    }
}
