package tickmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

abstract public class UtileTICKMAP
{
    public static String CODE_PROVIDER = "BC";
    public static String asymmetricAlg = "RSA/ECB/PKCS1Padding";
    public static String symmetricAlg = "DES/ECB/PKCS5Padding";
    public static String HMACAlg = "HMAC-MD5";
    
    public static byte[] serialize(Object obj) throws IOException 
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException 
    {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
    
    public static boolean sendObject(Socket socket, Object object)
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(object); 
            oos.flush();
            return true;
        }
        catch (IOException ex)
        { 
            System.err.println("Erreur réseau ? [" + ex.getMessage() + "]");
            return false;
        }
    }
    
    public static Object receiveObject(Socket socket)
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object object = ois.readObject();
            return object;
        }
        catch (ClassNotFoundException e)
        { 
            System.out.println("--- Erreur sur la classe = " + e.getMessage()); 
            return null;
        }
        catch (IOException e)
        { 
            System.out.println("--- Erreur IO = " + e.getMessage()); 
            return null;
        }
    }
    
    public static Cipher initCipher(String algorithm, String provider, int mode, Key key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(algorithm, provider);
            cipher.init(mode, key);
            return cipher;
        } 
        catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex)
        {
            System.err.println("Erreur lors de l'initialisation de l'objet Cipher : " + ex.getMessage());
            return null;
        } 
    }
    
    public static SealedObject createSealedObject(Serializable object, Cipher cipher)
    {
        SealedObject sealedObject = null;
        
        try
        {
            sealedObject = new SealedObject(object, cipher);
            return sealedObject;
        } 
        catch (IOException | IllegalBlockSizeException ex)
        {
            System.err.println("Erreur lors de la conversion de l'objet instance de " + object.getClass().getSimpleName() + 
                    " vers la classe " + SealedObject.class.getSimpleName());
            return null;
        }
    }
}
