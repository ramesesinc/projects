/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.financial.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author elmonazareno
 */
@Template(FormPage.class)
public class AccountMappingInitialPage extends javax.swing.JPanel {

    /**
     * Creates new form AccountMappingInitialPage
     */
    public AccountMappingInitialPage() {
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
        xLabel1 = new com.rameses.rcp.control.XLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xList1 = new com.rameses.rcp.control.XList();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(200, 200));
        jPanel1.setLayout(new java.awt.BorderLayout());

        xLabel1.setExpression("Select a Main Group");
        xLabel1.setLabelFor(xList1);
        xLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 20, 0));
        xLabel1.setFontStyle("font-weight: bold;");
        xLabel1.setForeground(new java.awt.Color(40, 40, 40));
        jPanel1.add(xLabel1, java.awt.BorderLayout.NORTH);

        xList1.setExpression("#{ item.title }");
        xList1.setItems("mainGroupList");
        xList1.setName("mainGroup"); // NOI18N
        jScrollPane1.setViewportView(xList1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XList xList1;
    // End of variables declaration//GEN-END:variables
}
