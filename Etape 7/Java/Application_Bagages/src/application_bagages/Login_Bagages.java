/*
RORIVE Olivier | VERWIMP Jim
Réseau : partie 3
Date de dernière mise à jour : le 25/10/2017 à 19h24
*/
package application_bagages;

import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;
import javax.swing.*;

import ProtocoleLUGAP.*;
import static application_bagages.RecupererReponse.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Login_Bagages extends javax.swing.JFrame implements RecupererReponse
{
    private Socket cliSock = null;
    private String adresse = null;
    private int port = 0;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private static final String codeProvider = "BC";
    
    public Login_Bagages() 
    {
        initComponents();
        InitialiserFenetre();
    }
    
    private void InitialiserFenetre()
    {
        this.setLocationRelativeTo(null);
        LireFichierProperties("config.properties");
        InitSocket();
    }
    
    private void LireFichierProperties(String filename)
    {
        Properties properties = null;
        InputStream input = null;
        
        properties = new Properties();
        try 
        {
            input = new FileInputStream(filename);
            properties.load(input);
        } 
        catch (IOException ex) 
        {
           System.err.println("Exception lors de la lecture du fichier " + filename + " : " + ex);
           System.exit(1);
        } 
        
        if(properties.getProperty("ADRESSE") != null)
            adresse = properties.getProperty("ADRESSE");
        if(properties.getProperty("PORT") != null)
            port = Integer.parseInt(properties.getProperty("PORT"));
    }
    
    private void InitSocket()
    {
        try
        {
            cliSock = new Socket(adresse, port);
        }
        catch (IOException ex)
        { 
            System.err.println("Erreur lors de la connexion à l'adresse : " + adresse + ":" + port); 
            System.exit(1);
        }
    }
    
    private LoginLUGAP CreerRequeteLogin()
    {
        Security.addProvider(new BouncyCastleProvider());
        long temps = 0;
        double alea = 0;
        
        MessageDigest md = null;
        try 
        {
            md = MessageDigest.getInstance("SHA-1", "BC");
        } 
        catch (NoSuchAlgorithmException | NoSuchProviderException ex) 
        {
            System.err.println("Erreur MessageDigest : " + ex);
        }
        
        if(PFPassword.getText() != null)
            md.update(PFPassword.getText().getBytes());
        
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
            System.err.println("Erreur IO : " + ex);
        }
        md.update(baos.toByteArray());
        byte[] msgD = md.digest();
        
        String chargeUtile = TFLogin.getText() + "#" + temps + "#" + alea + "#" + msgD.length;
        
        LoginLUGAP req = new LoginLUGAP(RequeteLUGAP.LOGIN, chargeUtile, msgD);
        
        return req;
    }
    
    private void EnvoyerRequete(LoginLUGAP req)
    {
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
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        LLogin = new javax.swing.JLabel();
        LPassword = new javax.swing.JLabel();
        TFLogin = new javax.swing.JTextField();
        BValider = new javax.swing.JButton();
        PFPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setLocation(new java.awt.Point(0, 0));

        LLogin.setText("Login :");

        LPassword.setText("Password :");

        TFLogin.setText("user1");

        BValider.setText("Valider");
        BValider.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BValiderActionPerformed(evt);
            }
        });

        PFPassword.setText("password1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PFPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(TFLogin)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BValider)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LLogin)
                    .addComponent(TFLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LPassword)
                    .addComponent(PFPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(BValider)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BValiderActionPerformed
        if(TFLogin.getText() != null && PFPassword.getText() != null)
        {  
            LoginLUGAP req = CreerRequeteLogin();

            EnvoyerRequete(req);
            
            ReponseLUGAP rep = RecupererReponse(cliSock);
            
            if(rep != null)
            {
                if("LOGIN_OK".equals(rep.getChargeUtile()))
                {
                    Application_Bagages appliBagages = new Application_Bagages(cliSock,TFLogin.getText());
                    appliBagages.setVisible(true);
                    this.dispose();
                }
                else
                {
                    if("WRONG_LOGIN".equals(rep.getChargeUtile()))
                    {
                        JOptionPane.showMessageDialog(null,"Login et/ou mot de passe incorrect.","Erreur de login",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null,"Mot de passe incorrect.","Erreur de login",JOptionPane.ERROR_MESSAGE);    
                    }
                    BValider.setSelected(false);
                }   
            }
        }
    }//GEN-LAST:event_BValiderActionPerformed
  
    public static void main(String args[]) 
    {
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
            java.util.logging.Logger.getLogger(Login_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login_Bagages().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BValider;
    private javax.swing.JLabel LLogin;
    private javax.swing.JLabel LPassword;
    private javax.swing.JPasswordField PFPassword;
    private javax.swing.JTextField TFLogin;
    // End of variables declaration//GEN-END:variables
}
