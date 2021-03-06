/*
 * DepositInitPage.java
 *
 * Created on June 23, 2014, 10:50 AM
 */

package com.rameses.clfc.treasury.deposit.bak;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  louie
 */

@StyleSheet
@Template(FormPage.class)
public class DepositInitPage extends javax.swing.JPanel {
    
    /** Creates new form DepositInitPage */
    public DepositInitPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xRadio1 = new com.rameses.rcp.control.XRadio();
        xRadio2 = new com.rameses.rcp.control.XRadio();

        xFormPanel1.setPadding(new java.awt.Insets(5, 10, 5, 5));

        xRadio1.setBackground(new java.awt.Color(232, 232, 226));
        xRadio1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xRadio1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xRadio1.setName("entity.txntype"); // NOI18N
        xRadio1.setOptionValue("vault");
        xRadio1.setPreferredSize(new java.awt.Dimension(81, 20));
        xRadio1.setShowCaption(false);
        xRadio1.setText("   To Vault");
        xFormPanel1.add(xRadio1);

        xRadio2.setBackground(new java.awt.Color(232, 232, 226));
        xRadio2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xRadio2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xRadio2.setName("entity.txntype"); // NOI18N
        xRadio2.setOptionValue("bank");
        xRadio2.setPreferredSize(new java.awt.Dimension(79, 20));
        xRadio2.setShowCaption(false);
        xRadio2.setText("   To Bank");
        xFormPanel1.add(xRadio2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(205, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(281, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XRadio xRadio1;
    private com.rameses.rcp.control.XRadio xRadio2;
    // End of variables declaration//GEN-END:variables
    
}
