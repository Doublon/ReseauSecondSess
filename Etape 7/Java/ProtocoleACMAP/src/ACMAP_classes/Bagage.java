package ACMAP_classes;

public class Bagage
{
    private String numBagage;
    private char valise;
    private double poids;
    
    public Bagage()
    {
        
    }

    /**
     * @return the numBagage
     */
    public String getNumBagage()
    {
        return numBagage;
    }

    /**
     * @param numBagage the numBagage to set
     */
    public void setNumBagage(String numBagage)
    {
        this.numBagage = numBagage;
    }

    /**
     * @return the valise
     */
    public char getValise()
    {
        return valise;
    }

    /**
     * @param valise the valise to set
     */
    public void setValise(char valise)
    {
        this.valise = valise;
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
