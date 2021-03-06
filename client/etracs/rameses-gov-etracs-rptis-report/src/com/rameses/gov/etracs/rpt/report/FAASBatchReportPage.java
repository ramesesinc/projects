/*
 * AbstractOfCollectionInitPage.java
 *
 * Created on June 3, 2011, 1:35 PM
 */

package com.rameses.gov.etracs.rpt.report;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;


@StyleSheet()
@Template(FormPage.class)
public class FAASBatchReportPage extends javax.swing.JPanel {
    
    /**
     * Creates new form AbstractOfCollectionInitPage
     */
    public FAASBatchReportPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xNumberField2 = new com.rameses.rcp.control.XNumberField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox4 = new com.rameses.rcp.control.XComboBox();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xSeparator2 = new com.rameses.rcp.control.XSeparator();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xComboBox5 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xSeparator1 = new com.rameses.rcp.control.XSeparator();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xLabel1 = new com.rameses.rcp.control.XLabel();

        setPreferredSize(new java.awt.Dimension(603, 291));

        formPanel1.setCaptionWidth(120);
        formPanel1.setPadding(new java.awt.Insets(5, 10, 5, 25));

        xNumberField2.setCaption("Revision Year");
        xNumberField2.setFieldType(Integer.class);
        xNumberField2.setIndex(-100);
        xNumberField2.setName("params.revisionyear"); // NOI18N
        xNumberField2.setPreferredSize(new java.awt.Dimension(120, 19));
        xNumberField2.setRequired(true);
        formPanel1.add(xNumberField2);

        xComboBox2.setCaption("State");
        xComboBox2.setEmptyText("ALL");
        xComboBox2.setIndex(2);
        xComboBox2.setItems("states");
        xComboBox2.setName("params.state"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(0, 21));
        formPanel1.add(xComboBox2);

        xComboBox4.setCaption("Page");
        xComboBox4.setEmptyText("ALL");
        xComboBox4.setIndex(2);
        xComboBox4.setItems("pagetypes");
        xComboBox4.setName("params.pagetype"); // NOI18N
        xComboBox4.setPreferredSize(new java.awt.Dimension(0, 21));
        formPanel1.add(xComboBox4);

        xIntegerField2.setCaption("No. of Copies");
        xIntegerField2.setName("params.copies"); // NOI18N
        xIntegerField2.setPreferredSize(new java.awt.Dimension(50, 20));
        xIntegerField2.setRequired(true);
        formPanel1.add(xIntegerField2);

        xSeparator2.setPreferredSize(new java.awt.Dimension(0, 10));

        org.jdesktop.layout.GroupLayout xSeparator2Layout = new org.jdesktop.layout.GroupLayout(xSeparator2);
        xSeparator2.setLayout(xSeparator2Layout);
        xSeparator2Layout.setHorizontalGroup(
            xSeparator2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 371, Short.MAX_VALUE)
        );
        xSeparator2Layout.setVerticalGroup(
            xSeparator2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 10, Short.MAX_VALUE)
        );

        formPanel1.add(xSeparator2);

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Selection Type");
        xComboBox1.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xComboBox1.setExpression("#{item.caption}");
        xComboBox1.setIndex(2);
        xComboBox1.setItems("selectiontypes");
        xComboBox1.setName("params.selectiontype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 21));
        xComboBox1.setRequired(true);
        formPanel1.add(xComboBox1);

        xComboBox5.setCaption("LGU");
        xComboBox5.setDepends(new String[] {"params.selectiontype"});
        xComboBox5.setExpression("#{item.name}");
        xComboBox5.setIndex(2);
        xComboBox5.setItems("lgus");
        xComboBox5.setName("params.lgu"); // NOI18N
        xComboBox5.setPreferredSize(new java.awt.Dimension(0, 21));
        formPanel1.add(xComboBox5);

        xComboBox3.setCaption("Barangay");
        xComboBox3.setDepends(new String[] {"params.selectiontype", "params.lgu"});
        xComboBox3.setDynamic(true);
        xComboBox3.setExpression("#{item.name}");
        xComboBox3.setIndex(2);
        xComboBox3.setItems("barangays");
        xComboBox3.setName("params.barangay"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(0, 21));
        formPanel1.add(xComboBox3);

        xTextField3.setCaption("Section No.");
        xTextField3.setDepends(new String[] {"params.selectiontype"});
        xTextField3.setName("params.section"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(120, 20));
        formPanel1.add(xTextField3);

        xTextField1.setCaption("Start TD No.");
        xTextField1.setDepends(new String[] {"params.selectiontype"});
        xTextField1.setName("params.starttdno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(120, 20));
        formPanel1.add(xTextField1);

        xTextField2.setCaption("End TD No.");
        xTextField2.setDepends(new String[] {"params.selectiontype"});
        xTextField2.setName("params.endtdno"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(120, 20));
        formPanel1.add(xTextField2);

        xSeparator1.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xSeparator1.setPreferredSize(new java.awt.Dimension(0, 20));

        org.jdesktop.layout.GroupLayout xSeparator1Layout = new org.jdesktop.layout.GroupLayout(xSeparator1);
        xSeparator1.setLayout(xSeparator1Layout);
        xSeparator1Layout.setHorizontalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 371, Short.MAX_VALUE)
        );
        xSeparator1Layout.setVerticalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 20, Short.MAX_VALUE)
        );

        formPanel1.add(xSeparator1);

        xIntegerField1.setCaption("Print Interval (sec)");
        xIntegerField1.setName("params.printinterval"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(50, 20));
        xIntegerField1.setRequired(true);
        formPanel1.add(xIntegerField1);

        xCheckBox1.setName("params.showprinterdialog"); // NOI18N
        xCheckBox1.setShowCaption(false);
        xCheckBox1.setText("Show Printer Dialog?");
        formPanel1.add(xCheckBox1);

        xLabel1.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        xLabel1.setForeground(new java.awt.Color(153, 0, 0));
        xLabel1.setName("msg"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(xLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 369, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 406, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(187, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 342, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XComboBox xComboBox4;
    private com.rameses.rcp.control.XComboBox xComboBox5;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XNumberField xNumberField2;
    private com.rameses.rcp.control.XSeparator xSeparator1;
    private com.rameses.rcp.control.XSeparator xSeparator2;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}
