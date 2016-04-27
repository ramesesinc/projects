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
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField3 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField4 = new com.rameses.rcp.control.XIntegerField();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        xButton1 = new com.rameses.rcp.control.XButton();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDateField2 = new com.rameses.rcp.control.XDateField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Consumption Information");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionWidth(150);

        xDateField1.setCaption("Reading Date");
        xDateField1.setName("info.dtreading"); // NOI18N
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        xIntegerField1.setCaption("Year");
        xIntegerField1.setName("info.year"); // NOI18N
        xIntegerField1.setRequired(true);
        xFormPanel1.add(xIntegerField1);

        xComboBox1.setCaption("Month");
        xComboBox1.setExpression("#{item.name}");
        xComboBox1.setItemKey("index");
        xComboBox1.setItems("listTypes.months");
        xComboBox1.setName("info.month"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(100, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xIntegerField2.setCaption("Prev. Reading");
        xIntegerField2.setDepends(new String[] {"info.reading", "info.volume"});
        xIntegerField2.setName("info.prevreading"); // NOI18N
        xIntegerField2.setRequired(true);
        xFormPanel1.add(xIntegerField2);

        xIntegerField3.setCaption("This Reading");
        xIntegerField3.setName("info.reading"); // NOI18N
        xIntegerField3.setRequired(true);
        xFormPanel1.add(xIntegerField3);

        xIntegerField4.setCaption("Volume");
        xIntegerField4.setDepends(new String[] {"info.prevreading", "info.reading"});
        xIntegerField4.setName("info.volume"); // NOI18N
        xFormPanel1.add(xIntegerField4);

        xCheckBox1.setName("info.postledger"); // NOI18N
        xCheckBox1.setText("Post To Ledger");

        xPanel2.setDepends(new String[] {"info.postledger"});
        xPanel2.setVisibleWhen("#{info.postledger == true}");

        xButton1.setImmediate(true);
        xButton1.setName("computeAmount"); // NOI18N
        xButton1.setText("Compute Amount");

        xLabel1.setCaption("Account");
        xLabel1.setExpression("#{info.item.title}");
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel2.add(xLabel1);

        xDecimalField2.setCaption("Amount");
        xDecimalField2.setEnabled(false);
        xDecimalField2.setName("info.amount"); // NOI18N
        xFormPanel2.add(xDecimalField2);

        xDateField2.setCaption("Due Date");
        xDateField2.setName("info.duedate"); // NOI18N
        xDateField2.setRequired(true);
        xFormPanel2.add(xDateField2);

        javax.swing.GroupLayout xPanel2Layout = new javax.swing.GroupLayout(xPanel2);
        xPanel2.setLayout(xPanel2Layout);
        xPanel2Layout.setHorizontalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        xPanel2Layout.setVerticalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addGroup(xPanel1Layout.createSequentialGroup()
                        .addGroup(xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(xCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                            .addComponent(xPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(xCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
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
                .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XIntegerField xIntegerField3;
    private com.rameses.rcp.control.XIntegerField xIntegerField4;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel2;
    // End of variables declaration//GEN-END:variables
}
