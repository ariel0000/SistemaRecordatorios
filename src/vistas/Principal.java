/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import controladores.ControladorPrincipal;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author ari_0
 */
public class Principal extends javax.swing.JFrame {
    private JPanel panel;  // p1 es siempre el panel referenciado
    private ControladorPrincipal controlador;
    private PanelPrincipal p1;

    public Principal() {
        initComponents();
        controlador = ControladorPrincipal.getInstancia(); //Creo el controlador (Singleton) y paso la vista entera
        controlador.setPrincipal(this);
        this.p1 = new PanelPrincipal(); 
        abrir_principal(); //Lo primero que hacemos cuando iniciamos el Sistema es poner el panel principal como objetivo
    }
     
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JTabbedPaneP = new javax.swing.JTabbedPane();
        jButtonCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(1280, 760));
        setPreferredSize(new java.awt.Dimension(1280, 723));

        JTabbedPaneP.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jButtonCerrar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonCerrar.setText("Cerrar Pestaña");
        jButtonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JTabbedPaneP)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 1008, Short.MAX_VALUE)
                        .addComponent(jButtonCerrar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JTabbedPaneP, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCerrar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarActionPerformed
        this.controlador.cerrarPanelSeleccionado();
    }//GEN-LAST:event_jButtonCerrarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane JTabbedPaneP;
    private javax.swing.JButton jButtonCerrar;
    // End of variables declaration//GEN-END:variables

    private void abrir_principal() {
        controlador.cambiarDePanel(p1, "Principal");  //Solo se puede quitar si se cierra el programa
        this.JTabbedPaneP.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                controlador.focusear();
            }
        });
    }
    
    public JPanel getP1() {
        return panel;
    }

    public void setP1(JPanel p1) {
        this.panel = p1;
    }

    public JTabbedPane getJTabbedPaneP() {
        return JTabbedPaneP;
    }

    public void setJTabbedPaneP(JTabbedPane JTabbedPaneP) {
        this.JTabbedPaneP = JTabbedPaneP;
    }
    
}
