/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.treasury.views;

/**
 *
 * @author Elmo Nazareno
 */
public class FundTransferPage extends javax.swing.JPanel {

    /**
     * Creates new form FundTransferPage
     */
    public FundTransferPage() {
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

        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xLookupField3 = new com.rameses.rcp.control.XLookupField();
        xLookupField4 = new com.rameses.rcp.control.XLookupField();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();

        xFormPanel4.setOpaque(true);

        xLookupField3.setCaption("From Bank Account");
        xLookupField3.setCaptionWidth(120);
        xLookupField3.setExpression("#{ entity.frombankaccount.code } #{ entity.frombankaccount.bank.name } ");
        xLookupField3.setHandler("fromBankAccountLookup");
        xLookupField3.setName("entity.frombankaccount"); // NOI18N
        xLookupField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLookupField3);

        xLookupField4.setCaption("To Bank Account");
        xLookupField4.setCaptionWidth(120);
        xLookupField4.setExpression("#{ entity.tobankaccount.code } #{ entity.tobankaccount.bank.name } ");
        xLookupField4.setHandler("toBankAccountLookup");
        xLookupField4.setName("entity.tobankaccount"); // NOI18N
        xLookupField4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLookupField4);

        xDecimalField1.setCaption("Amount  to deposit");
        xDecimalField1.setCaptionWidth(120);
        xDecimalField1.setName("entity.amount"); // NOI18N
        xDecimalField1.setPreferredSize(new java.awt.Dimension(150, 20));
        xDecimalField1.setRequired(true);
        xFormPanel4.add(xDecimalField1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 20, Short.MAX_VALUE)
                .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 832, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XLookupField xLookupField3;
    private com.rameses.rcp.control.XLookupField xLookupField4;
    // End of variables declaration//GEN-END:variables
}