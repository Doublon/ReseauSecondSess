/*
    RORIVE Olivier | VERWIMP Jim
    Réseaux et technologies internet : étape 7
    Date de mise à jour : le 21/01 à 13h37
*/

package ProtocoleACMAP;

import ACMAP_classes.Bagage;
import ACMAP_classes.Billet;
import ACMAP_classes.Vol;
import ProtocoleLUGAP.RequeteReadyLUGAP;
import database.utilities.BeanBDMySQL;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

public class RequeteACMAP implements Requete, Serializable
{
    public final static int REQUEST_TICKET_CHECK = 1;
    public final static int REQUEST_ADD_LUGGAGE = 2;
    public final static int REQUEST_FLIGHTS = 3;
    public final static int REQUEST_CHECKIN_OFF = 4;
    public final static int REQUEST_READY = 5;
    public final static int FLIGHT_DONE = 6;
    public final static int REQUEST_RUNWAYS = 7;
    public final static int REQUEST_RUNWAY_CHOICE = 8;
    
    private final int type;
    private final String numBillet;
    private final int nombreAccompagnants;
    private String chargeUtile;
    
    private final List<Bagage> listeBagages;
    
    private final int numPiste;
    
    public RequeteACMAP(int t)
    {
        type = t;
        numBillet = null;
        nombreAccompagnants = -1;
        listeBagages = null;
        numPiste = -1;
        this.chargeUtile = null;
    }
        
    public RequeteACMAP(int t, String nB, int nA)
    {
        type = t;
        numBillet = nB;
        nombreAccompagnants = nA;
        listeBagages = null;
        numPiste = -1;
    }
    
    public RequeteACMAP(int t, String nB, List<Bagage> lB)
    {
        type = t;
        numBillet = nB;
        nombreAccompagnants = -1;
        listeBagages = lB;
        numPiste = -1;
    }
    
    public RequeteACMAP(int t, int p)
    {
        type = t;
        numBillet = null;
        nombreAccompagnants = -1;
        listeBagages = null;
        numPiste = p;
    }
    
    public void TraiterVerificationBillet(Socket socket, ConsoleServeur cs)
    {
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "SELECT * FROM Billets WHERE numBillet = '" + numBillet + "' AND nombreAccompagnants = " + nombreAccompagnants + ";";
        String reponse = String.valueOf(ReponseACMAP.REQUEST_TICKET_ERROR) + '\0';
        reponse = reponse.substring(0, reponse.length());
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            ResultSet rs = BBMS.getResultat();
            while(rs.next())
            {
                Billet billet = new Billet();
                billet.setNumBillet(rs.getString("numBillet"));
                billet.setNombreAccompagnants(rs.getInt("nombreAccompagnants"));
                billet.setNumClient(rs.getInt("numClient"));
                billet.setNumPlace(rs.getInt("numPlace"));
                billet.setNumVol(rs.getString("numVol"));
                reponse = ReponseACMAP.REQUEST_TICKET_OK + ";" + billet.toString() + '\0';
                reponse = reponse.substring(0, reponse.length());
                cs.TraceEvenements(socket.getRemoteSocketAddress().toString()+"#REQUEST_TICKET_OK#" + Thread.currentThread().getName());
            }
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
            cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_TICKET_ERROR#" + Thread.currentThread().getName());
        }
        
