package ProtocoleLUGAP;

import java.io.Serializable;

public class VolsLUGAP implements Serializable
{
    private String numVol;
    private String compagnie;
    private String destination;
    private String heure;
    
    /**
     * @return the numVol
     */
    public String getNumVol()
    {
        return numVol;
    }

    /**
     * @param numVol the numVol to set
     */
    public void setNumVol(String numVol)
    {
        this.numVol = numVol;
    }

    /**
     * @return the compagnie
     */
    public String getCompagnie()
    {
        return compagnie;
    }

    /**
     * @param compagnie the compagnie to set
     */
    public void setCompagnie(String compagnie)
    {
        this.compagnie = compagnie;
    }

    /**
     * @return the destination
     */
    public String getDestination()
    {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    /**
     * @return the heure
     */
    public String getHeure()
    {
        return heure;
    }

    /**
     * @param heure the heure to set
     */
    public void setHeure(String heure)
    {
        this.heure = heure;
    }
    
    public VolsLUGAP(String n, String c, String d, String h)
    {
        numVol = n;
        compagnie = c;
        destination = d;
        heure = h;
    }
    
    @Override
    public String toString()
    {
        return "VOL " + getNumVol() + " " + getCompagnie() + " - " + getDestination() + " " + getHeure();
    }
}
