package application_bagages;

import ProtocoleACMAP.RequeteACMAP;
import ProtocoleLUGAP.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import static application_bagages.RecupererReponse.*;

public class Application_Bagages extends javax.swing.JFrame implements RecupererReponse
{
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    private String login;
    private LinkedList listeVols;
    private LinkedList listeBagages;
    private DefaultListModel dlm;
    
    public Application_Bagages()
    {
        initComponents();
    }
    
    public Application_Bagages(Socket s, String l) 
    {
        initComponents();
        cliSock = s;
        login = l;
        LUsername.setText(LUsername.getText() + login);
        
        InitialiserFenetre();
        
        ThreadReception tr = new ThreadReception(30021, this);
        tr.start();
    }
    
    private void InitialiserFenetre()
    {
        setLocationRelativeTo(null);
        listeVols = RecupererListeVols();
        
        dlm = new DefaultListModel();
        JLVols.setModel(dlm);
        for(int i = 0 ; i < listeVols.size() ; i++)
        {
            dlm.addElement(listeVols.get(i).toString());
        }
    }
    
    public LinkedList RecupererListeVols()
    {
        RequeteVolsLUGAP req = new RequeteVolsLUGAP(RequeteLUGAP.RECUPERER_LISTE_VOLS);
        
        oos = null;
	try
	{
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); 
            oos.flush();
	}
	catch (IOException e)
	{ 
            System.err.println("Erreur réseau : [" + e.getMessage() + "]");
	}
	
	ReponseLUGAP rep = RecupererReponse(cliSock);
        
        return rep.getListe();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        JLVols = new javax.swing.JList<>();
        LTitre = new javax.swing.JLabel();
        LUsername = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Application bagages");

        JLVols.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                JLVolsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(JLVols);

        LTitre.setText("Liste des vols de ce jour : ");

        LUsername.setText("Connecté en tant que : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LTitre)
                            .addComponent(LUsername))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(LUsername)
                .addGap(18, 18, 18)
                .addComponent(LTitre)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JLVolsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_JLVolsMouseClicked
    {//GEN-HEADEREND:event_JLVolsMouseClicked
        if(evt.getClickCount() == 2)
        {
            String s = JLVols.getSelectedValue();
            VolsLUGAP vol = getSelectedVol(s);
            listeBagages = RecupererBagagesVol(vol);

            Afficher_Bagages afficherBagages = new Afficher_Bagages(this, true, listeBagages, cliSock);
            afficherBagages.setVisible(true);
            afficherBagages.setModal(true);
            
            String requete = String.valueOf(RequeteACMAP.FLIGHT_DONE);
            
            Socket socketReception = null;
            try
            {
                StringTokenizer stk = new StringTokenizer(cliSock.getRemoteSocketAddress().toString(), ":");
                String adresseThreadReceptionServeur = stk.nextToken().substring(1);
                socketReception = new Socket(adresseThreadReceptionServeur, 30020);
            }
            catch(IOException ex)
            {
                System.err.println("Erreur lors de l'instanciation de la socket du thread reception du serveur : " + ex.getMessage());
            }
            
            if(socketReception != null)
            {
                try
                {
                    DataOutputStream dos = new DataOutputStream(socketReception.getOutputStream());
                    dos.writeUTF(requete);
                    dos.flush();
                }
                catch (IOException e)
                { 
                    System.err.println("Erreur réseau : [" + e.getMessage() + "]");
                }
            }
        }
    }//GEN-LAST:event_JLVolsMouseClicked

    private VolsLUGAP getSelectedVol(String s)
    {
        int compteur = 1;
        StringTokenizer parser = new StringTokenizer(s," ");
        String n = "", d = "", c = "", h = "";
        int maxTokens = parser.countTokens();
        while (compteur <= maxTokens)
        {
            switch(compteur)
            {
                case 2:
                    n = parser.nextToken();
                break;
                
                case 3:
                    d = parser.nextToken();
                break;
                    
                case 5:
                    c = parser.nextToken();;
                break;
                
                case 6:
                    h = parser.nextToken();
                break;
                
                default:
                    parser.nextToken();
                break;
            }
            compteur++;
        }
        
        VolsLUGAP volRetour = new VolsLUGAP(n,d,c,h);
        
        return volRetour;
    }
    
    private LinkedList RecupererBagagesVol(VolsLUGAP vol)
    {
        RequeteBagagesLUGAP req = new RequeteBagagesLUGAP(RequeteLUGAP.RECUPERER_LISTE_BAGAGES, vol.getNumVol());
        
        oos = null;
	try
	{
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); 
            oos.flush();
	}
	catch (IOException e)
	{ 
            System.err.println("Erreur réseau : [" + e.getMessage() + "]");
	}
	
	ReponseLUGAP rep = RecupererReponse(cliSock);
        
        return rep.getListe();
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Application_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Application_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Application_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Application_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Application_Bagages().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> JLVols;
    private javax.swing.JLabel LTitre;
    private javax.swing.JLabel LUsername;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
