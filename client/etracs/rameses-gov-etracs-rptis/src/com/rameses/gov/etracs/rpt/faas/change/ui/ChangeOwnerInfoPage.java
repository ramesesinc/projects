/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rpt.faas.change.ui;

import com.rameses.rcp.ui.annotations.Template;

@Template(ChangeInfoPage.class)
public class ChangeOwnerInfoPage extends javax.swing.JPanel {

    /**
     * Creates new form ChangeFaasInfoPage
     */
    public ChangeOwnerInfoPage() {
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        formPanel5 = new com.rameses.rcp.util.FormPanel();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xTextField10 = new com.rameses.rcp.control.XTextField();
        xTextField25 = new com.rameses.rcp.control.XTextField();
        xTextField26 = new com.rameses.rcp.control.XTextField();
        xTextField27 = new com.rameses.rcp.control.XTextField();
        jPanel2 = new javax.swing.JPanel();
        formPanel7 = new com.rameses.rcp.util.FormPanel();
        xTextField12 = new com.rameses.rcp.control.XTextField();
        xTextField31 = new com.rameses.rcp.control.XTextField();
        xTextField11 = new com.rameses.rcp.control.XTextField();
        xTextField28 = new com.rameses.rcp.control.XTextField();
        xTextField29 = new com.rameses.rcp.control.XTextField();
        xTextField30 = new com.rameses.rcp.control.XTextField();

        formPanel5.setCaptionWidth(110);

        xLookupField2.setCaption("Property Owner");
        xLookupField2.setExpression("#{changeinfo.newinfo.taxpayer.name}");
        xLookupField2.setHandler("lookupTaxpayer");
        xLookupField2.setName("entity.taxpayer"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField2.setRequired(true);
        xLookupField2.setStretchWidth(50);
        formPanel5.add(xLookupField2);

        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel1.setCaption("Address");
        xLabel1.setExpression("#{changeinfo.newinfo.taxpayer.address}");
        xLabel1.setPadding(new java.awt.Insets(1, 2, 1, 1));
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xLabel1.setStretchWidth(50);
        formPanel5.add(xLabel1);

        xTextField10.setCaption("Declared Owner");
        xTextField10.setName("changeinfo.newinfo.owner.name"); // NOI18N
        xTextField10.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField10.setRequired(true);
        xTextField10.setStretchWidth(50);
        formPanel5.add(xTextField10);

        xTextField25.setCaption("Address");
        xTextField25.setName("changeinfo.newinfo.owner.address"); // NOI18N
        xTextField25.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField25.setRequired(true);
        xTextField25.setStretchWidth(50);
        formPanel5.add(xTextField25);

        xTextField26.setCaption("Administrator");
        xTextField26.setName("changeinfo.newinfo.administrator.name"); // NOI18N
        xTextField26.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField26.setStretchWidth(50);
        formPanel5.add(xTextField26);

        xTextField27.setCaption("Address");
        xTextField27.setName("changeinfo.newinfo.administrator.address"); // NOI18N
        xTextField27.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField27.setStretchWidth(50);
        formPanel5.add(xTextField27);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Modified Information", jPanel1);

        formPanel7.setCaptionWidth(110);

        xTextField12.setCaption("Property Owner");
        xTextField12.setEnabled(false);
        xTextField12.setName("changeinfo.previnfo.taxpayer.name"); // NOI18N
        xTextField12.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField12.setStretchWidth(50);
        formPanel7.add(xTextField12);

        xTextField31.setCaption("Address");
        xTextField31.setEnabled(false);
        xTextField31.setName("changeinfo.previnfo.taxpayer.address"); // NOI18N
        xTextField31.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField31.setStretchWidth(50);
        formPanel7.add(xTextField31);

        xTextField11.setCaption("Declared Owner");
        xTextField11.setEnabled(false);
        xTextField11.setName("changeinfo.previnfo.owner.name"); // NOI18N
        xTextField11.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField11.setStretchWidth(50);
        formPanel7.add(xTextField11);

        xTextField28.setCaption("Address");
        xTextField28.setEnabled(false);
        xTextField28.setName("changeinfo.previnfo.owner.address"); // NOI18N
        xTextField28.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField28.setStretchWidth(50);
        formPanel7.add(xTextField28);

        xTextField29.setCaption("Administrator");
        xTextField29.setEnabled(false);
        xTextField29.setName("changeinfo.previnfo.administrator.name"); // NOI18N
        xTextField29.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField29.setStretchWidth(50);
        formPanel7.add(xTextField29);

        xTextField30.setCaption("Address");
        xTextField30.setEnabled(false);
        xTextField30.setName("changeinfo.previnfo.administrator.address"); // NOI18N
        xTextField30.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField30.setStretchWidth(50);
        formPanel7.add(xTextField30);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Previous Information", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel5;
    private com.rameses.rcp.util.FormPanel formPanel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField11;
    private com.rameses.rcp.control.XTextField xTextField12;
    private com.rameses.rcp.control.XTextField xTextField25;
    private com.rameses.rcp.control.XTextField xTextField26;
    private com.rameses.rcp.control.XTextField xTextField27;
    private com.rameses.rcp.control.XTextField xTextField28;
    private com.rameses.rcp.control.XTextField xTextField29;
    private com.rameses.rcp.control.XTextField xTextField30;
    private com.rameses.rcp.control.XTextField xTextField31;
    // End of variables declaration//GEN-END:variables
}
