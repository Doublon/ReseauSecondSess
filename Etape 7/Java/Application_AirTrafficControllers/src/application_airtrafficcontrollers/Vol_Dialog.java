package application_airtrafficcontrollers;

import ACMAP_classes.Vol;
import ProtocoleACMAP.ReponseACMAP;
import ProtocoleACMAP.RequeteACMAP;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

public class Vol_Dialog extends javax.swing.JDialog
{
    private final Main_Frame parent;
    private final ButtonGroup groupe;
    private final Vol vol;
    private final String adresse;
    private final int port;
    
    public Vol_Dialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        
        groupe = new ButtonGroup();
        
        groupe.add(JRBBusy);
        groupe.add(JRBCheckIn_Off);
        groupe.add(JRBReady);
        groupe.add(JRBReadyToFly);
        groupe.add(JRBFlying);
        groupe.add(JRBTakingOff);
        
        this.parent = (Main_Frame) parent;
        vol = null;
        adresse = null;
        port = -1;
    }
    
    public Vol_Dialog(java.awt.Frame parent, boolean modal, String a, int p, Vol v)
    {
        super(parent, modal);
        initComponents();
        
        groupe = new ButtonGroup();
        
        groupe.add(JRBBusy);
        groupe.add(JRBCheckIn_Off);
        groupe.add(JRBReady);
        groupe.add(JRBReadyToFly);
        groupe.add(JRBFlying);
        groupe.add(JRBTakingOff);
        
        this.parent = (Main_Frame) parent;
        adresse = a;
        port = p;
        vol = v;
        
        JLVols.setText(vol.toString());
        this.setTitle(vol.toString());
    }

    public void setGroupButtons(boolean busy, boolean checkin_off, boolean ready, boolean readytofly, boolean takingoff, boolean flying)
    {
        JRBBusy.setEnabled(busy);
        JRBCheckIn_Off.setEnabled(checkin_off);
        JRBReady.setEnabled(ready);
        JRBReadyToFly.setEnabled(readytofly);
        JRBFlying.setEnabled(flying);
        JRBTakingOff.setEnabled(takingoff);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        JRBBusy = new javax.swing.JRadioButton();
        JLVols = new javax.swing.JLabel();
        JRBCheckIn_Off = new javax.swing.JRadioButton();
        JRBReady = new javax.swing.JRadioButton();
        JRBReadyToFly = new javax.swing.JRadioButton();
        JRBTakingOff = new javax.swing.JRadioButton();
        JRBFlying = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        JRBBusy.setSelected(true);
        JRBBusy.setText("BUSY");
        JRBBusy.setEnabled(false);

        JLVols.setText("Vol : ");

        JRBCheckIn_Off.setText("CHECKIN_OFF");
        JRBCheckIn_Off.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                JRBCheckIn_OffActionPerformed(evt);
            }
        });

        JRBReady.setText("READY");
        JRBReady.setEnabled(false);
        JRBReady.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                JRBReadyActionPerformed(evt);
            }
        });

        JRBReadyToFly.setText("READY TO FLY");
        JRBReadyToFly.setEnabled(false);
        JRBReadyToFly.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                JRBReadyToFlyActionPerformed(evt);
            }
        });

        JRBTakingOff.setText("TAKING OFF");
        JRBTakingOff.setEnabled(false);
        JRBTakingOff.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                JRBTakingOffActionPerformed(evt);
            }
        });

        JRBFlying.setText("FLYING");
        JRBFlying.setEnabled(false);
        JRBFlying.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                JRBFlyingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLVols)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(JRBBusy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JRBCheckIn_Off)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JRBReady)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JRBReadyToFly)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JRBTakingOff)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JRBFlying)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JLVols)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JRBBusy)
                    .addComponent(JRBCheckIn_Off)
                    .addComponent(JRBReady)
                    .addComponent(JRBReadyToFly)
                    .addComponent(JRBTakingOff)
                    .addComponent(JRBFlying))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JRBCheckIn_OffActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_JRBCheckIn_OffActionPerformed
    {//GEN-HEADEREND:event_JRBCheckIn_OffActionPerformed
        Check_Ticket checkTicket = new Check_Ticket(this, adresse, port);
        checkTicket.setAlwaysOnTop(true);
        checkTicket.setVisible(true);
        
        this.setVisible(false);
        
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());
        long minutes = (vol.getHeureDepart().getTime() - now.getTime())/1000/60;
        System.out.println(minutes);
        System.out.println("Temps restant : " + minutes + "minutes");
        
        /*if(minutes >= 0 && minutes <= 20)
        {
            setGroupButtons(false,false,true,false,false,false);
        
            parent.initSocket(adresse, port);
            
            RequeteACMAP requete = new RequeteACMAP(RequeteACMAP.REQUEST_CHECKIN_OFF);
            ReponseACMAP reponse = null;
            try
            {
                reponse = (ReponseACMAP) parent.sendReceive(requete); 
            }
            catch(ClassNotFoundException ex)
            {
                System.err.println("Erreur CNFE lors de la récupération de la réponse : " + ex.getMessage());
            }
            catch(IOException ex)
            {
                System.err.println("Erreur IO lors de la récupération de la réponse : " + ex.getMessage());
            }
            
            if(reponse != null && reponse.getCode() == ReponseACMAP.REQUEST_CHECKIN_OFF_OK)
            {
                JOptionPane.showMessageDialog(null,"Le check in est désormais off.","Information",JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Le check n'a pas été mis en off.","Erreur",JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            //JRBCheckIn_Off.setSelected(false);
            //JRBBusy.setSelected(true);
            //JOptionPane.showMessageDialog(null,"Il reste plus de 20 minutes avant le départ.","Information",JOptionPane.INFORMATION_MESSAGE);
            parent.initSocket(adresse, port);
            
            RequeteACMAP requete = new RequeteACMAP(RequeteACMAP.REQUEST_CHECKIN_OFF);
            ReponseACMAP reponse = null;
            try
            {
                reponse = (ReponseACMAP) parent.sendReceive(requete); 
            }
            catch(ClassNotFoundException ex)
            {
                System.err.println("Erreur CNFE lors de la récupération de la réponse : " + ex.getMessage());
            }
            catch(IOException ex)
            {
                System.err.println("Erreur IO lors de la récupération de la réponse : " + ex.getMessage());
            }
            
            if(reponse != null && reponse.getCode() == ReponseACMAP.REQUEST_CHECKIN_OFF_OK)
            {
                setGroupButtons(false,false,true,false,false,false);
                JOptionPane.showMessageDialog(null,"Le check in est désormais off.","Information",JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Le check n'a pas été mis en off.","Erreur",JOptionPane.ERROR_MESSAGE);
            }
        }*/
    }//GEN-LAST:event_JRBCheckIn_OffActionPerformed

    private void JRBReadyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_JRBReadyActionPerformed
    {//GEN-HEADEREND:event_JRBReadyActionPerformed
        parent.initSocket(adresse, port);
            
        RequeteACMAP requete = new RequeteACMAP(RequeteACMAP.REQUEST_READY);
        ReponseACMAP reponse = null;
        try
        {
            reponse = (ReponseACMAP) parent.sendReceive(requete); 
        }
        catch(ClassNotFoundException ex)
        {
            System.err.println("Erreur CNFE lors de la récupération de la réponse : " + ex.getMessage());
        }
        catch(IOException ex)
        {
            System.err.println("Erreur IO lors de la récupération de la réponse : " + ex.getMessage());
        }
        
        if(reponse != null && reponse.getCode() == ReponseACMAP.REQUEST_READY_OK)
        {
            JOptionPane.showMessageDialog(null,"Les bagages sont chargés.","Information",JOptionPane.INFORMATION_MESSAGE);
            setGroupButtons(false,false,false,true,false,false);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Les bagages ne sont pas chargés.","Erreur",JOptionPane.ERROR_MESSAGE);
            JRBCheckIn_Off.setSelected(true);
            JRBReady.setSelected(false);
        }
    }//GEN-LAST:event_JRBReadyActionPerformed

    private void JRBReadyToFlyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_JRBReadyToFlyActionPerformed
    {//GEN-HEADEREND:event_JRBReadyToFlyActionPerformed
        int numPiste = EnvoyerDemandePistes();
        if(numPiste != -1)
        {
            JOptionPane.showMessageDialog(null,"Piste choisie : " + numPiste,"Information",JOptionPane.INFORMATION_MESSAGE);
            setGroupButtons(false,false,false,false,true,false);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"La piste choisie est occupée.","Information",JOptionPane.INFORMATION_MESSAGE);
            JRBReady.setSelected(true);
            JRBReadyToFly.setSelected(false);
        }
    }//GEN-LAST:event_JRBReadyToFlyActionPerformed

    private void JRBTakingOffActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_JRBTakingOffActionPerformed
    {//GEN-HEADEREND:event_JRBTakingOffActionPerformed
        setGroupButtons(false,false,false,false,false,true);
        JOptionPane.showMessageDialog(null,"Donnez l'autorisation au pilote pour décoller !","Information",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_JRBTakingOffActionPerformed

    private void JRBFlyingActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_JRBFlyingActionPerformed
    {//GEN-HEADEREND:event_JRBFlyingActionPerformed
        setGroupButtons(false,false,false,false,false,false);
        JOptionPane.showMessageDialog(null,"L'avion est dans les airs.","Information",JOptionPane.INFORMATION_MESSAGE);
        parent.getListeVols().remove(vol);
    }//GEN-LAST:event_JRBFlyingActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        if(JRBFlying.isSelected() == false)
        {
            JOptionPane.showMessageDialog(null,"Vous devez terminer le traitement de ce vol.","Information",JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            this.dispose();
        }
    }//GEN-LAST:event_formWindowClosing

    private int EnvoyerDemandePistes()
    {
        int retour = -1;
        
        parent.initSocket(adresse, port);
            
        RequeteACMAP requete = new RequeteACMAP(RequeteACMAP.REQUEST_RUNWAYS);
        ReponseACMAP reponse = null;
        try
        {
            reponse = (ReponseACMAP) parent.sendReceive(requete);
        }
        catch(ClassNotFoundException ex)
        {
            System.err.println("Erreur CNFE lors de la requête des pistes : " + ex.getMessage());
        }
        catch(IOException ex)
        {
            System.err.println("Erreur IO lors de la requête des pistes : " + ex.getMessage());
        }
        
        if(reponse != null && reponse.getCode() == ReponseACMAP.REQUEST_RUNWAYS_OK)
        {
            if(reponse.getListePistes().size() > 0)
            {
                Runway_Dialog runway_dialog = new Runway_Dialog(parent, true, reponse.getListePistes());
                runway_dialog.setVisible(true);
                runway_dialog.setModal(true);
                
                if(runway_dialog.valider == true && runway_dialog.piste != -1)
                {
                    int piste = runway_dialog.piste;
                    RequeteACMAP requetePiste = new RequeteACMAP(RequeteACMAP.REQUEST_RUNWAY_CHOICE, piste);
                    ReponseACMAP reponsePiste = null;
                    try
                    {
                        reponsePiste = (ReponseACMAP) parent.sendReceive(requetePiste);
                    }
                    catch(ClassNotFoundException ex)
                    {
                        System.err.println("Erreur CNFE lors de la requête des pistes : " + ex.getMessage());
                    }
                    catch(IOException ex)
                    {
                        System.err.println("Erreur IO lors de la requête des pistes : " + ex.getMessage());
                    }

                    if(reponsePiste != null && reponsePiste.getCode() == ReponseACMAP.REQUEST_RUNWAY_CHOICE_OK)
                    {
                        retour = piste;
                    } 
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"L'opération a été annulée.","Information",JOptionPane.INFORMATION_MESSAGE);
                }
                runway_dialog.dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Il n'y a pas de pistes libres.","Information",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Une erreur est survenue lors de la récupération des pistes.","Erreur",JOptionPane.ERROR_MESSAGE);
        }
        
        return retour;
    }
    
    public void initSocket(String adress, int port)
    {
        parent.initSocket(adresse, port);
    }
    
    
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
            java.util.logging.Logger.getLogger(Vol_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Vol_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Vol_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Vol_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                Vol_Dialog dialog = new Vol_Dialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel JLVols;
    private javax.swing.JRadioButton JRBBusy;
    private javax.swing.JRadioButton JRBCheckIn_Off;
    private javax.swing.JRadioButton JRBFlying;
    private javax.swing.JRadioButton JRBReady;
    private javax.swing.JRadioButton JRBReadyToFly;
    private javax.swing.JRadioButton JRBTakingOff;
    // End of variables declaration//GEN-END:variables
}
