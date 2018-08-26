package ProtocoleACMAP;

import ACMAP_classes.Vol;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import requetepoolthreads.Reponse;

public class ReponseACMAP implements Reponse, Serializable
{
    public final static int REQUEST_TICKET_OK = 200;
    public final static int REQUEST_TICKET_ERROR = 400;
    
    public final static int REQUEST_ADD_LUGGAGE_OK = 201;
    public final static int REQUEST_ADD_LUGGAGE_ERROR = 401;
    
    public final static int REQUEST_FLIGHTS_OK = 202;
    public final static int REQUEST_FLIGHTS_ERROR = 402;
    
    public final static int REQUEST_CHECKIN_OFF_OK = 203;
    public final static int REQUEST_CHECKIN_OFF_ERROR = 403;
    
    public final static int REQUEST_READY_OK = 204;
    public final static int REQUEST_READY_ERROR = 404;
    
    public final static int REQUEST_RUNWAYS_OK = 205;
    public final static int REQUEST_RUNWAYS_ERROR = 405;
    
    public final static int REQUEST_RUNWAY_CHOICE_OK = 206;
    public final static int REQUEST_RUNWAY_CHOICE_ERROR = 406;
    
    private final int codeRetour;
    private final List<Vol> listeVols;
    private final List<Integer> listePistes;
    
    public ReponseACMAP(int cR)
    {
        codeRetour = cR;
        listeVols = null;
        listePistes = null;
    }
    
    public ReponseACMAP(int cR, List<Vol> lv)
    {
        codeRetour = cR;
        listeVols = lv;
        listePistes = null;
    }
    
    public ReponseACMAP(int cR, ArrayList lP)
    {
        codeRetour = cR;
        listeVols = null;
        listePistes = lP;
    }

    @Override
    public int getCode()
    {
        return codeRetour;
    }
    
    public List<Vol> getListeVols()
    {
        return listeVols;
    }
    
    public List<Integer> getListePistes()
    {
        return listePistes;
    }
}
