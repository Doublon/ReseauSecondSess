package serveurpoolthreads;

import ACMAP_classes.Bagage;
import ProtocoleACMAP.RequeteACMAP;
import static ProtocoleACMAP.RequeteACMAP.REQUEST_ADD_LUGGAGE;
import static ProtocoleACMAP.RequeteACMAP.REQUEST_TICKET_CHECK;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import requetepoolthreads.ConsoleServeur;

public class ThreadClientCheckin extends Thread
{
    private final String nom;
    private String adresse;
    private final ServerSocket SSocket;
    private Socket CSocket;
    private ObjectInputStream ois;
    private final ConsoleServeur guiApplication;
    
    public ThreadClientCheckin(String n, ServerSocket s, ConsoleServeur cs)
    {
        nom = n;
        SSocket = s;
        guiApplication = cs;
    }
    
    @Override
    public void run()
    {
        while(!isInterrupted())
        {
            try
            {
                System.out.println("*** " + nom + " attend un client ***");
                CSocket = SSocket.accept();
                adresse = CSocket.getRemoteSocketAddress().toString();
                guiApplication.TraceEvenements(adresse + "#Accept#" + nom);
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept : [" + e.getMessage() + "]"); System.exit(1);
            }
            
            while(!CSocket.isClosed())
            {
                String recu = null;
                /*try
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(CSocket.getInputStream()));
                    recu = br.readLine();
                    System.out.println("Recu : " + recu);
                }
                catch(IOException ex)
                {
                    System.err.println("Exception lors de la lecture du message : " + ex.getMessage());
                    try
                    {
                        CSocket.close();
                    } 
                    catch (IOException exClose)
                    {
                        System.err.println("Erreur du close() de la socket Client : " + exClose);
                    }
                }*/
                
                byte[] messageByte = new byte[1000];               
                try
                {
                    DataInputStream dis = new DataInputStream(CSocket.getInputStream());
                    dis.readFully(messageByte);
                    recu = new String(messageByte);
                    recu = recu.trim();
                }
                catch(EOFException ex) 
                {
                    System.err.println("Erreur EOF lors de la lecture du message : " + ex.getMessage());
                }
                catch(IOException ex)
                {
                    System.err.println("Errer IO lors de la lecture du message : " + ex.getMessage());
                }
                
                if(CSocket.isClosed())
                {
                    guiApplication.TraceEvenements(adresse + "#" + "Déconnexion" + "#" + nom);
                    break;
                }
                
                if(recu != null)
                {
                    StringTokenizer stk = new StringTokenizer(recu, "|");                    
                    int length = Integer.parseInt(stk.nextToken());
                    String token = stk.nextToken();
                    recu = token.substring(0, length);
                    
                    stk = new StringTokenizer(recu, ";");
                    switch(Integer.parseInt(stk.nextToken()))
                    {
                        case REQUEST_TICKET_CHECK:
                            String numBillet = stk.nextToken();
                            int nombreAccompagnants = Integer.parseInt(stk.nextToken());
                            RequeteACMAP requete = new RequeteACMAP(RequeteACMAP.REQUEST_TICKET_CHECK, numBillet, nombreAccompagnants);
                            requete.TraiterVerificationBillet(CSocket, guiApplication);
                        break;
                        
                        case REQUEST_ADD_LUGGAGE:
                            numBillet = stk.nextToken();
                            List<Bagage> liste = new ArrayList();
                            while(stk.hasMoreTokens())
                            {
                                
                                StringTokenizer stkBagage = new StringTokenizer(stk.nextToken(), "|");
                                while(stkBagage.hasMoreTokens())
                                {
                                    String tmp = stkBagage.nextToken();
                                    StringTokenizer stkParts = new StringTokenizer(tmp, "$");
                                    if(stkParts.hasMoreTokens())
                                    {
                                        Bagage bagage = new Bagage();
                                        bagage.setNumBagage(stkParts.nextToken());
                                        bagage.setValise(stkParts.nextToken().charAt(0));
                                        bagage.setPoids(Double.parseDouble(stkParts.nextToken()));
                                        liste.add(bagage);
                                    }
                                }
                            }
                            requete = new RequeteACMAP(RequeteACMAP.REQUEST_ADD_LUGGAGE, numBillet, liste);
                            requete.TraiterAjoutBagages(CSocket, guiApplication);
                        break;
                        
                        default:
                            guiApplication.TraceEvenements(adresse + "#Requête inconnue#" + nom);
                            try
                            {
                                CSocket.close();
                            }
                            catch(IOException ex)
                            {
                                System.err.println("Erreur lors de la fermeture de la socket suite à une requête de type inconnu : " + ex.getMessage());
                            }
                        break;
                    }
                }
                else
                {
                    try
                    {
                        CSocket.close();
                    }
                    catch(IOException ex)
                    {
                        System.err.println("Erreur lors de la fermeture de la socket : " + ex.getMessage());
                    }
                    break;
                }
            }
        }
    }
}
