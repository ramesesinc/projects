/*
 * GeneralCollectionPage.java
 *
 * Created on April 20, 2011, 11:00 AM
 */

package com.rameses.gov.etracs.rpt.collection.ui;

import com.rameses.rcp.ui.annotations.StyleSheet;
import java.math.BigDecimal;

/**
 *
 * @author  jzamora
 */
public class RPTReceiptByCompromiseInstallment extends javax.swing.JPanel {
    
    /** Creates new form GeneralCollectionPage */
    public RPTReceiptByCompromiseInstallment() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();

        setPreferredSize(new java.awt.Dimension(884, 487));
        setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Installment Detail");
        xDataTable1.setBorder(xTitledBorder1);
        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "pay"}
                , new Object[]{"caption", "Pay?"}
                , new Object[]{"width", 60}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 60}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.CheckBoxColumnHandler(java.lang.Boolean.class, true, false)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "installmentno"}
                , new Object[]{"caption", "Installment"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler(null, -1, -1)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "duedate"}
                , new Object[]{"caption", "Due Date"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DateColumnHandler(null, "MMMMM dd, yyyy", null)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "balance"}
                , new Object[]{"caption", "Balance"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "amtdue"}
                , new Object[]{"caption", "Amount Paid*"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", "#{item.pay == true}"}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            })
        });
        xDataTable1.setHandler("installmentListHandler");
        xDataTable1.setImmediate(true);
        xDataTable1.setIndex(-100);
        xDataTable1.setName("selectedInstallment"); // NOI18N
        jPanel8.add(xDataTable1, java.awt.BorderLayout.CENTER);

        add(jPanel8, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel8;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    // End of variables declaration//GEN-END:variables
    
}
