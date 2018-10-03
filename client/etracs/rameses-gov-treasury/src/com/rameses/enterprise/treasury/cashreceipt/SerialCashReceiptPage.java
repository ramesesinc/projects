/*
 * CashReceiptPage.java
 *
 * Created on August 2, 2013, 2:19 PM
 */

package com.rameses.enterprise.treasury.cashreceipt;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  compaq
 */
@Template(FormPage.class)
public class SerialCashReceiptPage extends javax.swing.JPanel {
    
    /** Creates new form CashReceiptPage */
    public SerialCashReceiptPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        buttonCash = new com.rameses.rcp.control.XButton();
        buttonCheck = new com.rameses.rcp.control.XButton();
        jLabel6 = new javax.swing.JLabel();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField4 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField5 = new com.rameses.rcp.control.XDecimalField();
        jLabel4 = new javax.swing.JLabel();
        xDecimalField6 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField7 = new com.rameses.rcp.control.XDecimalField();
        jLabel7 = new javax.swing.JLabel();
        xButton3 = new com.rameses.rcp.control.XButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        entityLookup1 = new com.rameses.entity.components.EntityLookup();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        contentPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        buttonCash1 = new com.rameses.rcp.control.XButton();
        buttonCheck1 = new com.rameses.rcp.control.XButton();
        buttonCheck2 = new com.rameses.rcp.control.XButton();
        buttonCheck3 = new com.rameses.rcp.control.XButton();
        buttonCheck4 = new com.rameses.rcp.control.XButton();
        xDecimalField11 = new com.rameses.rcp.control.XDecimalField();

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Amount due");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Total Cash Payment");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 51, 51));
        jLabel3.setText("Credits");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Payment Methods");

        buttonCash.setName("doCashPayment"); // NOI18N
        buttonCash.setAccelerator("F9");
        buttonCash.setFocusable(false);
        buttonCash.setImmediate(true);
        buttonCash.setIndex(20);
        buttonCash.setText("F9 - CASH");

        buttonCheck.setName("doCheckPayment"); // NOI18N
        buttonCheck.setAccelerator("F10");
        buttonCheck.setFocusable(false);
        buttonCheck.setImmediate(true);
        buttonCheck.setIndex(21);
        buttonCheck.setText("F10 - CHECK ");

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(buttonCheck, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
            .add(buttonCash, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(buttonCash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonCheck, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Total Non Cash Payment");

        xDecimalField2.setDepends(new String[] {"cash"});
        xDecimalField2.setEditable(false);
        xDecimalField2.setName("entity.totalcredit"); // NOI18N
        xDecimalField2.setBackground(new java.awt.Color(204, 204, 255));
        xDecimalField2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField2.setEnabled(false);
        xDecimalField2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        xDecimalField3.setDepends(new String[] {"cash"});
        xDecimalField3.setEditable(false);
        xDecimalField3.setName("entity.totalnoncash"); // NOI18N
        xDecimalField3.setBackground(new java.awt.Color(204, 204, 255));
        xDecimalField3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField3.setEnabled(false);
        xDecimalField3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        xDecimalField4.setDepends(new String[] {"cash"});
        xDecimalField4.setEditable(false);
        xDecimalField4.setName("entity.totalcash"); // NOI18N
        xDecimalField4.setBackground(new java.awt.Color(204, 204, 255));
        xDecimalField4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField4.setEnabled(false);
        xDecimalField4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        xDecimalField5.setDepends(new String[] {"cash"});
        xDecimalField5.setEditable(false);
        xDecimalField5.setName("entity.amount"); // NOI18N
        xDecimalField5.setBackground(new java.awt.Color(204, 204, 255));
        xDecimalField5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField5.setEnabled(false);
        xDecimalField5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setText("Cash Change");

        xDecimalField6.setDepends(new String[] {"cash"});
        xDecimalField6.setEditable(false);
        xDecimalField6.setName("entity.cashchange"); // NOI18N
        xDecimalField6.setBackground(new java.awt.Color(204, 204, 255));
        xDecimalField6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField6.setEnabled(false);
        xDecimalField6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        xDecimalField7.setDepends(new String[] {"cash"});
        xDecimalField7.setEditable(false);
        xDecimalField7.setName("entity.balancedue"); // NOI18N
        xDecimalField7.setBackground(new java.awt.Color(204, 204, 255));
        xDecimalField7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField7.setEnabled(false);
        xDecimalField7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 51, 51));
        jLabel7.setText("Balance Unpaid");

        xButton3.setName("clearAllPayments"); // NOI18N
        xButton3.setFocusable(false);
        xButton3.setImmediate(true);
        xButton3.setIndex(22);
        xButton3.setText("CLEAR PAYMENTS");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 153, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(709, Short.MAX_VALUE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(xDecimalField5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(691, Short.MAX_VALUE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 187, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
                        .add(28, 28, 28))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(732, Short.MAX_VALUE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(732, Short.MAX_VALUE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(xDecimalField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, xDecimalField2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, xDecimalField3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                        .addContainerGap(691, Short.MAX_VALUE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, xDecimalField6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, xDecimalField7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                        .addContainerGap(691, Short.MAX_VALUE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(xButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 170, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addContainerGap(785, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xDecimalField5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xDecimalField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xDecimalField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xDecimalField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xDecimalField6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(8, 8, 8)
                .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xDecimalField7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setPreferredSize(new java.awt.Dimension(1024, 490));
        setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Payer Info");
        jPanel2.setBorder(xTitledBorder1);
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 100));

        jPanel1.setPreferredSize(new java.awt.Dimension(650, 70));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(550, 100));
        jPanel4.setLayout(new java.awt.BorderLayout());

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setCaptionWidth(100);

        xTextField1.setCaption("Form No");
        xTextField1.setName("entity.formno"); // NOI18N
        xTextField1.setEnabled(false);
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xTextField1);

        xDateField1.setCaption("Receipt Date");
        xDateField1.setName("entity.receiptdate"); // NOI18N
        xDateField1.setEnabled(false);
        xDateField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xDateField1);

        xTextField4.setCaption("Collector");
        xTextField4.setName("entity.collector.name"); // NOI18N
        xTextField4.setEnabled(false);
        xTextField4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xTextField4);

        jPanel4.add(xFormPanel2, java.awt.BorderLayout.CENTER);

        xFormPanel3.setPreferredSize(new java.awt.Dimension(200, 50));
        xFormPanel3.setShowCaption(false);

        xLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel2.setExpression("#{entity.receiptno}");
        xLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 26));
        xLabel2.setShowCaption(false);
        xFormPanel3.add(xLabel2);

        xLabel5.setCaption("Mode");
        xLabel5.setExpression("#{entity.txnmode}");
        xLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        xLabel5.setForeground(new java.awt.Color(255, 0, 0));
        xLabel5.setPreferredSize(new java.awt.Dimension(0, 20));
        xLabel5.setShowCaption(false);
        xFormPanel3.add(xLabel5);

        xLabel1.setExpression("stub no:  #{entity.stub}");
        xLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xLabel1.setShowCaption(false);
        xLabel1.setStretchWidth(100);
        xFormPanel3.add(xLabel1);

        jPanel4.add(xFormPanel3, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel4, java.awt.BorderLayout.EAST);

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setCaptionWidth(90);

        entityLookup1.setCaption("Payer");
        entityLookup1.setEntityTypeName("entityType");
        entityLookup1.setIndex(-1000);
        entityLookup1.setName("entity.payer"); // NOI18N
        entityLookup1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(entityLookup1);

        xTextField2.setCaption("Paid By");
        xTextField2.setDepends(new String[] {"entity.payer"});
        xTextField2.setName("entity.paidby"); // NOI18N
        xTextField2.setIndex(1);
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        xTextField3.setCaption("Address");
        xTextField3.setDepends(new String[] {"entity.payer"});
        xTextField3.setName("entity.paidbyaddress"); // NOI18N
        xTextField3.setIndex(2);
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField3.setRequired(true);
        xFormPanel1.add(xTextField3);

        jPanel1.add(xFormPanel1, java.awt.BorderLayout.CENTER);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 805, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(3, 3, 3)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel2, java.awt.BorderLayout.NORTH);

        contentPanel.setBackground(new java.awt.Color(204, 204, 204));
        contentPanel.setFocusable(false);
        contentPanel.setName("content"); // NOI18N
        contentPanel.setLayout(new java.awt.BorderLayout());
        jPanel8.add(contentPanel, java.awt.BorderLayout.CENTER);

        jPanel9.setPreferredSize(new java.awt.Dimension(100, 90));
        jPanel9.setLayout(new java.awt.BorderLayout());

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Remarks");
        jScrollPane1.setBorder(xTitledBorder2);

        xTextArea1.setIndex(100);
        xTextArea1.setName("entity.remarks"); // NOI18N
        jScrollPane1.setViewportView(xTextArea1);

        jPanel9.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel9, java.awt.BorderLayout.SOUTH);

        add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel6.setPreferredSize(new java.awt.Dimension(195, 45));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Amount Due");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Payment Methods");

        buttonCash1.setName("doCashPayment"); // NOI18N
        buttonCash1.setAccelerator("F9");
        buttonCash1.setFocusable(false);
        buttonCash1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        buttonCash1.setIndex(20);
        buttonCash1.setText("F9 - CASH  ");
        buttonCash1.setToolTipText("Cash Payment");

        buttonCheck1.setName("doCheckPayment"); // NOI18N
        buttonCheck1.setAccelerator("F10");
        buttonCheck1.setFocusable(false);
        buttonCheck1.setIndex(21);
        buttonCheck1.setText("F10 - CHECK ");
        buttonCheck1.setToolTipText("Check Payment");

        buttonCheck2.setName("doEFTPayment"); // NOI18N
        buttonCheck2.setAccelerator("F11");
        buttonCheck2.setFocusable(false);
        buttonCheck2.setIndex(21);
        buttonCheck2.setText("EFT ");
        buttonCheck2.setToolTipText("Electronic Fund Transfer Payment");

        buttonCheck3.setName("doCreditMemo"); // NOI18N
        buttonCheck3.setAccelerator("F11");
        buttonCheck3.setEnabled(false);
        buttonCheck3.setFocusable(false);
        buttonCheck3.setImmediate(true);
        buttonCheck3.setIndex(21);
        buttonCheck3.setText("DEBIT/CREDIT CARD ");

        buttonCheck4.setName("doCreditMemo"); // NOI18N
        buttonCheck4.setAccelerator("F11");
        buttonCheck4.setEnabled(false);
        buttonCheck4.setFocusable(false);
        buttonCheck4.setImmediate(true);
        buttonCheck4.setIndex(21);
        buttonCheck4.setText("MONEY ORDER");

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(buttonCash1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(buttonCheck1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(buttonCheck2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(buttonCheck4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(buttonCheck3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(buttonCash1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonCheck1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonCheck2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(8, 8, 8)
                .add(buttonCheck4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonCheck3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        xDecimalField11.setDepends(new String[] {"cash"});
        xDecimalField11.setEditable(false);
        xDecimalField11.setName("entity.amount"); // NOI18N
        xDecimalField11.setBackground(new java.awt.Color(204, 204, 255));
        xDecimalField11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField11.setEnabled(false);
        xDecimalField11.setFontStyle("font-size: 20; font-weight:bold;");

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(xDecimalField11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel8)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xDecimalField11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(31, 31, 31)
                .add(jLabel11)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(jPanel6, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XButton buttonCash;
    private com.rameses.rcp.control.XButton buttonCash1;
    private com.rameses.rcp.control.XButton buttonCheck;
    private com.rameses.rcp.control.XButton buttonCheck1;
    private com.rameses.rcp.control.XButton buttonCheck2;
    private com.rameses.rcp.control.XButton buttonCheck3;
    private com.rameses.rcp.control.XButton buttonCheck4;
    private javax.swing.JPanel contentPanel;
    private com.rameses.entity.components.EntityLookup entityLookup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField11;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XDecimalField xDecimalField4;
    private com.rameses.rcp.control.XDecimalField xDecimalField5;
    private com.rameses.rcp.control.XDecimalField xDecimalField6;
    private com.rameses.rcp.control.XDecimalField xDecimalField7;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    // End of variables declaration//GEN-END:variables
    
}
