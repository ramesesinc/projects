/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.clfc.treasury.ledger.amnesty.fix;

import com.rameses.rcp.ui.annotations.StyleSheet;

/**
 *
 * @author louie
 */

@StyleSheet
public class FixTermPage extends javax.swing.JPanel {

    /**
     * Creates new form FixTermPage
     */
    public FixTermPage() {
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
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();

        xFormPanel1.setPadding(new java.awt.Insets(0, 0, 5, 5));

        xDecimalField1.setCaption("Amount");
        xDecimalField1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField1.setName("entity.amount"); // NOI18N
        xFormPanel1.add(xDecimalField1);

        xCheckBox1.setCaption("Use Date?");
        xCheckBox1.setCheckValue(1);
        xCheckBox1.setName("entity.usedate"); // NOI18N
        xCheckBox1.setUncheckValue(0);
        xFormPanel1.add(xCheckBox1);

        xDateField1.setCaption("Date");
        xDateField1.setDepends(new String[] {"entity.usedate"});
        xDateField1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDateField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xDateField1.setName("entity.date"); // NOI18N
        xDateField1.setOutputFormat("MMM-dd-yyyy");
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Days");
        xComboBox1.setDepends(new String[] {"entity.usedate"});
        xComboBox1.setDynamic(true);
        xComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xComboBox1.setItems("daysList");
        xComboBox1.setName("entity.days"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(100, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xIntegerField1.setCaption("Months");
        xIntegerField1.setDepends(new String[] {"entity.usedate"});
        xIntegerField1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xIntegerField1.setName("entity.months"); // NOI18N
        xIntegerField1.setPattern("#,##0");
        xIntegerField1.setRequired(true);
        xFormPanel1.add(xIntegerField1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    // End of variables declaration//GEN-END:variables
}
