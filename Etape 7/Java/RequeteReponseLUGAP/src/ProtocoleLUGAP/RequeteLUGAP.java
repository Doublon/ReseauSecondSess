package ProtocoleLUGAP;

import java.io.Serializable;
import java.net.Socket;
import requetepoolthreads.*;

abstract public class RequeteLUGAP implements Requete, Serializable
{
    protected static String codeProvider = "BC";
    
    public static int LOGIN = 1;
    public static int RECUPERER_LISTE_VOLS = 2;
    public static int RECUPERER_LISTE_BAGAGES = 3;
    public static int ENVOYER_CHANGEMENT_BAGAGES = 4;
    
    protected int type;
    
    @Override
    abstract public void TraiterRequete(Socket sock, ConsoleServeur cs);
}
