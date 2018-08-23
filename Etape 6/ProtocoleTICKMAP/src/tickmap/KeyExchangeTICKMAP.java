package tickmap;

import java.io.Serializable;
import javax.crypto.SecretKey;
import requetepoolthreads.Reponse;

public class KeyExchangeTICKMAP implements Reponse, Serializable
{
    public static int SEND_KEYS_OK = 202;
    public static int SEND_KEYS_ERROR = 403;
    
    private final int codeRetour;
    
    private final SecretKey cleChiffrement;
    private final SecretKey cleHMAC;
    
    public KeyExchangeTICKMAP(int cR)
    {
        codeRetour = cR;
        cleChiffrement = null;
        cleHMAC = null;
    }
    
    public KeyExchangeTICKMAP(int cR, SecretKey cleC, SecretKey cleH)
    {
        codeRetour = cR;
        cleChiffrement = cleC;
        cleHMAC = cleH;
    }    
    
    @Override
    public int getCode()
    {
        return codeRetour;
    }

    public SecretKey getCleChiffrement()
    {
        return cleChiffrement;
    }
    
    public SecretKey getCleHMAC()
    {
        return cleHMAC;
    }
}
