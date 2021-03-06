/*
 * NewBPApplicationInitPage.java
 *
 * Created on October 3, 2013, 7:41 PM
 */

package com.rameses.gov.etracs.bpls.view;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Elmo
 */
@Template(OKCancelPage.class)
public class BusinessApplicationRequirement extends javax.swing.JPanel {
    
    /** Creates new form NewBPApplicationInitPage */
    public BusinessApplicationRequirement() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();

        xLabel1.setCaption("Title");
        xLabel1.setExpression("#{info.title}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        xLabel1.setName("entity.title"); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel1.add(xLabel1);

        xCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox1.setCaption("");
        xCheckBox1.setCheckValue(1);
        xCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox1.setName("info.completed"); // NOI18N
        xCheckBox1.setText("Check if completed");
        xCheckBox1.setUncheckValue(0);
        xFormPanel1.add(xCheckBox1);

        xTextField1.setCaption("Ref No");
        xTextField1.setName("info.refno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField1.setTextCase(com.rameses.rcp.constant.TextCase.NONE);
        xFormPanel1.add(xTextField1);

        xDateField1.setCaption("Date Issued");
        xDateField1.setName("info.dtissued"); // NOI18N
        xFormPanel1.add(xDateField1);

        xTextField2.setCaption("Place Issued");
        xTextField2.setName("info.placeissued"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setTextCase(com.rameses.rcp.constant.TextCase.NONE);
        xFormPanel1.add(xTextField2);

        xTextField3.setCaption("Verified By");
        xTextField3.setName("info.issuedby"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField3.setTextCase(com.rameses.rcp.constant.TextCase.NONE);
        xFormPanel1.add(xTextField3);

        xDecimalField1.setCaption("Amount");
        xDecimalField1.setName("info.amount"); // NOI18N
        xFormPanel1.add(xDecimalField1);

        xDateField2.setCaption("Expiry date");
        xDateField2.setName("info.expirydate"); // NOI18N
        xFormPanel1.add(xDateField2);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 60));

        xTextArea1.setLineWrap(true);
        xTextArea1.setWrapStyleWord(true);
        xTextArea1.setCaption("Remarks");
        xTextArea1.setName("info.remarks"); // NOI18N
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}
