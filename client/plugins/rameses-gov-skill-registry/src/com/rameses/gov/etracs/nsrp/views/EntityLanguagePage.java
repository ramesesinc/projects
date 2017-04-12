/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.nsrp.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Rameses
 */
@Template(CrudFormPage.class)
public class EntityLanguagePage extends javax.swing.JPanel {

    /**
     * Creates new form EntityEducationPage
     */
    public EntityLanguagePage() {
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
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xCheckBox2 = new com.rameses.rcp.control.XCheckBox();
        xCheckBox3 = new com.rameses.rcp.control.XCheckBox();
        xCheckBox4 = new com.rameses.rcp.control.XCheckBox();

        xFormPanel1.setCaptionWidth(120);

        xComboBox1.setCaption("Language");
        xComboBox1.setExpression("#{item.name}");
        xComboBox1.setItemKey("name");
        xComboBox1.setItems("languages");
        xComboBox1.setName("entity.language"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xCheckBox1.setCaption("Can Read ?");
        xCheckBox1.setName("entity.read"); // NOI18N
        xFormPanel1.add(xCheckBox1);

        xCheckBox2.setCaption("Can Write ?");
        xCheckBox2.setName("entity.write"); // NOI18N
        xFormPanel1.add(xCheckBox2);

        xCheckBox3.setCaption("Can Speak ?");
        xCheckBox3.setName("entity.speak"); // NOI18N
        xFormPanel1.add(xCheckBox3);

        xCheckBox4.setCaption("Can Understand ?");
        xCheckBox4.setName("entity.understand"); // NOI18N
        xFormPanel1.add(xCheckBox4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XCheckBox xCheckBox2;
    private com.rameses.rcp.control.XCheckBox xCheckBox3;
    private com.rameses.rcp.control.XCheckBox xCheckBox4;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    // End of variables declaration//GEN-END:variables
}
