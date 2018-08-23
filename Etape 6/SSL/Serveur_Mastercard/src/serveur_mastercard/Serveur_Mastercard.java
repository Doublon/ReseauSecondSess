package serveur_mastercard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.table.DefaultTableModel;
import requetepoolthreads.ConsoleServeur;
import serveurpoolthreads.ListeTaches;
import serveurpoolthreads.ThreadServeur;

public class Serveur_Mastercard extends javax.swing.JFrame implements ConsoleServeur
{
    private final String propertiesFile = "config.properties";
    private final String SSLKeyStorePath = "C:\\makecert\\server_mastercard_keystore";
    private KeyStore keyStore;
    
    private DefaultTableModel dtm = null;
    private int PORT_MASTERCARD;
    private int nombreThreads;
    private String ADRESSE;
    
    private SSLServerSocket SslSSocket;
    private ServerSocket sSocket;
    
    public Serveur_Mastercard()
    {
        initComponents();
        dtm = (DefaultTableModel)TableauEvenements.getModel();
        InitialiserFenetre();
    }
    
    private void InitialiserFenetre()
    {
        LireFichierProperties(propertiesFile);
        try
        {
            ADRESSE = InetAddress.getLocalHost().getHostAddress();
            TraceEvenements(ADRESSE + ":" + PORT_MASTERCARD + "#Acq. port MASTERCARD" + "#Main");
        } 
        catch (UnknownHostException ex)
        {
            System.err.println("Erreur lors de la récupération de l'adresse : " + ex.getMessage());
        }
        readSSLKeyStore(SSLKeyStorePath);
        
        // Version SSL
        
        createSSLServerSocket();
        ThreadServeur ts = new ThreadServeur(SslSSocket, new ListeTaches(), nombreThreads, this);
        ts.start();
        
        
        // Version non sécurisée
        /*try
        {
            sSocket = new ServerSocket(PORT_MASTERCARD);
        } 
        catch (IOException ex)
        {
            System.err.println("Erreur lors de la création de la socket : " + ex.getMessage());
        }
        ThreadServeur ts = new ThreadServeur(sSocket, new ListeTaches(), nombreThreads, this);
        ts.start();*/
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
            if(properties.getProperty("PORT_MASTERCARD") != null)
            {
                PORT_MASTERCARD = Integer.parseInt(properties.getProperty("PORT_MASTERCARD"));
            }
            else
            {
                System.err.println("Le numéro de port est introuvable dans le fichier " + filename);
                System.exit(1);
            }
            if(properties.getProperty("NOMBRETHREADS") != null)
            {
                nombreThreads = Integer.parseInt(properties.getProperty("NOMBRETHREADS"));
            }
            else
            {
                System.err.println("Le nombre de threads à utiliser dans le pool est introuvable dans le fichier " + filename);
                System.exit(1);
            }
        } 
        catch (FileNotFoundException fnfeEx) 
        {
           System.err.println("Exception lors de la lecture du fichier " + filename + " : " + fnfeEx);
           
            try
            {
                FileOutputStream fos = new FileOutputStream(filename);
                
                properties.put("PORT_MASTERCARD", "30018");
                PORT_MASTERCARD = 30018;
                properties.put("NOMBRETHREADS", "5");
                nombreThreads = 5;
                
                try 
                {
                    properties.store(fos, null);
                }
                catch (IOException ioEx) 
                {
                    System.err.println("Erreur IO lors de l'écriture du fichier "  + filename + " : " + ioEx);
                    System.exit(0);
                }
            } 
            catch (FileNotFoundException fnfeEx2)
            {
                System.err.println("Erreur FileNotFound lors de l'écriture du fichier " + filename + " : " + fnfeEx2);
                System.exit(0);
            }
        } 
        catch (IOException ex) 
        {
            System.err.println("Erreur IO lors de la lecture du fichier " + filename + " : " + ex);
            System.exit(0);
        }
    }
    
    private void readSSLKeyStore(String keyStorePath)
    {
        try 
        {
            keyStore = KeyStore.getInstance("JKS");
            char[] PASSWD_KEYSTORE = "mastercard123".toCharArray();
            FileInputStream ServerFK = new FileInputStream (keyStorePath);
            keyStore.load(ServerFK, PASSWD_KEYSTORE);
            try
            {
                PrivateKey test = (PrivateKey) keyStore.getKey("mastercardSer", "mastercard123".toCharArray());
                System.out.println(test);
            } catch (UnrecoverableKeyException ex)
            {
                Logger.getLogger(Serveur_Mastercard.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) 
        {
            System.err.println("Erreur lors de la lecture du KeyStore SSL à l'emplacement : " + keyStorePath + " : " + ex.getMessage());
            System.exit(1);
        } 
    }
    
    private void createSSLServerSocket()
    {       
        try
        {
            SSLContext SslC = SSLContext.getInstance("SSLv3");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            char[] PASSWD_KEY = "mastercard123".toCharArray();
            kmf.init(keyStore, PASSWD_KEY);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(keyStore);
            SslC.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            SSLServerSocketFactory SslSFac = SslC.getServerSocketFactory();
            
            SslSSocket = (SSLServerSocket) SslSFac.createServerSocket(PORT_MASTERCARD);
        } 
        catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException | IOException ex)
        {
            System.err.println("Erreur lors de la création de la socket serveur SSL : " + ex.getMessage());
        }
    }
    
    @Override
    public void TraceEvenements(String commentaire)
    {
        Vector ligne = new Vector();
        StringTokenizer parser = new StringTokenizer(commentaire,"#");
        
        while (parser.hasMoreTokens())
            ligne.add(parser.nextToken());

        dtm.insertRow(dtm.getRowCount(),ligne);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        LClients = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableauEvenements = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Serveur Mastercard");

        LClients.setText("CLIENTS");

        TableauEvenements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Origine", "Requête", "Thread"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TableauEvenements);
        if (TableauEvenements.getColumnModel().getColumnCount() > 0)
        {
            TableauEvenements.getColumnModel().getColumn(1).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(379, 379, 379)
                .addComponent(LClients)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LClients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Serveur_Mastercard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Serveur_Mastercard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Serveur_Mastercard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Serveur_Mastercard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new Serveur_Mastercard().setVisible(true);
            }
        });
    } 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LClients;
    private javax.swing.JTable TableauEvenements;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