        OutputStreamWriter osw;
        try
        {
            osw = new OutputStreamWriter(socket.getOutputStream());
            osw.write(reponse);
            osw.flush();
        }
        catch(IOException ex)
        {
            System.out.println("Erreur lors de l'envoi de la réponse au client : " + ex.getMessage());
        }
    }
    
    public void TraiterAjoutBagages(Socket socket, ConsoleServeur cs)
    {
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        int compteurInsertions = 0;
        String reponse;
        for(int i = 0 ; i < listeBagages.size() ; i++)
        {
            Bagage bagage = listeBagages.get(i);            
            String query = "INSERT INTO Bagages VALUES ('" + bagage.getNumBagage() + "', " + bagage.getPoids() + ", '" 
                    + bagage.getValise() + "', 'N', 'N', 'N', '', '" + numBillet + "');";
            
            try
            {
                compteurInsertions += BBMS.executerUpdate(query);
            }
            catch(SQLException ex)
            {
                System.err.println("Exception lors de l'ajout des bagages [" + query + "] : " + ex.getMessage());
            }
        }
        
        if(compteurInsertions == listeBagages.size())
        {
            reponse = String.valueOf(ReponseACMAP.REQUEST_ADD_LUGGAGE_OK) + '\0';
            reponse = reponse.substring(0, reponse.length());
            cs.TraceEvenements(socket.getRemoteSocketAddress().toString()+"#REQUEST_ADD_LUGGAGE_OK#" + Thread.currentThread().getName());
        }
        else
        {
            reponse = String.valueOf(ReponseACMAP.REQUEST_ADD_LUGGAGE_ERROR) + '\0';
            reponse = reponse.substring(0, reponse.length());
            cs.TraceEvenements(socket.getRemoteSocketAddress().toString()+"#REQUEST_ADD_LUGGAGE_ERROR#" + Thread.currentThread().getName());
        }
        
        OutputStreamWriter osw;
        try
        {
            osw = new OutputStreamWriter(socket.getOutputStream());
            osw.write(reponse);
            osw.flush();
        }
        catch(IOException ex)
        {
            System.out.println("Erreur lors de l'envoi de la réponse au client : " + ex.getMessage());
        }
    }
    
    public void TraiterRequeteVols(Socket socket, ConsoleServeur cs)
    {
        List<Vol> listeVols = null;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "SELECT * "
                + "FROM Vols ";
                //+ "WHERE TIMESTAMPDIFF(MINUTE, current_timestamp, heureDepart) >= 0 "
                //+ "AND TIMESTAMPDIFF(MINUTE, current_timestamp, heureDepart) <= 180";
        ReponseACMAP reponse;
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            listeVols = new ArrayList();
            ResultSet rs = BBMS.getResultat();
            while(rs.next())
            {
                Vol vol = new Vol();
                vol.setNumVol(rs.getString("numVol"));
                vol.setDestination(rs.getString("destination"));
                vol.setZone(rs.getString("zone"));
                vol.setNombrePlaces(rs.getInt("nombrePlaces"));
                vol.setPlacesRestantes(rs.getInt("placesRestantes"));
                vol.setDistance(rs.getDouble("distance"));
                vol.setHeureDepart(rs.getTimestamp("heureDepart"));
                vol.setHeureArrivee(rs.getTimestamp("heureArrivee"));
                vol.setPrixParPersonne(rs.getDouble("prixParPersonne"));
                vol.setNumAvion(rs.getString("numAvion"));
                listeVols.add(vol);
            }
            BBMS.DeconnexionDB();
            reponse = new ReponseACMAP(ReponseACMAP.REQUEST_FLIGHTS_OK, listeVols);
            cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_FLIGHTS_OK#" + Thread.currentThread().getName());
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
            reponse = new ReponseACMAP(ReponseACMAP.REQUEST_FLIGHTS_ERROR);
            cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_FLIGHTS_ERROR#" + Thread.currentThread().getName());
        }
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(reponse);
            oos.flush();
        }
        catch(IOException ex)
        {
            System.out.println("Erreur lors de l'envoi de la réponse au client : " + ex.getMessage());
        }
    }

    public void TraiterCheckinOff(Socket socket, ConsoleServeur cs, String adresseCheckin, int portCheckin)
    {
        Socket checkInSocket = null;
        DataInputStream dis=null;
        DataOutputStream dos=null;
        
        try
        {            
            checkInSocket = new Socket(adresseCheckin, portCheckin);
            dis = new DataInputStream(checkInSocket.getInputStream());
            dos = new DataOutputStream(checkInSocket.getOutputStream());
        }
        catch (IOException ex)
        {
            Logger.getLogger(RequeteACMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(checkInSocket == null || dis == null || dos == null)
            System.exit(1);
        
        String requete = "CHECK_TICKET;" + chargeUtile + "#";
        try
        {
            dos.write(requete.getBytes());
            dos.flush();
        }
        catch (IOException ex)
        {
            Logger.getLogger(RequeteACMAP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void TraiterReady(Socket socket, ConsoleServeur cs, String adresseBagages, int portBagages)
    {       
        Socket socketBagages = null;
        
        try
        {
            socketBagages = new Socket(adresseBagages, portBagages); 
        } 
        catch (IOException ex)
        {
            System.err.println("Erreur lors de la création de la socket Bagages : " + ex.getMessage());
        }
       
        if(socketBagages != null)
        {
            ObjectOutputStream oosBagages = null;
            try
            {
                RequeteReadyLUGAP requete = new RequeteReadyLUGAP(this.chargeUtile);
                oosBagages = new ObjectOutputStream(socketBagages.getOutputStream());
                System.out.println("Envoie de la requete");
                oosBagages.writeObject(requete); 
                oosBagages.flush();
            }
            catch(IOException ex)
            {
                System.err.println("Erreur lors de la récupération du flux d'écriture : " + ex.getMessage());
            }
            
            ServerSocket serveurBagage = null;
            Socket socketReception = null;
            
            try
            {
                serveurBagage = new ServerSocket(30024);
                socketReception = serveurBagage.accept();
            }
            catch (IOException ex)
            {
                Logger.getLogger(RequeteACMAP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            ObjectInputStream dis;
            String reponseBagages = null;
            try
            {
                dis = new ObjectInputStream(socketReception.getInputStream());
                reponseBagages = dis.readUTF();
                
            }
            catch (IOException ex)
            {
                Logger.getLogger(RequeteACMAP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            ReponseACMAP reponse;
            if(reponseBagages.equals("TERMINE"))
            {
                reponse = new ReponseACMAP(ReponseACMAP.REQUEST_READY_OK);
                cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_READY_OK#" + Thread.currentThread().getName());
            }
            else
            {
                reponse = new ReponseACMAP(ReponseACMAP.REQUEST_READY_ERROR);
                cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_READY_ERROR#" + Thread.currentThread().getName());
            }
            
            ObjectOutputStream oos;
            try
            {
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(reponse);
                oos.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Erreur lors de l'envoi de la réponse au client : " + ex.getMessage());
            }

        }
    }
    
    synchronized public void TraiterRequetePistes(Socket socket, ConsoleServeur cs)
    {
        List<Integer> listePistes = null;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "SELECT * FROM Pistes WHERE libre = 1;";
        ReponseACMAP reponse;
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            listePistes = new ArrayList();
            ResultSet rs = BBMS.getResultat();
            while(rs.next())
            {
                listePistes.add(rs.getInt("numPiste"));
            }
            BBMS.DeconnexionDB();
            reponse = new ReponseACMAP(ReponseACMAP.REQUEST_RUNWAYS_OK, (ArrayList<Integer>) listePistes);
            cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_#" + Thread.currentThread().getName());
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
            reponse = new ReponseACMAP(ReponseACMAP.REQUEST_RUNWAYS_ERROR);
            cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_RUNWAYS_ERROR#" + Thread.currentThread().getName());
        }
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(reponse);
            oos.flush();
        }
        catch(IOException ex)
        {
            System.out.println("Erreur lors de l'envoi de la réponse au client : " + ex.getMessage());
        }
        
    }
    
    public void TraiterChoixPiste(Socket socket, ConsoleServeur cs)
    {
        System.out.println("Dans TraiterChoixPiste");
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "SELECT * FROM Pistes WHERE numPiste = " + numPiste + ";";
        ReponseACMAP reponse;
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            ResultSet rs = BBMS.getResultat();
            if(rs.next())
            {
                if(rs.getInt("libre") == 1)
                {
                    query = "UPDATE Pistes SET libre = 0 WHERE numPiste = " + numPiste + ";";
                    
                    int retour = BBMS.executerUpdate(query);
                    if(retour == 1)
                    {
                        reponse = new ReponseACMAP(ReponseACMAP.REQUEST_RUNWAY_CHOICE_OK);
                        cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_RUNWAY_CHOICE_OK#" + Thread.currentThread().getName());
                    }
                    else
                    {
                        reponse = new ReponseACMAP(ReponseACMAP.REQUEST_RUNWAY_CHOICE_ERROR);
                        cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_RUNWAY_CHOICE_ERROR#" + Thread.currentThread().getName());
                    }
                }
                else
                {
                    reponse = new ReponseACMAP(ReponseACMAP.REQUEST_RUNWAY_CHOICE_ERROR);
                    cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_RUNWAY_CHOICE_ERROR#" + Thread.currentThread().getName());
                }
            }
            else
            {
                reponse = new ReponseACMAP(ReponseACMAP.REQUEST_RUNWAY_CHOICE_ERROR);
                cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_RUNWAY_CHOICE_ERROR#" + Thread.currentThread().getName());
            }
            BBMS.DeconnexionDB();

        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
            reponse = new ReponseACMAP(ReponseACMAP.REQUEST_RUNWAY_CHOICE_ERROR);
            cs.TraceEvenements(socket.getInetAddress().getHostAddress()+"#REQUEST_RUNWAY_CHOICE_ERROR#" + Thread.currentThread().getName());
        }
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(reponse);
            oos.flush();
        }
        catch(IOException ex)
        {
            System.out.println("Erreur lors de l'envoi de la réponse au client : " + ex.getMessage());
        }
    }

    @Override
    public void TraiterRequete(Socket sock, ConsoleServeur cs)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setChargeUtile(String charge)
    {
        this.chargeUtile = charge;
    }
}
