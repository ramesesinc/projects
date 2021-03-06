/*
 * LiquidationInitPage1.java
 *
 * Created on August 18, 2013, 9:23 PM
 */

package com.rameses.enterprise.treasury.liquidation;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Elmo
 */
@Template(FormPage.class)
public class LiquidationPage extends javax.swing.JPanel {
    
    /** Creates new form LiquidationInitPage1 */
    public LiquidationPage() {
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
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel8 = new com.rameses.rcp.control.XLabel();
        xDataTable2 = new com.rameses.rcp.control.XDataTable();
        jPanel8 = new javax.swing.JPanel();
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField11 = new com.rameses.rcp.control.XDecimalField();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField10 = new com.rameses.rcp.control.XDecimalField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        xDataTable3 = new com.rameses.rcp.control.XDataTable();
        jPanel4 = new javax.swing.JPanel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField5 = new com.rameses.rcp.control.XDecimalField();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        xSubFormPanel2 = new com.rameses.rcp.control.XSubFormPanel();
        jPanel3 = new javax.swing.JPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField6 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField7 = new com.rameses.rcp.control.XDecimalField();

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.BorderLayout());

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel1.setPadding(new java.awt.Insets(0, 0, 0, 0));

        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel1.setCaption("Liquidation No.");
        xLabel1.setCaptionWidth(100);
        xLabel1.setExpression("#{entity.txnno}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(200, 22));
        xFormPanel1.add(xLabel1);

        xLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel5.setCaption("Liquidation Date");
        xLabel5.setCaptionWidth(100);
        xLabel5.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xLabel5.setExpression("#{entity.liqdate}");
        xLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel5.setPreferredSize(new java.awt.Dimension(110, 22));
        xFormPanel1.add(xLabel5);

        xLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel6.setCaption("Txn Mode");
        xLabel6.setCaptionWidth(80);
        xLabel6.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xLabel6.setExpression("#{entity.txnmode}");
        xLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel6.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel1.add(xLabel6);

        jPanel6.add(xFormPanel1, java.awt.BorderLayout.NORTH);

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel2.setPadding(new java.awt.Insets(3, 0, 5, 0));

        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel2.setCaption("Liq. Officer");
        xLabel2.setCaptionWidth(100);
        xLabel2.setExpression("#{entity.liquidatingofficer.name}");
        xLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel2.setPreferredSize(new java.awt.Dimension(422, 22));
        xFormPanel2.add(xLabel2);

        xLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel8.setCaption("Date Posted");
        xLabel8.setCaptionWidth(80);
        xLabel8.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xLabel8.setExpression("#{entity.dtposted}");
        xLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel8.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel2.add(xLabel8);

        jPanel6.add(xFormPanel2, java.awt.BorderLayout.SOUTH);

        jPanel7.add(jPanel6, java.awt.BorderLayout.NORTH);

        xDataTable2.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "fund.code"}
                , new Object[]{"caption", "Fund Code"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 120}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "fund.title"}
                , new Object[]{"caption", "Fund Title"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "amount"}
                , new Object[]{"caption", "Amount"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 110}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 0)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "cashier"}
                , new Object[]{"caption", "Cashier * "}
                , new Object[]{"width", 250}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler("#{item.cashier.name}", "cashier:lookup")}
            })
        });
        xDataTable2.setHandler("fundSummaryModel");
        xDataTable2.setName("selectedItem"); // NOI18N
        jPanel7.add(xDataTable2, java.awt.BorderLayout.CENTER);

        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        jPanel8.setLayout(new java.awt.BorderLayout());

        xFormPanel6.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel6.setCaptionWidth(150);
        xFormPanel6.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel6.setPadding(new java.awt.Insets(0, 0, 5, 0));

        xDecimalField11.setCaption("Total amount to liquidate");
        xDecimalField11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField11.setEnabled(false);
        xDecimalField11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xDecimalField11.setName("entity.amount"); // NOI18N
        xDecimalField11.setPreferredSize(new java.awt.Dimension(180, 22));
        xFormPanel6.add(xDecimalField11);

        jPanel8.add(xFormPanel6, java.awt.BorderLayout.WEST);

        xFormPanel5.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel5.setCaptionWidth(150);
        xFormPanel5.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel5.setPadding(new java.awt.Insets(0, 0, 5, 0));

        xDecimalField10.setCaption("Total Cash");
        xDecimalField10.setCaptionWidth(80);
        xDecimalField10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField10.setEnabled(false);
        xDecimalField10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xDecimalField10.setName("entity.totalcash"); // NOI18N
        xDecimalField10.setPreferredSize(new java.awt.Dimension(180, 22));
        xFormPanel5.add(xDecimalField10);

        jPanel8.add(xFormPanel5, java.awt.BorderLayout.EAST);

        jPanel7.add(jPanel8, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Checks and other payments");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        jPanel2.add(jLabel2, java.awt.BorderLayout.NORTH);

        xDataTable3.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "refno"}
                , new Object[]{"caption", "Ref No"}
                , new Object[]{"width", 80}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 100}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "particulars"}
                , new Object[]{"caption", "Particulars"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "amount"}
                , new Object[]{"caption", "Amount"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 100}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 0)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "voided"}
                , new Object[]{"caption", "Void"}
                , new Object[]{"width", 50}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 50}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.CheckBoxColumnHandler(java.lang.Integer.class, 1, 0)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "reftype"}
                , new Object[]{"caption", "Ref Type"}
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
            })
        });
        xDataTable3.setHandler("checkModel");
        xDataTable3.setName("selectedCheck"); // NOI18N
        jPanel2.add(xDataTable3, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        jPanel4.setLayout(new java.awt.BorderLayout());

        xFormPanel4.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel4.setCaptionWidth(100);
        xFormPanel4.setPadding(new java.awt.Insets(0, 5, 5, 0));
        xFormPanel4.setPreferredSize(new java.awt.Dimension(270, 27));

        xDecimalField5.setCaption("Total Noncash");
        xDecimalField5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField5.setEnabled(false);
        xDecimalField5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xDecimalField5.setName("entity.totalnoncash"); // NOI18N
        xDecimalField5.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel4.add(xDecimalField5);

        jPanel4.add(xFormPanel4, java.awt.BorderLayout.EAST);

        jPanel2.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Cash Breakdown");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        jPanel5.add(jLabel3, java.awt.BorderLayout.NORTH);

        xSubFormPanel2.setDynamic(true);
        xSubFormPanel2.setHandler("cashBreakdown");
        jPanel5.add(xSubFormPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        jPanel3.setLayout(new java.awt.BorderLayout());

        xFormPanel3.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel3.setCaptionWidth(120);
        xFormPanel3.setPadding(new java.awt.Insets(0, 5, 5, 0));

        xDecimalField6.setCaption("Cash Breakdown");
        xDecimalField6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField6.setEnabled(false);
        xDecimalField6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xDecimalField6.setName("breakdown"); // NOI18N
        xDecimalField6.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel3.add(xDecimalField6);

        xDecimalField7.setCaption("Cash Remaining");
        xDecimalField7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField7.setEnabled(false);
        xDecimalField7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xDecimalField7.setName("remaining"); // NOI18N
        xDecimalField7.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel3.add(xDecimalField7);

        jPanel3.add(xFormPanel3, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel3, java.awt.BorderLayout.SOUTH);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 460, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 331, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 796, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 213, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private com.rameses.rcp.control.XDataTable xDataTable2;
    private com.rameses.rcp.control.XDataTable xDataTable3;
    private com.rameses.rcp.control.XDecimalField xDecimalField10;
    private com.rameses.rcp.control.XDecimalField xDecimalField11;
    private com.rameses.rcp.control.XDecimalField xDecimalField5;
    private com.rameses.rcp.control.XDecimalField xDecimalField6;
    private com.rameses.rcp.control.XDecimalField xDecimalField7;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLabel xLabel8;
    private com.rameses.rcp.control.XSubFormPanel xSubFormPanel2;
    // End of variables declaration//GEN-END:variables
    
}
