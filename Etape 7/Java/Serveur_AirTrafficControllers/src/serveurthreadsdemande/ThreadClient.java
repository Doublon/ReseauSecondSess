package serveurthreadsdemande;

import ProtocoleACMAP.RequeteACMAP;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import requetepoolthreads.ConsoleServeur;

public class ThreadClient extends Thread
{
    private final Socket socket;
    private final ConsoleServeur guiApplication;
    private final String adresseCheckin;
    private final int portCheckin;
    private final String adresseBagages;
    private final int portBagages;
    
    public ThreadClient(Socket sock, ConsoleServeur cs, String aC, int pC, String aB, int pB)
    {
        socket = sock;
        guiApplication = cs;
        adresseCheckin = aC;
        portCheckin = pC;
        adresseBagages = aB;
        portBagages = pB;
    }
    
    @Override
    public void run()
    {
        boolean ok = true;
        ObjectInputStream ois = null;
        RequeteACMAP requete = null;
        try
        {
            ois = new ObjectInputStream(socket.getInputStream());
            Object object = ois.readObject();
            if(object instanceof RequeteACMAP)
            {
                requete = (RequeteACMAP) object;
                guiApplication.TraceEvenements(socket.getRemoteSocketAddress().toString()+"#RequeteACMAP#ThreadServeur");
            }
            else
            {
                guiApplication.TraceEvenements(socket.getRemoteSocketAddress().toString()+"#Objet inconnu#ThreadServeur");
                ok = false;
            }
        }
        catch(ClassNotFoundException ex)
        {
            System.err.println("Impossible de charger la classe correspondant à l'objet reçu : " + ex.getMessage());
        }
        catch(IOException ex)
        {
            System.err.println("Erreur lors de la lecture de l'objet sur le flux : " + ex.getMessage());
        }
        
        if(ok == true && requete != null)
        {
            switch(requete.getType())
            {
                case RequeteACMAP.REQUEST_FLIGHTS:
                     requete.TraiterRequeteVols(socket, guiApplication);
                break;
                
                case RequeteACMAP.REQUEST_CHECKIN_OFF:
                    requete.TraiterCheckinOff(socket, guiApplication, "127.0.0.1", 50000);
                break;
                
                case RequeteACMAP.REQUEST_READY:
                    requete.TraiterReady(socket, guiApplication, adresseCheckin, portCheckin, adresseBagages, portBagages);
                break;
                
                case RequeteACMAP.REQUEST_RUNWAYS:
                    requete.TraiterRequetePistes(socket, guiApplication);
                break;
                
                case RequeteACMAP.REQUEST_RUNWAY_CHOICE:
                    requete.TraiterChoixPiste(socket, guiApplication);
                break;
                    
                default:
                    System.err.println("Type de requête inconnu - code : " + requete.getType());
                break;
            }
        }
    }
}
