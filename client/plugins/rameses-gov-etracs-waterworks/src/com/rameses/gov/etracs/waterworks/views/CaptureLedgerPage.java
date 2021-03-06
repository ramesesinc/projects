/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author Rameses
 */
@Template(OKCancelPage.class)
@StyleSheet
public class CaptureLedgerPage extends javax.swing.JPanel {

    /**
     * Creates new form CaptureConsumptionPage
     */
    public CaptureLedgerPage() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xPanel1 = new com.rameses.rcp.control.XPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Ledger Information");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setCaptionWidth(100);

        xLookupField1.setCaption("Account");
        xLookupField1.setExpression("#{item.title}");
        xLookupField1.setHandler("revenueitem:lookup");
        xLookupField1.setName("info.item"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField1.setRequired(true);
        xFormPanel2.add(xLookupField1);

        xComboBox1.setCaption("Txn Type");
        xComboBox1.setItems("txnTypes");
        xComboBox1.setName("info.txntype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel2.add(xComboBox1);

        xIntegerField1.setCaption("Year");
        xIntegerField1.setName("info.year"); // NOI18N
        xIntegerField1.setRequired(true);
        xFormPanel2.add(xIntegerField1);

        xComboBox2.setCaption("Month");
        xComboBox2.setDepends(new String[] {"info.year"});
        xComboBox2.setDynamic(true);
        xComboBox2.setExpression("#{item.monthname} ");
        xComboBox2.setItems("monthList");
        xComboBox2.setName("info.billingcycle"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(120, 20));
        xComboBox2.setRequired(true);
        xComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xComboBox2ActionPerformed(evt);
            }
        });
        xFormPanel2.add(xComboBox2);

        xLabel1.setCaption("Due Date");
        xLabel1.setDepends(new String[] {"info.billingcycle"});
        xLabel1.setExpression("#{info.billingcycle.duedate}");
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel1);

        xTextField1.setCaption("Particulars");
        xTextField1.setName("info.remarks"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField1.setRequired(true);
        xFormPanel2.add(xTextField1);

        xDecimalField2.setCaption("Amount");
        xDecimalField2.setName("info.amount"); // NOI18N
        xDecimalField2.setPreferredSize(new java.awt.Dimension(120, 20));
        xDecimalField2.setRequired(true);
        xFormPanel2.add(xDecimalField2);

        xDecimalField3.setCaption("Amount Paid");
        xDecimalField3.setName("info.amtpaid"); // NOI18N
        xDecimalField3.setPreferredSize(new java.awt.Dimension(120, 20));
        xDecimalField3.setRequired(true);
        xFormPanel2.add(xDecimalField3);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xComboBox2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}
