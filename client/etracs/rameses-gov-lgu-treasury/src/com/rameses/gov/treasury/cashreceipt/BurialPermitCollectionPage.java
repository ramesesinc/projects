/*
 * NewJPanel.java
 *
 * Created on August 9, 2013, 5:22 PM
 */

package com.rameses.gov.treasury.cashreceipt;

import com.rameses.enterprise.treasury.cashreceipt.SerialCashReceiptPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Elmo
 */
@Template(value=SerialCashReceiptPage.class, target="content")
public class BurialPermitCollectionPage extends javax.swing.JPanel {
    
    /** Creates new form NewJPanel */
    public BurialPermitCollectionPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        formPanel3 = new com.rameses.rcp.util.FormPanel();
        xTextField10 = new com.rameses.rcp.control.XTextField();
        xTextField11 = new com.rameses.rcp.control.XTextField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xTextField12 = new com.rameses.rcp.control.XTextField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xTextField13 = new com.rameses.rcp.control.XTextField();
        xTextField5 = new com.rameses.rcp.control.XTextField();
        xTextField6 = new com.rameses.rcp.control.XTextField();
        formPanel4 = new com.rameses.rcp.util.FormPanel();
        xSeparator1 = new com.rameses.rcp.control.XSeparator();
        formPanel5 = new com.rameses.rcp.util.FormPanel();
        xTextField7 = new com.rameses.rcp.control.XTextField();
        xTextField8 = new com.rameses.rcp.control.XTextField();
        xTextField9 = new com.rameses.rcp.control.XTextField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Burial Permit and License Fee Information");
        setBorder(xTitledBorder1);
        setPreferredSize(new java.awt.Dimension(585, 335));

        formPanel3.setCaptionFont(new java.awt.Font("Arial", 0, 12));
        formPanel3.setPadding(new java.awt.Insets(0, 5, 5, 5));
        xTextField10.setCaption("To City/Municipality of");
        xTextField10.setCaptionWidth(140);
        xTextField10.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField10.setName("entity.tocitymuni");
        xTextField10.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel3.add(xTextField10);

        xTextField11.setCaption("To Province");
        xTextField11.setCaptionWidth(140);
        xTextField11.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField11.setName("entity.toprovince");
        xTextField11.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel3.add(xTextField11);

        xComboBox1.setCaption("Permission Type");
        xComboBox1.setCaptionWidth(140);
        xComboBox1.setFont(new java.awt.Font("Arial", 0, 12));
        xComboBox1.setItems("permissionTypeList");
        xComboBox1.setName("entity.permissiontype");
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 19));
        xComboBox1.setRequired(true);
        formPanel3.add(xComboBox1);

        xTextField3.setCaption("Name");
        xTextField3.setCaptionWidth(140);
        xTextField3.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField3.setName("entity.name");
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField3.setRequired(true);
        formPanel3.add(xTextField3);

        xTextField4.setCaption("Nationality");
        xTextField4.setCaptionWidth(140);
        xTextField4.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField4.setName("entity.nationality");
        xTextField4.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel3.add(xTextField4);

        xTextField12.setCaption("Age");
        xTextField12.setCaptionWidth(140);
        xTextField12.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField12.setName("entity.age");
        xTextField12.setPreferredSize(new java.awt.Dimension(200, 19));
        formPanel3.add(xTextField12);

        xComboBox2.setCaption("Sex");
        xComboBox2.setCaptionWidth(140);
        xComboBox2.setFont(new java.awt.Font("Arial", 0, 12));
        xComboBox2.setItems("sexList");
        xComboBox2.setName("entity.sex");
        xComboBox2.setPreferredSize(new java.awt.Dimension(200, 19));
        xComboBox2.setRequired(true);
        formPanel3.add(xComboBox2);

        xTextField13.setCaption("Date of Death");
        xTextField13.setCaptionWidth(140);
        xTextField13.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField13.setName("entity.dtdeath");
        xTextField13.setPreferredSize(new java.awt.Dimension(200, 19));
        formPanel3.add(xTextField13);

        xTextField5.setCaption("Cause of Death");
        xTextField5.setCaptionWidth(140);
        xTextField5.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField5.setName("entity.deathcause");
        xTextField5.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel3.add(xTextField5);

        xTextField6.setCaption("Cemetery");
        xTextField6.setCaptionWidth(140);
        xTextField6.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField6.setName("entity.cemetery");
        xTextField6.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField6.setRequired(true);
        formPanel3.add(xTextField6);

        formPanel4.setAddCaptionColon(false);
        formPanel4.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        formPanel4.setCaptionFont(new java.awt.Font("Arial", 1, 12));
        formPanel4.setPadding(new java.awt.Insets(5, 0, 0, 0));
        formPanel4.setPreferredSize(new java.awt.Dimension(0, 150));
        formPanel4.setShowCaption(false);
        xSeparator1.setCaption("In case of disenterment");
        xSeparator1.setCaptionWidth(145);
        xSeparator1.setPreferredSize(new java.awt.Dimension(0, 20));
        xSeparator1.setShowCaption(true);
        org.jdesktop.layout.GroupLayout xSeparator1Layout = new org.jdesktop.layout.GroupLayout(xSeparator1);
        xSeparator1.setLayout(xSeparator1Layout);
        xSeparator1Layout.setHorizontalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 425, Short.MAX_VALUE)
        );
        xSeparator1Layout.setVerticalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 20, Short.MAX_VALUE)
        );
        formPanel4.add(xSeparator1);

        formPanel5.setCaptionFont(new java.awt.Font("Arial", 0, 12));
        formPanel5.setPadding(new java.awt.Insets(0, 20, 5, 5));
        formPanel5.setPreferredSize(new java.awt.Dimension(0, 150));
        formPanel5.setShowCaption(false);
        xTextField7.setCaption("Infectious or Non-Infectious");
        xTextField7.setCaptionWidth(230);
        xTextField7.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField7.setName("entity.infectious");
        xTextField7.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel5.add(xTextField7);

        xTextField8.setCaption("Body embalmed or not embalmed");
        xTextField8.setCaptionWidth(230);
        xTextField8.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField8.setName("entity.embalmed");
        xTextField8.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel5.add(xTextField8);

        xTextField9.setCaption("Disposition of remains");
        xTextField9.setCaptionWidth(230);
        xTextField9.setFont(new java.awt.Font("Arial", 0, 12));
        xTextField9.setName("entity.disposition");
        xTextField9.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel5.add(xTextField9);

        formPanel4.add(formPanel5);

        formPanel3.add(formPanel4);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(formPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(formPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 311, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel3;
    private com.rameses.rcp.util.FormPanel formPanel4;
    private com.rameses.rcp.util.FormPanel formPanel5;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XSeparator xSeparator1;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField11;
    private com.rameses.rcp.control.XTextField xTextField12;
    private com.rameses.rcp.control.XTextField xTextField13;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField5;
    private com.rameses.rcp.control.XTextField xTextField6;
    private com.rameses.rcp.control.XTextField xTextField7;
    private com.rameses.rcp.control.XTextField xTextField8;
    private com.rameses.rcp.control.XTextField xTextField9;
    // End of variables declaration//GEN-END:variables
    
}
