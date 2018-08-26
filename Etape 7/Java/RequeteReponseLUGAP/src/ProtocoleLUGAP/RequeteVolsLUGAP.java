package ProtocoleLUGAP;

import database.utilities.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import requetepoolthreads.*;

public class RequeteVolsLUGAP extends RequeteLUGAP
{
    private LinkedList liste;
    
    public RequeteVolsLUGAP(int t)
    {
        type = t;
        liste = new LinkedList();
    }
    
    @Override
    public void TraiterRequete(Socket sock, ConsoleServeur cs)
    {
        liste = RechercheListeVols();
        ReponseLUGAP rep = null;
        
        if(liste != null)
            rep = new ReponseLUGAP(ReponseLUGAP.RECUP_LISTEVOLS_OK,"RECUP_LISTEVOLS_OK",liste);
        else
            rep = new ReponseLUGAP(ReponseLUGAP.RECUP_LISTEVOLS_ERROR,"RECUP_LISTEVOLS_ERROR",(LinkedList)null);
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); 
            oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau vols : [" + e.getMessage() + "]");
        }
    }
    
    public LinkedList RechercheListeVols()
    {
        String query = null;
        LinkedList retour = null;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        query = "SELECT numVol, destination, compagnie, cast(heureDepart as time(0)) as heureDepart "
                + "FROM Vols INNER JOIN Avions "
                + "ON Vols.numAvion = Avions.numAvion"
                + " WHERE DATE(heureDepart) = curdate()";
        
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            
            ResultSet rs = BBMS.getResultat();
            while(rs.next())
            {
                String numVol = rs.getString("numVol");
                String destination = rs.getString("destination");
                String compagnie = rs.getString("compagnie");
                String heure = rs.getTime("heureDepart").toString();
                VolsLUGAP vol = new VolsLUGAP(numVol,destination,compagnie,heure);
                liste.add(vol);
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
