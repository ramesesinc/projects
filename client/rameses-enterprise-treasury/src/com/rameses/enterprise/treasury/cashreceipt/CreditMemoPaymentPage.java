/*
 * CashPayment.java
 *
 * Created on August 3, 2013, 5:20 PM
 */

package com.rameses.enterprise.treasury.cashreceipt;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  compaq
 */
@Template(OKCancelPage.class)
public class CreditMemoPaymentPage extends javax.swing.JPanel {
    
    /** Creates new form CashPayment */
    public CreditMemoPaymentPage() {
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
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();

        setPreferredSize(new java.awt.Dimension(489, 274));

        xFormPanel1.setCaptionFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        xFormPanel1.setCaptionWidth(160);
        xFormPanel1.setPadding(new java.awt.Insets(10, 10, 10, 10));

        xLookupField1.setCaption("Account No:");
        xLookupField1.setExpression("#{memo.account.code}");
        xLookupField1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        xLookupField1.setHandler("lookupAccount");
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 27));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xLabel1.setCaption("Bank");
        xLabel1.setExpression("#{memo.account.bank}");
        xLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 24));
        xFormPanel1.add(xLabel1);

        xLabel2.setCaption("Fund");
        xLabel2.setExpression("#{memo.account.fund.name}");
        xLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 24));
        xFormPanel1.add(xLabel2);

        xTextField1.setCaption("Ref No");
        xTextField1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        xTextField1.setName("memo.refno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 27));
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        xDateField1.setCaption("Ref Date");
        xDateField1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        xDateField1.setName("memo.refdate"); // NOI18N
        xFormPanel1.add(xDateField1);

        xDecimalField2.setCaption("Amount");
        xDecimalField2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        xDecimalField2.setName("memo.amount"); // NOI18N
        xDecimalField2.setPreferredSize(new java.awt.Dimension(0, 27));
        xDecimalField2.setRequired(true);
        xFormPanel1.add(xDecimalField2);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 63));

        xTextArea1.setCaption("Particulars");
        xTextArea1.setName("memo.particulars"); // NOI18N
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 469, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 243, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
    
}