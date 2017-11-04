/*
 * RemittanceInitPage.java
 *
 * Created on May 2, 2011, 3:01 PM
 */

package com.rameses.enterprise.treasury.remittance;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;
import java.math.BigDecimal;

/**
 *
 * @author  alvin
 */
@Template(FormPage.class)
public class RemittanceInitPage extends javax.swing.JPanel {
    
    /** Creates new form RemittanceInitPage */
    public RemittanceInitPage() {
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
        jPanel2 = new javax.swing.JPanel();
        formPanel3 = new com.rameses.rcp.util.FormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        formPanel2 = new com.rameses.rcp.util.FormPanel();
        xNumberField1 = new com.rameses.rcp.control.XNumberField();
        xLabel1 = new com.rameses.rcp.control.XLabel();

        setPreferredSize(new java.awt.Dimension(643, 466));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel1.setLayout(new java.awt.BorderLayout());

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Remittance Information");
        jPanel2.setBorder(xTitledBorder1);
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 70));
        jPanel2.setLayout(new java.awt.BorderLayout());

        formPanel3.setPadding(new java.awt.Insets(5, 15, 5, 15));

        xLookupField1.setCaption("Liquidating Officer");
        xLookupField1.setCaptionFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        xLookupField1.setCaptionWidth(140);
        xLookupField1.setExpression("#{item.name}");
        xLookupField1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        xLookupField1.setHandler("liquidatingofficer:lookup");
        xLookupField1.setName("entity.liquidatingofficer"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 22));
        xLookupField1.setRequired(true);
        formPanel3.add(xLookupField1);

        jPanel2.add(formPanel3, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Summary of Collections");
        xDataTable1.setBorder(xTitledBorder2);
        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "formno"}
                , new Object[]{"caption", "Form No"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "CENTER"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "startseries"}
                , new Object[]{"caption", "From"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "CENTER"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "endseries"}
                , new Object[]{"caption", "To"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "CENTER"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
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
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            })
        });
        xDataTable1.setHandler("listModel");
        jPanel1.add(xDataTable1, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        formPanel2.setCaptionFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        formPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        formPanel2.setPadding(new java.awt.Insets(2, 5, 5, 0));
        formPanel2.setPreferredSize(new java.awt.Dimension(220, 35));

        xNumberField1.setCaption("Total");
        xNumberField1.setName("entity.amount"); // NOI18N
        xNumberField1.setCaptionWidth(60);
        xNumberField1.setFieldType(BigDecimal.class);
        xNumberField1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        xNumberField1.setPattern("#,##0.00");
        xNumberField1.setPreferredSize(new java.awt.Dimension(0, 24));
        xNumberField1.setReadonly(true);
        formPanel2.add(xNumberField1);

        jPanel3.add(formPanel2, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel3, java.awt.BorderLayout.NORTH);

        xLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        xLabel1.setExpression("#{entity.message}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel1.setForeground(new java.awt.Color(204, 0, 0));
        xLabel1.setPreferredSize(new java.awt.Dimension(120, 40));
        jPanel4.add(xLabel1, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanel4, java.awt.BorderLayout.SOUTH);

        add(jPanel1, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel2;
    private com.rameses.rcp.util.FormPanel formPanel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XNumberField xNumberField1;
    // End of variables declaration//GEN-END:variables
    
}
