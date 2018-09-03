package tickmap;

import database.utilities.BeanBDMySQL;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLSocket;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;
import static tickmap.UtileTICKMAP.CODE_PROVIDER;
import static tickmap.UtileTICKMAP.HMACAlg;
import tickmap_classes.Agent;
import tickmap_classes.Client;
import tickmap_classes.Reservation;
import tickmap_classes.Vol;

public class RequeteTICKMAP implements Requete, Serializable
{
    public final static int LOGIN_TICKMAP = 1;
    public final static int SEND_CLIENT_CERTIFICATE = 2;
    public final static int REQUEST_KEY_EXCHANGE = 3;
    public final static int REQUEST_FLIGHTS = 4;
    public final static int ADD_TRAVELER = 5;
    public final static int REQUEST_RESERVATION = 6;
    public final static int RESERVATION_AGREEMENT = 7;
    public final static int RESERVATION_DISAGREEMENT = 8;
    public final static int RESERVATION_VALIDATION = 9;
    
    private final int type;
    private final String login;
    private final byte[] password;
    private final long temps;
    private final double alea;
    
    private final X509Certificate certificat_client;
    
    private Client client;
    
    private final Reservation reservation;
    
    private final String numAgent;
    private final byte[] hmacEmploye;
    
    private final String numVol;
    private final List<Integer> listeNumerosPlaces;
    
    /*
        Ce constructeur est utilisé pour les requêtes ne nécessitant pas de charge utile
    */
    public RequeteTICKMAP(int type)
    {
        this.type = type;
        this.login = null;
        this.password = null;
        this.temps = -1;
        this.alea = -1;
        
        this.certificat_client = null;
        
        this.client = null;
        
        this.reservation = null;
        
        this.numAgent = null;
        this.hmacEmploye = null;
        
        this.numVol = null;
        this.listeNumerosPlaces = null;
    }
    
    // Constructeur du login
    public RequeteTICKMAP(int type, String login, byte[] password, long temps, double alea)
    {
        this.type = type;
        this.login = login;
        this.password = password;
        this.temps = temps;
        this.alea = alea;
        
        this.certificat_client = null;
        
        this.client = null;
        
        this.reservation = null;
        
        this.numAgent = null;
        this.hmacEmploye = null;
        
        this.numVol = null;
        this.listeNumerosPlaces = null;
    }
    
    // Constructeur d'envoi du certificat et de requête de clés
    public RequeteTICKMAP(int type, X509Certificate certificat_client)
    {
        this.type = type;
        this.login = null;
        this.password = null;
        this.temps = -1;
        this.alea = -1;
        
        this.certificat_client = certificat_client;
        
        this.client = null;
        
        this.reservation = null;
        
        this.numAgent = null;
        this.hmacEmploye = null;
        
        this.numVol = null;
        this.listeNumerosPlaces = null;
    }
    
    /*
        Constructeur d'envoi d'un client pour vérification ou ajout
    */
    public RequeteTICKMAP(int type, Client client)
    {
        this.type = type;
        
        this.login = null;
        this.password = null;
        this.temps = -1;
        this.alea = -1;
        
        this.certificat_client = null;
        
        this.client = client;
        
        this.reservation = null;
        
        this.numAgent = null;
        this.hmacEmploye = null;
        
        this.numVol = null;
        this.listeNumerosPlaces = null;
    }
    
    /*
        Constructeur d'envoi d'une réservation d'un client
    */
    public RequeteTICKMAP(int type, Reservation reservation)
    {
        this.type = type;
        
        this.login = null;
        this.password = null;
        this.temps = -1;
        this.alea = -1;
        
        this.certificat_client = null;
        
        this.client = null;
        
        this.reservation = reservation;
        
        this.numAgent = null;
        this.hmacEmploye = null;
        
        this.numVol = null;
        this.listeNumerosPlaces = null;
    }
    
    /*
        Constructeur pour confirmation de l'employé
    */
    public RequeteTICKMAP(int type, String numAgent, byte[] hmacEmploye)
    {
        this.type = type;
        
        this.login = null;
        this.password = null;
        this.temps = -1;
        this.alea = -1;
        
        this.certificat_client = null;
        
        this.client = null;
        
        this.reservation = null;
        
        this.numAgent = numAgent;
        this.hmacEmploye = hmacEmploye;
        
        this.numVol = null;
        this.listeNumerosPlaces = null;
    }
    
