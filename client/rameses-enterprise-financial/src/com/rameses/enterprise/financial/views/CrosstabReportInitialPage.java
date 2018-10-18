/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.financial.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author Elmo Nazareno
 */
@Template(FormPage.class)
public class CrosstabReportInitialPage extends javax.swing.JPanel {

    public CrosstabReportInitialPage() {
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
        xTextField1 = new com.rameses.rcp.control.XTextField();
        jPanel2 = new javax.swing.JPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xComboBox7 = new com.rameses.rcp.control.XComboBox();
        xButton2 = new com.rameses.rcp.control.XButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        xList1 = new com.rameses.rcp.control.XList();
        xComboBox5 = new com.rameses.rcp.control.XComboBox();
        xComboBox6 = new com.rameses.rcp.control.XComboBox();
        jPanel3 = new javax.swing.JPanel();
        filterCriteria1 = new com.rameses.seti2.components.FilterCriteria();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("  Report Options  ");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setPadding(new java.awt.Insets(10, 5, 5, 5));

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Account Group");
        xComboBox1.setCaptionWidth(120);
        xComboBox1.setExpression("#{   item.title}");
        xComboBox1.setItems("mainGroupList");
        xComboBox1.setName("query.maingroup"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(200, 22));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xComboBox3.setAllowNull(false);
        xComboBox3.setCaption("Orientation");
        xComboBox3.setCaptionWidth(120);
        xComboBox3.setExpression("#{item.caption}");
        xComboBox3.setItems("orientationList");
        xComboBox3.setName("option.orientation"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(200, 22));
        xComboBox3.setRequired(true);
        xFormPanel1.add(xComboBox3);

        xTextField1.setCaption("Report Title");
        xTextField1.setName("option.reporttitle"); // NOI18N
        xTextField1.setCaptionWidth(120);
        xTextField1.setPreferredSize(new java.awt.Dimension(400, 20));
        xFormPanel1.add(xTextField1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("  Crosstab Options   ");
        jPanel2.setBorder(xTitledBorder2);

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setCaptionWidth(120);

        xFormPanel3.setCaption("Row Field");
        xFormPanel3.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel3.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel3.setPreferredSize(new java.awt.Dimension(0, 100));

        xComboBox7.setCaption("Row Field");
        xComboBox7.setCaptionWidth(120);
        xComboBox7.setExpression("#{item.caption}");
        xComboBox7.setItems("rowFields");
        xComboBox7.setName("option.rowfield"); // NOI18N
        xComboBox7.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox7.setShowCaption(false);
        xFormPanel3.add(xComboBox7);

        xButton2.setMargin(new java.awt.Insets(2, 4, 2, 4));
        xButton2.setName("addRowField"); // NOI18N
        xButton2.setShowCaption(false);
        xButton2.setText("+");
        xFormPanel3.add(xButton2);

        xFormPanel2.add(xFormPanel3);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 70));

        xList1.setCaption(" ");
        xList1.setExpression("#{item.caption}");
        xList1.setHandler("listHandler");
        xList1.setName("selectedRowField"); // NOI18N
        jScrollPane1.setViewportView(xList1);

        xFormPanel2.add(jScrollPane1);

        xComboBox5.setAllowNull(false);
        xComboBox5.setCaption("Column Field");
        xComboBox5.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xComboBox5.setExpression("#{item.caption}");
        xComboBox5.setItems("columnFields");
        xComboBox5.setName("option.columnfield"); // NOI18N
        xComboBox5.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox5.setRequired(true);
        xFormPanel2.add(xComboBox5);

        xComboBox6.setAllowNull(false);
        xComboBox6.setCaption("Measure Field");
        xComboBox6.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xComboBox6.setExpression("#{item.caption}");
        xComboBox6.setItems("measureFields");
        xComboBox6.setName("option.measurefield"); // NOI18N
        xComboBox6.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox6.setRequired(true);
        xFormPanel2.add(xComboBox6);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("Filters");
        jPanel3.setBorder(xTitledBorder3);

        filterCriteria1.setHandler("criteriaHandler");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterCriteria1, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterCriteria1, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(34, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(33, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(385, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.seti2.components.FilterCriteria filterCriteria1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XComboBox xComboBox5;
    private com.rameses.rcp.control.XComboBox xComboBox6;
    private com.rameses.rcp.control.XComboBox xComboBox7;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XList xList1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}