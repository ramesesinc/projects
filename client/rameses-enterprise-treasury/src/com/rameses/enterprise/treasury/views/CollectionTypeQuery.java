/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.treasury.views;

/**
 *
 * @author dell
 */
public class CollectionTypeQuery extends javax.swing.JPanel {

    /**
     * Creates new form CollectionTypeQuery
     */
    public CollectionTypeQuery() {
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
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();

        xFormPanel1.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xFormPanel1.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel1.setPadding(new java.awt.Insets(5, 5, 5, 5));

        xComboBox2.setCaption("Org Type");
        xComboBox2.setCaptionWidth(80);
        xComboBox2.setExpression("#{ item.name }");
        xComboBox2.setItems("orgTypes");
        xComboBox2.setName("orgType"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel1.add(xComboBox2);

        xComboBox3.setCaption("Org Name");
        xComboBox3.setCaptionWidth(80);
        xComboBox3.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xComboBox3.setDepends(new String[] {"orgType"});
        xComboBox3.setDynamic(true);
        xComboBox3.setExpression("#{item.name}");
        xComboBox3.setItems("orgList");
        xComboBox3.setName("org"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xComboBox3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    // End of variables declaration//GEN-END:variables
}
