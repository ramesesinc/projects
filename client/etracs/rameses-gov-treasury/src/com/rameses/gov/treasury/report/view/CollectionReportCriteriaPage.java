/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.treasury.report.view;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author wflores
 */
@StyleSheet
@Template(FormPage.class)
public class CollectionReportCriteriaPage extends javax.swing.JPanel {

    /**
     * Creates new form CollectionReportCriteriaPage
     */
    public CollectionReportCriteriaPage() {
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
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("  General Information   ");
        jPanel1.setBorder(xTitledBorder1);

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Period");
        xComboBox1.setCaptionWidth(100);
        xComboBox1.setExpression("#{item.title}");
        xComboBox1.setImmediate(true);
        xComboBox1.setItemKey("type");
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

        xDateField1.setCaption("Date");
        xDateField1.setCaptionWidth(100);
        xDateField1.setDepends(new String[] {"entity.period"});
        xDateField1.setName("entity.date"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(99, 20));
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        xLookupField1.setCaption("Collector");
        xLookupField1.setCaptionWidth(100);
        xLookupField1.setDisableWhen("#{tag != 'all'}");
        xLookupField1.setExpression("#{item.name}");
        xLookupField1.setHandler("lookupCollector");
        xLookupField1.setName("entity.collector"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(300, 20));
        xFormPanel1.add(xLookupField1);

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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    // End of variables declaration//GEN-END:variables
}
