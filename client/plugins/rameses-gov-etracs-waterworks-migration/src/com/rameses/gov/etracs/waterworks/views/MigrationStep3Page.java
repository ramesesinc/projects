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
 * @author ramesesinc
 */
@StyleSheet
@Template(FormPage.class)
public class MigrationStep3Page extends javax.swing.JPanel {

    /**
     * Creates new form MigrationStep3Page
     */
    public MigrationStep3Page() {
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

        jPanel3 = new javax.swing.JPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();

        jPanel3.setLayout(new java.awt.BorderLayout());

        xLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 10));
        xLabel3.setFontStyle("font-size:12; font-weight:bold;");
        xLabel3.setForeground(new java.awt.Color(100, 100, 100));
        xLabel3.setText("General Information");
        jPanel3.add(xLabel3, java.awt.BorderLayout.NORTH);

        xFormPanel1.setPadding(new java.awt.Insets(10, 20, 10, 10));

        xTextField1.setCaption("Title");
        xTextField1.setName("entity.title"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 21));
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 63));

        xTextArea1.setCaption("Remarks");
        xTextArea1.setName("entity.remarks"); // NOI18N
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        xTextField2.setCaption("Posted By");
        xTextField2.setName("entity.createdby.name"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 21));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        xDateField1.setCaption("Date Posted");
        xDateField1.setName("entity.dtcreated"); // NOI18N
        xDateField1.setOutputFormat("yyyy-MM-dd");
        xDateField1.setPreferredSize(new java.awt.Dimension(100, 21));
        xFormPanel1.add(xDateField1);

        xIntegerField1.setCaption("Total Rows");
        xIntegerField1.setName("entity.totalrows"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(100, 21));
        xFormPanel1.add(xIntegerField1);

        jPanel3.add(xFormPanel1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(243, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}
