/*
 * DefaultRequirementTypeHandlerPage.java
 *
 * Created on February 23, 2014, 3:32 PM
 */

package com.rameses.gov.etracs.rptis.master.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Rameses
 */
@Template(FormPage.class)
@StyleSheet
public class RequirementTypeDefaultHandlerPage extends javax.swing.JPanel {
    
    /** Creates new form DefaultRequirementTypeHandlerPage */
    public RequirementTypeDefaultHandlerPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        jPanel4 = new javax.swing.JPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        fileViewPanel1 = new com.rameses.filemgmt.components.FileViewPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        xFormPanel1.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel1.setPadding(new java.awt.Insets(10, 5, 10, 5));

        xCheckBox1.setName("entity.complied"); // NOI18N
        xCheckBox1.setShowCaption(false);
        xCheckBox1.setText("Complied?");
        xFormPanel1.add(xCheckBox1);

        xTextField2.setCaption("Ref. No.");
        xTextField2.setName("entity.value.txnno"); // NOI18N
        xTextField2.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xFormPanel1.add(xTextField2);

        xDateField2.setCaption("Ref. Date");
        xDateField2.setName("entity.value.txndate"); // NOI18N
        xDateField2.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xFormPanel1.add(xDateField2);

        jPanel1.add(xFormPanel1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        jPanel4.setLayout(new java.awt.BorderLayout());

        xFormPanel2.setCaptionWidth(100);
        xFormPanel2.setPadding(new java.awt.Insets(0, 0, 5, 0));

        xLookupField1.setCaption("Add Attachment");
        xLookupField1.setExpression("#{item.title}");
        xLookupField1.setHandler("lookupAttachment");
        xLookupField1.setName("attachment"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel2.add(xLookupField1);

        jPanel4.add(xFormPanel2, java.awt.BorderLayout.PAGE_START);

        fileViewPanel1.setHandler("attachmentHandler");
        jPanel4.add(fileViewPanel1, java.awt.BorderLayout.CENTER);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.filemgmt.components.FileViewPanel fileViewPanel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
    
}
