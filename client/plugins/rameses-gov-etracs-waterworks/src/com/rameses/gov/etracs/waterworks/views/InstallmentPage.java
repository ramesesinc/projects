/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author dell
 */
@Template(CrudFormPage.class)
public class InstallmentPage extends javax.swing.JPanel {

    /**
     * Creates new form InstallmentPage
     */
    public InstallmentPage() {
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

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xDecimalField4 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xDecimalField5 = new com.rameses.rcp.control.XDecimalField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(30, 15, 10, 20));
        xTitledBorder1.setTitle("Installment Information");
        xFormPanel1.setBorder(xTitledBorder1);
        xFormPanel1.setCaptionWidth(150);
        xFormPanel1.setDepends(new String[] {"payOption.type"});
        xFormPanel1.setPadding(new java.awt.Insets(0, 5, 5, 15));

        xLabel1.setBackground(new java.awt.Color(245, 245, 245));
        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel1.setCaption("Control No");
        xLabel1.setExpression("#{entity.controlno}");
        xLabel1.setOpaque(true);
        xLabel1.setPreferredSize(new java.awt.Dimension(100, 20));
        xLabel1.setStretchWidth(100);
        xFormPanel1.add(xLabel1);

        xLabel2.setBackground(new java.awt.Color(245, 245, 245));
        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel2.setCaption("Date Filed");
        xLabel2.setExpression("#{entity.dtfiled}");
        xLabel2.setOpaque(true);
        xLabel2.setPreferredSize(new java.awt.Dimension(100, 20));
        xLabel2.setStretchWidth(100);
        xFormPanel1.add(xLabel2);

        xComboBox1.setCaption("Txn Type");
        xComboBox1.setItems("txntypelist");
        xComboBox1.setName("entity.txntypeid"); // NOI18N
        xComboBox1.setRequired(true);
        xComboBox1.setStretchWidth(100);
        xFormPanel1.add(xComboBox1);

        xIntegerField1.setCaption("Term (No. of Months)");
        xIntegerField1.setName("entity.term"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(150, 20));
        xIntegerField1.setRequired(true);
        xFormPanel1.add(xIntegerField1);

        xDecimalField4.setCaption("Amount");
        xDecimalField4.setName("entity.amount"); // NOI18N
        xDecimalField4.setPreferredSize(new java.awt.Dimension(150, 20));
        xFormPanel1.add(xDecimalField4);

        xDecimalField3.setCaption("Down Payment");
        xDecimalField3.setName("entity.downpayment"); // NOI18N
        xDecimalField3.setPreferredSize(new java.awt.Dimension(150, 20));
        xFormPanel1.add(xDecimalField3);

        xLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        xLabel3.setBackground(new java.awt.Color(245, 245, 245));
        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel3.setCaption("Installment Amount");
        xLabel3.setDepends(new String[] {"entity.term", "entity.amount", "entity.downpayment"});
        xLabel3.setExpression("#{formattedInstallmentAmount}");
        xLabel3.setOpaque(true);
        xLabel3.setPreferredSize(new java.awt.Dimension(150, 20));
        xFormPanel1.add(xLabel3);

        xDecimalField5.setCaption("Amt Paid");
        xDecimalField5.setName("entity.amtpaid"); // NOI18N
        xDecimalField5.setPreferredSize(new java.awt.Dimension(150, 20));
        xFormPanel1.add(xDecimalField5);

        xTextField1.setCaption("Particulars");
        xTextField1.setName("entity.particulars"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xTextField1);

        xIntegerField2.setCaption("Start Year");
        xIntegerField2.setName("entity.startyear"); // NOI18N
        xIntegerField2.setPreferredSize(new java.awt.Dimension(150, 20));
        xIntegerField2.setRequired(true);
        xFormPanel1.add(xIntegerField2);

        xComboBox3.setCaption("Start Month");
        xComboBox3.setExpression("#{item.name}");
        xComboBox3.setItemKey("index");
        xComboBox3.setItems("months");
        xComboBox3.setName("entity.startmonth"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(150, 20));
        xComboBox3.setRequired(true);
        xFormPanel1.add(xComboBox3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XDecimalField xDecimalField4;
    private com.rameses.rcp.control.XDecimalField xDecimalField5;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}
