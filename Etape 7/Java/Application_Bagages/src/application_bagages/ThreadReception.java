package application_bagages;

import ProtocoleACMAP.RequeteACMAP;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ThreadReception extends Thread
{
    private final Application_Bagages parent;
    private final int port;
    private ServerSocket SSocket;
    
    public ThreadReception(int port, Application_Bagages parent)
    {
        this.port = port;
        this.parent = parent;
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
                System.out.println("*** ThreadReception en attente ***");
                CSocket = SSocket.accept();
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]"); System.exit(1);
            }
            
            String message = null;
            try
            {
                DataInputStream dis = new DataInputStream(CSocket.getInputStream());
                message = dis.readUTF();
            }
            catch(IOException ex)
            {
                System.err.println("Erreur lors de la lecture du message du ThreadReception : " + ex.getMessage());
            }
            
            if(message != null && message.equals("OFF"))
            {
                try
                {
                    JOptionPane.showMessageDialog(null,"Le checkin va se terminer dans 10 minutes.","Information",JOptionPane.INFORMATION_MESSAGE);
                    DataOutputStream dos = new DataOutputStream(CSocket.getOutputStream());
                    dos.writeUTF("OK");
                    dos.flush();
                }
                catch(IOException ex)
                {
                    System.err.println("Erreur lors de l'envoi de la réponse de l'alerte : " + ex.getMessage());
                }
            }
            else
            {
                
            }
        }
    }
}
