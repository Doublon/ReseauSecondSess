package application_bagages;

import ProtocoleLUGAP.*;
import static application_bagages.RecupererReponse.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class Afficher_Bagages extends javax.swing.JDialog implements TableModelListener,RecupererReponse
{
    private Socket cliSock = null;
    private DefaultTableModel dtm = null;;
    private LinkedList listeBagages = null;
    private Hashtable nomColonnes = null;
    
    public Afficher_Bagages(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
    }
    
    public Afficher_Bagages(java.awt.Frame parent, boolean modal, LinkedList liste, Socket cS)
    {
        super(parent, modal);
        initComponents();
        
        cliSock = cS;
        listeBagages = liste;
        
        InitialiserFenetre();
    }
    
    private void InitialiserFenetre()
    {
        setLocationRelativeTo(null);
        
        dtm = (DefaultTableModel) JTableBagages.getModel();
        
        nomColonnes = new Hashtable();
        nomColonnes.put("Identifiant","numBagage");
        nomColonnes.put("Poids","poids");
        nomColonnes.put("Valise","valise");
        nomColonnes.put("Réceptionné (O/N)","receptionne");
        nomColonnes.put("Chargé en soute (O/N/R)","chargeSoute");
        nomColonnes.put("Vérifié par la douane (O/N)","verifieDouane");
        nomColonnes.put("Remarques","remarques");
        
        RemplirTable(listeBagages);
        dtm.addTableModelListener(this);
    }
    
    public void EnvoyerMAJBD(String id, String colonne, String changement)
    {
        RequeteColumnChangedLUGAP req = new RequeteColumnChangedLUGAP(RequeteLUGAP.ENVOYER_CHANGEMENT_BAGAGES, id, colonne, changement);
        
        ObjectOutputStream oos = null;
	try
	{
		oos = new ObjectOutputStream(cliSock.getOutputStream());
		oos.writeObject(req); 
		oos.flush();
	}
	catch (IOException e)
	{ 
		System.err.println("Erreur réseau : " + e.getMessage());
	}
	
	ReponseLUGAP rep = RecupererReponse(cliSock);
    }
    
    private void RemplirTable(LinkedList liste)
    {
        int compteurLignes = 0, nombreColonnes = nomColonnes.size(), compteurColonnes = 0;
        
        dtm.setRowCount(liste.size());
        while(compteurLignes < liste.size())
        {
            BagagesLUGAP bagage = (BagagesLUGAP) liste.get(compteurLignes);
            compteurColonnes = 0;
            while(compteurColonnes < nombreColonnes)
            {
                InsererValeur(bagage, compteurLignes, compteurColonnes);
                compteurColonnes++;
            }
            compteurLignes++;
        }
    }
    
    private void InsererValeur(BagagesLUGAP bagage, int ligne, int colonne)
    {
        switch(colonne)
        {
            case 0:
                dtm.setValueAt(bagage.getIdentifiant(), ligne, colonne);
            break;

            case 1:
                dtm.setValueAt(bagage.getPoids(), ligne, colonne);
            break;

            case 2:
                dtm.setValueAt(bagage.getType(), ligne, colonne);
            break;

            case 3:
                dtm.setValueAt(bagage.getReceptionne(), ligne, colonne);
            break;

            case 4:
                dtm.setValueAt(bagage.getChargeEnSoute(), ligne, colonne);
            break;

            case 5:
                dtm.setValueAt(bagage.getVerifieParDouane(), ligne, colonne);
            break;

            case 6:
                dtm.setValueAt(bagage.getRemarques(), ligne, colonne);
            break;
        }
    }
    
    @Override
    public void tableChanged(TableModelEvent e)
    {
        int row = JTableBagages.getSelectedRow();
        int column = JTableBagages.getSelectedColumn();

        String colonne = JTableBagages.getColumnName(column);
        String changement = JTableBagages.getValueAt(row, column).toString();
        String id = JTableBagages.getValueAt(row, 0).toString();
        
        colonne = parcoursTableau(nomColonnes, colonne); 

        if(colonne.equals("receptionne") || colonne.equals("chargeSoute") || colonne.equals("verifieDouane") ||
            colonne.equals("remarques"))
        {
            EnvoyerMAJBD(id, colonne, changement);                   
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Cette colonne ne peut pas être modifiée", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public String parcoursTableau(Hashtable<String,String> tableau, String colonne)
    {
        Enumeration e = tableau.keys();
        String retour = null;
        
        while(e.hasMoreElements())
        {
            String key = (String) e.nextElement();
            if(key.equals(colonne))
            {
                retour = tableau.get(key);
                break;
            }
        }
        
        return retour;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        SPJTBagages = new javax.swing.JScrollPane();
        JTableBagages = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Liste des bagages");
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        JTableBagages.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Identifiant", "Poids", "Valise", "Réceptionné (O/N)", "Chargé en soute (O/N/R)", "Vérifié par la douane (O/N)", "Remarques"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        SPJTBagages.setViewportView(JTableBagages);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SPJTBagages, javax.swing.GroupLayout.DEFAULT_SIZE, 930, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SPJTBagages, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        int erreur = 0;

        for(int i = 0 ; i < dtm.getRowCount() ; i++)
        {
            if(!"O".equals(dtm.getValueAt(i, 4)) && !("R".equals(dtm.getValueAt(i, 4))))
            {
                erreur = 1;
                break;
            }
        }

        if(erreur == 0)
        {
            Login_Bagages loginBagages = new Login_Bagages();
            loginBagages.setVisible(true);
            try
            {
                cliSock.close();
            } 
            catch (IOException ex)
            {
                System.err.println("Erreur lors de la fermeture de la socket : " + ex.getMessage());
            }
            this.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Tous les bagages doivent avoir été chargés ou refusés !", "Erreur", JOptionPane.ERROR_MESSAGE);            
        }
    }//GEN-LAST:event_formWindowClosing
  
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(Afficher_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Afficher_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Afficher_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Afficher_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                Afficher_Bagages dialog = new Afficher_Bagages(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable JTableBagages;
    private javax.swing.JScrollPane SPJTBagages;
    // End of variables declaration//GEN-END:variables
}
