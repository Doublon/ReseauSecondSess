package serveurpoolthreads;

import java.net.*;
import java.io.*;
import requetepoolthreads.*;

public class ThreadServeurCheckin extends Thread
{
    private final int port;
    private final int nombreThreads;
    private final ConsoleServeur guiApplication;
    private ServerSocket SSocket = null;
    
    public ThreadServeurCheckin(int p, int nbT, ConsoleServeur fs)
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
            ThreadClientCheckin thr = new ThreadClientCheckin("Thread n°" + String.valueOf(i) + " CheckIn",SSocket,guiApplication);
            thr.start();
        }
    }
}
