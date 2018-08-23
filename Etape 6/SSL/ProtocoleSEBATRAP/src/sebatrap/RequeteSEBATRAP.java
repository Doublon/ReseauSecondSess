package sebatrap;

import database.utilities.BeanBDMySQL;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.crypto.SecretKey;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.SSLSocket;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

public class RequeteSEBATRAP implements Requete, Serializable
{
    public final static int REQUEST_PAYMENT_OPERATION = 1;
    public final static int MYSQL_ERROR_DUPLICATE_KEY = 1062;
    
    private final int type;
    private final int numClient;
    private final double montant;
    private final String numCarte;
    private final String adresseMail;
    
    public RequeteSEBATRAP(int type, int numClient, double montant, String numCarte, String adresseMail)
    {
        this.type = type;
        this.numClient = numClient;
        this.montant = montant;
        this.numCarte = numCarte;
        this.adresseMail = adresseMail;
    }

    @Override
    public Runnable createRunnableSEBATRAP(SSLSocket sslS, ConsoleServeur cs)
    {
        switch(type)
        {    
            case REQUEST_PAYMENT_OPERATION:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterOperationPaiement(sslS, cs);
                    }
                };
                
            default:
                System.err.println("Une requête de type inconnu a été détectée.");
                return null;
        }
    }
    
    private void TraiterOperationPaiement(SSLSocket sslSocket, ConsoleServeur cs)
    {
        System.out.println("Je vais m'occuper du client : " + numClient + " avec la carte : " + numCarte);
        
        boolean ok = false;
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "INSERT INTO Comptes VALUES ('" + numCarte + "', 500," + numClient + ");";
            
        try
        {
            BBMS.executerUpdate(query);
            
            query = "SELECT * FROM COMPTES WHERE numCompte = '" + numCarte + "';";
            try
            {
                BBMS.setResultat(BBMS.executerRequete(query));
                ResultSet rs = BBMS.getResultat();
                if(rs.next())
                {
                    ok = true;
                }
            }
            catch(SQLException ex)
            {
                System.err.println("Erreur lors de la récupération des données du compte : " + ex.getMessage());
            }
        }
        catch (SQLException ex)
        {
            if(ex.getErrorCode() == MYSQL_ERROR_DUPLICATE_KEY)
            {
                query = "SELECT * FROM COMPTES WHERE numCompte = '" + numCarte + "';";
                try
                {
                    BBMS.setResultat(BBMS.executerRequete(query));
                    ResultSet rs = BBMS.getResultat();
                    if(rs.next())
                    {
                        ok = true;
                    }
                }
                catch(SQLException ex2)
                {
                    System.err.println("Erreur lors de la récupération des données du compte : " + ex.getMessage());
                }
            }
        }
        
        ReponseSEBATRAP reponse;
        if(ok == true)
        {
            query = "UPDATE Comptes SET balance = balance - " + montant + " WHERE numCompte = '" + numCarte + "' AND numClient = " + numClient + ";";
            
            try
            {
                int ret = BBMS.executerUpdate(query);
                if(ret == 1)
                {
                    Properties mailProperties;
                    mailProperties = System.getProperties();
                    mailProperties.put("mail.smtp.host", "10.59.26.134");
                    mailProperties.put("file.encoding", "iso-8859-1");
                    
                    Session mailSession;
                    mailSession = Session.getDefaultInstance(mailProperties);

                    try
                    {
                        String email = "verwimp@u2.tech.hepl.local";

                        MimeMessage msg = new MimeMessage(mailSession);
                        msg.setFrom (new InternetAddress (email));
                        msg.setRecipient (Message.RecipientType.TO, new InternetAddress (adresseMail));
                        msg.setSentDate(new java.util.Date());
                        msg.setSubject("Vos billets ont été réservés et payés avec succès !");

                        MimeBodyPart corpsMessage = new MimeBodyPart();
                        corpsMessage.setText("Votre réservation a bien été prise en compte et le paiement a été effectué avec succès. Nous vous souhaitons un bon séjour grâce à InpresAirport !");
                        Multipart contenu = new MimeMultipart();
                        contenu.addBodyPart(corpsMessage);        

                        msg.setContent(contenu);
                        Transport.send(msg);
                    } 
                    catch (MessagingException ex)
                    {
                        System.out.println("Messaging Exception : " + ex);
                    }
                    
                    reponse = new ReponseSEBATRAP(ReponseSEBATRAP.PAYMENT_OPERATION_OK);
                }
                else
                {
                    reponse = new ReponseSEBATRAP(ReponseSEBATRAP.PAYMENT_OPERATION_ERROR);
                }
            }
            catch(SQLException ex)
            {
                System.err.println("Exception lors de la mise à jour de la balance du compte : " + ex.getMessage());
                reponse = new ReponseSEBATRAP(ReponseSEBATRAP.PAYMENT_OPERATION_ERROR);
            }
        }
        else
        {
            reponse = new ReponseSEBATRAP(ReponseSEBATRAP.PAYMENT_OPERATION_ERROR);
        }
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sslSocket.getOutputStream());
            oos.writeObject(reponse);
            oos.flush();
        }
        catch(IOException ex)
        {
            System.err.println("Erreur lors de l'envoi de la réponse : " + ex.getMessage());
        }
    }
    
    @Override
    public Runnable createRunnableTICKMAP(Socket s, ConsoleServeur cs, X509Certificate certificat_serveur, SecretKey cleChiffrement, SecretKey cleHMAC)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Runnable createRunnablePAY(Socket s, ConsoleServeur cs, PrivateKey key, KeyStore keyStore, String adresse, int port)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Runnable createRunnableSEBATRAP(Socket s, ConsoleServeur cs)
    {
        switch(type)
        {    
            case REQUEST_PAYMENT_OPERATION:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterOperationPaiement(s, cs);
                    }
                };
                
            default:
                System.err.println("Une requête de type inconnu a été détectée.");
                return null;
        }
    }
    
    private void TraiterOperationPaiement(Socket socket, ConsoleServeur cs)
    {
        System.out.println("Je vais m'occuper du client : " + numClient + " avec la carte : " + numCarte);
        
        boolean ok = false;
        BeanBDMySQL BBMS = new BeanBDMySQL("localhost", "3306", "jim", "root", "BD_AIRPORT");
        
        try
        {
            BBMS.ConnexionDB();
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.err.println("Erreur lors de la connexion à la " + BBMS.getSchema() + " : " + ex);
        }
        
        String query = "INSERT INTO Comptes VALUES ('" + numCarte + "', 500," + numClient + ");";
            
        try
        {
            BBMS.executerUpdate(query);
            
            query = "SELECT * FROM COMPTES WHERE numCompte = '" + numCarte + "';";
            try
            {
                BBMS.setResultat(BBMS.executerRequete(query));
                ResultSet rs = BBMS.getResultat();
                if(rs.next())
                {
                    ok = true;
                }
            }
            catch(SQLException ex)
            {
                System.err.println("Erreur lors de la récupération des données du compte : " + ex.getMessage());
            }
        }
        catch (SQLException ex)
        {
            if(ex.getErrorCode() == MYSQL_ERROR_DUPLICATE_KEY)
            {
                query = "SELECT * FROM COMPTES WHERE numCompte = '" + numCarte + "';";
                try
                {
                    BBMS.setResultat(BBMS.executerRequete(query));
                    ResultSet rs = BBMS.getResultat();
                    if(rs.next())
                    {
                        ok = true;
                    }
                }
                catch(SQLException ex2)
                {
                    System.err.println("Erreur lors de la récupération des données du compte : " + ex.getMessage());
                }
            }
        }
        
        ReponseSEBATRAP reponse;
        if(ok == true)
        {
            query = "UPDATE Comptes SET balance = balance - " + montant + " WHERE numCompte = '" + numCarte + "' AND numClient = " + numClient + ";";
            
            try
            {
                int ret = BBMS.executerUpdate(query);
                if(ret == 1)
                {
                    /*Properties mailProperties;
                    mailProperties = System.getProperties();
                    mailProperties.put("mail.smtp.host", "10.59.26.134");
                    mailProperties.put("file.encoding", "iso-8859-1");
                    
                    Session mailSession;
                    mailSession = Session.getDefaultInstance(mailProperties);

                    try
                    {
                        String email = "verwimp@u2.tech.hepl.local";

                        MimeMessage msg = new MimeMessage(mailSession);
                        msg.setFrom (new InternetAddress (email));
                        msg.setRecipient (Message.RecipientType.TO, new InternetAddress (adresseMail));
                        msg.setSentDate(new java.util.Date());
                        msg.setSubject("Vos billets ont été réservés et payés avec succès !");

                        MimeBodyPart corpsMessage = new MimeBodyPart();
                        corpsMessage.setText("Votre réservation a bien été prise en compte et le paiement a été effectué avec succès. Nous vous souhaitons un bon séjour grâce à InpresAirport !");
                        Multipart contenu = new MimeMultipart();
                        contenu.addBodyPart(corpsMessage);        

                        msg.setContent(contenu);
                        Transport.send(msg);
                    } 
                    catch (MessagingException ex)
                    {
                        System.out.println("Messaging Exception : " + ex);
                    }*/
                    
                    reponse = new ReponseSEBATRAP(ReponseSEBATRAP.PAYMENT_OPERATION_OK);
                }
                else
                {
                    reponse = new ReponseSEBATRAP(ReponseSEBATRAP.PAYMENT_OPERATION_ERROR);
                }
            }
            catch(SQLException ex)
            {
                System.err.println("Exception lors de la mise à jour de la balance du compte : " + ex.getMessage());
                reponse = new ReponseSEBATRAP(ReponseSEBATRAP.PAYMENT_OPERATION_ERROR);
            }
        }
        else
        {
            reponse = new ReponseSEBATRAP(ReponseSEBATRAP.PAYMENT_OPERATION_ERROR);
        }
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(reponse);
            oos.flush();
        }
        catch(IOException ex)
        {
            System.err.println("Erreur lors de l'envoi de la réponse : " + ex.getMessage());
        }
    }
}
