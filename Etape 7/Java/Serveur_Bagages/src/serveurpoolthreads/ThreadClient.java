package serveurpoolthreads;

import ProtocoleLUGAP.*;
import java.io.*;
import java.net.*;
import requetepoolthreads.*;
import serveur_bagages.Serveur_Bagages;

public class ThreadClient extends Thread
{
    private final String nom;
    private String adresse;
    private final ServerSocket SSocket;
    private Socket CSocket;
    private ObjectInputStream ois;
    private Requete req;
    private final Serveur_Bagages guiApplication;
    
    public ThreadClient(String n, ServerSocket s, Serveur_Bagages cs)
    {
        nom = n;
        SSocket = s;
        guiApplication = cs;
    }
    
    @Override
    public void run()
    {
        while(!isInterrupted())
        {
            try
            {
                System.out.println("*** " + nom + " attend un client ***");
                CSocket = SSocket.accept();
                adresse = CSocket.getRemoteSocketAddress().toString();
                guiApplication.TraceEvenements(adresse + "#Accept#" + nom);
                guiApplication.getListeClients().add(CSocket);
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept : [" + e.getMessage() + "]"); System.exit(1);
            }

            while(!CSocket.isClosed())
            {
                try
                {
                    ois = new ObjectInputStream(CSocket.getInputStream());
                    req = (Requete)ois.readObject();
                    System.out.println("Instance de " + req.getClass().getSimpleName());
                } 
                catch (IOException | ClassNotFoundException ex)
                {
                    try
                    {
                        guiApplication.getListeClients().remove(CSocket);
                        CSocket.close();
                    } 
                    catch (IOException exClose)
                    {
                        System.err.println("Erreur du close() de la socket Client : " + exClose);
                    }
                }
                
                if(!CSocket.isClosed())
                {
                    guiApplication.TraceEvenements(adresse + "#" + req.getClass().getSimpleName() + "#" + nom);                    
                }
                else
                {
                    guiApplication.TraceEvenements(adresse + "#" + "Déconnexion" + "#" + nom);
                    break;
                }
                
                System.out.println("Recepetion d'une requete");
                
                if(req instanceof LoginLUGAP)
                {
                    ((LoginLUGAP) req).TraiterRequete(CSocket, guiApplication);
                }
                else if(req instanceof RequeteVolsLUGAP)
                    ((RequeteVolsLUGAP) req).TraiterRequete(CSocket, guiApplication);
                else if(req instanceof RequeteBagagesLUGAP)
                    ((RequeteBagagesLUGAP) req).TraiterRequete(CSocket, guiApplication);
                else if(req instanceof RequeteColumnChangedLUGAP)
                    ((RequeteColumnChangedLUGAP) req).TraiterRequete(CSocket, guiApplication);
                else if(req instanceof RequeteReadyLUGAP)
                    ((RequeteReadyLUGAP)req).TraiterRequete(CSocket, guiApplication);

            }
        }    
    }
}
