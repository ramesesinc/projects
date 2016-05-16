/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rpt.faas.change.ui;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

@StyleSheet
@Template(ChangeInfoPage.class)
public class ChangeFaasInfoPage extends javax.swing.JPanel {

    /**
     * Creates new form ChangeFaasInfoPage
     */
    public ChangeFaasInfoPage() {
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        formPanel5 = new com.rameses.rcp.util.FormPanel();
        xTextField17 = new com.rameses.rcp.control.XTextField();
        xTextField18 = new com.rameses.rcp.control.XTextField();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField5 = new com.rameses.rcp.control.XIntegerField();
        xComboBox12 = new com.rameses.rcp.control.XComboBox();
        xSeparator1 = new com.rameses.rcp.control.XSeparator();
        xComboBox5 = new com.rameses.rcp.control.XComboBox();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        xTextArea2 = new com.rameses.rcp.control.XTextArea();
        jPanel2 = new javax.swing.JPanel();
        formPanel6 = new com.rameses.rcp.util.FormPanel();
        xTextField19 = new com.rameses.rcp.control.XTextField();
        xTextField20 = new com.rameses.rcp.control.XTextField();
        xDateField3 = new com.rameses.rcp.control.XDateField();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xIntegerField4 = new com.rameses.rcp.control.XIntegerField();
        xComboBox11 = new com.rameses.rcp.control.XComboBox();
        xSeparator2 = new com.rameses.rcp.control.XSeparator();
        xComboBox6 = new com.rameses.rcp.control.XComboBox();
        xComboBox4 = new com.rameses.rcp.control.XComboBox();
        xCheckBox2 = new com.rameses.rcp.control.XCheckBox();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        xTextArea3 = new com.rameses.rcp.control.XTextArea();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("FAAS and RPU Information");
        formPanel5.setBorder(xTitledBorder1);
        formPanel5.setCaptionWidth(120);

        xTextField17.setCaption("ARP/TD No.");
        xTextField17.setName("changeinfo.newinfo.tdno"); // NOI18N
        xTextField17.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xTextField17);

