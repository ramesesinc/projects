package com.rameses.enterprise.treasury.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import java.math.BigDecimal;

/**
 *
 * @author  wflores
 */
@StyleSheet
@Template(FormPage.class)
public class CashTicketEntryPage_2 extends javax.swing.JPanel {
    
    /** Creates new form CashTicketEntryPage_2 */
    public CashTicketEntryPage_2() {
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
        xPanel1 = new com.rameses.rcp.control.XPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xButton1 = new com.rameses.rcp.control.XButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        xButton2 = new com.rameses.rcp.control.XButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        xButton3 = new com.rameses.rcp.control.XButton();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();
        jPanel4 = new com.rameses.rcp.control.XPanel();
        jLabel3 = new javax.swing.JLabel();
        xNumberField3 = new com.rameses.rcp.control.XNumberField();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(32767, 0));
        jLabel2 = new javax.swing.JLabel();
        xNumberField2 = new com.rameses.rcp.control.XNumberField();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(32767, 0));
        jLabel1 = new javax.swing.JLabel();
        xNumberField1 = new com.rameses.rcp.control.XNumberField();

        setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        xPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 5, 0));
        xPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        xLabel3.setExpression("<b>Receipt Items</b>");
        xLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 20));
        xLabel3.setFontStyle("font-size:12;");
        xLabel3.setForeground(new java.awt.Color(100, 100, 100));
        xLabel3.setUseHtml(true);
        xPanel1.add(xLabel3);

        xButton1.setMnemonic('A');
        xButton1.setName("addItem"); // NOI18N
        xButton1.setText("Add");
        xPanel1.add(xButton1);
        xPanel1.add(filler1);

        xButton2.setDepends(new String[] {"selectedItem"});
        xButton2.setMnemonic('R');
        xButton2.setName("removeItem"); // NOI18N
        xButton2.setText("Remove");
        xPanel1.add(xButton2);
        xPanel1.add(filler2);

        xButton3.setDepends(new String[] {"selectedItem"});
        xButton3.setMnemonic('O');
        xButton3.setName("openItem"); // NOI18N
        xButton3.setText("Open");
        xPanel1.add(xButton3);

        jPanel3.add(xPanel1, java.awt.BorderLayout.NORTH);

        xDataTable1.setHandler("listModel");
        xDataTable1.setName("selectedItem"); // NOI18N
        xDataTable1.setAutoResize(false);
        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "receiptno"}
                , new Object[]{"caption", "Rct No"}
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
                new Object[]{"name", "receiptdate"}
                , new Object[]{"caption", "Rct Date "}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", "#{entity.state == 'DRAFT'}"}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DateColumnHandler("yyyy-MM-dd", "yyyy-MM-dd", "yyyy-MM-dd")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "paidby"}
                , new Object[]{"caption", "Paid By*"}
                , new Object[]{"width", 200}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", "#{entity.state == 'DRAFT'}"}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "paidbyaddress"}
                , new Object[]{"caption", "Address*"}
                , new Object[]{"width", 200}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", "#{entity.state == 'DRAFT'}"}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", null}
                , new Object[]{"caption", "Account*"}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", "#{entity.state == 'DRAFT'}"}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler("#{item.acctinfo}", "lookupAccount")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "amount"}
                , new Object[]{"caption", "Amount*"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 100}
                , new Object[]{"maxWidth", 100}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", "#{entity.state == 'DRAFT'}"}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "remarks"}
                , new Object[]{"caption", "Remarks"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", "#{entity.state == 'DRAFT'}"}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "voided"}
                , new Object[]{"caption", "Voided"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 60}
                , new Object[]{"maxWidth", 60}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", "#{entity.state == 'DRAFT'}"}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.CheckBoxColumnHandler(java.lang.Integer.class, 1, 0)}
            })
        });
        xDataTable1.setVarStatus("status");
        jPanel3.add(xDataTable1, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 5, 0));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 3, 0));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Total Cash:");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        jPanel4.add(jLabel3);

        xNumberField3.setName("entity.totalcash"); // NOI18N
        xNumberField3.setEnabled(false);
        xNumberField3.setFieldType(BigDecimal.class);
        xNumberField3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xNumberField3.setForeground(new java.awt.Color(204, 0, 0));
        xNumberField3.setPattern("#,##0.00");
        xNumberField3.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel4.add(xNumberField3);
        jPanel4.add(filler3);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Total Non-Cash:");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        jPanel4.add(jLabel2);

        xNumberField2.setName("entity.totalnoncash"); // NOI18N
        xNumberField2.setEnabled(false);
        xNumberField2.setFieldType(BigDecimal.class);
        xNumberField2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xNumberField2.setForeground(new java.awt.Color(204, 0, 0));
        xNumberField2.setPattern("#,##0.00");
        xNumberField2.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel4.add(xNumberField2);
        jPanel4.add(filler4);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Total Collection:");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        jPanel4.add(jLabel1);

        xNumberField1.setName("entity.totalamount"); // NOI18N
        xNumberField1.setEnabled(false);
        xNumberField1.setFieldType(BigDecimal.class);
        xNumberField1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xNumberField1.setForeground(new java.awt.Color(204, 0, 0));
        xNumberField1.setPattern("#,##0.00");
        xNumberField1.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel4.add(xNumberField1);

        add(jPanel4, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private com.rameses.rcp.control.XPanel jPanel4;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XNumberField xNumberField1;
    private com.rameses.rcp.control.XNumberField xNumberField2;
    private com.rameses.rcp.control.XNumberField xNumberField3;
    private com.rameses.rcp.control.XPanel xPanel1;
    // End of variables declaration//GEN-END:variables
    
}
