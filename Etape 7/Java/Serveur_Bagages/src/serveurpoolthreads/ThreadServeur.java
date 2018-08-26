package serveurpoolthreads;

import java.net.*;
import java.io.*;
import serveur_bagages.Serveur_Bagages;

public class ThreadServeur extends Thread
{
    private final int port;
    private final int nombreThreads;
    private final Serveur_Bagages guiApplication;
    private ServerSocket SSocket = null;
    
    public ThreadServeur(int p, int nbT, Serveur_Bagages fs)
    {
        port = p; 
        nombreThreads = nbT; 
        guiApplication = fs;
    }
    
    @Override
    public void run()
    {
        try
        {
            SSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]"); System.exit(1);
        }
        
        for (int i=1 ; i <= nombreThreads ; i++)
        {
            ThreadClient thr = new ThreadClient ("Thread n°" + String.valueOf(i),SSocket,guiApplication);
            thr.start();
        }
    }
}
