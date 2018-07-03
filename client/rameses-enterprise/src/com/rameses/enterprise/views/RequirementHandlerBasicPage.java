/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.views;

import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author dell
 */
@Template(RequirementHandlerTemplate.class)
public class RequirementHandlerBasicPage extends javax.swing.JPanel {

    /**
     * Creates new form BasicRequirementHandlerPage
     */
    public RequirementHandlerBasicPage() {
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
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setCaptionWidth(100);

        xTextField1.setCaption("Ref No");
        xTextField1.setName("entry.refno"); // NOI18N
        xFormPanel1.add(xTextField1);

        xDateField1.setCaption("Date Issued");
        xDateField1.setName("entry.dtissued"); // NOI18N
        xFormPanel1.add(xDateField1);

        xTextField2.setCaption("Remarks");
        xTextField2.setName("entry.remarks"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xTextField2);

        xCheckBox1.setCaption(" ");
        xCheckBox1.setCheckValue(1);
        xCheckBox1.setName("entry.complied"); // NOI18N
        xCheckBox1.setUncheckValue(0);
        xCheckBox1.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox1.setText("Complied?");
        xFormPanel1.add(xCheckBox1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(114, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(195, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}
