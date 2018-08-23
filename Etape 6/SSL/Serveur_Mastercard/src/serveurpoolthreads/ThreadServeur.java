package serveurpoolthreads;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

public class ThreadServeur extends Thread
{
    private final SourceTaches tachesAExecuter;
    private final int nombreThreads;
    private final ConsoleServeur guiApplication;
    private final SSLServerSocket SslSSocket;
    private final ServerSocket SSocket;
    
    public ThreadServeur(SSLServerSocket SslSSock, SourceTaches st, int nbT, ConsoleServeur fs)
    {
        SslSSocket = SslSSock;
        tachesAExecuter = st; 
        nombreThreads = nbT;
        guiApplication = fs;
        SSocket = null;
    }
    
    public ThreadServeur(ServerSocket SSock, SourceTaches st, int nbT, ConsoleServeur fs)
    {
        SslSSocket = null;
        tachesAExecuter = st; 
        nombreThreads = nbT;
        guiApplication = fs;
        SSocket = SSock;
    }
    
    @Override
    public void run()
    {
        for (int i=0 ; i < nombreThreads ; i++)
        {
            ThreadClient thr = new ThreadClient(tachesAExecuter, "Thread n°" + String.valueOf(i));
            thr.start();
        }
        
        SSLSocket SslCSocket = null;
        //Socket CSocket = null;
        while (!isInterrupted())
        {
            try
            {
                System.out.println("*** Serveur SSL en attente ***");
                SslCSocket = (SSLSocket) SslSSocket.accept();
                guiApplication.TraceEvenements(SslCSocket.getRemoteSocketAddress().toString()+"#Accept#ThreadServeur");
                
                //CSocket = SSocket.accept();
                //guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#Accept#ThreadServeur");
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]"); System.exit(1);
            }
            
            ObjectInputStream ois = null;
            Requete req = null;
            try
            {
                ois = new ObjectInputStream(SslCSocket.getInputStream());
                //ois = new ObjectInputStream(CSocket.getInputStream());
                Object object = ois.readObject();
                if(object instanceof Requete)
                {
                    req = (Requete) object;
                    guiApplication.TraceEvenements(SslCSocket.getRemoteSocketAddress().toString()+"#" + req.getClass().getSimpleName() + "#ThreadServeur");
                    //guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#" + req.getClass().getSimpleName() + "#ThreadServeur");
                }
                else
                {
                    guiApplication.TraceEvenements(SslCSocket.getRemoteSocketAddress().toString()+"#Objet inconnu#ThreadServeur");
                    //guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#Objet inconnu#ThreadServeur");
                }
            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
            }
            catch (IOException e)
            {
                System.err.println("Erreur ? [" + e.getMessage() + "]");
                e.printStackTrace();
            }
            
            if(req != null)
            {
                Runnable travail = req.createRunnableSEBATRAP(SslCSocket, guiApplication);
                //Runnable travail = req.createRunnableSEBATRAP(CSocket, guiApplication);
                if (travail != null)
                {
                    tachesAExecuter.recordTache(travail);
                }
            }
        }
    }
}
