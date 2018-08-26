package ACMAP_classes;

public class Billet
{
    private String numBillet;
    private int nombreAccompagnants;
    private int numClient;
    private int numPlace;
    private String numVol;
    
    public Billet()
    {
        
    }

    /**
     * @return the numBillet
     */
    public String getNumBillet()
    {
        return numBillet;
    }

    /**
     * @param numBillet the numBillet to set
     */
    public void setNumBillet(String numBillet)
    {
        this.numBillet = numBillet;
    }

    /**
     * @return the nombreAccompagnants
     */
    public int getNombreAccompagnants()
    {
        return nombreAccompagnants;
    }

    /**
     * @param nombreAccompagnants the nombreAccompagnants to set
     */
    public void setNombreAccompagnants(int nombreAccompagnants)
    {
        this.nombreAccompagnants = nombreAccompagnants;
    }

    /**
     * @return the numClient
     */
    public int getNumClient()
    {
        return numClient;
    }

    /**
     * @param numClient the numClient to set
     */
    public void setNumClient(int numClient)
    {
        this.numClient = numClient;
    }

    /**
     * @return the numPlace
     */
    public int getNumPlace()
    {
        return numPlace;
    }

    /**
     * @param numPlace the numPlace to set
     */
    public void setNumPlace(int numPlace)
    {
        this.numPlace = numPlace;
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
    
    @Override
    public String toString()
    {
        return numBillet + ";" + nombreAccompagnants + ";" + numClient + ";" + numPlace + ";" + numVol;
    }
}
