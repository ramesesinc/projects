/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Rameses
 */
@StyleSheet
@Template(CrudFormPage.class)
public class WaterConsumptionPage extends javax.swing.JPanel {

    /**
     * Creates new form CaptureConsumptionPage
     */
    public WaterConsumptionPage() {
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

        xButton1 = new com.rameses.rcp.control.XButton();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        monthList1 = new com.rameses.enterprise.components.MonthList();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField3 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField4 = new com.rameses.rcp.control.XIntegerField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();

        xButton1.setDepends(new String[] {"entity.txnmode"});
        xButton1.setName("calculate"); // NOI18N
        xButton1.setVisibleWhen("#{ entity.txnmode != 'BEGIN_BALANCE' }");
        xButton1.setImmediate(true);
        xButton1.setText("Calculate");

        xFormPanel5.setCaptionWidth(180);

        xComboBox1.setCaption("Txn Type");
        xComboBox1.setItems("txnModes");
        xComboBox1.setName("entity.txnmode"); // NOI18N
        xFormPanel5.add(xComboBox1);

        xIntegerField1.setCaption("Year");
        xIntegerField1.setDisableWhen("#{mode != 'create' }");
        xIntegerField1.setName("entity.year"); // NOI18N
        xIntegerField1.setVisibleWhen("#{ entity.batchid == null }");
        xIntegerField1.setRequired(true);
        xFormPanel5.add(xIntegerField1);

        xLabel1.setCaption("Year");
        xLabel1.setExpression("#{ entity.year }");
        xLabel1.setVisibleWhen("#{ entity.batchid != null }");
        xFormPanel5.add(xLabel1);

        xLabel2.setCaption("Month");
        xLabel2.setExpression("#{ entity.monthname }");
        xLabel2.setVisibleWhen("#{ entity.batchid != null }");
        xFormPanel5.add(xLabel2);

        monthList1.setCaption("Month");
        monthList1.setName("entity.month"); // NOI18N
        monthList1.setPreferredSize(new java.awt.Dimension(100, 25));
        monthList1.setRequired(true);
        monthList1.setVisibleWhen("#{ entity.batchid == null }");
        xFormPanel5.add(monthList1);

        xIntegerField2.setCaption("Prev. Reading");
        xIntegerField2.setDepends(new String[] {"entity.txnmode"});
        xIntegerField2.setDisableWhen("#{ 1== 1}");
        xIntegerField2.setName("entity.prevreading"); // NOI18N
        xIntegerField2.setVisibleWhen("#{ entity.txnmode != 'BEGIN_BALANCE' && masterEntity.meter?.objid !=null }");
        xIntegerField2.setCellPadding(new java.awt.Insets(20, 0, 0, 0));
        xFormPanel5.add(xIntegerField2);

        xIntegerField3.setCaption("Current Reading");
        xIntegerField3.setDepends(new String[] {"entity.txnmode"});
        xIntegerField3.setDisableWhen("#{ 1== 1}");
        xIntegerField3.setName("entity.reading"); // NOI18N
        xIntegerField3.setVisibleWhen("#{ entity.txnmode != 'BEGIN_BALANCE' && masterEntity.meter?.objid !=null }");
        xFormPanel5.add(xIntegerField3);

        xIntegerField4.setCaption("Volume");
        xIntegerField4.setDepends(new String[] {"entity.txnmode"});
        xIntegerField4.setDisableWhen("#{ 1== 1}");
        xIntegerField4.setName("entity.volume"); // NOI18N
        xIntegerField4.setVisibleWhen("#{ entity.txnmode != 'BEGIN_BALANCE' }");
        xFormPanel5.add(xIntegerField4);

        xDecimalField2.setCaption("Amount");
        xDecimalField2.setDepends(new String[] {"entity.txnmode"});
        xDecimalField2.setDisableWhen("#{ entity.txnmode != 'BEGIN_BALANCE'  }");
        xDecimalField2.setName("entity.amount"); // NOI18N
        xDecimalField2.setCellPadding(new java.awt.Insets(20, 0, 0, 0));
        xDecimalField2.setRequired(true);
        xDecimalField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xDecimalField2ActionPerformed(evt);
            }
        });
        xFormPanel5.add(xDecimalField2);

        xDecimalField3.setCaption("Amount Paid");
        xDecimalField3.setName("entity.amtpaid"); // NOI18N
        xDecimalField3.setRequired(true);
        xFormPanel5.add(xDecimalField3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xDecimalField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xDecimalField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xDecimalField2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.enterprise.components.MonthList monthList1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XIntegerField xIntegerField3;
    private com.rameses.rcp.control.XIntegerField xIntegerField4;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    // End of variables declaration//GEN-END:variables
}