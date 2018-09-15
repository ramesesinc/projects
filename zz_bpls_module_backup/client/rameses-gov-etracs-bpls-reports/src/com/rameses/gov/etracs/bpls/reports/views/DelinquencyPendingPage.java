/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.bpls.reports.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author wflores 
 */
@StyleSheet
@Template(FormPage.class)
public class DelinquencyPendingPage extends javax.swing.JPanel {

    /**
     * Creates new form DelinquencyPendingPage
     */
    public DelinquencyPendingPage() {
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
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        xLabel7 = new com.rameses.rcp.control.XLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new com.rameses.rcp.control.layout.YLayout());

        xPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        xPanel1.setLayout(new java.awt.BorderLayout());

        xLabel3.setIconResource("com/rameses/rcp/icons/loading16.gif");
        xLabel3.setName("loadingicon"); // NOI18N
        xPanel1.add(xLabel3, java.awt.BorderLayout.WEST);

        xLabel1.setExpression("#{msg}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel1.setForeground(new java.awt.Color(153, 0, 0));
        xPanel1.add(xLabel1, java.awt.BorderLayout.CENTER);

        add(xPanel1);

        xPanel2.setLayout(new java.awt.BorderLayout());

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setCaptionWidth(110);
        xFormPanel1.setPadding(new java.awt.Insets(20, 0, 0, 0));

        xLabel2.setBackground(new java.awt.Color(250, 250, 250));
        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel2.setCaption("Status");
        xLabel2.setExpression("#{entity.state}");
        xLabel2.setOpaque(true);
        xLabel2.setPreferredSize(new java.awt.Dimension(300, 20));
        xFormPanel1.add(xLabel2);

        xLabel4.setBackground(new java.awt.Color(250, 250, 250));
        xLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel4.setCaption("Date Created");
        xLabel4.setExpression("#{entity.dtfiled}");
        xLabel4.setOpaque(true);
        xLabel4.setPreferredSize(new java.awt.Dimension(300, 20));
        xFormPanel1.add(xLabel4);

        xLabel5.setBackground(new java.awt.Color(250, 250, 250));
        xLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel5.setCaption("Created By");
        xLabel5.setExpression("#{entity.username}");
        xLabel5.setOpaque(true);
        xLabel5.setPreferredSize(new java.awt.Dimension(300, 20));
        xFormPanel1.add(xLabel5);

        xLabel6.setBackground(new java.awt.Color(250, 250, 250));
        xLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel6.setCaption("Total Count");
        xLabel6.setExpression("#{entity.totalcount}");
        xLabel6.setOpaque(true);
        xLabel6.setPreferredSize(new java.awt.Dimension(300, 20));
        xFormPanel1.add(xLabel6);

        xLabel7.setBackground(new java.awt.Color(250, 250, 250));
        xLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel7.setCaption("Processed Count");
        xLabel7.setExpression("#{entity.processedcount}");
        xLabel7.setOpaque(true);
        xLabel7.setPreferredSize(new java.awt.Dimension(300, 20));
        xFormPanel1.add(xLabel7);

        xPanel2.add(xFormPanel1, java.awt.BorderLayout.NORTH);

        add(xPanel2);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLabel xLabel7;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel2;
    // End of variables declaration//GEN-END:variables
}