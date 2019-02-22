/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rptis.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

@Template(FormPage.class)
public class ResectionInitPage extends javax.swing.JPanel {

    /**
     * Creates new form ResectionInitPage
     */
    public ResectionInitPage() {
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
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();

        xFormPanel1.setCaptionWidth(130);

        xComboBox1.setCaption("LGU");
        xComboBox1.setExpression("#{item.name}");
        xComboBox1.setItems("lgus");
        xComboBox1.setName("entity.lgu"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xComboBox2.setCaption("Barangay");
        xComboBox2.setDepends(new String[] {"entity.lgu"});
        xComboBox2.setExpression("#{item.name}");
        xComboBox2.setItems("barangays");
        xComboBox2.setName("entity.barangay"); // NOI18N
        xComboBox2.setDynamic(true);
        xComboBox2.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox2.setRequired(true);
        xFormPanel1.add(xComboBox2);

        xComboBox3.setCaption("PIN Type");
        xComboBox3.setItems("pintypes");
        xComboBox3.setName("entity.pintype"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(100, 20));
        xComboBox3.setRequired(true);
        xFormPanel1.add(xComboBox3);

        xTextField1.setCaption("Section");
        xTextField1.setName("entity.section"); // NOI18N
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 100));

        xTextArea1.setCaption("Default Memoranda");
        xTextArea1.setLineWrap(true);
        xTextArea1.setName("entity.memoranda"); // NOI18N
        xTextArea1.setWrapStyleWord(true);
        xTextArea1.setCellPadding(new java.awt.Insets(20, 0, 0, 0));
        xTextArea1.setRequired(true);
        xTextArea1.setTextCase(com.rameses.rcp.constant.TextCase.UPPER);
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(111, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}