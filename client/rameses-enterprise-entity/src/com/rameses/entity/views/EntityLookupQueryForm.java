/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.entity.views;

/**
 *
 * @author dell
 */
public class EntityLookupQueryForm extends javax.swing.JPanel {

    /**
     * Creates new form EntityLookupQueryForm
     */
    public EntityLookupQueryForm() {
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

        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 5));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setPadding(new java.awt.Insets(0, 0, 0, 0));

        xComboBox2.setCaption("Type");
        xComboBox2.setCaptionWidth(50);
        xComboBox2.setEmptyText("- - All Types - -");
        xComboBox2.setItems("entityTypes");
        xComboBox2.setName("selectedType"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(100, 20));
        xComboBox2.setVisibleWhen("#{allowSelectEntityType == true}");
        xFormPanel1.add(xComboBox2);

        jPanel1.add(xFormPanel1, java.awt.BorderLayout.NORTH);

        add(jPanel1, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    // End of variables declaration//GEN-END:variables
}