/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.treasury.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Elmo Nazareno
 */
@Template(CrudFormPage.class)
public class DepositSlipInitialPage extends javax.swing.JPanel {

    /**
     * Creates new form BankDepositInitialPage
     */
    public DepositSlipInitialPage() {
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

        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();

        xFormPanel4.setOpaque(true);

        xLookupField2.setCaption("Bank Account");
        xLookupField2.setCaptionWidth(120);
        xLookupField2.setExpression("#{ entity.bankaccount.code } #{ entity.bankaccount.bank.name } ");
        xLookupField2.setHandler("bankAccountLookup");
        xLookupField2.setName("entity.bankaccount"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField2.setRequired(true);
        xFormPanel4.add(xLookupField2);

        xDateField1.setCaption("Deposit Date");
        xDateField1.setCaptionWidth(120);
        xDateField1.setName("entity.depositdate"); // NOI18N
        xDateField1.setRequired(true);
        xFormPanel4.add(xDateField1);

        xDecimalField1.setCaption("Amount  to deposit");
        xDecimalField1.setCaptionWidth(120);
        xDecimalField1.setName("entity.amount"); // NOI18N
        xDecimalField1.setPreferredSize(new java.awt.Dimension(150, 20));
        xDecimalField1.setRequired(true);
        xFormPanel4.add(xDecimalField1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(416, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(499, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    // End of variables declaration//GEN-END:variables
}
