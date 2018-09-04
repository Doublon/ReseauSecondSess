package ProtocoleLUGAP;

import database.utilities.*;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.LinkedList;
import requetepoolthreads.ConsoleServeur;

public class RequeteBagagesLUGAP extends RequeteLUGAP
{
    public LinkedList liste;
    public String numVol;
    
    public RequeteBagagesLUGAP(int t, String n)
    {
        type = t;
        numVol = n;
        liste = new LinkedList();
    }
    
    @Override
    public void TraiterRequete(Socket sock, ConsoleServeur cs)
    {
        liste = RechercheListeBagages();
        
        ReponseLUGAP rep = null;
        
        if(liste != null)
            rep = new ReponseLUGAP(ReponseLUGAP.RECUP_LISTEBAGAGES_OK,"RECUP_LISTE_OK",liste);
        else
            rep = new ReponseLUGAP(ReponseLUGAP.RECUP_LISTEBAGAGES_ERROR,"RECUP_LISTE_ERROR",(LinkedList)null);
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); 
            oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau bagages : [" + e.getMessage() + "]");
        }
    }
    
    public LinkedList RechercheListeBagages()
    {
        String query = null;
        LinkedList retour = null;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "tusset", "123soleil", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        query = "SELECT numBagage, poids, valise, receptionne, chargeSoute, verifieDouane, remarques "
                + "FROM Bagages INNER JOIN Billets "
                + "ON Bagages.numBillet = Billets.numBillet "
                + "WHERE numVol = '" + numVol + "' ";
        
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            
            ResultSet rs = BBMS.getResultat();
            while(rs.next())
            {
                String numBagage = rs.getString("numBagage");
                double poids = rs.getDouble("poids");
                String recup = rs.getString("valise");
                String valise = null;
                if("O".equals(recup.toUpperCase()))
                {
                    valise = "VALISE";
                }
                else
                {
                    valise = "PAS VALISE";
                }
                String receptionne = rs.getString("receptionne");
                String chargeSoute = rs.getString("chargeSoute");
                String verifieDouane = rs.getString("verifieDouane");
                String remarques = rs.getString("remarques");
                BagagesLUGAP bagage = new BagagesLUGAP(numBagage,poids,valise,receptionne,chargeSoute,verifieDouane,remarques);
                liste.add(bagage);
            }
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
        }
        
        retour = liste;
        
        return retour;
    }
}
