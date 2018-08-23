package tickmap;

import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.util.List;
import requetepoolthreads.Reponse;
import tickmap_classes.Agent;
import tickmap_classes.Client;
import tickmap_classes.Vol;

public class ReponseTICKMAP implements Reponse, Serializable
{
    public static int LOGIN_OK = 200;
    public static int LOGIN_ERROR = 400;
    public static int LOGIN_UNKNOWN = 401;

    public static int SEND_SERVER_CERTIFICATE = 201;
    public static int CLIENT_CERTIFICAT_CHECK_ERROR = 402;
    
    public static int REQUEST_FLIGHTS_OK = 202;
    public static int REQUEST_FLIGHTS_ERROR = 403;
    
    public static int REQUEST_ADD_TRAVELER_OK = 203;
    public static int REQUEST_ADD_TRAVELER_ERROR = 404;
    
    public static int REQUEST_RESERVATION_OK = 204;
    public static int REQUEST_RESERVATION_ERROR = 405;
    
    public static int RESERVATION_DECISION_OK = 205;
    public static int RESERVATION_DECISION_ERROR = 406;
    
    public static int RESERVATION_VALIDATION_OK = 206;
    public static int RESERVATION_VALIDATION_ERROR = 207;
    
    private final int codeRetour;
    
    private final Agent agent;
    
    private final X509Certificate certificatServeur;
    
    private final List<Vol> listeVols;
    
    public final Client client;
    
    public final List<Integer> listeNumerosPlaces;
    public final double prixReservation;
    
    public final String message;

    // Constructeur code d'erreur ou pas de charge utile
    public ReponseTICKMAP(int cR)
    {
        codeRetour = cR;
        agent = null;
        certificatServeur = null;
        listeVols = null;
        client = null;
        listeNumerosPlaces = null;
        prixReservation = -1;
        message = null;
    }
    
    // Constructeur login ok
    public ReponseTICKMAP(int cR, Agent a)
    {
        codeRetour = cR;
        agent = a;
        certificatServeur = null;  
        listeVols = null;
        client = null;
        listeNumerosPlaces = null;
        prixReservation = -1;
        message = null;
    }
    
    // Constructeur vérification certificat client ok
    public ReponseTICKMAP(int cR, X509Certificate certificat_serveur)
    {
        codeRetour = cR;
        agent = null;
        certificatServeur = certificat_serveur;
        listeVols = null;
        client = null;
        listeNumerosPlaces = null;
        prixReservation = -1;
        message = null;
    }
    
    // Constructeur liste vols ok
    public ReponseTICKMAP(int cR, List<Vol> lV)
    {
        codeRetour = cR;
        agent = null;
        certificatServeur = null;
        listeVols = lV;
        client = null;
        listeNumerosPlaces = null;
        prixReservation = -1;
        message = null;
    }
    
    // Constructeur client ok
    public ReponseTICKMAP(int cR, Client c)
    {
        codeRetour = cR;
        agent = null;
        certificatServeur = null;
        listeVols = null;
        client = c;
        listeNumerosPlaces = null;
        prixReservation = -1;
        message = null;
    }
    
    // Constructeur réservation ok
    public ReponseTICKMAP(int cR, List<Integer> lNP, double pR)
    {
        codeRetour = cR;
        agent = null;
        certificatServeur = null;
        listeVols = null;
        client = null;
        listeNumerosPlaces = lNP;
        prixReservation = pR;
        message = null;
    }
    
    public ReponseTICKMAP(int cR, String m)
    {
        codeRetour = cR;
        agent = null;
        certificatServeur = null;
        listeVols = null;
        client = null;
        listeNumerosPlaces = null;
        prixReservation = -1;
        message = m;
    }
    
    @Override
    public int getCode()
    {
        return codeRetour;
    }
    
    public Agent getAgent()
    {
        return agent;
    }
    
    public X509Certificate getCertificatServeur()
    {
        return certificatServeur;
    }
    
    public List<Vol> getListeVols()
    {
        return listeVols;
    }
    
    public Client getClient()
    {
        return client;
    }
    
    public List<Integer> getListeNumerosPlaces()
    {
        return listeNumerosPlaces;
    }
    
    public double getPrixReservation()
    {
        return prixReservation;
    }
    
    public String getMessage()
    {
        return message;
    }
}
