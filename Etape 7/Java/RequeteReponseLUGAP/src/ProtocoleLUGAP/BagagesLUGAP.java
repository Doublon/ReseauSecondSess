package ProtocoleLUGAP;

import java.io.Serializable;

public class BagagesLUGAP implements Serializable
{
    private String identifiant;
    private double poids;
    private String type;
    private String receptionne;
    private String chargeEnSoute;
    private String verifieParDouane;
    private String remarques;
    
    public BagagesLUGAP(String i, double p, String t, String rep, String cS, String vD, String rem)
    {
        identifiant = i;
        poids = p;
        type = t;
        receptionne = rep;
        chargeEnSoute = cS;
        verifieParDouane = vD;
        remarques = rem;
    }
    
    /**
     * @return the identifiant
     */
    public String getIdentifiant()
    {
        return identifiant;
    }

    /**
     * @param identifiant the identifiant to set
     */
    public void setIdentifiant(String identifiant)
    {
        this.identifiant = identifiant;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return the receptionne
     */
    public String getReceptionne()
    {
        return receptionne;
    }

    /**
     * @param receptionne the receptionne to set
     */
    public void setReceptionne(String receptionne)
    {
        this.receptionne = receptionne;
    }

    /**
     * @return the chargeEnSoute
     */
    public String getChargeEnSoute()
    {
        return chargeEnSoute;
    }

    /**
     * @param chargeEnSoute the chargeEnSoute to set
     */
    public void setChargeEnSoute(String chargeEnSoute)
    {
        this.chargeEnSoute = chargeEnSoute;
    }

    /**
     * @return the verifieParDouane
     */
    public String getVerifieParDouane()
    {
        return verifieParDouane;
    }

    /**
     * @param verifieParDouane the verifieParDouane to set
     */
    public void setVerifieParDouane(String verifieParDouane)
    {
        this.verifieParDouane = verifieParDouane;
    }

    /**
     * @return the remarques
     */
    public String getRemarques()
    {
        return remarques;
    }

    /**
     * @param remarques the remarques to set
     */
    public void setRemarques(String remarques)
    {
        this.remarques = remarques;
    }

    /**
     * @return the poids
     */
    public double getPoids()
    {
        return poids;
    }
    
    /**
     * @param poids the poids to set
     */
    public void setPoids(double poids)
    {
        this.poids = poids;
    }    
}
