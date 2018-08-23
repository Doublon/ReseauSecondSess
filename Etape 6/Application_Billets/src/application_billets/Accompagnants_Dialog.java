package application_billets;

import java.net.Socket;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import tickmap.ReponseTICKMAP;
import tickmap.RequeteTICKMAP;
import tickmap.UtileTICKMAP;
import static tickmap.UtileTICKMAP.CODE_PROVIDER;
import static tickmap.UtileTICKMAP.symmetricAlg;
import tickmap_classes.Agent;
import tickmap_classes.Client;
import tickmap_classes.Reservation;
import tickmap_classes.Vol;

public class Accompagnants_Dialog extends javax.swing.JDialog
{
    private final Main_Frame parent;
    
    private Agent agent;
    
    private Socket socket;
    
    private SecretKey cleChiffrement;
    
    public Reservation reservation;
    private DefaultListModel dlm;
    
    public boolean valider;
    
    public Accompagnants_Dialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        
        this.parent = (Main_Frame) parent;
    }
    
    public Accompagnants_Dialog(java.awt.Frame parent, boolean modal, Agent a, Vol v, Client c, Socket sock, SecretKey cle)
    {
        super(parent, modal);
        initComponents();
        
        this.parent = (Main_Frame) parent;
        
        agent = a;
        
        reservation = new Reservation(v, c, new ArrayList());
        
        socket = sock;
        
        cleChiffrement = cle;
        
        JLDescription.setText(JLDescription.getText() + 
                reservation.getClient().getNumClient() + " : " + reservation.getClient().getNom() + " " + reservation.getClient().getPrenom());
        
        dlm = new DefaultListModel();
        JLAccompagnants.setModel(dlm);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        JLAccompagnants = new javax.swing.JList<>();
        BAjouter = new javax.swing.JButton();
        BSupprimer = new javax.swing.JButton();
        BTerminer = new javax.swing.JButton();
        BAnnuler = new javax.swing.JButton();
        JLDescription = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setViewportView(JLAccompagnants);

        BAjouter.setText("Ajouter");
        BAjouter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BAjouterActionPerformed(evt);
            }
        });

        BSupprimer.setText("Supprimer");
        BSupprimer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BSupprimerActionPerformed(evt);
            }
        });

        BTerminer.setText("Terminer");
        BTerminer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BTerminerActionPerformed(evt);
            }
        });

        BAnnuler.setText("Annuler");
        BAnnuler.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BAnnulerActionPerformed(evt);
            }
        });

        JLDescription.setText("Liste d'accompagnants pour le client ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLDescription))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BSupprimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BAjouter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BAnnuler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BTerminer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JLDescription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BAjouter)
                        .addGap(18, 18, 18)
                        .addComponent(BSupprimer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addComponent(BAnnuler)
                        .addGap(18, 18, 18)
                        .addComponent(BTerminer))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTerminerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BTerminerActionPerformed
    {//GEN-HEADEREND:event_BTerminerActionPerformed
        valider = true;
        
        this.setVisible(false);
        parent.setVisible(true);
    }//GEN-LAST:event_BTerminerActionPerformed

    private void BSupprimerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BSupprimerActionPerformed
    {//GEN-HEADEREND:event_BSupprimerActionPerformed
        if(JLAccompagnants.getSelectedIndex() == -1)
        {
            JOptionPane.showMessageDialog(null,"Vous devez sélectionner un accompagnant.","Erreur",JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            Client c = reservation.getListeAccompagnants().remove(JLAccompagnants.getSelectedIndex());
            dlm.removeElement(c);
            if(c != null)
            {
                JOptionPane.showMessageDialog(null,"L'accompagnant " + c.getNom() + " " + c.getPrenom() + " a bien été supprimé.","Information",JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                System.err.println("Une erreur est survenue lors de la suppression de l'accompagnant " + c.getNom() + " " + c.getPrenom());
            }
        }
    }//GEN-LAST:event_BSupprimerActionPerformed

    private void BAjouterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BAjouterActionPerformed
    {//GEN-HEADEREND:event_BAjouterActionPerformed
        if(reservation.getVol().getPlacesRestantes() <= reservation.getListeAccompagnants().size()+1)
        {
            JOptionPane.showMessageDialog(null,"Ce vol est complet. Aucun passager supplémentaire ne peut être ajouté.","Erreur",JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            Client_Dialog client_dialog = new Client_Dialog(parent, true, agent, reservation.getVol());
            client_dialog.setVisible(true);
            client_dialog.setModal(true);
            if(client_dialog.valider == true)
            {
                RequeteTICKMAP requete = new RequeteTICKMAP(RequeteTICKMAP.ADD_TRAVELER, client_dialog.client);
                Cipher cipherE = UtileTICKMAP.initCipher(symmetricAlg, CODE_PROVIDER, Cipher.ENCRYPT_MODE, cleChiffrement);
                SealedObject sealedObject = UtileTICKMAP.createSealedObject(requete, cipherE);
                ReponseTICKMAP reponse = (ReponseTICKMAP) parent.sendReceive(sealedObject, parent.getAdresseBillets(), parent.getPortBillets());

                if(reponse != null && reponse.getCode() == ReponseTICKMAP.REQUEST_ADD_TRAVELER_OK)
                {
                    reservation.getListeAccompagnants().add(reponse.getClient());
                    dlm.addElement(reponse.getClient());
                    JOptionPane.showMessageDialog(null,"L'accompagnant " + reponse.getClient().getNom() + " " 
                            + reponse.getClient().getPrenom() + " a bien été ajouté." ,"Information",JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null,"L'opération a été annulée.","Information",JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_BAjouterActionPerformed

    private void BAnnulerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BAnnulerActionPerformed
    {//GEN-HEADEREND:event_BAnnulerActionPerformed
        valider = false;
        
        this.setVisible(false);
        parent.setVisible(true);
    }//GEN-LAST:event_BAnnulerActionPerformed

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
            java.util.logging.Logger.getLogger(Accompagnants_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Accompagnants_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Accompagnants_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Accompagnants_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                Accompagnants_Dialog dialog = new Accompagnants_Dialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton BAjouter;
    private javax.swing.JButton BAnnuler;
    private javax.swing.JButton BSupprimer;
    private javax.swing.JButton BTerminer;
    private javax.swing.JList<String> JLAccompagnants;
    private javax.swing.JLabel JLDescription;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
