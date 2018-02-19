/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author dell
 */
@Template(OKCancelPage.class)
public class PayOption extends javax.swing.JPanel {

    /**
     * Creates new form PayOption
     */
    public PayOption() {
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
        xRadio1 = new com.rameses.rcp.control.XRadio();
        xRadio2 = new com.rameses.rcp.control.XRadio();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xRadio3 = new com.rameses.rcp.control.XRadio();

        xRadio1.setName("payOption"); // NOI18N
        xRadio1.setOptionValue("FULL");
        xRadio1.setShowCaption(false);
        xRadio1.setText("Full");
        xFormPanel1.add(xRadio1);

        xRadio2.setName("payOption"); // NOI18N
        xRadio2.setOptionValue("MONTH");
        xRadio2.setShowCaption(false);
        xRadio2.setText("Up to Month");
        xFormPanel1.add(xRadio2);

        xComboBox1.setCaption("");
        xComboBox1.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xComboBox1.setDepends(new String[] {"payOption"});
        xComboBox1.setExpression("#{item.year} - #{item.monthname}");
        xComboBox1.setItems("monthList");
        xComboBox1.setName("month"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 20));
        xComboBox1.setShowCaption(false);
        xComboBox1.setVisibleWhen("#{payOption == 'MONTH'}");
        xFormPanel1.add(xComboBox1);

        xRadio3.setEnabled(false);
        xRadio3.setName("payOption"); // NOI18N
        xRadio3.setOptionValue("PARTIAL");
        xRadio3.setShowCaption(false);
        xRadio3.setText("Specify Amount");
        xFormPanel1.add(xRadio3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XRadio xRadio1;
    private com.rameses.rcp.control.XRadio xRadio2;
    private com.rameses.rcp.control.XRadio xRadio3;
    // End of variables declaration//GEN-END:variables
}