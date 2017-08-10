/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.accounting.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Elmo Nazareno
 */
@Template(CrudFormPage.class)
public class AccountMainGroupViewPage extends javax.swing.JPanel {

    /**
     * Creates new form AccountHeaderViewPage
     */
    public AccountMainGroupViewPage() {
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

        jPanel2 = new javax.swing.JPanel();
        formPanel4 = new com.rameses.rcp.util.FormPanel();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        xLabel7 = new com.rameses.rcp.control.XLabel();
        xLabel8 = new com.rameses.rcp.control.XLabel();
        xTabbedPane1 = new com.rameses.rcp.control.XTabbedPane();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("General Information");
        jPanel2.setBorder(xTitledBorder1);
        jPanel2.setLayout(null);

        formPanel4.setCaptionWidth(120);

        xLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel6.setCaption("Name");
        xLabel6.setExpression("#{entity.name}");
        xLabel6.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel4.add(xLabel6);

        xLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel7.setCaption("Title");
        xLabel7.setExpression("#{entity.title}");
        xLabel7.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel4.add(xLabel7);

        xLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel8.setCaption("Report Type");
        xLabel8.setExpression("#{entity.reporttype}");
        xLabel8.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel4.add(xLabel8);

        jPanel2.add(formPanel4);
        formPanel4.setBounds(10, 20, 420, 90);

        xTabbedPane1.setItems("sections");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel4;
    private javax.swing.JPanel jPanel2;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLabel xLabel7;
    private com.rameses.rcp.control.XLabel xLabel8;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
