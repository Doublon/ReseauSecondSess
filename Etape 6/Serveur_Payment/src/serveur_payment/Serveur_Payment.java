package serveur_payment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import requetepoolthreads.ConsoleServeur;
import serveurpoolthreads.ListeTaches;
import serveurpoolthreads.ThreadServeur;

public class Serveur_Payment extends javax.swing.JFrame implements ConsoleServeur
{
    private final String propertiesFile = "config.properties";
    private final String keyStorePath = "D:\\GitHub\\ReseauSecondSess\\Etape 6\\Certificats\\keystore_serveur_payment";
    private final String SSLKeyStorePath = "D:\\GitHub\\ReseauSecondSess\\Etape 6\\Certificats\\client_payment_keystore";
    
    private Key privateKey;
    private PublicKey publicKey;
    private X509Certificate certificat;
    
    private DefaultTableModel dtm = null;
    private int PORT_PAYMENT;
    private int nombreThreads;
    private String ADRESSE;
    
    private KeyStore keyStore;
    
    private int PORT_MASTERCARD;
    private String ADRESSE_MASTERCARD;
    
    public Serveur_Payment()
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
            System.out.println("Adresse Serveur Payment : " + ADRESSE);
            TraceEvenements(InetAddress.getLocalHost().getHostAddress() + ":" + PORT_PAYMENT + "#Acq. port PAYMENT" + "#Main");
        } 
        catch (UnknownHostException ex)
        {
            System.err.println("Erreur lors de la récupération de l'adresse : " + ex.getMessage());
        }
        readKeyStore(keyStorePath);

        readSSLKeyStore(SSLKeyStorePath);

        ThreadServeur ts = new ThreadServeur(ADRESSE, PORT_PAYMENT, new ListeTaches(), nombreThreads, this, getCertificat(), (PrivateKey)getPrivateKey(), keyStore, ADRESSE_MASTERCARD, PORT_MASTERCARD);
        ts.start();
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
            if(properties.getProperty("PORT_PAYMENT") != null)
            {
                PORT_PAYMENT = Integer.parseInt(properties.getProperty("PORT_PAYMENT"));
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
            if(properties.getProperty("PORT_MASTERCARD") != null)
            {
                PORT_MASTERCARD = Integer.parseInt(properties.getProperty("PORT_MASTERCARD"));
            }
            else
            {
                System.err.println("Le numéro de port est introuvable dans le fichier " + filename);
                System.exit(1);
            }
            if(properties.getProperty("ADRESSE_MASTERCARD") != null)
            {
                ADRESSE_MASTERCARD = properties.getProperty("ADRESSE_MASTERCARD");
            }
            else
            {
                System.err.println("L'adresse du serveur mastercard est introuvable dans le fichier " + filename);
                System.exit(1);
            }
        } 
        catch (FileNotFoundException fnfeEx) 
        {
           System.err.println("Exception lors de la lecture du fichier " + filename + " : " + fnfeEx);
           
            try
            {
                FileOutputStream fos = new FileOutputStream(filename);
                
                properties.put("PORT_PAYMENT", "30017");
                PORT_PAYMENT = 30017;
                properties.put("NOMBRETHREADS", "5");
                nombreThreads = 5;
                properties.put("PORT_MASTERCARD", "30018");
                PORT_MASTERCARD = 30018;
                properties.put("ADRESSE_MASTERCARD", "192.168.0.5");
                ADRESSE_MASTERCARD = "192.168.0.5";
                
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
    
    private void readKeyStore(String keyStorePath)
    {
        try
        {
            Security.addProvider(new BouncyCastleProvider());
            KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
            ks.load(new FileInputStream(keyStorePath),"payment123".toCharArray());
            Enumeration en = ks.aliases();
            String aliasCourant = null;
            Vector vectaliases = new Vector();
            while (en.hasMoreElements()) vectaliases.add(en.nextElement());
                Object[] aliases = vectaliases.toArray();
            //OU : String[] aliases = (String []) (vectaliases.toArray(new String[0]));
            
            for (int i = 0; i < aliases.length; i++)
            {
                aliasCourant = (String)aliases[i];
                
                setCertificat((X509Certificate)ks.getCertificate(aliasCourant));
                setPrivateKey((PrivateKey) ks.getKey(aliases[i].toString(), "payment123".toCharArray()));
                setPublicKey(getCertificat().getPublicKey());
            }
        }
        catch (Exception e)
        {
            System.err.println("Impossible de lire le keyStore à l'emplacement : " + keyStorePath);
            System.err.println("[" + e.getMessage() + "]");
            System.exit(1);
        }
    }
    
    private void readSSLKeyStore(String path)
    {
        try 
        {
            Security.addProvider(new BouncyCastleProvider());
            keyStore = KeyStore.getInstance("JKS");
            char[] PASSWD_KEYSTORE = "payment123".toCharArray();
            FileInputStream ServerFK = new FileInputStream (path);
            keyStore.load(ServerFK, PASSWD_KEYSTORE);
        } 
        catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) 
        {
            System.err.println("Erreur lors de la lecture du KeyStore SSL à l'emplacement : " + path  + " : " + ex.getMessage());
            System.exit(1);
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
        setTitle("Serveur Payment");

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
            java.util.logging.Logger.getLogger(Serveur_Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Serveur_Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Serveur_Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Serveur_Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new Serveur_Payment().setVisible(true);
            }
        });
    } 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LClients;
    private javax.swing.JTable TableauEvenements;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the privateKey
     */
    public Key getPrivateKey()
    {
        return privateKey;
    }

    /**
     * @param privateKey the privateKey to set
     */
    public void setPrivateKey(Key privateKey)
    {
        this.privateKey = privateKey;
    }

    /**
     * @return the publicKey
     */
    public PublicKey getPublicKey()
    {
        return publicKey;
    }

    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(PublicKey publicKey)
    {
        this.publicKey = publicKey;
    }

    /**
     * @return the certificat
     */
    public X509Certificate getCertificat()
    {
        return certificat;
    }

    /**
     * @param certificat the certificat to set
     */
    public void setCertificat(X509Certificate certificat)
    {
        this.certificat = certificat;
    }
}
