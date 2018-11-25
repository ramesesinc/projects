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

        xTabbedPane1 = new com.rameses.rcp.control.XTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        xButton1 = new com.rameses.rcp.control.XButton();
        xButton2 = new com.rameses.rcp.control.XButton();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xLabel11 = new com.rameses.rcp.control.XLabel();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField3 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField4 = new com.rameses.rcp.control.XIntegerField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel10 = new com.rameses.rcp.control.XLabel();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        xDateField3 = new com.rameses.rcp.control.XDateField();
        xDateField4 = new com.rameses.rcp.control.XDateField();
        xDateField5 = new com.rameses.rcp.control.XDateField();
        xDateField6 = new com.rameses.rcp.control.XDateField();

        xTabbedPane1.setItems("sections");

        xButton1.setDepends(new String[] {"entity.txnmode"});
        xButton1.setDisableWhen("#{ mode == 'read' } ");
        xButton1.setName("calculate"); // NOI18N
        xButton1.setVisibleWhen("#{ mode != 'read' && entity.txnmode != 'BEGIN_BALANCE' }");
        xButton1.setImmediate(true);
        xButton1.setText("Calculate");

        xButton2.setDepends(new String[] {"entity.txnmode"});
        xButton2.setDisableWhen("");
        xButton2.setName("lookupSchedule"); // NOI18N
        xButton2.setVisibleWhen("#{ mode != 'read' }");
        xButton2.setImmediate(true);
        xButton2.setText("Add Schedule");

        xFormPanel5.setCaptionWidth(180);

        xComboBox1.setCaption("Txn Type");
        xComboBox1.setItems("txnModes");
        xComboBox1.setName("entity.txnmode"); // NOI18N
        xComboBox1.setVisibleWhen("#{ mode != 'read' }");
        xFormPanel5.add(xComboBox1);

        xLabel11.setCaption("Txn Type");
        xLabel11.setExpression("#{ entity.txnmode }");
        xLabel11.setVisibleWhen("#{ mode == 'read' }");
        xLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel11.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel5.add(xLabel11);

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

        xFormPanel1.setCaptionWidth(150);

        xLabel10.setCaption("Block Schedule");
        xLabel10.setExpression("#{ entity.schedule.scheduleid }");
        xLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel10.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel10);

        xDateField1.setCaption("From Period");
        xDateField1.setDisableWhen("#{ true }");
        xDateField1.setName("entity.schedule.fromperiod"); // NOI18N
        xFormPanel1.add(xDateField1);

        xDateField2.setCaption("To Period");
        xDateField2.setDisableWhen("#{ true }");
        xDateField2.setName("entity.schedule.toperiod"); // NOI18N
        xFormPanel1.add(xDateField2);

        xDateField3.setCaption("Reading Date");
        xDateField3.setDisableWhen("#{ true }");
        xDateField3.setName("entity.schedule.readingdate"); // NOI18N
        xDateField3.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xFormPanel1.add(xDateField3);

        xDateField4.setCaption("Reading Due Date");
        xDateField4.setDisableWhen("#{ true }");
        xDateField4.setName("entity.schedule.readingduedate"); // NOI18N
        xDateField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xDateField4ActionPerformed(evt);
            }
        });
        xFormPanel1.add(xDateField4);

        xDateField5.setCaption("Disc Date");
        xDateField5.setDisableWhen("#{ true }");
        xDateField5.setName("entity.schedule.discdate"); // NOI18N
        xDateField5.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xFormPanel1.add(xDateField5);

        xDateField6.setCaption("Pay Due Date");
        xDateField6.setDisableWhen("#{ true }");
        xDateField6.setName("entity.schedule.duedate"); // NOI18N
        xDateField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xDateField6ActionPerformed(evt);
            }
        });
        xFormPanel1.add(xDateField6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(55, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(xButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        xTabbedPane1.addTab("General Info", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xDecimalField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xDecimalField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xDecimalField2ActionPerformed

    private void xDateField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xDateField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xDateField4ActionPerformed

    private void xDateField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xDateField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xDateField6ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDateField xDateField3;
    private com.rameses.rcp.control.XDateField xDateField4;
    private com.rameses.rcp.control.XDateField xDateField5;
    private com.rameses.rcp.control.XDateField xDateField6;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XIntegerField xIntegerField3;
    private com.rameses.rcp.control.XIntegerField xIntegerField4;
    private com.rameses.rcp.control.XLabel xLabel10;
    private com.rameses.rcp.control.XLabel xLabel11;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
