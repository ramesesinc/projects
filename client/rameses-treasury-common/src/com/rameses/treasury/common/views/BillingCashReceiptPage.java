/*
 * PaymentOrderCashReceiptPage.java
 *
 * Created on April 21, 2014, 6:07 PM
 */

package com.rameses.treasury.common.views;

import com.rameses.enterprise.treasury.cashreceipt.SerialCashReceiptPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  arnel
 */

@Template(value=SerialCashReceiptPage.class, target="content")
@StyleSheet
public class BillingCashReceiptPage extends javax.swing.JPanel {
    
    /** Creates new form BPCashReceipt */
    public BillingCashReceiptPage() {
        initComponents();
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paymentPage1 = new com.rameses.treasury.common.components.PaymentPage();

        setLayout(new java.awt.BorderLayout());
        add(paymentPage1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.treasury.common.components.PaymentPage paymentPage1;
    // End of variables declaration//GEN-END:variables
    
}
