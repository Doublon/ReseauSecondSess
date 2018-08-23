package requetepoolthreads;

import java.net.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLSocket;

public interface Requete
{
    public Runnable createRunnableTICKMAP (Socket s, ConsoleServeur cs, X509Certificate certificat_serveur, SecretKey cleChiffrement, SecretKey cleHMAC);
    public Runnable createRunnablePAY (Socket s, ConsoleServeur cs, PrivateKey key, KeyStore keyStore, String adresse, int port);
    public Runnable createRunnableSEBATRAP(SSLSocket sslS, ConsoleServeur cs);
    public Runnable createRunnableSEBATRAP(Socket s, ConsoleServeur cs);
}