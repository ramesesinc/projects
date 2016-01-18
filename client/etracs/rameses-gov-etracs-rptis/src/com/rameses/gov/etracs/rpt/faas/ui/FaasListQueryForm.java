/*
 * BusinessListQueryForm.java
 *
 * Created on December 21, 2013, 3:15 PM
 */

package com.rameses.gov.etracs.rpt.faas.ui;

import com.rameses.rcp.ui.annotations.StyleSheet;

@StyleSheet
public class FaasListQueryForm extends javax.swing.JPanel {
    
    /** Creates new form BusinessListQueryForm */
    public FaasListQueryForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField5 = new com.rameses.rcp.control.XTextField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();

        setLayout(new java.awt.BorderLayout());

        xFormPanel4.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(3, 1, 1, 1));
        xFormPanel4.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel4.setPadding(new java.awt.Insets(5, 5, 5, 5));

        xCheckBox1.setCaption("My Task");
        xCheckBox1.setCaptionWidth(60);
        xCheckBox1.setCellPadding(new java.awt.Insets(0, 0, 0, 10));
        xCheckBox1.setName("query.mytask"); // NOI18N
        xCheckBox1.setShowCaption(false);
        xCheckBox1.setText("My Task");
        xFormPanel4.add(xCheckBox1);

        xTextField1.setCaption("TD No.");
        xTextField1.setCaptionWidth(55);
        xTextField1.setCellPadding(new java.awt.Insets(0, 0, 0, 10));
        xTextField1.setName("query.tdno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel4.add(xTextField1);

        xTextField5.setCaption("Prev. TD No.");
        xTextField5.setCellPadding(new java.awt.Insets(0, 0, 0, 10));
        xTextField5.setName("query.prevtdno"); // NOI18N
        xTextField5.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel4.add(xTextField5);

        xTextField4.setCaption("Owner");
        xTextField4.setCaptionWidth(55);
        xTextField4.setCellPadding(new java.awt.Insets(0, 0, 0, 10));
        xTextField4.setName("query.ownername"); // NOI18N
        xTextField4.setPreferredSize(new java.awt.Dimension(125, 20));
        xFormPanel4.add(xTextField4);

        xTextField2.setCaption("Lot No.");
        xTextField2.setCaptionWidth(55);
        xTextField2.setCellPadding(new java.awt.Insets(0, 0, 0, 10));
        xTextField2.setName("query.cadastrallotno"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(60, 20));
        xFormPanel4.add(xTextField2);

        xTextField3.setCaption("PIN");
        xTextField3.setCaptionWidth(40);
        xTextField3.setCellPadding(new java.awt.Insets(0, 0, 0, 10));
        xTextField3.setName("query.fullpin"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(140, 20));
        xFormPanel4.add(xTextField3);

        add(xFormPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField5;
    // End of variables declaration//GEN-END:variables
    
}
