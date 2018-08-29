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
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import requetepoolthreads.ConsoleServeur;

/**
 *
 * @author Tusse
 */
public class RequeteReady extends RequeteLUGAP
{
    private Socket socketTower;
    private String ticket;
    
    public RequeteReady(Socket socket, String ticket)
    {
        this.ticket = ticket;
    }
    
    
    @Override
    public void TraiterRequete(Socket sock, ConsoleServeur cs)
    {
        System.out.println("Traitement de la requete");
        System.out.println("Ticket : " + ticket);
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "select chargeSoute "
                + "from bagages "
                + "where numBillet = \"" + ticket + "\"";
        
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));

            ResultSet rs = BBMS.getResultat();
            String charge = null;
            while(rs.next())
            {
                charge = rs.getString("chargeSoute");
            }
            BBMS.DeconnexionDB();
            
            Socket socket = null;
            socket = new Socket("192.168.0.19", 30024);
            
            if(charge.equals("O"))
            {
                System.out.println("Bagages chargés");
                ObjectOutputStream dos; 
                dos = new ObjectOutputStream(socket.getOutputStream());
                dos.writeUTF("TERMINE");
                dos.flush();
                System.out.println("Réponse envoyé");
            }
            else
            {
                System.out.println("KO");
            }
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(RequeteReady.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
