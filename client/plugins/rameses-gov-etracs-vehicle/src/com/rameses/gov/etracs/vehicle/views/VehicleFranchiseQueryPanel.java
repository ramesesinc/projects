/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.vehicle.views;

/**
 *
 * @author dell
 */
public class VehicleFranchiseQueryPanel extends javax.swing.JPanel {

    /**
     * Creates new form ApplicationListQueryForm
     */
    public VehicleFranchiseQueryPanel() {
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

        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();

        xComboBox2.setCaption("Cluster");
        xComboBox2.setDepends(new String[] {"vehicletype"});
        xComboBox2.setExpression("#{item.name}");
        xComboBox2.setItems("clusterList");
        xComboBox2.setName("cluster"); // NOI18N
        xComboBox2.setDynamic(true);
        xComboBox2.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel3.add(xComboBox2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 274, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xFormPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    // End of variables declaration//GEN-END:variables
}
