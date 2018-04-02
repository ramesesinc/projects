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
public class CaptureConsumptionPage extends javax.swing.JPanel {

    /**
     * Creates new form CaptureConsumptionPage
     */
    public CaptureConsumptionPage() {
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
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        monthList1 = new com.rameses.enterprise.components.MonthList();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        xPanel4 = new com.rameses.rcp.control.XPanel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField3 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField4 = new com.rameses.rcp.control.XIntegerField();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Billing Information");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel5.setCaptionWidth(120);

        xIntegerField1.setCaption("Year");
        xIntegerField1.setDisableWhen("#{mode != 'create' }");
        xIntegerField1.setName("entity.year"); // NOI18N
        xIntegerField1.setRequired(true);
        xFormPanel5.add(xIntegerField1);

        monthList1.setCaption("Month");
        monthList1.setDisableWhen("#{ 1 == 1 }");
        monthList1.setName("entity.month"); // NOI18N
        monthList1.setPreferredSize(new java.awt.Dimension(0, 20));
        monthList1.setRequired(true);
        xFormPanel5.add(monthList1);

        xFormPanel1.setCaptionWidth(120);

        xDateField1.setName("entity.readingdate"); // NOI18N
        xDateField1.setCaption("Reading Date");
        xDateField1.setDepends(new String[] {"entity.prevreading", "entity.reading"});
        xFormPanel1.add(xDateField1);

        xDateField2.setName("entity.duedate"); // NOI18N
        xDateField2.setCaption("Due Date");
        xDateField2.setDepends(new String[] {"entity.prevreading", "entity.reading"});
        xFormPanel1.add(xDateField2);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Consumption Information");
        xPanel4.setBorder(xTitledBorder2);

        xFormPanel4.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel4.setCaptionWidth(110);

        xIntegerField2.setCaption("Prev. Reading");
        xIntegerField2.setDepends(new String[] {"entity.reading", "entity.volume"});
        xIntegerField2.setName("entity.prevreading"); // NOI18N
        xIntegerField2.setVisibleWhen("#{ entity.meter?.objid != null }");
        xFormPanel4.add(xIntegerField2);

        xIntegerField3.setCaption("This Reading");
        xIntegerField3.setName("entity.reading"); // NOI18N
        xIntegerField3.setVisibleWhen("#{ entity.meter?.objid != null }");
        xFormPanel4.add(xIntegerField3);

        xIntegerField4.setCaption("Volume");
        xIntegerField4.setName("entity.volume"); // NOI18N
        xFormPanel4.add(xIntegerField4);

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setCaptionWidth(110);

        xDecimalField2.setName("entity.amount"); // NOI18N
        xDecimalField2.setCaption("Amount");
        xDecimalField2.setDepends(new String[] {"entity.prevreading", "entity.reading"});
        xDecimalField2.setDisableWhen("#{ 1==1 }");
        xDecimalField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xDecimalField2ActionPerformed(evt);
            }
        });
        xFormPanel2.add(xDecimalField2);

        xDecimalField3.setCaption("Amount Paid");
        xDecimalField3.setName("entity.amtpaid"); // NOI18N
        xDecimalField3.setRequired(true);
        xFormPanel2.add(xDecimalField3);

        javax.swing.GroupLayout xPanel4Layout = new javax.swing.GroupLayout(xPanel4);
        xPanel4.setLayout(xPanel4Layout);
        xPanel4Layout.setHorizontalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(146, Short.MAX_VALUE))
        );
        xPanel4Layout.setVerticalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(xPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xDecimalField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xDecimalField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xDecimalField2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.enterprise.components.MonthList monthList1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XIntegerField xIntegerField3;
    private com.rameses.rcp.control.XIntegerField xIntegerField4;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel4;
    // End of variables declaration//GEN-END:variables
}