    /*
        Constructeur pour désaccord de l'employé
    */
    public RequeteTICKMAP(int type, String numAgent, byte[] hmacEmploye, String numVol, List<Integer> listeNumerosPlaces)
    {
        this.type = type;
        
        this.login = null;
        this.password = null;
        this.temps = -1;
        this.alea = -1;
        
        this.certificat_client = null;
        
        this.client = null;
        
        this.reservation = null;
        
        this.numAgent = numAgent;
        this.hmacEmploye = hmacEmploye;
        
        this.numVol = numVol;
        this.listeNumerosPlaces = listeNumerosPlaces;
    }
    
    public RequeteTICKMAP(int type, Reservation reservation, List<Integer> listeNumerosPlaces)
    {
        this.type = type;
        
        this.login = null;
        this.password = null;
        this.temps = -1;
        this.alea = -1;
        
        this.certificat_client = null;
        
        this.client = null;
        
        this.reservation = reservation;
        
        this.numAgent = null;
        this.hmacEmploye = null;
        
        this.numVol = null;
        this.listeNumerosPlaces = listeNumerosPlaces;
    }
    
    @Override
    public Runnable createRunnableTICKMAP(Socket s, ConsoleServeur cs, X509Certificate certificat_serveur, SecretKey cleChiffrement, SecretKey cleHMAC)
    {
        switch(type)
        {
            case LOGIN_TICKMAP:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterLogin(s, cs);
                    }
                };
            
