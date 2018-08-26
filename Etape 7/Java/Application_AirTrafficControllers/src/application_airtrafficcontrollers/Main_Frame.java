package application_airtrafficcontrollers;

import ACMAP_classes.Vol;
import ProtocoleACMAP.ReponseACMAP;
import ProtocoleACMAP.RequeteACMAP;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Main_Frame extends javax.swing.JFrame
{
    private final static String PROPERTIES_PATH = "D:\\GitHub\\Etape 7\\Application_AirTrafficControllers\\config.properties";
    
    private String ADRESSE_TOWER;
    private int PORT_TOWER;
    
    private Socket socket;
    
    private final DefaultTableModel dtm;
    private List<Vol> listeVols;
    
    public Main_Frame()
    {
        initComponents();
        
        dtm = (DefaultTableModel) JTVols.getModel();
        
        LireFichierProperties();
        
        InitialiserTableVols();
    }
    
    private void LireFichierProperties()
    {
        Properties properties = null;
        InputStream input = null;
        
        properties = new Properties();
        try 
        {
            input = new FileInputStream(PROPERTIES_PATH);
            properties.load(input);  
            
            if(properties.getProperty("ADRESSE_TOWER") != null)
            {
                ADRESSE_TOWER = properties.getProperty("ADRESSE_TOWER");
            }
            else
            {
                System.err.println("L'adresse du serveur billets est introuvable dans le fichier " + PROPERTIES_PATH);
                System.exit(1);
            }
            
            if(properties.getProperty("PORT_TOWER") != null)
            {
                PORT_TOWER = Integer.parseInt(properties.getProperty("PORT_TOWER"));
            }
            else
            {
                System.err.println("Le numéro de port billets est introuvable dans le fichier " + PROPERTIES_PATH);
                System.exit(1);
            }
        } 
        catch (FileNotFoundException fnfeEx) 
        {
            try
            {
                FileOutputStream fos = new FileOutputStream(PROPERTIES_PATH);
                
                properties.put("ADRESSE_TOWER", "192.168.0.5");
                ADRESSE_TOWER = "192.168.0.19";
                properties.put("PORT_TOWER", "30019");
                PORT_TOWER = 30019;
                
                try 
                {
                    properties.store(fos, null);
                }
                catch (IOException ioEx) 
                {
                    System.err.println("Erreur IO lors de l'écriture du fichier "  + PROPERTIES_PATH + " : " + ioEx);
                    System.exit(0);
                }
            } 
            catch (FileNotFoundException fnfeEx2)
            {
                System.err.println("Erreur FileNotFound lors de l'écriture du fichier " + PROPERTIES_PATH + " : " + fnfeEx2);
                System.exit(0);
            }
        } 
        catch (IOException ex) 
        {
            System.err.println("Erreur IO lors de la lecture du fichier " + PROPERTIES_PATH + " : " + ex);
            System.exit(0);
        }
    }
    
    private void InitialiserTableVols()
    {
        RequeteACMAP requeteVols = new RequeteACMAP(RequeteACMAP.REQUEST_FLIGHTS);
        try
        {
            ReponseACMAP reponseVols = (ReponseACMAP) sendReceive(requeteVols);
            listeVols = reponseVols.getListeVols();
            RemplirTableauVols();
        }
        catch(ClassNotFoundException ex)
        {
            System.err.println("La classe n'a pas été trouvée lors de la requête vols : " + ex.getMessage());
            System.exit(1);
        }
        catch(IOException ex)
        {
            System.err.println("Erreur IO lors de la récupération de la liste des vols : " + ex.getMessage());
            System.exit(1);
        }
    }
    
    public void initSocket(String adresse, int port)
    {
        socket = null;
        try
        {
            System.out.println("Port : " + port + " Adresse : " + adresse);
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
    }
    
    public Object sendReceive(Object requete) throws IOException, ClassNotFoundException
    {
        initSocket(ADRESSE_TOWER, PORT_TOWER);
        
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(getSocket().getOutputStream());
        oos.writeObject(requete);
        oos.flush();
        
        ObjectInputStream ois = null;
        ReponseACMAP reponseVols;
        ois = new ObjectInputStream(getSocket().getInputStream());
        reponseVols = (ReponseACMAP) ois.readObject();
        
        return reponseVols;
    }

    private void RemplirTableauVols()
    {
        dtm.setRowCount(0);
        
        ((DefaultTableCellRenderer)JTVols.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        if(listeVols.size() > 0)
        {
            for(int i = 0 ; i < listeVols.size() ; i++)
            {
                Vol vol = listeVols.get(i);
                Object[] tabObjet = new Object[1];
                tabObjet[0] = vol.toString();
                dtm.addRow(tabObjet);
                for(int j = 0 ; j < tabObjet.length ; j++)
                {
                    JTVols.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Aucun vol n'est prévu dans les 3 heures.","Information",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        JTVols = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Application AirTrafficControllers");

        JTVols.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Vols"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false
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
        JTVols.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                JTVolsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(JTVols);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTVolsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_JTVolsMouseClicked
    {//GEN-HEADEREND:event_JTVolsMouseClicked
        if(evt.getClickCount() == 2)
        {
            if(JTVols.getSelectedRow() == -1)
            {
                JOptionPane.showMessageDialog(null,"Vous devez sélectionner un vol.","Erreur",JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                Vol vol = listeVols.get(JTVols.getSelectedRow());
                
                Vol_Dialog vol_dialog = new Vol_Dialog(this, true, ADRESSE_TOWER, PORT_TOWER, vol);
                vol_dialog.setVisible(true);
                vol_dialog.setModal(true);
            }
        }
    }//GEN-LAST:event_JTVolsMouseClicked

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
            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new Main_Frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable JTVols;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the socket
     */
    public Socket getSocket()
    {
        return socket;
    }
    
    public List<Vol> getListeVols()
    {
        return listeVols;
    }
}
