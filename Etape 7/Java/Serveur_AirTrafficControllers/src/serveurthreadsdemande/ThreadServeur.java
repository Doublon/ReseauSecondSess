package serveurthreadsdemande;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import requetepoolthreads.ConsoleServeur;

public class ThreadServeur extends Thread
{
    private final int port;
    private ServerSocket SSocket = null;
    private final ConsoleServeur guiApplication;
    private final String adresseBagages;
    private final int portBagages;
    
    public ThreadServeur(int p, ConsoleServeur cs, String aB, int pB)
    {
        port = p;
        guiApplication = cs;
        adresseBagages = aB;
        portBagages = pB;
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
            
            ThreadClient tc = new ThreadClient(CSocket, guiApplication, adresseBagages, portBagages);
            tc.start();
        }
    }
}
