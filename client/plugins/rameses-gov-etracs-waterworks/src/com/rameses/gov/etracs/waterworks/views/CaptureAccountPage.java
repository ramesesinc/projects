/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author Rameses
 */
@Template(FormPage.class)
@StyleSheet
public class CaptureAccountPage extends javax.swing.JPanel {

    /**
     * Creates new form CaptureAccountPage
     */
    public CaptureAccountPage() {
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
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xPanel3 = new com.rameses.rcp.control.XPanel();
        localAddressPanel1 = new com.rameses.etracs.common.LocalAddressPanel();
        xPanel4 = new com.rameses.rcp.control.XPanel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xLookupField3 = new com.rameses.rcp.control.XLookupField();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField3 = new com.rameses.rcp.control.XIntegerField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        xPanel5 = new com.rameses.rcp.control.XPanel();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xDateField3 = new com.rameses.rcp.control.XDateField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Account Information");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionWidth(140);

        xTextField1.setCaption("Account No.");
        xTextField1.setName("entity.acctno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(250, 20));
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        xLookupField1.setCaption("Owner Name");
        xLookupField1.setExpression("#{entity.owner.name}");
        xLookupField1.setHandler("entity:lookup");
        xLookupField1.setName("entity.owner"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xTextField2.setCaption("Account Name");
        xTextField2.setName("entity.acctname"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        xComboBox1.setCaption("Classification");
        xComboBox1.setExpression("#{item.objid}");
        xComboBox1.setItemKey("objid");
        xComboBox1.setItems("classifications");
        xComboBox1.setName("entity.classificationid"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xComboBox1);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Billing Information");
        xPanel3.setBorder(xTitledBorder2);

        localAddressPanel1.setName("entity.address"); // NOI18N

        javax.swing.GroupLayout xPanel3Layout = new javax.swing.GroupLayout(xPanel3);
        xPanel3.setLayout(xPanel3Layout);
        xPanel3Layout.setHorizontalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(localAddressPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        xPanel3Layout.setVerticalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel3Layout.createSequentialGroup()
                .addComponent(localAddressPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("Meter Information");
        xPanel4.setBorder(xTitledBorder3);

        xFormPanel4.setCaptionWidth(100);

        xLookupField3.setCaption("Serial No.");
        xLookupField3.setExpression("#{entity.meter.serialno}");
        xLookupField3.setHandler("waterworks_meter_unused:lookup");
        xLookupField3.setName("entity.meter"); // NOI18N
        xLookupField3.setPreferredSize(new java.awt.Dimension(150, 20));
        xLookupField3.setRequired(true);
        xFormPanel4.add(xLookupField3);

        xIntegerField1.setCaption("Last Reading");
        xIntegerField1.setName("entity.lastreading"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(150, 20));
        xIntegerField1.setRequired(true);
        xFormPanel4.add(xIntegerField1);

        xIntegerField2.setCaption("Prev. Reading");
        xIntegerField2.setName("entity.prevreading"); // NOI18N
        xIntegerField2.setPreferredSize(new java.awt.Dimension(150, 20));
        xIntegerField2.setRequired(true);
        xFormPanel4.add(xIntegerField2);

        xFormPanel6.setCaptionWidth(120);

        xIntegerField3.setCaption("Last Reading Year");
        xIntegerField3.setName("entity.lastreadingyear"); // NOI18N
        xIntegerField3.setPreferredSize(new java.awt.Dimension(150, 20));
        xIntegerField3.setRequired(true);
        xFormPanel6.add(xIntegerField3);

        xComboBox2.setCaption("Last Reading Month");
        xComboBox2.setExpression("#{item.name}");
        xComboBox2.setItemKey("id");
        xComboBox2.setItems("months");
        xComboBox2.setName("entity.lastreadingmonth"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(150, 20));
        xComboBox2.setRequired(true);
        xFormPanel6.add(xComboBox2);

        xDateField2.setCaption("Last Reading Date");
        xDateField2.setName("entity.lastreadingdate"); // NOI18N
        xDateField2.setPreferredSize(new java.awt.Dimension(150, 20));
        xDateField2.setRequired(true);
        xFormPanel6.add(xDateField2);

        javax.swing.GroupLayout xPanel4Layout = new javax.swing.GroupLayout(xPanel4);
        xPanel4.setLayout(xPanel4Layout);
        xPanel4Layout.setHorizontalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(xFormPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        xPanel4Layout.setVerticalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder4 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder4.setTitle("Other Details");
        xPanel5.setBorder(xTitledBorder4);

        xFormPanel5.setCaptionWidth(140);

        xDateField3.setCaption("Date Started");
        xDateField3.setName("entity.dtstarted"); // NOI18N
        xDateField3.setPreferredSize(new java.awt.Dimension(150, 20));
        xDateField3.setRequired(true);
        xFormPanel5.add(xDateField3);

        xDateField1.setCaption("Last Transaction Date");
        xDateField1.setName("entity.lasttxndate"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(150, 20));
        xDateField1.setRequired(true);
        xFormPanel5.add(xDateField1);

        xDecimalField1.setCaption("Remaining Balance");
        xDecimalField1.setName("entity.balance"); // NOI18N
        xDecimalField1.setPreferredSize(new java.awt.Dimension(150, 20));
        xDecimalField1.setRequired(true);
        xFormPanel5.add(xDecimalField1);

        javax.swing.GroupLayout xPanel5Layout = new javax.swing.GroupLayout(xPanel5);
        xPanel5.setLayout(xPanel5Layout);
        xPanel5Layout.setHorizontalGroup(
            xPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(320, Short.MAX_VALUE))
        );
        xPanel5Layout.setVerticalGroup(
            xPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xPanel5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.etracs.common.LocalAddressPanel localAddressPanel1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDateField xDateField3;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XIntegerField xIntegerField3;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XLookupField xLookupField3;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel3;
    private com.rameses.rcp.control.XPanel xPanel4;
    private com.rameses.rcp.control.XPanel xPanel5;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}
