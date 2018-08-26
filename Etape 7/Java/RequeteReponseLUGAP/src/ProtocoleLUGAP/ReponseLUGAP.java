package ProtocoleLUGAP;

import java.io.*;
import java.util.*;

import requetepoolthreads.Reponse;

public class ReponseLUGAP implements Reponse, Serializable
{
    public static int LOGIN_OK = 201;
    public static int WRONG_LOGIN = 401;
    public static int WRONG_PASSWORD = 402;
    public static int RECUP_LISTEVOLS_OK = 202;
    public static int RECUP_LISTEVOLS_ERROR = 403;
    public static int RECUP_LISTEBAGAGES_OK = 203;
    public static int RECUP_LISTEBAGAGES_ERROR = 404;
    public static int MAJBD_OK = 204;
    public static int MAJBD_ERROR = 405;
    
    private int codeRetour;
    private String chargeUtile;
    private LinkedList liste;
    
    public ReponseLUGAP(int c, String chu)
    {
        codeRetour = c; 
        chargeUtile = chu;
    }
    
    public ReponseLUGAP(int c, LinkedList l)
    {
        codeRetour = c; 
        liste = l;
    }
    
    public ReponseLUGAP(int c, String chu, LinkedList l)
    {
        codeRetour = c; 
        chargeUtile = chu;
        liste = l;
    } 
    
    @Override
    public int getCode() 
    { 
        return codeRetour; 
    }
    
    public String getChargeUtile() 
    { 
        return chargeUtile; 
    }
    
    public void setChargeUtile(String chargeUtile) 
    { 
        this.chargeUtile = chargeUtile; 
    }
    
    public LinkedList getListe() 
    { 
        return liste; 
    }
    
    public void setListe(LinkedList liste) 
    { 
        this.liste = liste; 
    }
}
