package serveur_airtrafficcontrollers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import requetepoolthreads.ConsoleServeur;
import serveurthreadsdemande.ThreadServeur;

public class Serveur_AirTrafficControllers extends javax.swing.JFrame implements ConsoleServeur
{   
    private final static String PROPERTIES_FILE = "config.properties";
    
    private DefaultTableModel dtm = null;
    private int PORT_TOWER;
    private String ADRESSE;
    private String ADRESSE_BAGAGES;
    private int PORT_BAGAGES;
    
    public Serveur_AirTrafficControllers()
    {
        initComponents();
        dtm = (DefaultTableModel)TableauEvenements.getModel();
        InitialiserFenetre();
    }
    
    private void InitialiserFenetre()
    {
        LireFichierProperties(PROPERTIES_FILE);
        try
        {
            ADRESSE = InetAddress.getLocalHost().getHostAddress();
            TraceEvenements(ADRESSE + ":" + PORT_TOWER + "#Acq. port TOWER" + "#Main");
        } 
        catch (UnknownHostException ex)
        {
            System.err.println("Erreur lors de la récupération de l'adresse : " + ex.getMessage());
        }

        System.out.println("Serveur Bagage : " + ADRESSE_BAGAGES + ":" + PORT_BAGAGES);
        ThreadServeur ts = new ThreadServeur(PORT_TOWER, this, ADRESSE_BAGAGES, PORT_BAGAGES);
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
            if(properties.getProperty("PORT_TOWER") != null)
            {
                PORT_TOWER = Integer.parseInt(properties.getProperty("PORT_TOWER"));
            }
            else
            {
                System.err.println("Le numéro de port est introuvable dans le fichier " + filename);
                System.exit(1);
            }
                        
            if(properties.getProperty("ADRESSE_BAGAGES") != null)
            {
                ADRESSE_BAGAGES = properties.getProperty("ADRESSE_BAGAGES");
            }
            else
            {
                System.err.println("L'adresse du serveur bagages est introuvable dans le fichier " + filename);
                System.exit(1);
            }
            
            if(properties.getProperty("PORT_BAGAGES") != null)
            {
                PORT_BAGAGES = Integer.parseInt(properties.getProperty("PORT_BAGAGES"));
                System.out.println("Port : " + PORT_BAGAGES);
            }
            else
            {
                System.err.println("Le numéro de port bagages est introuvable dans le fichier " + filename);
                System.exit(1);
            }
        } 
        catch (FileNotFoundException fnfeEx) 
        {
           System.err.println("Exception lors de la lecture du fichier " + filename + " : " + fnfeEx);
           
            try
            {
                FileOutputStream fos = new FileOutputStream(filename);
                
                properties.put("PORT_TOWER", "30019");
                PORT_TOWER = 30019;
                properties.put("ADRESSE_BAGAGES", "192.168.0.5");
                ADRESSE_BAGAGES = "192.168.0.19";
                properties.put("PORT_BAGAGES", "30011");
                PORT_BAGAGES = 30011;
                
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
        setTitle("Serveur AirTraficControllers");

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
            java.util.logging.Logger.getLogger(Serveur_AirTrafficControllers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Serveur_AirTrafficControllers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Serveur_AirTrafficControllers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Serveur_AirTrafficControllers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new Serveur_AirTrafficControllers().setVisible(true);
            }
        });
    } 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LClients;
    private javax.swing.JTable TableauEvenements;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
