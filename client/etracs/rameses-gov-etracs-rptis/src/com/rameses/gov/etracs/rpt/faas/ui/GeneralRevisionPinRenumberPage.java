/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rpt.faas.ui;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

@StyleSheet
@Template(FormPage.class)
public class GeneralRevisionPinRenumberPage extends javax.swing.JPanel {

    /**
     * Creates new form GeneralRevisionRepinPage2
     */
    public GeneralRevisionPinRenumberPage() {
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

        xPanel2 = new com.rameses.rcp.control.XPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xComboBox4 = new com.rameses.rcp.control.XComboBox();
        xComboBox5 = new com.rameses.rcp.control.XComboBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();

        xPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        xPanel2.setVisibleWhen("#{mode == 'processing'}");
        xPanel2.setLayout(new java.awt.BorderLayout());

        xLabel3.setFontStyle("font-weight:bold;font-size:12;");
        xLabel3.setForeground(new java.awt.Color(51, 51, 51));
        xLabel3.setPadding(new java.awt.Insets(1, 5, 1, 1));
        xLabel3.setPreferredSize(new java.awt.Dimension(150, 20));
        xLabel3.setText("Processing request please wait...");
        xPanel2.add(xLabel3, java.awt.BorderLayout.CENTER);

        xLabel4.setIconResource("com/rameses/rcp/icons/loading16.gif");
        xPanel2.add(xLabel4, java.awt.BorderLayout.WEST);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Initial Information");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionWidth(100);
        xFormPanel1.setPadding(new java.awt.Insets(5, 5, 5, 5));

        xIntegerField1.setCaption("Revision Year");
        xIntegerField1.setEnabled(false);
        xIntegerField1.setName("entity.ry"); // NOI18N
        xIntegerField1.setRequired(true);
        xFormPanel1.add(xIntegerField1);

        xComboBox4.setAllowNull(false);
        xComboBox4.setCaption("LGU");
        xComboBox4.setDepends(new String[] {"entity.rputype", "entity.lgu"});
        xComboBox4.setExpression("#{item.name}");
        xComboBox4.setItems("lgus");
        xComboBox4.setName("entity.lgu"); // NOI18N
        xComboBox4.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox4.setRequired(true);
        xFormPanel1.add(xComboBox4);

        xComboBox5.setAllowNull(false);
        xComboBox5.setCaption("Barangay");
        xComboBox5.setDepends(new String[] {"entity.lgu"});
        xComboBox5.setDynamic(true);
        xComboBox5.setExpression("#{item.name}");
        xComboBox5.setItems("barangays");
        xComboBox5.setName("entity.barangay"); // NOI18N
        xComboBox5.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox5.setRequired(true);
        xFormPanel1.add(xComboBox5);

        xTextField1.setCaption("Section");
        xTextField1.setName("entity.section"); // NOI18N
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        xCheckBox1.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xCheckBox1.setName("entity.approvefaas"); // NOI18N
        xCheckBox1.setShowCaption(false);
        xCheckBox1.setText("Approve FAAS Automatically?");
        xFormPanel1.add(xCheckBox1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Logs");
        jPanel2.setBorder(xTitledBorder2);
        jPanel2.setLayout(new java.awt.BorderLayout());

        xTextArea1.setLineWrap(true);
        xTextArea1.setWrapStyleWord(true);
        xTextArea1.setName("logs"); // NOI18N
        xTextArea1.setReadonly(true);
        jScrollPane1.setViewportView(xTextArea1);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(288, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XComboBox xComboBox4;
    private com.rameses.rcp.control.XComboBox xComboBox5;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XPanel xPanel2;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}