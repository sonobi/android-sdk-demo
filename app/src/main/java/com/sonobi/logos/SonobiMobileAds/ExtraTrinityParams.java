package com.sonobi.logos.SonobiMobileAds;



/**
 * Created by jgo on 10/5/17.
 */


import androidx.annotation.Keep;

/**
 * Class to define extra trinity parameters for targeting
 * Available Options:
 * hfa - A publisher provided ID
 * cdf - A Custom Data field for reporting
 * ant - Extra Targeting paramater for appnexus s2s
 * gmgt - Extra Targeting for guardian?
 */
@Keep
public class ExtraTrinityParams {

    private String hfa = "";
    private String cdf = "";
    private String ant = "";
    private String gmgt = "";
    private String floor = "";

    public String getHfa() {
        return hfa;
    }

    public void setHfa(String hfa) {
        this.hfa = hfa;
    }

    public String getCdf() {
        return cdf;
    }

    public void setCdf(String cdf) {
        this.cdf = cdf;
    }

    public String getAnt() {
        return ant;
    }

    public void setAnt(String ant) {
        this.ant = ant;
    }

    public String getGmgt() {
        return gmgt;
    }

    public void setGmgt(String gmgt) {
        this.gmgt = gmgt;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}