            case SEND_CLIENT_CERTIFICATE:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterCertificat(s, cs, certificat_serveur);
                    }
                };
                
            case REQUEST_KEY_EXCHANGE:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterEchangeCles(s, cs, certificat_serveur, cleChiffrement, cleHMAC);
                    }
                };
            
            case REQUEST_FLIGHTS:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterRequeteVols(s, cs);
                    }
                };
                
            case ADD_TRAVELER:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterClient(s, cs, cleChiffrement);
                    }
                };
                
            case REQUEST_RESERVATION:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterReservation(s, cs, cleChiffrement);
                    }
                };
                
            case RESERVATION_AGREEMENT:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterAccord(s, cs, cleHMAC);
                    }
                };
                
            case RESERVATION_DISAGREEMENT:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterDesaccord(s, cs, cleHMAC);
                    }
                };
                
            case RESERVATION_VALIDATION:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterValidation(s, cs);
                    }
                };
                
            default:
                System.err.println("Une requête de type inconnu a été détectée.");
                return null;
        }
    }
    
    public void TraiterLogin(Socket sock, ConsoleServeur cs)
    {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = null;
        ReponseTICKMAP reponse = null;
        
        Agent agent = RechercheBDD(login);
        
        if(agent != null)
        {
            try 
            {
                md = MessageDigest.getInstance("SHA-1", CODE_PROVIDER);
            } 
            catch (NoSuchAlgorithmException | NoSuchProviderException ex) 
            {
                System.err.println("Problème messageDigest : " + ex);
            }
            
            if(md != null)
            {
                md.update(agent.getPassword().getBytes());  
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream bdos = new DataOutputStream(baos);
                try
                { 
                    bdos.writeLong(temps);
                    bdos.writeDouble(alea);
                } 
                catch (IOException ex) 
                {
                    System.err.println("Erreur IO lors de l'écriture du nombre aléatoire et du temps : " + ex);
                }
                md.update(baos.toByteArray());
                byte[] msgDLocal = md.digest();

                if(MessageDigest.isEqual(this.password, msgDLocal))
                {
                    reponse = new ReponseTICKMAP(ReponseTICKMAP.LOGIN_OK, agent);
                    cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "LOGIN_OK#" + Thread.currentThread().getName());
                }
                else
                {
                    reponse = new ReponseTICKMAP(ReponseTICKMAP.LOGIN_ERROR);
                    cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "LOGIN_ERROR#" + Thread.currentThread().getName());
                }
            }
        }
        else
        {
            reponse = new ReponseTICKMAP(ReponseTICKMAP.LOGIN_UNKNOWN);
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "LOGIN_UNKNOWN#" + Thread.currentThread().getName());
        }
        
        boolean retour = UtileTICKMAP.sendObject(sock, reponse);
        if(retour == false)
        {
            System.err.println("Une réponse à une requête de login n'a pas été envoyée.");
        }
    }
    
    private Agent RechercheBDD(String login)
    {        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        //BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "olivier", "root", "BD_AIRPORT");
        
        try
        {
            System.out.println("Avant");
            BBMS.ConnexionDB();
            System.out.println("Apres");
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "SELECT * FROM Agents WHERE login = \'" + login + "';";
        Agent retour = null;
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));

            if(BBMS.getResultat().next())
            {
                retour = new Agent();
                retour.setNumAgent(BBMS.getResultat().getString("numAgent"));
                retour.setNom(BBMS.getResultat().getString("nom"));
                retour.setPrenom(BBMS.getResultat().getString("prenom"));
                retour.setPoste(BBMS.getResultat().getString("poste"));
                retour.setLogin(BBMS.getResultat().getString("login"));
                retour.setPassword(BBMS.getResultat().getString("password"));
            }
        } 
        catch (SQLException ex)
        {
            System.err.println("Erreur lors du traitement du résultat : " + ex);
        }
        
        return retour;
    }
    
    private void TraiterCertificat(Socket sock, ConsoleServeur cs, X509Certificate certificat_serveur)
    {
        boolean erreur = false;
        
        try
        {
            certificat_client.verify(certificat_client.getPublicKey());
            certificat_client.checkValidity();
        } 
        catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException ex)
        {
            erreur = true;
            System.err.println("Erreur lors de la vérification du certificat du client : " + ex.getMessage());
        }
        
        ReponseTICKMAP reponse;
        if(erreur == false)
        {
            reponse = new ReponseTICKMAP(ReponseTICKMAP.SEND_SERVER_CERTIFICATE, certificat_serveur);
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "SEND_SERVER_CERTIFICATE#" + Thread.currentThread().getName());
        }
        else
        {
            reponse = new ReponseTICKMAP(ReponseTICKMAP.CLIENT_CERTIFICAT_CHECK_ERROR);
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "CLIENT_CERTIFICATE_CHECK_ERROR#" + Thread.currentThread().getName());
        }
        
        boolean retour = UtileTICKMAP.sendObject(sock, reponse);
        if(retour == false)
        {
            System.err.println("Une réponse à une requête de certificat n'a pas été envoyée.");
        }
    }
    
    private void TraiterEchangeCles(Socket sock, ConsoleServeur cs, X509Certificate certificat_serveur, SecretKey cleChiffrement, SecretKey cleHMAC)
    {   
        KeyExchangeTICKMAP reponse = null;
            
        reponse = new KeyExchangeTICKMAP(KeyExchangeTICKMAP.SEND_KEYS_OK, cleChiffrement, cleHMAC);

        try
        {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", CODE_PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, certificat_client.getPublicKey());
            SealedObject objet = new SealedObject(reponse, cipher);
            boolean retour = UtileTICKMAP.sendObject(sock, objet);
            if(retour == false)
            {
                System.err.println("Une réponse à une requête de login n'a pas été envoyée.");
            }
            else
            {
                cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "SEND_KEYS_OK#" + Thread.currentThread().getName());
            }
        } 
        catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | 
                InvalidKeyException | IllegalBlockSizeException | IOException ex)
        {
            System.err.println("Erreur lors du chiffrement asymétrique : " + ex.getMessage());
            reponse = new KeyExchangeTICKMAP(KeyExchangeTICKMAP.SEND_KEYS_ERROR);
            boolean retour = UtileTICKMAP.sendObject(sock, reponse);
            if(retour == false)
            {
                System.err.println("Une réponse à une requête de login n'a pas été envoyée.");
            }
            else
            {
                cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "SEND_KEYS_ERROR#" + Thread.currentThread().getName());
            }
        }
    }
    
    private void TraiterRequeteVols(Socket sock, ConsoleServeur cs)
    {
        List<Vol> listeVols = RechercheListeVols();
        ReponseTICKMAP reponse = null;
        
        if(listeVols != null)
        {
            reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_FLIGHTS_OK,listeVols);
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "REQUEST_FLIGHTS_OK#" + Thread.currentThread().getName());
        }  
        else
        {
            reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_FLIGHTS_ERROR,(List<Vol>)null);
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "REQUEST_FLIGHTS_ERROR#" + Thread.currentThread().getName());
        }
            
        
        boolean retour = UtileTICKMAP.sendObject(sock, reponse);
        if(retour == false)
        {
            System.err.println("Une réponse à une requête de certificat n'a pas été envoyée.");
        }
    }
    
    public List<Vol> RechercheListeVols()
    {
        List<Vol> retour = null;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "SELECT * "
                + "FROM Vols INNER JOIN Avions "
                + "ON Vols.numAvion = Avions.numAvion"
                + " WHERE DATEDIFF(heureDepart, curdate()) >= 0 AND DATEDIFF(heureDepart, curdate()) <= 7 AND placesRestantes > 0";
        
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            retour = new ArrayList();
            ResultSet rs = BBMS.getResultat();
            while(rs.next())
            {
                Vol vol = new Vol();
                vol.setNumVol(rs.getString("numVol"));
                vol.setDestination(rs.getString("destination"));
                vol.setZone(rs.getString("zone"));
                vol.setNombrePlaces(rs.getInt("nombrePlaces"));
                vol.setPlacesRestantes(rs.getInt("placesRestantes"));
                vol.setDistance(rs.getDouble("distance"));
                vol.setHeureDepart(rs.getTimestamp("heureDepart"));
                vol.setHeureArrivee(rs.getTimestamp("heureArrivee"));
                vol.setPrixParPersonne(rs.getDouble("prixParPersonne"));
                vol.setNumAvion(rs.getString("numAvion"));
                retour.add(vol);
            }
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
        }
        
        return retour;
    }
    
    private void TraiterClient(Socket sock, ConsoleServeur cs, SecretKey cleChiffrement)
    {
        ReponseTICKMAP reponse = null;
        
        if(client.getNumClient() != -1)
        {
            client = VerificationClient();
            
            if(client != null)
            {
                reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_ADD_TRAVELER_OK, client);
            }
            else
            {
                reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_ADD_TRAVELER_ERROR, (Client) null);
            }
        }
        else
        {
            int retourInsertion = InsererClient();
            if(retourInsertion == 1)
            {
                client = VerificationClient();
                reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_ADD_TRAVELER_OK, client);
                cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "REQUEST_ADD_TRAVELER_OK#" + Thread.currentThread().getName());
            }
            else
            {
                reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_ADD_TRAVELER_ERROR, (Client) null);
                cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "REQUEST_ADD_TRAVELER_ERROR#" + Thread.currentThread().getName());
            }
        }
        
        try
        {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding",CODE_PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE,cleChiffrement);
            SealedObject sealedObject = new SealedObject(reponse, cipher);
            boolean retour = UtileTICKMAP.sendObject(sock, sealedObject);
            if(retour == false)
            {
                System.err.println("Erreur lors de l'envoi de la réponse à l'ajout d'un voyageur...");
            }
        } 
        catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | 
                InvalidKeyException | IllegalBlockSizeException | IOException ex)
        {
            System.err.println("Erreur lors du chiffrement symétrique : " + ex.getMessage());
        }
    }
    
    private Client VerificationClient()
    {
        Client retour = null;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query;
        if(client.getNumClient() != 0)
        {
            query = "SELECT * FROM CLIENTS WHERE numClient = " + client.getNumClient() + ";";
        
            try
            {
                BBMS.setResultat(BBMS.executerRequete(query));
                ResultSet rs = BBMS.getResultat();
                if(rs.next())
                {
                    retour = new Client();
                    retour.setNumClient(Integer.parseInt(rs.getString("numClient")));
                    retour.setNom(rs.getString("nom"));
                    retour.setPrenom(rs.getString("prenom"));
                    retour.setLogin(rs.getString("login"));
                    retour.setPassword(rs.getString("password"));
                    retour.setSexe(rs.getString("sexe").charAt(0));
                }
                else
                {
                    retour = null;
                }
            } 
            catch (SQLException ex)
            {
                System.out.println("Erreur SQL : " + ex);
            }
        }
        else
        {
            query = "INSERT INTO Clients (nom, prenom, sexe, login, password) VALUES ('" + client.getNom() + "', '" + client.getPrenom() + "', '" +
                    client.getSexe() + "', '" + client.getLogin() + "', '" + client.getPassword() + "');";
            
            try
            {
                BBMS.executerUpdate(query);
                
                query = "SELECT * FROM CLIENTS WHERE login = '" + client.getLogin() + "';";
                
                BBMS.setResultat(BBMS.executerRequete(query));
                ResultSet rs = BBMS.getResultat();
                if(rs.next())
                {
                    retour = new Client();
                    retour.setNumClient(Integer.parseInt(rs.getString("numClient")));
                    retour.setNom(rs.getString("nom"));
                    retour.setPrenom(rs.getString("prenom"));
                    retour.setLogin(rs.getString("login"));
                    retour.setPassword(rs.getString("password"));
                    retour.setSexe(rs.getString("sexe").charAt(0));
                }
                else
                {
                    retour = null;
                }
            }
            catch (SQLException ex)
            {
                System.out.println("Erreur SQL : " + ex);
            }
        }
        
        try
        {
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
        }
        
        return retour;
    }
    
    private int InsererClient()
    {
        int retour;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "INSERT INTO Client(nom, prenom, sexe, login, password) VALUES(" + 
                client.getNom() + ", " + client.getPrenom() + ", " + client.getSexe() +", " + client.getLogin() + ", " + client.getPassword() + ")";
        
        try
        {
            retour = BBMS.executerUpdate(query);
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
            retour = 0;
        }
        
        return retour;
    }
    
    private void TraiterReservation(Socket sock, ConsoleServeur cs, SecretKey cleChiffrement)
    {
        boolean retour = EffectuerInsertionsReservation();
        ReponseTICKMAP reponse;
        if(retour == true)
        {
            List<Integer> listePlaces = RecupererNumerosPlaces();
            double prixReservation = reservation.getVol().getPrixParPersonne()*(reservation.getListeAccompagnants().size()+1);
            reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_RESERVATION_OK, listePlaces, prixReservation);
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "REQUEST_RESERVATION_OK#" + Thread.currentThread().getName());
        }
        else
        {
            reponse = new ReponseTICKMAP(ReponseTICKMAP.REQUEST_RESERVATION_ERROR);
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "REQUEST_RESERVATION_ERROR#" + Thread.currentThread().getName());
        }
        
        try
        {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding",CODE_PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE,cleChiffrement);
            SealedObject sealedObject = new SealedObject(reponse, cipher);
            boolean retourEnvoi = UtileTICKMAP.sendObject(sock, sealedObject);
            if(retourEnvoi == false)
            {
                System.err.println("Erreur lors de l'envoi de la réponse à la réservation d'un voyageur...");
            }
        } 
        catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | 
                InvalidKeyException | IllegalBlockSizeException | IOException ex)
        {
            System.err.println("Erreur lors du chiffrement symétrique : " + ex.getMessage());
        }
    }
    
    private int getNumeroPlace(String numVol)
    {
        int retour;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        } 
        
        String query = "SELECT nombrePlaces, placesRestantes FROM Vols WHERE numVol = '" + numVol + "';";
        
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            ResultSet rs = BBMS.getResultat();
            if(rs.next())
            {
                retour = rs.getInt("nombrePlaces") - rs.getInt("placesRestantes") + 1;
            }
            else
            {
                retour = -1;
            }
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.err.println("Erreur SQL : " + ex);
            retour = -1;
        }
        
        return retour;
    }
    
    private boolean EffectuerInsertionsReservation()
    {
        boolean retour = false;
        int nbreTuples = 0;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }

        int numPlace = getNumeroPlace(reservation.getVol().getNumVol()); 
        String[] queries = new String[reservation.getListeAccompagnants().size()+1];
        queries[0] = "INSERT INTO Reserve (nombreAccompagnants, numClient, numPlace, numVol, heureReservation) VALUES (" +
                reservation.getListeAccompagnants().size() + ", " + reservation.getClient().getNumClient() + ", " + numPlace + ", " + 
                reservation.getVol().getNumVol() + ", current_timestamp);";
        
        for(int i = 0 ; i < reservation.getListeAccompagnants().size() ; i++)
        {
            numPlace = numPlace + 1; 
            queries[i+1] = "INSERT INTO Reserve (nombreAccompagnants, numClient, numPlace, numVol, heureReservation) VALUES (null, " + 
                reservation.getListeAccompagnants().get(i).getNumClient() + ", " + numPlace + ", " + 
                reservation.getVol().getNumVol() + ", current_timestamp);";
        }
        
        for(int i = 0 ; i < queries.length ; i++)
        {
            try
            {
                nbreTuples += BBMS.executerUpdate(queries[i]);
            } 
            catch (SQLException ex)
            {
                System.err.println("Erreur lors de l'insertion du tuple en indice " + i);
            }
        }
        
        int nombrePlaces = reservation.getListeAccompagnants().size() + 1;
        String query = "UPDATE Vols set placesRestantes = placesRestantes - " + nombrePlaces
                + " WHERE numVol = '" + reservation.getVol().getNumVol() + "';";
        try
        {
            BBMS.executerUpdate(query);
        } 
        catch (SQLException ex)
        {
            System.err.println("Erreur lors de la mise à jour des places restantes pour le vol : " + ex.getMessage());
        }
        
        try
        {
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
        }
        
        if(nbreTuples == reservation.getListeAccompagnants().size()+1)
        {
            retour = true;
        }
        
        return retour;
    }
    
    private List<Integer> RecupererNumerosPlaces()
    {
        List<Integer> retour = new ArrayList();
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String listeClients = String.valueOf(reservation.getClient().getNumClient());
        for(int i = 0 ; i < reservation.getListeAccompagnants().size() ; i++)
        {
            listeClients += "," + reservation.getListeAccompagnants().get(i).getNumClient();
        }
        String query = "SELECT numPlace FROM Reserve WHERE numClient IN (" + listeClients + ") and numVol = " + reservation.getVol().getNumVol() + ";";
        
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            ResultSet rs = BBMS.getResultat();
            while(rs.next())
            {
                retour.add(rs.getInt("numPlace"));
            }
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.err.println("Erreur SQL : " + ex);
        }
        
        return retour;
    }
    
    private void TraiterAccord(Socket sock, ConsoleServeur cs, SecretKey cleHMAC)
    {
        Agent agentCourantDB = RecupererAgent();
        ReponseTICKMAP reponse;
        
        if(agentCourantDB != null)
        {
            boolean retour = CheckAgentIdentity(agentCourantDB.toString().getBytes(), cleHMAC);
            
            if(retour == true)
            {
                reponse = new ReponseTICKMAP(ReponseTICKMAP.RESERVATION_DECISION_OK, "La commande a bien été confirmée.");
                cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "RESERVATION_DECISION_OK#" + Thread.currentThread().getName());
            }
            else
            {
                reponse = new ReponseTICKMAP(ReponseTICKMAP.RESERVATION_DECISION_ERROR, "Une erreur est survenue lors de la vérification de l'identité de l'agent.");
                cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "RESERVATION_DECISION_ERROR#" + Thread.currentThread().getName());
            }
        }
        else
        {
            reponse = new ReponseTICKMAP(ReponseTICKMAP.RESERVATION_DECISION_ERROR, "Une erreur est survenue lors de la vérification de l'identité de l'agent.");
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "RESERVATION_DECISION_ERROR#" + Thread.currentThread().getName());
        }
        
        boolean retour = UtileTICKMAP.sendObject(sock, reponse);
        if(retour == false)
        {
            System.err.println("Une réponse à une requête de login n'a pas été envoyée.");
        }
    }
    
    private void TraiterDesaccord(Socket sock, ConsoleServeur cs, SecretKey cleHMAC)
    {
        Agent agentCourantDB = RecupererAgent();
        ReponseTICKMAP reponse;
        
        if(agentCourantDB != null)
        {
            boolean retour = CheckAgentIdentity(agentCourantDB.toString().getBytes(), cleHMAC);
            
            if(retour == true)
            {
                boolean retourSuppressions = SupprimerReservations();
                
                if(retourSuppressions == true)
                {
                    reponse = new ReponseTICKMAP(ReponseTICKMAP.RESERVATION_DECISION_OK, "Les réservations ont bien été supprimées.");
                    cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "RESERVATION_DECISION_OK#" + Thread.currentThread().getName());
                }
                else
                {
                    reponse = new ReponseTICKMAP(ReponseTICKMAP.RESERVATION_DECISION_ERROR, "Une erreur est survenue lors de la suppression des réservations.");
                    cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "RESERVATION_DECISION_ERROR#" + Thread.currentThread().getName());
                }
            }
            else
            {
                System.err.println("Erreur lors de l'identification de l'agent...");
                reponse = new ReponseTICKMAP(ReponseTICKMAP.RESERVATION_DECISION_ERROR, "Une erreur est survenue lors de la vérification de l'identité de l'agent.");
                cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "RESERVATION_DECISION_ERROR#" + Thread.currentThread().getName());
            }
        }
        else
        {
            System.err.println("Il semblerait que l'agent n'ait pas été trouvé dans la base...");
            reponse = new ReponseTICKMAP(ReponseTICKMAP.RESERVATION_DECISION_ERROR, "Une erreur est survenue lors de la vérification de l'identité de l'agent.");
        }
        
        boolean retour = UtileTICKMAP.sendObject(sock, reponse);
        if(retour == false)
        {
            System.err.println("Une réponse à une requête de login n'a pas été envoyée.");
        }
    }
    
    private Agent RecupererAgent()
    {
        Agent retour = null;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        } 
        
        String query = "SELECT * FROM Agents WHERE numAgent = '" + numAgent + "';";
        
        try
        {
            BBMS.setResultat(BBMS.executerRequete(query));
            ResultSet rs = BBMS.getResultat();
            if(rs.next())
            {
                retour = new Agent();
                retour.setNumAgent(rs.getString("numAgent"));
                retour.setNom(rs.getString("nom"));
                retour.setPrenom(rs.getString("prenom"));
                retour.setPoste(rs.getString("poste"));
                retour.setLogin(rs.getString("login"));
                retour.setPassword(rs.getString("password"));
            }
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.err.println("Erreur SQL : " + ex);
        }
        
        return retour;
    }
    
    private boolean CheckAgentIdentity(byte[] agent, SecretKey cle)
    {
        boolean retour;
        Mac hlocal;
        try
        {
            hlocal = Mac.getInstance(HMACAlg, CODE_PROVIDER);
            hlocal.init(cle);
            hlocal.update(agent);
            byte[] hlocalb = hlocal.doFinal();
            if (MessageDigest.isEqual(hmacEmploye,hlocalb))
            {
                retour = true;
            }
            else
            {
                retour = false;
            } 
        } 
        catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException ex)
        {
            System.err.println("Erreur lors de la construction du HMAC local : " + ex.getMessage());
            retour = false;
        }
        
        return retour;
    }
    
    private boolean SupprimerReservations()
    {
        boolean retour = false;
        int nbreTuples = 0;
        String listePlaces = String.valueOf(listeNumerosPlaces.get(0));
        
        for(int i = 1 ; i < listeNumerosPlaces.size() ; i++)
        {
            listePlaces += ", " + listeNumerosPlaces.get(i);
        }
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "DELETE FROM Reserve WHERE numVol = '" + numVol + "' AND numPlace IN (" + listePlaces + ");";
        try
        {
            nbreTuples = BBMS.executerUpdate(query);
            query = "UPDATE Vols SET placesRestantes = placesRestantes + " + listeNumerosPlaces.size() + " WHERE numVol = '" + numVol + "';";
            BBMS.executerUpdate(query);
            BBMS.DeconnexionDB();
            if(nbreTuples == listeNumerosPlaces.size())
            {
                retour = true;
            }
            else
            {
                retour = false;
            }
        } 
        catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex);
            retour = false;
        }
        
        return retour;
    }
    
    private void TraiterValidation(Socket sock, ConsoleServeur cs)
    {
        boolean retourValidation = EffectuerInsertionsValidation();
        ReponseTICKMAP reponse;
        
        if(retourValidation == true)
        {
            reponse = new ReponseTICKMAP(ReponseTICKMAP.RESERVATION_VALIDATION_OK);
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "RESERVATION_VALIDATION_OK#" + Thread.currentThread().getName());
        }
        else
        {
            reponse = new ReponseTICKMAP(ReponseTICKMAP.RESERVATION_VALIDATION_ERROR);
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "RESERVATION_VALIDATION_ERROR#" + Thread.currentThread().getName());
        }
        
        boolean retour = UtileTICKMAP.sendObject(sock, reponse);
        if(retour == false)
        {
            System.err.println("Une réponse à une requête de login n'a pas été envoyée.");
        }
    }
    
    private boolean EffectuerInsertionsValidation()
    {
        boolean retour = false;
        int nbreTuples = 0;
        
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }        
           
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        
        int numPlace = listeNumerosPlaces.get(0);
        String num = String.format("%04d", numPlace);
        String date = sdf.format(reservation.getVol().getHeureDepart());
        String numBillet = reservation.getVol().getNumVol() + "-" + date + "-" + num;
                
        String[] queriesInsert = new String[reservation.getListeAccompagnants().size()+1];
        String[] queriesDelete = new String[reservation.getListeAccompagnants().size()+1];
        queriesInsert[0] = "INSERT INTO Billets VALUES ('" + 
                numBillet + "', " + reservation.getListeAccompagnants().size() + ", " + reservation.getClient().getNumClient()
                + ", " + numPlace + ", '" + reservation.getVol().getNumVol() + "');";
        queriesDelete[0] = "DELETE FROM Reserve WHERE numVol = '" + reservation.getVol().getNumVol() + "' AND "
                + "numPlace = " + numPlace + " AND numClient = " + reservation.getClient().getNumClient() + ";";
        
        for(int i = 1 ; i < reservation.getListeAccompagnants().size()+1 ; i++)
        {
            numPlace = listeNumerosPlaces.get(i);
            num = String.format("%04d", numPlace);
            numBillet = reservation.getVol().getNumVol() + "-" + date + "-" + num;
            queriesInsert[i] = "INSERT INTO Billets VALUES ('" + numBillet + "', null, " + reservation.getListeAccompagnants().get(i-1).getNumClient()
                    + ", " + numPlace + ", '" + reservation.getVol().getNumVol() + "');";
            
            queriesDelete[i] = "DELETE FROM Reserve WHERE numVol = '" + reservation.getVol().getNumVol() + "' AND "
                + "numPlace = " + numPlace + " AND numClient = " + reservation.getListeAccompagnants().get(i-1).getNumClient() + ";";
        }
        
        for(int i = 0 ; i < queriesInsert.length ; i++)
        {
            try
            {
                BBMS.executerUpdate(queriesInsert[i]);
                nbreTuples += BBMS.executerUpdate(queriesDelete[i]);
            } 
            catch (SQLException ex)
            {
                System.err.println("Erreur lors de l'insertion d'un billet dans la table : " + ex.getMessage());
            }
        }
        
        try
        {
            BBMS.DeconnexionDB();
        } 
        catch (SQLException ex)
        {
            System.err.println("Erreur SQL lors de la déconnexion à la base de données : " + ex);
        }
        
        if(nbreTuples == reservation.getListeAccompagnants().size()+1)
        {
            retour = true;
        }
        
        return retour;
    }

    @Override
    public Runnable createRunnableSEBATRAP(SSLSocket sslS, ConsoleServeur cs)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Runnable createRunnableSEBATRAP(Socket s, ConsoleServeur cs)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Runnable createRunnablePAY(Socket s, ConsoleServeur cs, PrivateKey key, KeyStore keyStore, String adresse, int port, X509Certificate certificat_serveur)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}