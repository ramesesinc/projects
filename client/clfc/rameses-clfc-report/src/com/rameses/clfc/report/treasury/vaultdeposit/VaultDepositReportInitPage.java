/*
 * ShortageSummaryReportInitPage.java
 *
 * Created on June 20, 2014, 11:48 AM
 */

package com.rameses.clfc.report.treasury.vaultdeposit;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  louie
 */

@StyleSheet
@Template(FormPage.class)
public class VaultDepositReportInitPage extends javax.swing.JPanel {
    
    /** Creates new form ShortageSummaryReportInitPage */
    public VaultDepositReportInitPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xDateField1 = new com.rameses.rcp.control.XDateField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(25, 5, 5, 5));
        xTitledBorder1.setTitle("   Report Criteria Information");
        xFormPanel1.setBorder(xTitledBorder1);
        xFormPanel1.setCaptionWidth(60);
        xFormPanel1.setPadding(new java.awt.Insets(5, 10, 5, 5));
        xDateField1.setCaption("Date");
        xDateField1.setFont(new java.awt.Font("Tahoma", 0, 12));
        xDateField1.setName("txndate");
        xDateField1.setOutputFormat("MMM-dd-yyyy");
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    // End of variables declaration//GEN-END:variables
    
}
