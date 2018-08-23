package application_billets;

import javax.swing.JOptionPane;

public class Payment_Dialog extends javax.swing.JDialog
{
    private final Main_Frame parent;
    
    public String numCarte;
    public String proprietaire;
    public String adresseMail;
    
    public Payment_Dialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        
        this.parent = (Main_Frame) parent;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        JLProprietaire = new javax.swing.JLabel();
        JTFProprietaire = new javax.swing.JTextField();
        JLNumero = new javax.swing.JLabel();
        JTFNumero = new javax.swing.JTextField();
        BConfirmer = new javax.swing.JButton();
        JLMail = new javax.swing.JLabel();
        JTFMail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Informations de carte");

        JLProprietaire.setText("Propriétaire de la carte : ");

        JLNumero.setText("Numéro de la carte : ");

        BConfirmer.setText("Confirmer");
        BConfirmer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BConfirmerActionPerformed(evt);
            }
        });

        JLMail.setText("Adresse mail :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(BConfirmer)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JLProprietaire)
                                    .addComponent(JLNumero))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(JTFProprietaire)
                                    .addComponent(JTFNumero, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(JLMail, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTFMail)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLProprietaire)
                    .addComponent(JTFProprietaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLNumero)
                    .addComponent(JTFNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLMail)
                    .addComponent(JTFMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(BConfirmer)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BConfirmerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BConfirmerActionPerformed
    {//GEN-HEADEREND:event_BConfirmerActionPerformed
        if(JTFProprietaire.getText().isEmpty() || JTFNumero.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Tous les champs sont requis.","Erreur",JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            numCarte = JTFNumero.getText();
            proprietaire = JTFProprietaire.getText();
            adresseMail = JTFMail.getText();
            
            parent.setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_BConfirmerActionPerformed

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
            java.util.logging.Logger.getLogger(Payment_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Payment_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Payment_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Payment_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                Payment_Dialog dialog = new Payment_Dialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton BConfirmer;
    private javax.swing.JLabel JLMail;
    private javax.swing.JLabel JLNumero;
    private javax.swing.JLabel JLProprietaire;
    private javax.swing.JTextField JTFMail;
    private javax.swing.JTextField JTFNumero;
    private javax.swing.JTextField JTFProprietaire;
    // End of variables declaration//GEN-END:variables
}
