package ProtocoleLUGAP;

import database.utilities.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import requetepoolthreads.*;

public class RequeteColumnChangedLUGAP extends RequeteLUGAP
{
    private String id;
    private String colonne;
    private String valeur;
    
    public RequeteColumnChangedLUGAP(int t, String id, String c, String v)
    {
        type = t;
        this.id = id;
        colonne = c;
        valeur = v;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @param colonne the colonne to set
     */
    public void setColonne(String colonne)
    {
        this.colonne = colonne;
    }

    /**
     * @param valeur the valeur to set
     */
    public void setValeur(String valeur)
    {
        this.valeur = valeur;
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the colonne
     */
    public String getColonne()
    {
        return colonne;
    }

    /**
     * @return the valeur
     */
    public String getValeur()
    {
        return valeur;
    }
    
    @Override
    public void TraiterRequete(Socket sock, ConsoleServeur cs)
    {
        ReponseLUGAP rep = null;
        
        String retour = MiseAJour();
        
        if("MAJBD_OK".equals(retour))
            rep = new ReponseLUGAP(ReponseLUGAP.MAJBD_OK,"MAJBD_OK");
        else
            rep = new ReponseLUGAP(ReponseLUGAP.MAJBD_ERROR,"MAJBD_ERROR");
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); 
            oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau : [" + e.getMessage() + "]");
        }
    }
    
    public String MiseAJour()
    {
        String retour = null;
        String query = null;
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        query = "UPDATE Bagages SET " + getColonne() + " = '" + getValeur() + "' WHERE numBagage = '" + getId() + "';";
        
        try
        {
            BBMS.executerUpdate(query);
            retour = "MAJBD_OK";
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            retour = "MADBD_ERROR";
        }
        
        return retour;
    }
}
