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
public class LiquidationProcessPage_1 extends javax.swing.JPanel {
    
    /** Creates new form LiquidationInitPage1 */
    public LiquidationProcessPage_1() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xDataTable2 = new com.rameses.rcp.control.XDataTable();
        jPanel4 = new javax.swing.JPanel();
        xSubFormPanel2 = new com.rameses.rcp.control.XSubFormPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField4 = new com.rameses.rcp.control.XDecimalField();
        jLabel1 = new javax.swing.JLabel();

        xDataTable2.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "fund.code"}
                , new Object[]{"caption", "Fund Code"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "amount"}
                , new Object[]{"caption", "Amount"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false)}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "cashier"}
                , new Object[]{"caption", "Cashier"}
                , new Object[]{"width", 250}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler("#{item.cashier.name}", "cashier:lookup")}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
            })
        });
        xDataTable2.setHandler("fundSummaryModel");
        xDataTable2.setName("selectedItem");

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Cash Breakdown");
        jPanel4.setBorder(xTitledBorder1);
        xSubFormPanel2.setHandler("cashBreakdown");

        xFormPanel2.setCaptionWidth(160);
        xDecimalField4.setCaption("Total Cash Breakdown");
        xDecimalField4.setEnabled(false);
        xDecimalField4.setFont(new java.awt.Font("Tahoma", 1, 12));
        xDecimalField4.setName("breakdown");
        xDecimalField4.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel2.add(xDecimalField4);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, xFormPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, xSubFormPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(xSubFormPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 212, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xFormPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Assign a cashier to deposit by fund");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(319, 319, 319)
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, xDataTable2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 671, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(388, 388, 388))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xDataTable2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 165, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 283, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel4;
    private com.rameses.rcp.control.XDataTable xDataTable2;
    private com.rameses.rcp.control.XDecimalField xDecimalField4;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XSubFormPanel xSubFormPanel2;
    // End of variables declaration//GEN-END:variables
    
}