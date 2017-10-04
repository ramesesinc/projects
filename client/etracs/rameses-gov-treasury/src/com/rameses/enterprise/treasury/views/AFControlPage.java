/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.treasury.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Elmo Nazareno
 */
@Template(CrudFormPage.class)
public class AFControlPage extends javax.swing.JPanel {

    /**
     * Creates new form AFControlPage
     */
    public AFControlPage() {
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
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel7 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel8 = new com.rameses.rcp.control.XLabel();
        xSeparator1 = new com.rameses.rcp.control.XSeparator();
        xNumberField1 = new com.rameses.rcp.control.XNumberField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xSeparator2 = new com.rameses.rcp.control.XSeparator();
        xNumberField3 = new com.rameses.rcp.control.XNumberField();
        xNumberField4 = new com.rameses.rcp.control.XNumberField();
        xNumberField5 = new com.rameses.rcp.control.XNumberField();
        xSeparator3 = new com.rameses.rcp.control.XSeparator();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xSeparator4 = new com.rameses.rcp.control.XSeparator();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        jPanel2 = new javax.swing.JPanel();

        formPanel1.setCaptionWidth(110);

        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel1.setCaption("AF No");
        xLabel1.setExpression("#{ entity.af.objid }");
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel1.add(xLabel1);

        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel2.setCaption("Issued To");
        xLabel2.setExpression("#{ entity.owner.name }");
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel1.add(xLabel2);

        xLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel7.setCaption("Assigned To");
        xLabel7.setExpression("#{ entity.assignee.name }");
        xLabel7.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel1.add(xLabel7);

        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel3.setCaption("Date Filed");
        xLabel3.setExpression("#{ entity.dtfiled }");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel1.add(xLabel3);

        xLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel8.setCaption("Txn Mode");
        xLabel8.setExpression("#{ entity.txnmode }");
        xLabel8.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel1.add(xLabel8);

        xSeparator1.setPreferredSize(new java.awt.Dimension(0, 20));

        javax.swing.GroupLayout xSeparator1Layout = new javax.swing.GroupLayout(xSeparator1);
        xSeparator1.setLayout(xSeparator1Layout);
        xSeparator1Layout.setHorizontalGroup(
            xSeparator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );
        xSeparator1Layout.setVerticalGroup(
            xSeparator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        formPanel1.add(xSeparator1);

        xNumberField1.setCaption("Stub No");
        xNumberField1.setName("entity.stubno"); // NOI18N
        xNumberField1.setFieldType(Integer.class);
        xNumberField1.setPreferredSize(new java.awt.Dimension(150, 20));
        xNumberField1.setRequired(true);
        formPanel1.add(xNumberField1);

        xTextField1.setCaption("Prefix");
        xTextField1.setName("entity.prefix"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(150, 20));
        formPanel1.add(xTextField1);

        xTextField2.setCaption("Suffix");
        xTextField2.setName("entity.suffix"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(150, 20));
        formPanel1.add(xTextField2);

        xSeparator2.setPreferredSize(new java.awt.Dimension(0, 20));

        javax.swing.GroupLayout xSeparator2Layout = new javax.swing.GroupLayout(xSeparator2);
        xSeparator2.setLayout(xSeparator2Layout);
        xSeparator2Layout.setHorizontalGroup(
            xSeparator2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );
        xSeparator2Layout.setVerticalGroup(
            xSeparator2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        formPanel1.add(xSeparator2);

        xNumberField3.setCaption("Start Series");
        xNumberField3.setName("entity.startseries"); // NOI18N
        xNumberField3.setFieldType(Integer.class);
        xNumberField3.setPreferredSize(new java.awt.Dimension(150, 20));
        xNumberField3.setRequired(true);
        formPanel1.add(xNumberField3);

        xNumberField4.setCaption("End Series");
        xNumberField4.setName("entity.endseries"); // NOI18N
        xNumberField4.setFieldType(Integer.class);
        xNumberField4.setPreferredSize(new java.awt.Dimension(150, 20));
        xNumberField4.setRequired(true);
        formPanel1.add(xNumberField4);

        xNumberField5.setCaption("Current Series");
        xNumberField5.setName("entity.endseries"); // NOI18N
        xNumberField5.setFieldType(Integer.class);
        xNumberField5.setPreferredSize(new java.awt.Dimension(150, 20));
        xNumberField5.setRequired(true);
        formPanel1.add(xNumberField5);

        xSeparator3.setPreferredSize(new java.awt.Dimension(0, 20));

        javax.swing.GroupLayout xSeparator3Layout = new javax.swing.GroupLayout(xSeparator3);
        xSeparator3.setLayout(xSeparator3Layout);
        xSeparator3Layout.setHorizontalGroup(
            xSeparator3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );
        xSeparator3Layout.setVerticalGroup(
            xSeparator3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        formPanel1.add(xSeparator3);

        xLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        xLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel4.setCaption("Qty Balance");
        xLabel4.setExpression("#{ entity.qtybalance }");
        xLabel4.setPreferredSize(new java.awt.Dimension(150, 20));
        formPanel1.add(xLabel4);

        xLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        xLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel5.setCaption("Qty Issued");
        xLabel5.setExpression("#{ entity.qtyissued }");
        xLabel5.setPreferredSize(new java.awt.Dimension(150, 20));
        formPanel1.add(xLabel5);

        xSeparator4.setPreferredSize(new java.awt.Dimension(0, 20));

        javax.swing.GroupLayout xSeparator4Layout = new javax.swing.GroupLayout(xSeparator4);
        xSeparator4.setLayout(xSeparator4Layout);
        xSeparator4Layout.setHorizontalGroup(
            xSeparator4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );
        xSeparator4Layout.setVerticalGroup(
            xSeparator4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        formPanel1.add(xSeparator4);

        xLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        xLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel6.setCaption("Fund");
        xLabel6.setExpression("#{ entity.fund.title }");
        xLabel6.setName("entity.fund.title"); // NOI18N
        xLabel6.setPreferredSize(new java.awt.Dimension(150, 20));
        formPanel1.add(xLabel6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(formPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        xTabbedPane1.addTab("General Info", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 456, Short.MAX_VALUE)
        );

        xTabbedPane1.addTab("Transaction History", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLabel xLabel7;
    private com.rameses.rcp.control.XLabel xLabel8;
    private com.rameses.rcp.control.XNumberField xNumberField1;
    private com.rameses.rcp.control.XNumberField xNumberField3;
    private com.rameses.rcp.control.XNumberField xNumberField4;
    private com.rameses.rcp.control.XNumberField xNumberField5;
    private com.rameses.rcp.control.XSeparator xSeparator1;
    private com.rameses.rcp.control.XSeparator xSeparator2;
    private com.rameses.rcp.control.XSeparator xSeparator3;
    private com.rameses.rcp.control.XSeparator xSeparator4;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}
