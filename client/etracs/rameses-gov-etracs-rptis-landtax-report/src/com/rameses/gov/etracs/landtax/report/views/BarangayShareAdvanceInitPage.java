/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.landtax.report.views;

import com.rameses.gov.etracs.landtax.report.*;
import com.rameses.gov.etracs.landtax.report.abstractofcollection.*;
import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

@Template(FormPage.class)
@StyleSheet
public class BarangayShareAdvanceInitPage extends javax.swing.JPanel {

    /**
     * Creates new form AbstractRPTCollectionPage
     */
    public BarangayShareAdvanceInitPage() {
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
        xNumberField2 = new com.rameses.rcp.control.XNumberField();
        xComboBox6 = new com.rameses.rcp.control.XComboBox();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Initial Information");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionWidth(110);

        xNumberField2.setCaption("Advance Year");
        xNumberField2.setDepends(new String[] {"entity.reporttype"});
        xNumberField2.setName("entity.advanceyear"); // NOI18N
        xNumberField2.setPattern("#0000");
        xNumberField2.setPreferredSize(new java.awt.Dimension(125, 20));
        xNumberField2.setRequired(true);
        xFormPanel1.add(xNumberField2);

        xComboBox6.setCaption("Summary Type");
        xComboBox6.setDepends(new String[] {"entity.reporttype"});
        xComboBox6.setExpression("#{item.caption}");
        xComboBox6.setItems("summarytypes");
        xComboBox6.setName("entity.summarytype"); // NOI18N
        xComboBox6.setAllowNull(false);
        xComboBox6.setImmediate(true);
        xComboBox6.setPreferredSize(new java.awt.Dimension(125, 20));
        xComboBox6.setRequired(true);
        xFormPanel1.add(xComboBox6);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(223, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(280, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox6;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XNumberField xNumberField2;
    private com.rameses.rcp.control.XPanel xPanel1;
    // End of variables declaration//GEN-END:variables
}