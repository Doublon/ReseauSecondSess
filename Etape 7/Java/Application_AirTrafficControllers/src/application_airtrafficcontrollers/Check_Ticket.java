/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application_airtrafficcontrollers;

import ProtocoleACMAP.ReponseACMAP;
import ProtocoleACMAP.RequeteACMAP;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Tusse
 */
public class Check_Ticket extends javax.swing.JFrame
{
    static private Vol_Dialog parent;
    static private String adresse;
    static private int port;
    
    private Socket socket = null;
    
    public Check_Ticket(javax.swing.JDialog parent, String adresse, int port)
    {
        this.parent = (Vol_Dialog) parent;
        this.adresse = adresse;
        this.port = port;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabelTitre = new javax.swing.JLabel();
        jTicketTextField = new javax.swing.JTextField();
        jAnnulerButton = new javax.swing.JButton();
        jOkButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelTitre.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabelTitre.setText("Veuillez saison votre numéro de ticket : ");

        jAnnulerButton.setText("Annuler");
        jAnnulerButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jAnnulerButtonActionPerformed(evt);
            }
        });

        jOkButton.setText("Ok");
        jOkButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jOkButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jTicketTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelTitre))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jOkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jAnnulerButton)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitre, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTicketTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jAnnulerButton)
                    .addComponent(jOkButton))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jAnnulerButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jAnnulerButtonActionPerformed
    {//GEN-HEADEREND:event_jAnnulerButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_jAnnulerButtonActionPerformed

    private void jOkButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jOkButtonActionPerformed
    {//GEN-HEADEREND:event_jOkButtonActionPerformed
        String ticket = jTicketTextField.getText();
        if(ticket == null)
            JOptionPane.showMessageDialog(null,"Vous devez saisir un numéro de ticket", "Warning", JOptionPane.WARNING_MESSAGE);
        else
        {
            parent.initSocket(adresse, port);
            System.out.println("ticket : " + ticket);
            RequeteACMAP requete = new RequeteACMAP(RequeteACMAP.REQUEST_CHECKIN_OFF);
            requete.setChargeUtile(ticket);
            System.out.println("ChargeUtile : " + requete.chargeUtile);
            String reponse = null;
            
            try
            {
                reponse = sendReceive(requete);
                if(reponse.equals("TICKET_CHECKED_OK"))
                {
                    this.setAlwaysOnTop(false);
                    this.setVisible(false);
                    this.dispose();
                    parent.setGroupButtons(false,true,true,false,false,false);
                    parent.setVisible(true);
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Numéro de ticket invalide", "Warning", JOptionPane.WARNING_MESSAGE);
                    this.setAlwaysOnTop(false);
                    this.setVisible(false);
                    this.dispose();
                    parent.setGroupButtons(true,true,false,false,false,false);
                    parent.setVisible(true);
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(Check_Ticket.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(Check_Ticket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
    }//GEN-LAST:event_jOkButtonActionPerformed

    public String sendReceive(RequeteACMAP requete) throws IOException, ClassNotFoundException
    {
        Socket sockCpp = null;
        sockCpp = initSocket("127.0.0.1", 50000);
        
        DataOutputStream dos = null;
        dos = new DataOutputStream(new BufferedOutputStream(sockCpp.getOutputStream()));
        String message = "CHECK_TICKET;" + requete.chargeUtile + "#";
        dos.write(message.getBytes());
        dos.flush();
        
        DataInputStream dis = null;
        dis = new DataInputStream(new BufferedInputStream(sockCpp.getInputStream()));
       
        byte b = 0;
        StringBuffer buffer = new StringBuffer();
        while(b != (byte)'\n')
        {
            b = dis.readByte();
            if(b != (byte)'\n')
                buffer.append((char)b);   
        }
            
        String reponse = buffer.toString().trim();
        System.out.println("Reponse : " + reponse);
        
        return reponse;        
    }
    
    public Socket initSocket(String adresse, int port)
    {
        Socket socket = null;
        try
        {
            socket = new Socket(adresse, port);
        }
        catch (UnknownHostException e)
        { 
            System.err.println("Erreur ! Host non trouvé [" + e + "]"); 
        }
        catch (IOException e)
        { 
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]"); 
        }
        
        return socket;
    }
    
    /**
     * @param args the command line arguments
     */
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
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(Check_Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Check_Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Check_Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Check_Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new Check_Ticket(parent, adresse, port).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAnnulerButton;
    private javax.swing.JLabel jLabelTitre;
    private javax.swing.JButton jOkButton;
    private javax.swing.JTextField jTicketTextField;
    // End of variables declaration//GEN-END:variables

}
