package tickmap_classes;

import java.io.Serializable;
import java.sql.Timestamp;

public class Vol implements Serializable
{
    private String numVol;
    private String destination;
    private String zone;
    private int nombrePlaces;
    private int placesRestantes;
    private double distance;
    private Timestamp heureDepart;
    private Timestamp heureArrivee;
    private double prixParPersonne;
    private String numAvion;
    
    public Vol()
    {
        
    }

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
     * @return the zone
     */
    public String getZone()
    {
        return zone;
    }

    /**
     * @param zone the zone to set
     */
    public void setZone(String zone)
    {
        this.zone = zone;
    }

    /**
     * @return the nombrePlaces
     */
    public int getNombrePlaces()
    {
        return nombrePlaces;
    }

    /**
     * @param nombrePlaces the nombrePlaces to set
     */
    public void setNombrePlaces(int nombrePlaces)
    {
        this.nombrePlaces = nombrePlaces;
    }

    /**
     * @return the placesRestantes
     */
    public int getPlacesRestantes()
    {
        return placesRestantes;
    }

    /**
     * @param placesRestantes the placesRestantes to set
     */
    public void setPlacesRestantes(int placesRestantes)
    {
        this.placesRestantes = placesRestantes;
    }

    /**
     * @return the distance
     */
    public double getDistance()
    {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    /**
     * @return the heureDepart
     */
    public Timestamp getHeureDepart()
    {
        return heureDepart;
    }

    /**
     * @param heureDepart the heureDepart to set
     */
    public void setHeureDepart(Timestamp heureDepart)
    {
        this.heureDepart = heureDepart;
    }

    /**
     * @return the heureArrivee
     */
    public Timestamp getHeureArrivee()
    {
        return heureArrivee;
    }

    /**
     * @param heureArrivee the heureArrivee to set
     */
    public void setHeureArrivee(Timestamp heureArrivee)
    {
        this.heureArrivee = heureArrivee;
    }

    /**
     * @return the numAvion
     */
    public String getNumAvion()
    {
        return numAvion;
    }

    /**
     * @param numAvion the numAvion to set
     */
    public void setNumAvion(String numAvion)
    {
        this.numAvion = numAvion;
    }

    /**
     * @return the prixParPersonne
     */
    public double getPrixParPersonne()
    {
        return prixParPersonne;
    }

    /**
     * @param prixParPersonne the prixParPersonne to set
     */
    public void setPrixParPersonne(double prixParPersonne)
    {
        this.prixParPersonne = prixParPersonne;
    }
}
