package application_airtrafficcontrollers;

import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class Runway_Dialog extends javax.swing.JDialog
{
    public boolean valider;
    private List<Integer> listePistes;
    private DefaultComboBoxModel cbm;
    public int piste;
    
    public Runway_Dialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
    }
    
    public Runway_Dialog(java.awt.Frame parent, boolean modal, List<Integer> lP)
    {
        super(parent, modal);
        initComponents();
        
        listePistes = lP;
        
        cbm = (DefaultComboBoxModel) JCBPiste.getModel();
        
        for(int i = 0 ; i < listePistes.size() ; i++)
        {
            cbm.addElement(listePistes.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        JCBPiste = new javax.swing.JComboBox<>();
        BValider = new javax.swing.JButton();
        BAnnuler = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        BValider.setText("Valider");
        BValider.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BValiderActionPerformed(evt);
            }
        });

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(JCBPiste, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BAnnuler)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BValider)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JCBPiste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BValider)
                    .addComponent(BAnnuler))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BAnnulerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BAnnulerActionPerformed
    {//GEN-HEADEREND:event_BAnnulerActionPerformed
        valider = false;
        piste = -1;
        
        this.setVisible(false);
        this.setModal(false);
    }//GEN-LAST:event_BAnnulerActionPerformed

    private void BValiderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BValiderActionPerformed
    {//GEN-HEADEREND:event_BValiderActionPerformed
        valider = true;
        piste = (Integer) cbm.getSelectedItem();
        
        this.setVisible(false);
        this.setModal(false);
    }//GEN-LAST:event_BValiderActionPerformed

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
            java.util.logging.Logger.getLogger(Runway_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Runway_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Runway_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Runway_Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                Runway_Dialog dialog = new Runway_Dialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> JCBPiste;
    // End of variables declaration//GEN-END:variables
}
