package ProtocoleLUGAP;

import database.utilities.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.sql.*;
import java.util.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import requetepoolthreads.*;

public class LoginLUGAP extends RequeteLUGAP
{    
    private String chargeUtile;
    private byte[] msgDigest;
    
    public LoginLUGAP(int t, String chu, byte[] msgD)
    {
        type = t; 
        setChargeUtile(chu); 
        msgDigest = msgD;
    }
    
    public String getChargeUtile() 
    { 
        return chargeUtile; 
    }
    
    public void setChargeUtile(String chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }
    
    @Override
    public void TraiterRequete(Socket sock, ConsoleServeur cs)
    {
        String login = null, password = null;
        long temps = 0;
        double alea = 0; 
        int compteur = 0, longueur = 0;
        byte[] msgD = null;
        ReponseLUGAP rep = null;
        
        StringTokenizer parser = new StringTokenizer(this.getChargeUtile(),"#");
 
        while (parser.hasMoreTokens())
        {
            switch(compteur)
            {
                case 1:
                    login = parser.nextToken();
                break;
                
                case 2:
                    temps = Long.parseLong(parser.nextToken());  
                break;
                
                case 3:
                    alea = Double.parseDouble(parser.nextToken());
                break;
                    
                case 4:
                    longueur = Integer.parseInt(parser.nextToken());
                break;
            }
            compteur++;
        }
        
        password = RechercheMotDePasse(login);
        
        if(password != null)
        {
            MessageDigest md = null;
            try 
            {
                Security.addProvider(new BouncyCastleProvider());
                md = MessageDigest.getInstance("SHA-1", "BC");
            } 
            catch (NoSuchAlgorithmException | NoSuchProviderException ex) 
            {
                System.err.println("Problème messageDigest : " + ex);
            }
            md.update(password.getBytes());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream bdos = new DataOutputStream(baos);
            try
            { 
                bdos.writeLong(temps);
                bdos.writeDouble(alea);
            } 
            catch (IOException ex) 
            {
                System.err.println("IOException : " + ex);
            }
            md.update(baos.toByteArray());
            byte[] msgDLocal = md.digest();

            if(MessageDigest.isEqual(this.msgDigest, msgDLocal))
            {
                rep = new ReponseLUGAP(ReponseLUGAP.LOGIN_OK, "LOGIN_OK");
            }
            else
            {
                rep = new ReponseLUGAP(ReponseLUGAP.WRONG_PASSWORD, "WRONG_PASSWORD");
            }    
        }
        else
        {
            rep = new ReponseLUGAP(ReponseLUGAP.WRONG_LOGIN, "WRONG_LOGIN");    
        }
        
        if(!sock.isClosed())
        {
            ObjectOutputStream oos;
            try
            {
                oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(rep); 
                oos.flush();
            }
            catch (IOException e)
            {
                System.err.println("Erreur réseau : " + e.getMessage());
            }   
        }
    }
    
    public String RechercheMotDePasse(String login)
    {
        String retour = null, query = null;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        query = "SELECT password FROM Agents WHERE login = \'" + login + "';";
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            if(BBMS.getResultat().next())
                retour = BBMS.getResultat().getString("password");
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
        }
        
        return retour;
    }
}
