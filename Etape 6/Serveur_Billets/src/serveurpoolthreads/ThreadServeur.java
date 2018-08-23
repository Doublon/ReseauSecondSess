package serveurpoolthreads;

import java.net.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.X509Certificate;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;
import static tickmap.UtileTICKMAP.CODE_PROVIDER;

public class ThreadServeur extends Thread
{
    private final String ADRESSE;
    private final int PORT_BILLETS;
    private final SourceTaches tachesAExecuter;
    private final int nombreThreads;
    private final ConsoleServeur guiApplication;
    private ServerSocket SSocket = null;
    private final X509Certificate certificat;
    private final SecretKey cleChiffrement;
    private final SecretKey cleHMAC;
    
    public ThreadServeur(String a, int port, SourceTaches st, int nbT, ConsoleServeur fs, X509Certificate c, SecretKey cleC, SecretKey cleH)
    {
        ADRESSE = a;
        PORT_BILLETS = port;
        tachesAExecuter = st; 
        nombreThreads = nbT;
        guiApplication = fs;
        certificat = c;
        cleChiffrement = cleC;
        cleHMAC = cleH;
    }
    
    @Override
    public void run()
    {
        try
        {
            SSocket = new ServerSocket(PORT_BILLETS);
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
                }
                else
                {
                    if(object instanceof SealedObject)
                    {
                        try
                        {
                            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding", CODE_PROVIDER);
                            cipher.init(Cipher.DECRYPT_MODE,cleChiffrement);
                            req = (Requete) ((SealedObject) object).getObject(cipher);
                        } 
                        catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | 
                                IllegalBlockSizeException | BadPaddingException | InvalidKeyException ex)
                        {
                            System.err.println("Un objet de nature inconnue a été détecté : " + ex.getMessage());
                        }
                    }
                }
                if(req != null)
                    guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#"+req.getClass().getSimpleName()+"#ThreadServeur");
                else
                    guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#Objet de type inconnu#Thread Serveur");
            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
            }
            catch (IOException e)
            {
                System.err.println("Erreur ? [" + e.getMessage() + "]");
            }
            
            if(req != null)
            {
                Runnable travail = req.createRunnableTICKMAP(CSocket, guiApplication, certificat, cleChiffrement, cleHMAC);
                if (travail != null)
                {
                    tachesAExecuter.recordTache(travail);
                }
            }
        }
    }
}
