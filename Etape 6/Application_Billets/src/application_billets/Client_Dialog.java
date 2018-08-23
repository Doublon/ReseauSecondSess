package application_billets;

import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import tickmap_classes.Agent;
import tickmap_classes.Client;
import tickmap_classes.Vol;

public class Client_Dialog extends javax.swing.JDialog
{
    private final Main_Frame parent;
    
    private final Agent agent;
    private final Vol vol;
    
    private ButtonGroup dejaClient;
    private ButtonGroup groupeSexe;
    
    public boolean valider;
    
    public Client client;
    
    public Client_Dialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        
        this.parent = (Main_Frame) parent;
        
        agent = null;
        vol = null;
    }
    
    public Client_Dialog(java.awt.Frame parent, boolean modal, Agent a, Vol v)
    {
        super(parent, modal);
        initComponents();
        
        this.parent = (Main_Frame) parent;
        
        agent = a;
        vol = v;
        
        dejaClient = new ButtonGroup();
        dejaClient.add(JRBNouveauClient);
        dejaClient.add(JRBDejaClient);
        
        groupeSexe = new ButtonGroup();
        groupeSexe.add(JRBMasculin);
        groupeSexe.add(JRBFeminin);
        
        setEnabledFields(true,false,false,false,false,false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        JLNom = new javax.swing.JLabel();
        JLPrenom = new javax.swing.JLabel();
        JRBMasculin = new javax.swing.JRadioButton();
        JLSexe = new javax.swing.JLabel();
        JRBFeminin = new javax.swing.JRadioButton();
        JTFNom = new javax.swing.JTextField();
        JTFPrenom = new javax.swing.JTextField();
        BValider = new javax.swing.JButton();
        JRBNouveauClient = new javax.swing.JRadioButton();
        JRBDejaClient = new javax.swing.JRadioButton();
        JLNumClient = new javax.swing.JLabel();
        JTFNumClient = new javax.swing.JTextField();
        JLLogin = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JTFLogin = new javax.swing.JTextField();
        JPFPassword = new javax.swing.JPasswordField();
        BAnnuler = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ajout d'un voyageur");

        JLNom.setText("Nom : ");

        JLPrenom.setText("Prénom : ");

        JRBMasculin.setText("Masculin");

        JLSexe.setText("Sexe : ");

        JRBFeminin.setText("Féminin");

        BValider.setText("Valider");
        BValider.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BValiderActionPerformed(evt);
            }
        });

        JRBNouveauClient.setText("Nouveau client");
        JRBNouveauClient.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                JRBNouveauClientActionPerformed(evt);
            }
        });

        JRBDejaClient.setSelected(true);
        JRBDejaClient.setText("Déjà client");
        JRBDejaClient.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                JRBDejaClientActionPerformed(evt);
            }
        });

        JLNumClient.setText("Numéro de client : ");

        JLLogin.setText("Nom d'utilisateur : ");

        jLabel2.setText("Mot de passe : ");

        BAnnuler.setText("Annuler");
        BAnnuler.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BAnnulerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JLLogin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JLNumClient, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JLNom, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JLPrenom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTFPrenom)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(JRBMasculin)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(JRBFeminin)
                                        .addGap(38, 38, 38))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(JTFNom)
                                            .addComponent(JTFNumClient)
                                            .addComponent(JTFLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                                        .addComponent(JPFPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JLSexe)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(JRBNouveauClient)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JRBDejaClient))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(135, 135, 135)
                                .addComponent(BAnnuler)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(BValider, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JRBNouveauClient)
                    .addComponent(JRBDejaClient))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLNumClient)
                    .addComponent(JTFNumClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLNom)
                    .addComponent(JTFNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLPrenom)
                    .addComponent(JTFPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLLogin)
                    .addComponent(JTFLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JPFPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLSexe)
                    .addComponent(JRBMasculin)
                    .addComponent(JRBFeminin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BValider)
                    .addComponent(BAnnuler))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BValiderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BValiderActionPerformed
    {//GEN-HEADEREND:event_BValiderActionPerformed
        if(JRBDejaClient.isSelected() == true)
        {
            if(JTFNumClient.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(null,"Vous devez saisir le numéro du client.","Erreur",JOptionPane.ERROR_MESSAGE);
                client = null;
            }
            else
            {
                if(Integer.parseInt(JTFNumClient.getText()) < 1)
                {
                    JOptionPane.showMessageDialog(null,"Le numéro du client doit être positif.","Erreur",JOptionPane.ERROR_MESSAGE);
                    client = null;
                }
                else
                {
                    client = new Client(Integer.parseInt(JTFNumClient.getText()));
                    valider = true;
                }
            }
        }
        else
        {
            if(JTFNom.getText().isEmpty() == true || JTFPrenom.getText().isEmpty() == true || JTFLogin.getText().isEmpty() == true || 
                    JPFPassword.getPassword().length == 0 || (JRBMasculin.isSelected() == false && JRBFeminin.isSelected() == false))
            {
                JOptionPane.showMessageDialog(null,"Tous les champs doivent être remplis.","Erreur",JOptionPane.ERROR_MESSAGE);
                client = null;
            }
            else
            {
                client = new Client(JTFNom.getText(), JTFPrenom.getText(), JTFLogin.getText(), new String(JPFPassword.getPassword()));
                if(JRBMasculin.isSelected())
                {
                    client.setSexe(Client.MASCULIN);
                }
                else
                {
                    client.setSexe(Client.FEMININ);
                }
                valider = true;
            }
        }
        
        if(valider == true)
        {
            parent.setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_BValiderActionPerformed

    private void JRBDejaClientActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_JRBDejaClientActionPerformed
    {//GEN-HEADEREND:event_JRBDejaClientActionPerformed
        setEnabledFields(true,false,false,false,false,false);
    }//GEN-LAST:event_JRBDejaClientActionPerformed

    private void JRBNouveauClientActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_JRBNouveauClientActionPerformed
    {//GEN-HEADEREND:event_JRBNouveauClientActionPerformed
        setEnabledFields(false,true,true,true,true,true);
    }//GEN-LAST:event_JRBNouveauClientActionPerformed

    private void BAnnulerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BAnnulerActionPerformed
    {//GEN-HEADEREND:event_BAnnulerActionPerformed
        valider = false;
        
        parent.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_BAnnulerActionPerformed

    private void setEnabledFields(boolean numClient, boolean nom, boolean prenom, boolean login, boolean password, boolean sexe)
    {
        emptyFields();
        JTFNumClient.setEnabled(numClient);
        JTFNom.setEnabled(nom);
        JTFPrenom.setEnabled(prenom);
        JTFLogin.setEnabled(login);
        JPFPassword.setEnabled(password);
        
        Enumeration<AbstractButton> enumeration = groupeSexe.getElements();
        while (enumeration.hasMoreElements()) 
        {
            enumeration.nextElement().setEnabled(sexe);
        }
    }
    
    private void emptyFields()
    {
        JTFNumClient.setText("");
        JTFNom.setText("");
        JTFPrenom.setText("");
        JTFLogin.setText("");
        JPFPassword.setText("");
        
        Enumeration<AbstractButton> enumeration = groupeSexe.getElements();
        while (enumeration.hasMoreElements()) 
        {
            enumeration.nextElement().setSelected(false);
        }
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
            java.util.logging.Logger.getLogger(Client_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Client_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Client_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Client_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                Client_Dialog dialog = new Client_Dialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton BAnnuler;
    private javax.swing.JButton BValider;
    private javax.swing.JLabel JLLogin;
    private javax.swing.JLabel JLNom;
    private javax.swing.JLabel JLNumClient;
    private javax.swing.JLabel JLPrenom;
    private javax.swing.JLabel JLSexe;
    private javax.swing.JPasswordField JPFPassword;
    private javax.swing.JRadioButton JRBDejaClient;
    private javax.swing.JRadioButton JRBFeminin;
    private javax.swing.JRadioButton JRBMasculin;
    private javax.swing.JRadioButton JRBNouveauClient;
    private javax.swing.JTextField JTFLogin;
    private javax.swing.JTextField JTFNom;
    private javax.swing.JTextField JTFNumClient;
    private javax.swing.JTextField JTFPrenom;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
