/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.bpls.view;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author wflores
 */
@StyleSheet
@Template(FormPage.class)
public class BPPermitCriteriaPage extends javax.swing.JPanel {

    /**
     * Creates new form BPPermitCriteriaPage
     */
    public BPPermitCriteriaPage() {
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

        xPanel1 = new com.rameses.rcp.control.XPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xComboBox5 = new com.rameses.rcp.control.XComboBox();
        xComboBox4 = new com.rameses.rcp.control.XComboBox();

        xPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        xPanel1.setVisibleWhen("#{mode == 'processing'}");
        xPanel1.setLayout(new java.awt.BorderLayout());

        xLabel1.setFontStyle("font-weight:bold;font-size:12;");
        xLabel1.setForeground(new java.awt.Color(51, 51, 51));
        xLabel1.setPadding(new java.awt.Insets(1, 5, 1, 1));
        xLabel1.setPreferredSize(new java.awt.Dimension(150, 20));
        xLabel1.setText("Processing request please wait...");
        xPanel1.add(xLabel1, java.awt.BorderLayout.CENTER);

        xLabel2.setIconResource("com/rameses/rcp/icons/loading16.gif");
        xPanel1.add(xLabel2, java.awt.BorderLayout.WEST);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("  General Information   ");
        jPanel1.setBorder(xTitledBorder1);

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Period");
        xComboBox1.setCaptionWidth(100);
        xComboBox1.setExpression("#{item.title}");
        xComboBox1.setImmediate(true);
        xComboBox1.setItemKey("code");
        xComboBox1.setItems("periods");
        xComboBox1.setName("entity.period"); // NOI18N
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xIntegerField1.setCaption("Year");
        xIntegerField1.setCaptionWidth(100);
        xIntegerField1.setDepends(new String[] {"entity.period"});
        xIntegerField1.setName("entity.year"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(99, 20));
        xIntegerField1.setRequired(true);
        xFormPanel1.add(xIntegerField1);

        xComboBox2.setCaption("Quarter");
        xComboBox2.setCaptionWidth(100);
        xComboBox2.setDepends(new String[] {"entity.period"});
        xComboBox2.setExpression("");
        xComboBox2.setImmediate(true);
        xComboBox2.setItems("quarters");
        xComboBox2.setName("entity.qtr"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(99, 20));
        xComboBox2.setRequired(true);
        xFormPanel1.add(xComboBox2);

        xComboBox3.setCaption("Month");
        xComboBox3.setCaptionWidth(100);
        xComboBox3.setDepends(new String[] {"entity.period"});
        xComboBox3.setExpression("#{item.name}");
        xComboBox3.setImmediate(true);
        xComboBox3.setItems("months");
        xComboBox3.setName("entity.month"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(99, 20));
        xComboBox3.setRequired(true);
        xFormPanel1.add(xComboBox3);

        xComboBox5.setAllowNull(false);
        xComboBox5.setCaption("Txn Status");
        xComboBox5.setCaptionWidth(100);
        xComboBox5.setExpression("#{item.title}");
        xComboBox5.setImmediate(true);
        xComboBox5.setItemKey("code");
        xComboBox5.setItems("appstates");
        xComboBox5.setName("entity.appstate"); // NOI18N
        xComboBox5.setPreferredSize(new java.awt.Dimension(300, 20));
        xComboBox5.setRequired(true);
        xFormPanel1.add(xComboBox5);

        xComboBox4.setCaption("Barangay");
        xComboBox4.setCaptionWidth(100);
        xComboBox4.setExpression("#{item.name}");
        xComboBox4.setImmediate(true);
        xComboBox4.setItems("barangaylist");
        xComboBox4.setName("entity.barangay"); // NOI18N
        xComboBox4.setPreferredSize(new java.awt.Dimension(300, 20));
        xFormPanel1.add(xComboBox4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XComboBox xComboBox4;
    private com.rameses.rcp.control.XComboBox xComboBox5;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XPanel xPanel1;
    // End of variables declaration//GEN-END:variables
}
