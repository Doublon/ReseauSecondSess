/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleLUGAP;

import java.net.Socket;
import java.util.LinkedList;
import requetepoolthreads.ConsoleServeur;

/**
 *
 * @author Tusse
 */
public class RequeteReady extends RequeteLUGAP
{
    private LinkedList liste;
    
    public RequeteReady()
    {
        liste = new LinkedList();
    }
    
    
    @Override
    public void TraiterRequete(Socket sock, ConsoleServeur cs)
    {
        System.out.println("Traitement de la requete");
    }
    
}
