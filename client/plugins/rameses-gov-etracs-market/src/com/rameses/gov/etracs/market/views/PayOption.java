/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.market.views;

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
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xRadio4 = new com.rameses.rcp.control.XRadio();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xRadio3 = new com.rameses.rcp.control.XRadio();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xRadio5 = new com.rameses.rcp.control.XRadio();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xRadio6 = new com.rameses.rcp.control.XRadio();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();

        xRadio1.setName("payOption"); // NOI18N
        xRadio1.setOptionValue("FULL");
        xRadio1.setShowCaption(false);
        xRadio1.setText("Full");
        xFormPanel1.add(xRadio1);

        xRadio2.setCaption("Up to date");
        xRadio2.setName("payOption"); // NOI18N
        xRadio2.setOptionValue("DATE");
        xRadio2.setShowCaption(false);
        xRadio2.setText("Up to date");
        xFormPanel1.add(xRadio2);

        xDateField1.setCaption("Enter Until Date");
        xDateField1.setCaptionWidth(90);
        xDateField1.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xDateField1.setDepends(new String[] {"payOption"});
        xDateField1.setName("date"); // NOI18N
        xDateField1.setVisibleWhen("#{payOption == 'DATE' }");
        xFormPanel1.add(xDateField1);

        xRadio4.setCaption("Up to date");
        xRadio4.setName("payOption"); // NOI18N
        xRadio4.setOptionValue("NUMDAYS");
        xRadio4.setShowCaption(false);
        xRadio4.setText("No. of days");
        xFormPanel1.add(xRadio4);

        xIntegerField1.setCaption("No. of days");
        xIntegerField1.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xIntegerField1.setDepends(new String[] {"payOption"});
        xIntegerField1.setName("numdays"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(40, 20));
        xIntegerField1.setVisibleWhen("#{payOption == 'NUMDAYS' }");
        xFormPanel1.add(xIntegerField1);

        xRadio3.setName("payOption"); // NOI18N
        xRadio3.setOptionValue("SPECIFYAMOUNT");
        xRadio3.setShowCaption(false);
        xRadio3.setText("Specify Amount");
        xRadio3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xRadio3ActionPerformed(evt);
            }
        });
        xFormPanel1.add(xRadio3);

        xDecimalField1.setCaption("Amount");
        xDecimalField1.setCaptionWidth(60);
        xDecimalField1.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xDecimalField1.setDepends(new String[] {"payOption"});
        xDecimalField1.setName("amount"); // NOI18N
        xDecimalField1.setVisibleWhen("#{payOption == 'SPECIFYAMOUNT' }");
        xFormPanel1.add(xDecimalField1);

        xRadio5.setCaption("Up to date");
        xRadio5.setName("payOption"); // NOI18N
        xRadio5.setOptionValue("MONTH");
        xRadio5.setShowCaption(false);
        xRadio5.setText("Up to Month");
        xFormPanel1.add(xRadio5);

        xComboBox1.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xComboBox1.setDepends(new String[] {"payOption"});
        xComboBox1.setExpression("#{item.monthname} #{item.year}");
        xComboBox1.setItems("monthList");
        xComboBox1.setName("monthyear"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 20));
        xComboBox1.setShowCaption(false);
        xComboBox1.setVisibleWhen("#{payOption == 'MONTH' }");
        xFormPanel1.add(xComboBox1);

        xRadio6.setCaption("Up to date");
        xRadio6.setName("payOption"); // NOI18N
        xRadio6.setOptionValue("QTR");
        xRadio6.setShowCaption(false);
        xRadio6.setText("Up to Qtr");
        xFormPanel1.add(xRadio6);

        xComboBox2.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xComboBox2.setDepends(new String[] {"payOption"});
        xComboBox2.setItems("qtrList");
        xComboBox2.setName("qtr"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(150, 20));
        xComboBox2.setShowCaption(false);
        xComboBox2.setVisibleWhen("#{payOption == 'QTR' }");
        xFormPanel1.add(xComboBox2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xRadio3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xRadio3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xRadio3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XRadio xRadio1;
    private com.rameses.rcp.control.XRadio xRadio2;
    private com.rameses.rcp.control.XRadio xRadio3;
    private com.rameses.rcp.control.XRadio xRadio4;
    private com.rameses.rcp.control.XRadio xRadio5;
    private com.rameses.rcp.control.XRadio xRadio6;
    // End of variables declaration//GEN-END:variables
}
