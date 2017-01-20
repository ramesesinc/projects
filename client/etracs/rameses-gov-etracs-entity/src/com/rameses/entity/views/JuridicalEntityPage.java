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
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xTextField7 = new com.rameses.rcp.control.XTextField();
        entityAddressLookup1 = new com.rameses.entity.components.EntityAddressLookup();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        xTextArea2 = new com.rameses.rcp.control.XTextArea();
        xTextField11 = new com.rameses.rcp.control.XTextField();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField9 = new com.rameses.rcp.control.XTextField();
        xTextField10 = new com.rameses.rcp.control.XTextField();

        xTabbedPane1.setDynamic(true);
        xTabbedPane1.setItems("sections");

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(30, 15, 15, 15));
        xTitledBorder1.setTitle("General Information");
        xFormPanel1.setBorder(xTitledBorder1);

        xLabel1.setBackground(new java.awt.Color(245, 245, 245));
        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel1.setCaption("Entity No");
        xLabel1.setCaptionWidth(120);
        xLabel1.setExpression("#{entity.entityno}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xLabel1);

        xLabel2.setBackground(new java.awt.Color(245, 245, 245));
        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel2.setCaption("Name");
        xLabel2.setCaptionWidth(120);
        xLabel2.setExpression("#{entity.name}");
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xLabel2.setStretchWidth(100);
        xFormPanel1.add(xLabel2);

        xLabel3.setBackground(new java.awt.Color(245, 245, 245));
        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel3.setCaption("Org Type");
        xLabel3.setCaptionWidth(120);
        xLabel3.setExpression("#{entity.orgtype}");
        xLabel3.setPreferredSize(new java.awt.Dimension(200, 20));
        xLabel3.setStretchWidth(100);
        xFormPanel1.add(xLabel3);

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
        entityAddressLookup1.setName("entity.address"); // NOI18N
        entityAddressLookup1.setPreferredSize(new java.awt.Dimension(0, 53));
        xFormPanel1.add(entityAddressLookup1);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setPadding(new java.awt.Insets(30, 15, 15, 15));
        xTitledBorder2.setTitle("Administrator");
        xFormPanel3.setBorder(xTitledBorder2);
        xFormPanel3.setCaptionWidth(120);

        xTextField3.setCaption("Name");
        xTextField3.setCaptionWidth(120);
        xTextField3.setName("entity.administrator.name"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xTextField3);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane2.setName("entity.administrator.address"); // NOI18N
        jScrollPane2.setPreferredSize(new java.awt.Dimension(0, 50));

        xTextArea2.setCaption("Address");
        xTextArea2.setName("entity.administrator.address"); // NOI18N
        jScrollPane2.setViewportView(xTextArea2);

        xFormPanel3.add(jScrollPane2);

        xTextField11.setCaption("Position");
        xTextField11.setCaptionWidth(120);
        xTextField11.setName("entity.administrator.position"); // NOI18N
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
                .addContainerGap(101, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        xTabbedPane1.addTab("General", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.entity.components.EntityAddressLookup entityAddressLookup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    private com.rameses.rcp.control.XTextArea xTextArea2;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField11;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField7;
    private com.rameses.rcp.control.XTextField xTextField9;
    // End of variables declaration//GEN-END:variables
}
