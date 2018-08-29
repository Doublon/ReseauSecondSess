package serveur_bagages;

import ProtocoleACMAP.RequeteACMAP;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ThreadTimer extends Thread
{
    public int temps;
    public boolean interrupted;
    public ThreadReception threadReception;
    
    public ThreadTimer(ThreadReception tr)
    {
        threadReception = tr;
    }
    
    @Override
    synchronized public void run()
    {
        temps = 0;
        interrupted = false;
        while(temps < 60 && !isInterrupted())
        {
            temps++;
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException ex)
            {
                interrupted = true;
            }
        }
        
        if(interrupted == false)
        {
            String requete = String.valueOf(RequeteACMAP.FLIGHT_DONE);
            
            Socket socketReception = null;
            try
            {
                socketReception = new Socket(InetAddress.getLocalHost().getHostAddress(), 30020);
            }
            catch(IOException ex)
            {
                System.err.println("Erreur lors de l'instanciation de la socket du thread reception du serveur : " + ex.getMessage());
            }
            
            if(socketReception != null)
            {
                try
                {
                    DataOutputStream dos = new DataOutputStream(socketReception.getOutputStream());
                    dos.writeUTF(requete);
                    dos.flush();
                }
                catch (IOException e)
                { 
                    System.err.println("Erreur réseau : [" + e.getMessage() + "]");
                }
            }
        }
    }
}
