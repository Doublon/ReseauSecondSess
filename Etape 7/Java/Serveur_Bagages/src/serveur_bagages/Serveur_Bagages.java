package serveur_bagages;

import java.util.*;
import javax.swing.table.*;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

import requetepoolthreads.*;
import serveurpoolthreads.*;

public class Serveur_Bagages extends javax.swing.JFrame implements ConsoleServeur
{
    private int port_bagages;
    private int nombreThreadsBagages;
    private DefaultTableModel dtm = null;
    private List<Socket> listeClients;
    
    public Serveur_Bagages() 
    {
        initComponents();
        dtm = (DefaultTableModel)TableauEvenements.getModel();
        listeClients = new ArrayList();
        InitialiserFenetre();
    }
    
    private void InitialiserFenetre()
    {
        LireFichierProperties("config.properties");
        try
        {
            TraceEvenements(InetAddress.getLocalHost().getHostAddress() + ":" + port_bagages + "#Acquisition du port bagages" + "#Main");
        } 
        catch (UnknownHostException ex)
        {
            System.err.println("Erreur lors de la récupération de l'adresse : " + ex.getMessage());
        }
        ThreadServeur ts = new ThreadServeur(port_bagages, nombreThreadsBagages, this);
<<<<<<< HEAD
        ts.start();
=======
        ThreadServeurCheckin tsc = new ThreadServeurCheckin(port_checkin, nombreThreadsCheckin, this);
        ThreadReception tr = new ThreadReception(port_takeoff, this);
        ts.start();
        tsc.start();
        tr.start();
>>>>>>> parent of 2599914... Etape 7 : Correction serveur bagage
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
        catch (FileNotFoundException ex) 
        {
           System.err.println("Exception lors de la lecture du fichier " + filename + " : " + ex);
        } 
        catch (IOException ex) 
        {
            System.err.println("Exception lors de la lecture du fichier " + filename + " : " + ex);
        }
        
        if(properties.getProperty("PORT_BAGAGES") != null)
            port_bagages = Integer.parseInt(properties.getProperty("PORT_BAGAGES"));
        if(properties.getProperty("NOMBRETHREADSBAGAGES") != null)
            nombreThreadsBagages = Integer.parseInt(properties.getProperty("NOMBRETHREADSBAGAGES"));
    }
    
    public void AfficherAlerteCheckinOff()
    {
        JOptionPane.showMessageDialog(null,"Vous devez sélectionner un vol.","Information",JOptionPane.INFORMATION_MESSAGE);
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableauEvenements = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Serveur bagages");

        jLabel1.setText("CLIENTS");

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
            boolean[] canEdit = new boolean []
            {
                false, false, false
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
        jScrollPane1.setViewportView(TableauEvenements);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(228, 228, 228)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Serveur_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Serveur_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Serveur_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Serveur_Bagages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Serveur_Bagages().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableauEvenements;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    public List<Socket> getListeClients()
    {
        return listeClients;
    }
}
