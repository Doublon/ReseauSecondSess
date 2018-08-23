package pay_classes;

import java.io.Serializable;

public class Payment implements Serializable
{
    private int numClient;
    private String proprietaireCarte;
    private byte[] numeroCarte;
    private double montant;
    private byte[] signature;
    private String adresseMail;
    
    public Payment()
    {
        
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
     * @return the proprietaireCarte
     */
    public String getProprietaireCarte()
    {
        return proprietaireCarte;
    }

    /**
     * @param proprietaireCarte the proprietaireCarte to set
     */
    public void setProprietaireCarte(String proprietaireCarte)
    {
        this.proprietaireCarte = proprietaireCarte;
    }

    /**
     * @return the numeroCarte
     */
    public byte[] getNumeroCarte()
    {
        return numeroCarte;
    }

    /**
     * @param numeroCarte the numeroCarte to set
     */
    public void setNumeroCarte(byte[] numeroCarte)
    {
        this.numeroCarte = numeroCarte;
    }

    /**
     * @return the montant
     */
    public double getMontant()
    {
        return montant;
    }

    /**
     * @param montant the montant to set
     */
    public void setMontant(double montant)
    {
        this.montant = montant;
    }
    
    /**
     * @return the signature
     */
    public byte[] getSignature()
    {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(byte[] signature)
    {
        this.signature = signature;
    }

    /**
     * @return the adresseMail
     */
    public String getAdresseMail()
    {
        return adresseMail;
    }

    /**
     * @param adresseMail the adresseMail to set
     */
    public void setAdresseMail(String adresseMail)
    {
        this.adresseMail = adresseMail;
    }
}