        xTextField18.setCaption("Title No.");
        xTextField18.setName("changeinfo.newinfo.titleno"); // NOI18N
        xTextField18.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xTextField18);

        xDateField2.setCaption("Title Date");
        xDateField2.setName("changeinfo.newinfo.titledate"); // NOI18N
        xDateField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xDateField2.setStretchWidth(20);
        formPanel5.add(xDateField2);

        xComboBox2.setCaption("Restriction");
        xComboBox2.setCellPadding(new java.awt.Insets(2, 0, 0, 0));
        xComboBox2.setItems("restrictions");
        xComboBox2.setName("changeinfo.newinfo.restrictionid"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox2.setStretchWidth(100);
        formPanel5.add(xComboBox2);

        xFormPanel2.setCaptionWidth(120);
        xFormPanel2.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel2.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel2.setPreferredSize(new java.awt.Dimension(0, 21));
        xFormPanel2.setShowCaption(false);

        xIntegerField5.setCaption("Effectivity Year");
        xIntegerField5.setName("changeinfo.newinfo.effectivityyear"); // NOI18N
        xIntegerField5.setRequired(true);
        xIntegerField5.setStretchWidth(60);
        xFormPanel2.add(xIntegerField5);

        xComboBox12.setCaption("Qtr");
        xComboBox12.setCaptionWidth(50);
        xComboBox12.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xComboBox12.setDepends(new String[] {"rpu.taxable"});
        xComboBox12.setItems("quarters");
        xComboBox12.setName("changeinfo.newinfo.effectivityqtr"); // NOI18N
        xComboBox12.setOpaque(false);
        xComboBox12.setPreferredSize(new java.awt.Dimension(100, 20));
        xComboBox12.setRequired(true);
        xComboBox12.setStretchWidth(40);
        xFormPanel2.add(xComboBox12);

        formPanel5.add(xFormPanel2);

        xSeparator1.setPreferredSize(new java.awt.Dimension(0, 10));

        javax.swing.GroupLayout xSeparator1Layout = new javax.swing.GroupLayout(xSeparator1);
        xSeparator1.setLayout(xSeparator1Layout);
        xSeparator1Layout.setHorizontalGroup(
            xSeparator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );
        xSeparator1Layout.setVerticalGroup(
            xSeparator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        formPanel5.add(xSeparator1);

        xComboBox5.setCaption("Transaction Type");
        xComboBox5.setExpression("#{item.name}");
        xComboBox5.setItems("txntypes");
        xComboBox5.setName("changeinfo.newinfo.txntype"); // NOI18N
        xComboBox5.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox5.setRequired(true);
        formPanel5.add(xComboBox5);

        xComboBox1.setCaption("Classification");
        xComboBox1.setExpression("#{item.name}");
        xComboBox1.setItems("classifications");
        xComboBox1.setName("changeinfo.newinfo.classification"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox1.setRequired(true);
        formPanel5.add(xComboBox1);

        xCheckBox1.setCellPadding(new java.awt.Insets(0, 120, 0, 0));
        xCheckBox1.setName("changeinfo.newinfo.publicland"); // NOI18N
        xCheckBox1.setShowCaption(false);
        xCheckBox1.setText("Is Public Land?");
        xCheckBox1.setVisibleWhen("#{changeinfo.newinfo.rputype=='land'}");
        formPanel5.add(xCheckBox1);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Memoranda");
        xTitledBorder2.setTitlePadding(new java.awt.Insets(2, 4, 0, 4));
        jScrollPane2.setBorder(xTitledBorder2);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(0, 83));

        xTextArea2.setLineWrap(true);
        xTextArea2.setWrapStyleWord(true);
        xTextArea2.setCaption("Memoranda");
        xTextArea2.setCaptionWidth(90);
        xTextArea2.setHint("Memoranda");
        xTextArea2.setName("changeinfo.newinfo.memoranda"); // NOI18N
        xTextArea2.setRequired(true);
        xTextArea2.setShowCaption(false);
        jScrollPane2.setViewportView(xTextArea2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                    .addComponent(formPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Modified Information", jPanel1);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("FAAS Information");
        formPanel6.setBorder(xTitledBorder3);
        formPanel6.setCaptionWidth(120);

        xTextField19.setCaption("ARP/TD No.");
        xTextField19.setEnabled(false);
        xTextField19.setName("changeinfo.previnfo.tdno"); // NOI18N
        xTextField19.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel6.add(xTextField19);

        xTextField20.setCaption("Title No.");
        xTextField20.setEnabled(false);
        xTextField20.setName("changeinfo.previnfo.titleno"); // NOI18N
        xTextField20.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel6.add(xTextField20);

        xDateField3.setCaption("Title Date");
        xDateField3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDateField3.setEnabled(false);
        xDateField3.setName("changeinfo.previnfo.titledate"); // NOI18N
        xDateField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xDateField3.setStretchWidth(20);
        formPanel6.add(xDateField3);

        xComboBox3.setCaption("Restriction");
        xComboBox3.setCellPadding(new java.awt.Insets(2, 0, 0, 0));
        xComboBox3.setEnabled(false);
        xComboBox3.setItems("restrictions");
        xComboBox3.setName("changeinfo.previnfo.restrictionid"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox3.setStretchWidth(100);
        formPanel6.add(xComboBox3);

        xIntegerField4.setCaption("Effectivity Year");
        xIntegerField4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xIntegerField4.setEnabled(false);
        xIntegerField4.setName("changeinfo.previnfo.effectivityyear"); // NOI18N
        xIntegerField4.setStretchWidth(60);
        formPanel6.add(xIntegerField4);

        xComboBox11.setCaption("Qtr");
        xComboBox11.setDepends(new String[] {"rpu.taxable"});
        xComboBox11.setEnabled(false);
        xComboBox11.setItems("quarters");
        xComboBox11.setName("changeinfo.previnfo.effectivityqtr"); // NOI18N
        xComboBox11.setOpaque(false);
        xComboBox11.setPreferredSize(new java.awt.Dimension(100, 20));
        xComboBox11.setStretchWidth(40);
        formPanel6.add(xComboBox11);

        xSeparator2.setPreferredSize(new java.awt.Dimension(0, 10));

        javax.swing.GroupLayout xSeparator2Layout = new javax.swing.GroupLayout(xSeparator2);
        xSeparator2.setLayout(xSeparator2Layout);
        xSeparator2Layout.setHorizontalGroup(
            xSeparator2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );
        xSeparator2Layout.setVerticalGroup(
            xSeparator2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        formPanel6.add(xSeparator2);

        xComboBox6.setCaption("Transaction Type");
        xComboBox6.setEnabled(false);
        xComboBox6.setExpression("#{item.name}");
        xComboBox6.setItems("txntypes");
        xComboBox6.setName("changeinfo.previnfo.txntype"); // NOI18N
        xComboBox6.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel6.add(xComboBox6);

        xComboBox4.setCaption("Classification");
        xComboBox4.setEnabled(false);
        xComboBox4.setExpression("#{item.name}");
        xComboBox4.setItems("classifications");
        xComboBox4.setName("changeinfo.previnfo.classification"); // NOI18N
        xComboBox4.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel6.add(xComboBox4);

        xCheckBox2.setCellPadding(new java.awt.Insets(0, 120, 0, 0));
        xCheckBox2.setEnabled(false);
        xCheckBox2.setName("changeinfo.previnfo.publicland"); // NOI18N
        xCheckBox2.setShowCaption(false);
        xCheckBox2.setText("Is Public Land?");
        formPanel6.add(xCheckBox2);
        formPanel6.add(xFormPanel1);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder4 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder4.setTitle("Memoranda");
        xTitledBorder4.setTitlePadding(new java.awt.Insets(2, 4, 0, 4));
        jScrollPane3.setBorder(xTitledBorder4);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(0, 83));

        xTextArea3.setLineWrap(true);
        xTextArea3.setWrapStyleWord(true);
        xTextArea3.setCaption("Memoranda");
        xTextArea3.setCaptionWidth(90);
        xTextArea3.setEnabled(false);
        xTextArea3.setHint("Memoranda");
        xTextArea3.setName("changeinfo.previnfo.memoranda"); // NOI18N
        xTextArea3.setRequired(true);
        xTextArea3.setShowCaption(false);
        jScrollPane3.setViewportView(xTextArea3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(formPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Previous Information", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel5;
    private com.rameses.rcp.util.FormPanel formPanel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XCheckBox xCheckBox2;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox11;
    private com.rameses.rcp.control.XComboBox xComboBox12;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XComboBox xComboBox4;
    private com.rameses.rcp.control.XComboBox xComboBox5;
    private com.rameses.rcp.control.XComboBox xComboBox6;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDateField xDateField3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XIntegerField xIntegerField4;
    private com.rameses.rcp.control.XIntegerField xIntegerField5;
    private com.rameses.rcp.control.XSeparator xSeparator1;
    private com.rameses.rcp.control.XSeparator xSeparator2;
    private com.rameses.rcp.control.XTextArea xTextArea2;
    private com.rameses.rcp.control.XTextArea xTextArea3;
    private com.rameses.rcp.control.XTextField xTextField17;
    private com.rameses.rcp.control.XTextField xTextField18;
    private com.rameses.rcp.control.XTextField xTextField19;
    private com.rameses.rcp.control.XTextField xTextField20;
    // End of variables declaration//GEN-END:variables
}
