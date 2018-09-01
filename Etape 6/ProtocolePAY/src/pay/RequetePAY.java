package pay;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import static pay.UtilePAY.CODE_PROVIDER;
import static pay.UtilePAY.asymmetricAlg;
import pay_classes.Payment;
import requetepoolthreads.ConsoleServeur;
import requetepoolthreads.Requete;

public class RequetePAY implements Requete, Serializable
{
    public final static int REQUEST_PAYMENT = 1;
    
    private final int type;    
    private final byte[] agent;
    private final X509Certificate certificat;    
    private final Payment payment;

    public RequetePAY(int type, byte[] agent, X509Certificate certificat, Payment payment)
    {
        this.type = type;
        this.agent = agent;
        this.certificat = certificat;
        this.payment = payment;
    }
    
    @Override
    public Runnable createRunnablePAY(Socket s, ConsoleServeur cs, PrivateKey key, KeyStore keyStore, String adresse, int port)
    {
        switch(type)
        {    
            case REQUEST_PAYMENT:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TraiterPaiement(s, cs, key, keyStore, adresse, port);
                    }
                };
                
            default:
                System.err.println("Une requête de type inconnu a été détectée.");
                return null;
        }
    }
    
    private void TraiterPaiement(Socket sock, ConsoleServeur cs, PrivateKey key, KeyStore keyStore, String adresse, int port)
    {
        boolean erreur = false;

        try
        {
            certificat.verify(certificat.getPublicKey());
            certificat.checkValidity();
        } 
        catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException ex)
        {
            erreur = true;
            System.err.println("Erreur lors de la vérification du certificat du client : " + ex.getMessage());
        }
        
        ReponsePAY reponse;
        boolean ok;
        if(erreur == false)
        {
            Signature signature;
            try 
            {
                
                signature = Signature.getInstance (UtilePAY.signatureAlg, UtilePAY.CODE_PROVIDER);
                signature.initVerify(certificat.getPublicKey());
                signature.update(agent);
                ok = signature.verify(payment.getSignature());
            } 
            catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException ex) 
            {
                System.err.println("Erreur lors de la vérification de la signature : " + ex.getMessage());
                ok = false;
            }
            
            if(ok == true)
            {
                Cipher chiffrementd;
                byte[] numCarteClair;
                String numCarteDechiffre = "";
                try
                {
                    chiffrementd = Cipher.getInstance(asymmetricAlg, CODE_PROVIDER);
                    chiffrementd.init(Cipher.DECRYPT_MODE, key);
                    numCarteClair = chiffrementd.doFinal(payment.getNumeroCarte());
                    numCarteDechiffre = new String(numCarteClair);
                    System.out.println("Numéro de carte : " + numCarteDechiffre);
                } 
                catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex)
                {
                    System.err.println("Erreur lors du déchiffrement du message : " + ex.getMessage());
                }
                
                reponse = new ReponsePAY(ReponsePAY.REQUEST_PAYMENT_OK);
                cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "REQUEST_PAYMENT_OK#" + Thread.currentThread().getName());   
            }
                
            else
            {
                reponse = new ReponsePAY(ReponsePAY.REQUEST_PAYMENT_ERROR);
                cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "REQUEST_PAYMENT_ERROR#" + Thread.currentThread().getName());
            }
        }
        else
        {
            reponse = new ReponsePAY(ReponsePAY.REQUEST_PAYMENT_ERROR);
            cs.TraceEvenements(sock.getInetAddress() + ":" + sock.getLocalPort() + "#" + "REQUEST_PAYMENT_ERROR#" + Thread.currentThread().getName());
        }  
        
        boolean retour = UtilePAY.sendObject(sock, reponse);
        if(retour == false)
        {
            System.err.println("Une réponse à une requête de paiement n'a pas été envoyée.");
        }
    }

    @Override
    public Runnable createRunnableTICKMAP(Socket s, ConsoleServeur cs, X509Certificate certificat_serveur, SecretKey cleChiffrement, SecretKey cleHMAC)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
