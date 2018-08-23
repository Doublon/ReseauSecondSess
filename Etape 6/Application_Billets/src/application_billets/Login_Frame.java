package application_billets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import javax.swing.JOptionPane;
import tickmap.ReponseTICKMAP;
import tickmap.RequeteTICKMAP;
import static tickmap.UtileTICKMAP.CODE_PROVIDER;

public class Login_Frame extends javax.swing.JFrame
{
    private Main_Frame parent;
    private String login;
    private byte[] password;
    
    public Login_Frame()
    {
        initComponents();
    }
    
    public Login_Frame(Main_Frame parent)
    {
        initComponents();
        
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        Username_Label = new javax.swing.JLabel();
        Username_TextField = new javax.swing.JTextField();
        Password_Label = new javax.swing.JLabel();
        Login_Button = new javax.swing.JButton();
        Password_PasswordField = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");

        Username_Label.setText("Nom d'utilisateur : ");

        Username_TextField.setText("user1");

        Password_Label.setText("Mot de passe : ");

        Login_Button.setText("Connexion");
        Login_Button.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Login_ButtonActionPerformed(evt);
            }
        });

        Password_PasswordField.setText("password1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Login_Button)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(Password_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Username_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Username_TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(Password_PasswordField))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Username_Label)
                    .addComponent(Username_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Password_Label)
                    .addComponent(Password_PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(Login_Button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Login_ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_Login_ButtonActionPerformed
    {//GEN-HEADEREND:event_Login_ButtonActionPerformed
        if(Username_TextField.getText().isEmpty() || Password_PasswordField.getPassword().length == 0)
        {
            JOptionPane.showMessageDialog(null,"Les champs doivent être remplis.","Erreur de login",JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            login = Username_TextField.getText();
            password = new String(Password_PasswordField.getPassword()).getBytes();
            
            RequeteTICKMAP requeteLogin = CreerRequeteLogin();
            
            if(requeteLogin != null)
            {
                ReponseTICKMAP reponse = (ReponseTICKMAP) parent.sendReceive(requeteLogin, parent.getAdresseBillets(), parent.getPortBillets());
            
                if(reponse != null)
                {
                    if(reponse.getCode() == ReponseTICKMAP.LOGIN_OK)
                    {
                        parent.setAgent(reponse.getAgent());
                        parent.setVisible(true);
                        this.dispose();
                        parent.readKeyStore(parent.keyStorePath);
                        parent.sendCertificateToServeurBillets();
                    }
                    else
                    {
                        if(reponse.getCode() == ReponseTICKMAP.LOGIN_ERROR)
                        {
                            JOptionPane.showMessageDialog(null,"Login et/ou mot de passe incorrect(s).","Erreur de login",JOptionPane.ERROR_MESSAGE);
                        }
                        else
                        {
                            if(reponse.getCode() == ReponseTICKMAP.LOGIN_UNKNOWN)
                            {
                                JOptionPane.showMessageDialog(null,"Login inconnu.","Erreur de login",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
                else
                {
                    System.err.println("Une erreur est survenue lors de la récupération de la réponse serveur...");
                }
            }
            else
            {
                System.err.println("Une erreur est survenue lors de la création du digest salé.");
            }
        }
    }//GEN-LAST:event_Login_ButtonActionPerformed

    private RequeteTICKMAP CreerRequeteLogin()
    {
        long temps = 0;
        double alea = 0;
        
        MessageDigest md = null;
        try 
        {
            md = MessageDigest.getInstance("SHA-1", CODE_PROVIDER);
        } 
        catch (NoSuchAlgorithmException | NoSuchProviderException ex) 
        {
            System.err.println("Erreur MessageDigest : " + ex);
        }

        if(md != null)
        {
            md.update(password);
        
            temps = (new Date()).getTime();
            alea = Math.random();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();   
            DataOutputStream bdos = new DataOutputStream(baos);
            try 
            {
                bdos.writeLong(temps);
                bdos.writeDouble(alea);
            } 
            catch (IOException ex) 
            {
                System.err.println("Erreur IO lors de l'écriture du temps et du nombre aléatoire : " + ex);
            }
            md.update(baos.toByteArray());
            byte[] msgD = md.digest();

            RequeteTICKMAP requete = new RequeteTICKMAP(RequeteTICKMAP.LOGIN_TICKMAP, login, msgD, temps, alea);

            return requete;
        }
        else
        {
            return null;
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
            java.util.logging.Logger.getLogger(Login_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Login_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Login_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Login_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new Login_Frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Login_Button;
    private javax.swing.JLabel Password_Label;
    private javax.swing.JPasswordField Password_PasswordField;
    private javax.swing.JLabel Username_Label;
    private javax.swing.JTextField Username_TextField;
    // End of variables declaration//GEN-END:variables
}
