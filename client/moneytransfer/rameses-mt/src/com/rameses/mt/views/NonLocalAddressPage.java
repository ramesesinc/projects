/*
 * NewPatientPage.java
 *
 * Created on March 29, 2014, 11:15 AM
 */

package com.rameses.mt.views;

import com.rameses.rcp.ui.annotations.StyleSheet;

/**
 *
 * @author  Elmo
 */
@StyleSheet
public class NonLocalAddressPage extends javax.swing.JPanel {
    
    /** Creates new form NewPatientPage */
    public NonLocalAddressPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xTextField6 = new com.rameses.rcp.control.XTextField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField7 = new com.rameses.rcp.control.XTextField();
        xTextField8 = new com.rameses.rcp.control.XTextField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xTextField9 = new com.rameses.rcp.control.XTextField();
        xTextField5 = new com.rameses.rcp.control.XTextField();
        xTextField10 = new com.rameses.rcp.control.XTextField();

        xFormPanel1.setCaptionWidth(100);

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Address Type");
        xComboBox1.setCaptionWidth(120);
        xComboBox1.setItems("addressTypes");
        xComboBox1.setName("entity.addresstype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 22));
        xFormPanel1.add(xComboBox1);

        xTextField6.setCaption("Unit No");
        xTextField6.setCaptionWidth(120);
        xTextField6.setName("entity.unitno"); // NOI18N
        xTextField6.setPreferredSize(new java.awt.Dimension(100, 22));
        xFormPanel1.add(xTextField6);

        xTextField1.setCaption("House / Bldg No");
        xTextField1.setCaptionWidth(120);
        xTextField1.setName("entity.bldgno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(100, 22));
        xFormPanel1.add(xTextField1);

        xTextField3.setCaption("Bldg Name");
        xTextField3.setCaptionWidth(120);
        xTextField3.setName("entity.bldgname"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel1.add(xTextField3);

        xTextField2.setCaption("Street");
        xTextField2.setCaptionWidth(120);
        xTextField2.setName("entity.street"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel1.add(xTextField2);

        xTextField7.setCaption("Subdivision");
        xTextField7.setCaptionWidth(120);
        xTextField7.setName("entity.subdivision"); // NOI18N
        xTextField7.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel1.add(xTextField7);

        xTextField8.setCaption("Barangay");
        xTextField8.setCaptionWidth(120);
        xTextField8.setName("entity.barangay.name"); // NOI18N
        xTextField8.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel1.add(xTextField8);

        xTextField4.setCaption("Municipality");
        xTextField4.setCaptionWidth(120);
        xTextField4.setDepends(new String[] {"entity.addresstype"});
        xTextField4.setName("entity.municipality"); // NOI18N
        xTextField4.setPreferredSize(new java.awt.Dimension(0, 22));
        xTextField4.setRequired(true);
        xFormPanel1.add(xTextField4);

        xTextField9.setCaption("City");
        xTextField9.setCaptionWidth(120);
        xTextField9.setDepends(new String[] {"entity.addresstype"});
        xTextField9.setName("entity.city"); // NOI18N
        xTextField9.setPreferredSize(new java.awt.Dimension(0, 22));
        xTextField9.setRequired(true);
        xFormPanel1.add(xTextField9);

        xTextField5.setCaption("Province");
        xTextField5.setCaptionWidth(120);
        xTextField5.setName("entity.province"); // NOI18N
        xTextField5.setPreferredSize(new java.awt.Dimension(0, 22));
        xTextField5.setRequired(true);
        xFormPanel1.add(xTextField5);

        xTextField10.setCaption("PIN");
        xTextField10.setCaptionWidth(120);
        xTextField10.setName("entity.pin"); // NOI18N
        xTextField10.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel1.add(xTextField10);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField5;
    private com.rameses.rcp.control.XTextField xTextField6;
    private com.rameses.rcp.control.XTextField xTextField7;
    private com.rameses.rcp.control.XTextField xTextField8;
    private com.rameses.rcp.control.XTextField xTextField9;
    // End of variables declaration//GEN-END:variables
    
}
