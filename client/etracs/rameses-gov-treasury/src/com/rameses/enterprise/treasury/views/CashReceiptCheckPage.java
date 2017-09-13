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
public class CashReceiptCheckPage extends javax.swing.JPanel {

    /**
     * Creates new form CashReceiptCheckPage
     */
    public CashReceiptCheckPage() {
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

        jPanel1 = new javax.swing.JPanel();
        xTabbedPane1 = new com.rameses.rcp.control.XTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xLabel1 = new com.rameses.rcp.control.XLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        xTabbedPane1.setItems("sections");

        xFormPanel1.setCaptionFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        xFormPanel1.setCaptionWidth(160);

        xTextField1.setCaption("Check No");
        xTextField1.setName("entity.refno"); // NOI18N
        xTextField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 27));
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        xTextField2.setCaption("Received From");
        xTextField2.setName("entity.receivedfrom"); // NOI18N
        xTextField2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 27));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        xLookupField1.setCaption("Bank");
        xLookupField1.setExpression("#{entity.bank.name}");
        xLookupField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        xLookupField1.setHandler("bank:lookup");
        xLookupField1.setName("entity.bank"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 27));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xDateField1.setCaption("Check Date");
        xDateField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        xDateField1.setName("entity.refdate"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(0, 27));
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        xDecimalField2.setCaption("Check Amount");
        xDecimalField2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        xDecimalField2.setName("entity.amount"); // NOI18N
        xDecimalField2.setPreferredSize(new java.awt.Dimension(0, 27));
        xDecimalField2.setRequired(true);
        xFormPanel1.add(xDecimalField2);

        xLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        xLabel1.setCaption("Balance Unused");
        xLabel1.setExpression("#{ entity.balance }");
        xLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 24));
        xFormPanel1.add(xLabel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(153, Short.MAX_VALUE))
        );

        xTabbedPane1.addTab("General Info", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}
