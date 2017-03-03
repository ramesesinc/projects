/*
 * NewBPApplicationInitPage.java
 *
 * Created on October 3, 2013, 7:41 PM
 */

package com.rameses.gov.etracs.bpls.business;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import java.awt.Dimension;

/**
 *
 * @author  Elmo
 */
@Template(OKCancelPage.class)
@StyleSheet
public class RedflagPage extends javax.swing.JPanel {
    
    /** Creates new form NewBPApplicationInitPage */
    public RedflagPage() {
        initComponents();
        setMinimumSize(new Dimension(573, 200)); 
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        xTextArea2 = new com.rameses.rcp.control.XTextArea();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jPanel1.setLayout(new com.rameses.rcp.control.layout.YLayout());

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(25, 15, 15, 15));
        xTitledBorder1.setTitle("  Business Information   ");
        xFormPanel3.setBorder(xTitledBorder1);
        xFormPanel3.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel3.setCaptionWidth(160);

        xTextField1.setCaption("BIN");
        xTextField1.setName("entity.bin"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField1.setReadonly(true);
        xFormPanel3.add(xTextField1);

        xTextField2.setCaption("Business Name");
        xTextField2.setName("entity.businessname"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setReadonly(true);
        xFormPanel3.add(xTextField2);

        xTextField3.setCaption("Owner Name");
        xTextField3.setName("entity.ownername"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField3.setReadonly(true);
        xFormPanel3.add(xTextField3);

        jPanel1.add(xFormPanel3);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setPadding(new java.awt.Insets(25, 15, 15, 15));
        xTitledBorder2.setTitle("Case");
        xFormPanel1.setBorder(xTitledBorder2);
        xFormPanel1.setCaptionWidth(160);

        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel3.setCaption("Case No");
        xLabel3.setExpression("#{entity.caseno}");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel3);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 60));

        xTextArea1.setCaption("Message");
        xTextArea1.setName("entity.message"); // NOI18N
        xTextArea1.setRequired(true);
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        xComboBox1.setCaption("If unresolved, block action");
        xComboBox1.setItems("blockActions");
        xComboBox1.setName("entity.blockaction"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xDateField1.setCaption("Effective Date");
        xDateField1.setName("entity.effectivedate"); // NOI18N
        xFormPanel1.add(xDateField1);

        xLabel1.setCaption("");
        xLabel1.setExpression("Posted by #{entity.filedby.name} on #{entity.dtfiled}");
        xLabel1.setName("entity.postedby"); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 25));
        xFormPanel1.add(xLabel1);

        jPanel1.add(xFormPanel1);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setPadding(new java.awt.Insets(25, 15, 15, 15));
        xTitledBorder3.setTitle("Resolution");
        xFormPanel2.setBorder(xTitledBorder3);
        xFormPanel2.setCaptionWidth(160);
        xFormPanel2.setVisibleWhen("#{mode=='resolve'}");

        jScrollPane2.setPreferredSize(new java.awt.Dimension(0, 60));

        xTextArea2.setCaption("Remarks");
        xTextArea2.setName("entity.remarks"); // NOI18N
        xTextArea2.setRequired(true);
        jScrollPane2.setViewportView(xTextArea2);

        xFormPanel2.add(jScrollPane2);

        jPanel1.add(xFormPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextArea xTextArea2;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}
