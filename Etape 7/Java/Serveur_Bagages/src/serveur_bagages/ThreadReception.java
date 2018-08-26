package serveur_bagages;

import ProtocoleACMAP.RequeteACMAP;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class ThreadReception extends Thread
{
    private final Serveur_Bagages parent;
    private final int port_takeoff;
    private ServerSocket SSocket;
    private ThreadTimer threadTimer;
    private Socket socketAirTraffic;
    
    public ThreadReception(int port, Serveur_Bagages parent)
    {
        port_takeoff = port;
        this.parent = parent;
        threadTimer = new ThreadTimer(this);
    }    
    
    @Override
    synchronized public void run()
    {
        try
        {
            SSocket = new ServerSocket(port_takeoff);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]"); System.exit(1);
        } 
        
        Socket CSocket = null;
        while (!isInterrupted())
        {
            try
            {
                System.out.println("*** ThreadReception en attente ***");
                CSocket = SSocket.accept();
                parent.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#Accept#ThreadReception");
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]"); System.exit(1);
            }
            
            String message = null;
            try
            {
                DataInputStream dis = new DataInputStream(CSocket.getInputStream());
                message = dis.readUTF();
            }
            catch(IOException ex)
            {
                System.err.println("Erreur lors de la lecture du message du ThreadReception : " + ex.getMessage());
            }
            
            String reponseAirTraffic = "ERROR";
            int compteur = 0;
            boolean doitRepondre = false;
            System.out.println(message);
            if(message != null && Integer.parseInt(message.trim()) == RequeteACMAP.REQUEST_CHECKIN_OFF)
            {
                parent.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#REQUEST_CHECKIN_OFF#ThreadReception");
                for(int i = 0 ; i < parent.getListeClients().size() ; i++)
                {
                    try
                    {
                        StringTokenizer stk = new StringTokenizer(parent.getListeClients().get(i).getRemoteSocketAddress().toString(), ":");
                        String adresse = stk.nextToken().substring(1);
                        Socket socket = new Socket(adresse, 30021);
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        dos.writeUTF("OFF");
                        dos.flush();

                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        String reponse = dis.readUTF();
                        if(reponse.equals("OK"))
                        {
                            parent.TraceEvenements(socket.getRemoteSocketAddress().toString()+"#CheckIn_OFF_OK#ThreadReception");
                            compteur++;
                        }
                        else
                        {
                            parent.TraceEvenements(socket.getRemoteSocketAddress().toString()+"#CheckIn_OFF_ERROR#ThreadReception");
                        }
                    }
                    catch(IOException ex)
                    {
                        System.err.println("Erreur lors de la création de la socket d'alerte : " + ex.getMessage());
                    }
                }
                
                if(compteur == parent.getListeClients().size())
                {
                    reponseAirTraffic = "OK";
                }
                
                doitRepondre = true;
            }
            else
            {
                System.out.println("Message différent reçu !");
                if(message != null && (Integer.parseInt(message.trim()) == RequeteACMAP.REQUEST_READY || Integer.parseInt(message.trim()) == RequeteACMAP.FLIGHT_DONE))
                {
                    System.out.println("Dernier message reçu : " + message.trim());
                    if(Integer.parseInt(message.trim()) == RequeteACMAP.REQUEST_READY)
                    {
                        System.out.println("Début de la fin...");
                        threadTimer.start();
                        doitRepondre = false;
                        socketAirTraffic = CSocket;
                    }
                    else
                    {
                        System.out.println("Ready !");
                        if(threadTimer.isAlive())
                            threadTimer.interrupt();
                        reponseAirTraffic = "TERMINE";
                        doitRepondre = true;
                        CSocket = socketAirTraffic;
                    }
                }
            }
            
            if(doitRepondre == true)
            {
                try
                {
                    DataOutputStream dos = new DataOutputStream(CSocket.getOutputStream());
                    System.out.println("Réponse au serveur air traffic : " + reponseAirTraffic);
                    dos.writeUTF(reponseAirTraffic);
                }
                catch(IOException ex)
                {
                    System.err.println("Erreur lors de l'envoi de la réponse du ThreadReception : " + ex.getMessage());
                }
            }
        }
    }
}
