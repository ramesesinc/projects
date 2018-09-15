/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.bpls.view;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author dell
 */
@Template(OKCancelPage.class)
public class AddReceivablePage extends javax.swing.JPanel {

    /**
     * Creates new form AddReceivablePage
     */
    public AddReceivablePage() {
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
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setCaptionWidth(120);

        xComboBox1.setCaption("Line of Business");
        xComboBox1.setExpression("#{item.name}");
        xComboBox1.setItems("parent.lobs");
        xComboBox1.setName("entity.lob"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xComboBox1);

        xLookupField1.setCaption("Account");
        xLookupField1.setExpression("#{entity.account.title}");
        xLookupField1.setHandler("revenueitem:lookup");
        xLookupField1.setName("entity.account"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xComboBox2.setCaption("Tax Fee Type");
        xComboBox2.setItems("txnTypes");
        xComboBox2.setName("entity.taxfeetype"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox2.setRequired(true);
        xFormPanel1.add(xComboBox2);

        xComboBox3.setCaption("Assessment Type");
        xComboBox3.setItems("assessmentTypes");
        xComboBox3.setName("entity.assessmenttype"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox3.setRequired(true);
        xFormPanel1.add(xComboBox3);

        xDecimalField1.setCaption("Total Amount Due");
        xDecimalField1.setName("entity.amount"); // NOI18N
        xDecimalField1.setRequired(true);
        xFormPanel1.add(xDecimalField1);

        xDecimalField2.setCaption("Total Amount Paid");
        xDecimalField2.setName("entity.amtpaid"); // NOI18N
        xDecimalField2.setRequired(true);
        xFormPanel1.add(xDecimalField2);

        xDecimalField3.setCaption("Balance Due");
        xDecimalField3.setDepends(new String[] {"entity.amount", "entity.amtpaid"});
        xDecimalField3.setEnabled(false);
        xDecimalField3.setName("balance"); // NOI18N
        xFormPanel1.add(xDecimalField3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    // End of variables declaration//GEN-END:variables
}