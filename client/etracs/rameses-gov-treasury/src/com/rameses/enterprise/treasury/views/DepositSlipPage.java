/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.treasury.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Elmo Nazareno
 */
@Template(CrudFormPage.class)
public class DepositSlipPage extends javax.swing.JPanel {

    /**
     * Creates new form CashDepositDepositPage
     */
    public DepositSlipPage() {
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

        xPanel2 = new com.rameses.rcp.control.XPanel();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField5 = new com.rameses.rcp.control.XDecimalField();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField9 = new com.rameses.rcp.control.XDecimalField();
        jPanel2 = new javax.swing.JPanel();
        cashDenomination1 = new com.rameses.enterprise.treasury.components.CashDenomination();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();

        xDataTable1.setHandler("checkListModel");
        xDataTable1.setName("selectedCheck"); // NOI18N
        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Checks and Other Payments");
        xDataTable1.setBorder(xTitledBorder1);
        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "refno"}
                , new Object[]{"caption", "Check No"}
                , new Object[]{"width", 80}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 80}
                , new Object[]{"required", false}
                , new Object[]{"resizable", false}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "refdate"}
                , new Object[]{"caption", "Check Date"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DateColumnHandler(null, null, null)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "bank.name"}
                , new Object[]{"caption", "Bank"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "amount"}
                , new Object[]{"caption", "Amount"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 100}
                , new Object[]{"maxWidth", 100}
                , new Object[]{"required", false}
                , new Object[]{"resizable", false}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            })
        });
        xDataTable1.setDynamic(true);

        xFormPanel2.setCaptionWidth(150);

        xDecimalField5.setCaption("Total Non-Cash");
        xDecimalField5.setName("entity.totalcheck"); // NOI18N
        xDecimalField5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField5.setEnabled(false);
        xDecimalField5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xDecimalField5.setPreferredSize(new java.awt.Dimension(150, 23));
        xFormPanel2.add(xDecimalField5);

        xIntegerField1.setCaption("No. of checks");
        xIntegerField1.setEditable(false);
        xIntegerField1.setName("checksCount"); // NOI18N
        xIntegerField1.setEnabled(false);
        xFormPanel2.add(xIntegerField1);

        xDecimalField9.setCaption("Total Cash");
        xDecimalField9.setCaptionWidth(70);
        xDecimalField9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField9.setEnabled(false);
        xDecimalField9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xDecimalField9.setName("entity.totalcash"); // NOI18N
        xDecimalField9.setPreferredSize(new java.awt.Dimension(100, 23));
        xFormPanel3.add(xDecimalField9);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Cash Breakdown");
        jPanel2.setBorder(xTitledBorder2);

        cashDenomination1.setAmountField("entity.totalcash");
        cashDenomination1.setEnabled(false);
        cashDenomination1.setName("entity.cashbreakdown"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cashDenomination1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cashDenomination1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout xPanel2Layout = new javax.swing.GroupLayout(xPanel2);
        xPanel2.setLayout(xPanel2Layout);
        xPanel2Layout.setHorizontalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xDataTable1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(xPanel2Layout.createSequentialGroup()
                        .addGap(0, 231, Short.MAX_VALUE)
                        .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(xPanel2Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        xPanel2Layout.setVerticalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xDataTable1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        xFormPanel4.setOpaque(true);

        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel1.setCaption("Bank Account");
        xLabel1.setCaptionWidth(120);
        xLabel1.setExpression("#{ entity.bankaccount.code } #{ entity.bankaccount.bank.name } ");
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel1);

        xLabel2.setCaption("Amount");
        xLabel2.setExpression("#{ entity.amount } ");
        xLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel2.setCaptionWidth(120);
        xLabel2.setPreferredSize(new java.awt.Dimension(150, 20));
        xFormPanel4.add(xLabel2);

        xLabel5.setCaption("Deposit Type");
        xLabel5.setExpression("#{ entity.deposittype } ");
        xLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        xLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel5.setCaptionWidth(120);
        xLabel5.setPreferredSize(new java.awt.Dimension(150, 20));
        xFormPanel4.add(xLabel5);

        xLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel3.setCaption("Validation No.");
        xLabel3.setCaptionWidth(100);
        xLabel3.setExpression("#{ entity.validation.refno } ");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel3);

        xLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        xLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel4.setCaption("Validation Date");
        xLabel4.setCaptionWidth(100);
        xLabel4.setExpression("#{ entity.validation.refdate } ");
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.enterprise.treasury.components.CashDenomination cashDenomination1;
    private javax.swing.JPanel jPanel2;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XDecimalField xDecimalField5;
    private com.rameses.rcp.control.XDecimalField xDecimalField9;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XPanel xPanel2;
    // End of variables declaration//GEN-END:variables
}
