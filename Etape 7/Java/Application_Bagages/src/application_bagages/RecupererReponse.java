package application_bagages;

import ProtocoleLUGAP.ReponseLUGAP;
import java.net.*;
import java.io.*;

public interface RecupererReponse
{
    public static ReponseLUGAP RecupererReponse(Socket sock)
    {
        ReponseLUGAP rep = null;
        
        try
        {
            ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
            rep = (ReponseLUGAP)ois.readObject();
            System.out.println("*** Réponse reçue : " + rep.getChargeUtile());
        }
        catch (ClassNotFoundException e)
        { 
            System.err.println("--- Erreur sur la classe = " + e.getMessage()); 
        }
        catch (IOException e)
        { 
            System.err.println("--- Erreur IO = " + e.getMessage()); 
        }
        
        return rep;
    }
}
