/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.accounting.views;


import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author Elmo Nazareno
 */
@Template(FormPage.class)
public class StandardReportInitialPage extends javax.swing.JPanel {

    /**
     * Creates new form StandardReportInitialPage
     */
    public StandardReportInitialPage() {
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
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xCheckBox4 = new com.rameses.rcp.control.XCheckBox();
        xCheckBox5 = new com.rameses.rcp.control.XCheckBox();
        jPanel2 = new javax.swing.JPanel();
        filterCriteria1 = new com.rameses.seti2.components.FilterCriteria();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Report Options");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setPadding(new java.awt.Insets(5, 5, 5, 5));

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Account Group");
        xComboBox1.setCaptionWidth(120);
        xComboBox1.setExpression("#{   item.title}");
        xComboBox1.setItems("mainGroupList");
        xComboBox1.setName("query.maingroup"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(200, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xComboBox3.setAllowNull(false);
        xComboBox3.setCaption("Template");
        xComboBox3.setCaptionWidth(120);
        xComboBox3.setExpression("#{item.caption}");
        xComboBox3.setItems("reportTemplates");
        xComboBox3.setName("query.template"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(200, 20));
        xComboBox3.setRequired(true);
        xFormPanel1.add(xComboBox3);

        xComboBox2.setAllowNull(false);
        xComboBox2.setCaption("Report Type");
        xComboBox2.setCaptionWidth(120);
        xComboBox2.setItems("reportTypes");
        xComboBox2.setName("query.type"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(200, 20));
        xComboBox2.setRequired(true);
        xFormPanel1.add(xComboBox2);

        xTextField1.setCaption("Report Title");
        xTextField1.setName("query.reporttitle"); // NOI18N
        xTextField1.setCaptionWidth(120);
        xTextField1.setPreferredSize(new java.awt.Dimension(400, 20));
        xFormPanel1.add(xTextField1);

        xFormPanel3.setCaptionWidth(120);
        xFormPanel3.setPadding(new java.awt.Insets(5, 120, 0, 0));
        xFormPanel3.setShowCaption(false);

        xCheckBox4.setCaption("");
        xCheckBox4.setCaptionWidth(120);
        xCheckBox4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox4.setName("query.hidenoactualvalue"); // NOI18N
        xCheckBox4.setShowCaption(false);
        xCheckBox4.setText(" Hide No Actual Values");
        xFormPanel3.add(xCheckBox4);

        xCheckBox5.setCaption("");
        xCheckBox5.setCaptionWidth(120);
        xCheckBox5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox5.setName("query.hidetarget"); // NOI18N
        xCheckBox5.setShowCaption(false);
        xCheckBox5.setText(" Hide Income Targets");
        xFormPanel3.add(xCheckBox5);

        xFormPanel1.add(xFormPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Filters");
        jPanel2.setBorder(xTitledBorder2);

        filterCriteria1.setHandler("criteriaHandler");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterCriteria1, javax.swing.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterCriteria1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.seti2.components.FilterCriteria filterCriteria1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private com.rameses.rcp.control.XCheckBox xCheckBox4;
    private com.rameses.rcp.control.XCheckBox xCheckBox5;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}
