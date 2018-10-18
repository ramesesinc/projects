/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.treasury.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author Elmo Nazareno
 */
@Template(FormPage.class)
public class CashReceiptAddCashPage extends javax.swing.JPanel {

    /**
     * Creates new form CashReceiptAskCheckNoPage
     */
    public CashReceiptAddCashPage() {
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

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xButton1 = new com.rameses.rcp.control.XButton();

        xFormPanel1.setCaptionFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        xFormPanel1.setCaptionWidth(200);

        xDecimalField3.setCaption("Amount Due");
        xDecimalField3.setDisableWhen("#{ true }");
        xDecimalField3.setName("balance"); // NOI18N
        xDecimalField3.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        xDecimalField3.setPreferredSize(new java.awt.Dimension(0, 25));
        xFormPanel1.add(xDecimalField3);

        xDecimalField1.setCaption("Cash Tendered");
        xDecimalField1.setName("cashtendered"); // NOI18N
        xDecimalField1.setCellPadding(new java.awt.Insets(20, 0, 0, 0));
        xDecimalField1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        xDecimalField1.setPreferredSize(new java.awt.Dimension(0, 25));
        xFormPanel1.add(xDecimalField1);

        xDecimalField2.setCaption("Change");
        xDecimalField2.setDepends(new String[] {"cashtendered"});
        xDecimalField2.setDisableWhen("#{ true }");
        xDecimalField2.setName("change"); // NOI18N
        xDecimalField2.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        xDecimalField2.setPreferredSize(new java.awt.Dimension(0, 25));
        xFormPanel1.add(xDecimalField2);

        xButton1.setName("verifyCashPayment"); // NOI18N
        xButton1.setText("OK");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(101, 101, 101))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    // End of variables declaration//GEN-END:variables
}