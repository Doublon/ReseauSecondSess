package pay;

import java.io.Serializable;
import java.security.cert.X509Certificate;
import requetepoolthreads.Reponse;

public class ReponsePAY implements Reponse, Serializable
{   
    public static int SEND_SERVER_OK = 200;
    public static int CLIENT_CERTIFICAT_CHECK_ERROR = 400;
    
    public final static int REQUEST_PAYMENT_OK = 201;
    public final static int REQUEST_PAYMENT_ERROR = 401;
    
    private final int codeRetour;
    private final X509Certificate certificatServeur;
    
    public ReponsePAY(int cR)
    {
        codeRetour = cR;
        certificatServeur = null;
    }
    
    public ReponsePAY(int cR, X509Certificate certificat_serveur)
    {
        codeRetour = cR;
        certificatServeur = certificat_serveur;
    }
    
    @Override
    public int getCode()
    {
        return codeRetour;
    }
    
    public X509Certificate getCertificatServeur()
    {
        return certificatServeur;
    }
}
