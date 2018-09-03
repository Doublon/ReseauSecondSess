/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleLUGAP;

import database.utilities.BeanBDMySQL;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import requetepoolthreads.ConsoleServeur;

/**
 *
 * @author Tusse
 */
public class RequeteReadyLUGAP extends RequeteLUGAP
{
    private String vol;
    
    public RequeteReadyLUGAP(String vol)
    {
        this.vol = vol;
    }
    
    
    @Override
    public void TraiterRequete(Socket sock, ConsoleServeur cs)
    {
        System.out.println("Traitement de la requete");
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "SELECT chargeSoute "
                + "FROM vols JOIN billets "
                + "ON vols.numVol = billets.numVol "
                + "JOIN bagages "
                + "ON billets.numBillet = bagages.numBillet "
                + "WHERE vols.numVol = " + vol;
        
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));

            ResultSet rs = BBMS.getResultat();
            List<String> etatBagages = null;
            while(rs.next())
            {
                etatBagages.add(rs.getString("chargeSoute"));
            }
            BBMS.DeconnexionDB();
            
            boolean toutCharge = false;
            if(etatBagages != null)
            {
                toutCharge = true;
                for(int i = 0; i < etatBagages.size() && toutCharge == true; i++)
                {
                    if(etatBagages.get(i).equals("N"))
                        toutCharge = false;
                } 
            }
            
            Socket socket = null;
            socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 30024);
            
            if(toutCharge)
            {
                System.out.println("Tous les bagages sont chargés");
                ObjectOutputStream dos; 
                dos = new ObjectOutputStream(socket.getOutputStream());
                dos.writeUTF("TERMINE");
                dos.flush();
            }
            else
            {
                System.out.println("Chargement des bagages");
                TimeUnit.SECONDS.sleep(10);
                ObjectOutputStream dos; 
                dos = new ObjectOutputStream(socket.getOutputStream());
                dos.writeUTF("TERMINE");
                dos.flush();
            }
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(RequeteReadyLUGAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(RequeteReadyLUGAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
