package serveurpoolthreads;

import java.net.*;
import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

public class ThreadServeur extends Thread
{
    private final String ADRESSE;
    private final int PORT_PAYMENT;
    private final SourceTaches tachesAExecuter;
    private final int nombreThreads;
    private final ConsoleServeur guiApplication;
    private ServerSocket SSocket = null;
    private final X509Certificate certificat;
    private final PrivateKey privateKey;
    private final KeyStore keyStore;
    private final String ADRESSE_MASTERCARD;
    private final int PORT_MASTERCARD;
    
    public ThreadServeur(String a, int port, SourceTaches st, int nbT, ConsoleServeur fs, X509Certificate c, PrivateKey key, KeyStore ks, String a_m, int p_m)
    {
        ADRESSE = a;
        PORT_PAYMENT = port;
        tachesAExecuter = st; 
        nombreThreads = nbT;
        guiApplication = fs;
        certificat = c;
        privateKey = key;
        keyStore = ks;
        ADRESSE_MASTERCARD = a_m;
        PORT_MASTERCARD = p_m;
    }
    
    @Override
    public void run()
    {
        try
        {
            SSocket = new ServerSocket(PORT_PAYMENT);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]"); System.exit(1);
        }        

        for (int i=0 ; i < nombreThreads ; i++)
        {
            ThreadClient thr = new ThreadClient(tachesAExecuter, "Thread n°" + String.valueOf(i));
            thr.start();
        }

        Socket CSocket = null;
        while (!isInterrupted())
        {
            try
            {
                System.out.println("*** Serveur en attente ***");
                CSocket = SSocket.accept();
                guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#Accept#ThreadServeur");
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]"); System.exit(1);
            }
            
            ObjectInputStream ois = null;
            Requete req = null;
            try
            {
                ois = new ObjectInputStream(CSocket.getInputStream());
                Object object = ois.readObject();
                if(object instanceof Requete)
                {
                    req = (Requete) object;
                    guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#" + req.getClass().getSimpleName() + "#ThreadServeur");
                }
                else
                {
                    guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#Objet inconnu#ThreadServeur");
                }
            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
            }
            catch (IOException e)
            {
                System.err.println("Erreur IO :  [" + e.getMessage() + "]");
            }
            
            if(req != null)
            {
                Runnable travail = req.createRunnablePAY(CSocket, guiApplication, privateKey, keyStore, "192.168.0.19", 30018);
                if (travail != null)
                {
                    tachesAExecuter.recordTache(travail);
                }
            }
        }
    }
}
