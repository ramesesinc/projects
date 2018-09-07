/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.entity.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author dell
 */
@Template(CrudFormPage.class)
public class JuridicalEntityPage extends javax.swing.JPanel {

    /**
     * Creates new form JuridicalEntityPage
     */
    public JuridicalEntityPage() {
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

        xTabbedPane1 = new com.rameses.rcp.control.XTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        lOVList1 = new com.rameses.enterprise.components.LOVList();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xTextField7 = new com.rameses.rcp.control.XTextField();
        entityAddressLookup1 = new com.rameses.entity.components.EntityAddressLookup();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        entityLookup1 = new com.rameses.entity.components.EntityLookup();
        entityAddressLookup2 = new com.rameses.entity.components.EntityAddressLookup();
        xTextField11 = new com.rameses.rcp.control.XTextField();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField9 = new com.rameses.rcp.control.XTextField();
        xTextField10 = new com.rameses.rcp.control.XTextField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(30, 15, 15, 15));
        xTitledBorder1.setTitle("General Information");
        xFormPanel1.setBorder(xTitledBorder1);
        xFormPanel1.setCaptionWidth(120);

        xLabel1.setBackground(new java.awt.Color(245, 245, 245));
        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel1.setCaption("Entity No");
        xLabel1.setCaptionWidth(120);
        xLabel1.setExpression("#{entity.entityno}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xLabel1);

        xTextField4.setCaption("Name");
        xTextField4.setDisableWhen("#{ mode != 'create' }");
        xTextField4.setName("entity.name"); // NOI18N
        xTextField4.setVisibleWhen("");
        xTextField4.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField4.setRequired(true);
        xFormPanel1.add(xTextField4);

        lOVList1.setCaption("Org Type");
        lOVList1.setDisableWhen("#{ mode != 'create' }");
        lOVList1.setListName("JURIDICAL_ORG_TYPES");
        lOVList1.setName("entity.orgtype"); // NOI18N
        lOVList1.setRequired(true);
        xFormPanel1.add(lOVList1);

        xDateField1.setCaption("Date Registered");
        xDateField1.setCaptionWidth(120);
        xDateField1.setName("entity.dtregistered"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xDateField1);

        xTextField7.setCaption("Nature of Business");
        xTextField7.setCaptionWidth(120);
        xTextField7.setName("entity.nature"); // NOI18N
        xTextField7.setPreferredSize(new java.awt.Dimension(200, 20));
        xTextField7.setStretchWidth(100);
        xFormPanel1.add(xTextField7);

        entityAddressLookup1.setCaption("Address");
        entityAddressLookup1.setCaptionWidth(120);
        entityAddressLookup1.setDisableWhen("#{ mode == 'read' }");
        entityAddressLookup1.setName("entity.address"); // NOI18N
        entityAddressLookup1.setPreferredSize(new java.awt.Dimension(0, 53));
        entityAddressLookup1.setShowAddressList("#{ mode != 'create' }");
        entityAddressLookup1.setShowEditAddress("#{ mode == 'create' }");
        xFormPanel1.add(entityAddressLookup1);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setPadding(new java.awt.Insets(30, 15, 15, 15));
        xTitledBorder2.setTitle("Administrator");
        xFormPanel3.setBorder(xTitledBorder2);
        xFormPanel3.setCaptionWidth(120);

        entityLookup1.setCaption("Administrator Name");
        entityLookup1.setEntityType("individual");
        entityLookup1.setName("entity.administrator"); // NOI18N
        entityLookup1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(entityLookup1);

        entityAddressLookup2.setCaption("Address");
        entityAddressLookup2.setDepends(new String[] {"entity.administrator"});
        entityAddressLookup2.setDisableWhen("#{ mode == 'read' }");
        entityAddressLookup2.setName("entity.administrator.address"); // NOI18N
        entityAddressLookup2.setParentIdName("entity.administrator.objid");
        entityAddressLookup2.setPreferredSize(new java.awt.Dimension(0, 60));
        entityAddressLookup2.setShowEditAddress("#{ false }");
        xFormPanel3.add(entityAddressLookup2);

        xTextField11.setCaption("Position");
        xTextField11.setDepends(new String[] {"entity.administrator"});
        xTextField11.setName("entity.administrator.position"); // NOI18N
        xTextField11.setCaptionWidth(120);
        xTextField11.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xTextField11);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setPadding(new java.awt.Insets(30, 15, 15, 15));
        xTitledBorder3.setTitle("Contact Info");
        xFormPanel2.setBorder(xTitledBorder3);
        xFormPanel2.setCaptionWidth(120);

        xTextField1.setCaption("Mobile No");
        xTextField1.setCaptionWidth(120);
        xTextField1.setName("entity.mobileno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel2.add(xTextField1);

        xTextField9.setCaption("Phone No");
        xTextField9.setCaptionWidth(120);
        xTextField9.setName("entity.phoneno"); // NOI18N
        xTextField9.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel2.add(xTextField9);

        xTextField10.setCaption("Email");
        xTextField10.setCaptionWidth(120);
        xTextField10.setName("entity.email"); // NOI18N
        xTextField10.setPreferredSize(new java.awt.Dimension(200, 20));
        xTextField10.setTextCase(com.rameses.rcp.constant.TextCase.LOWER);
        xFormPanel2.add(xTextField10);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                    .addComponent(xFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        xTabbedPane1.addTab("General", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.entity.components.EntityAddressLookup entityAddressLookup1;
    private com.rameses.entity.components.EntityAddressLookup entityAddressLookup2;
    private com.rameses.entity.components.EntityLookup entityLookup1;
    private javax.swing.JPanel jPanel1;
    private com.rameses.enterprise.components.LOVList lOVList1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField11;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField7;
    private com.rameses.rcp.control.XTextField xTextField9;
    // End of variables declaration//GEN-END:variables
}
