package com.rameses.gov.etracs.rpt.subdivision.ui;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

@StyleSheet
public class SubdivisionAffectedRpuPage extends javax.swing.JPanel {
    
    /** Creates new form SubdivisionLandMain */
    public SubdivisionAffectedRpuPage() {
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
        xDataTable2 = new com.rameses.rcp.control.XDataTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xActionBar1 = new com.rameses.rcp.control.XActionBar();
        xSubFormPanel1 = new com.rameses.rcp.control.XSubFormPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(380, 100));
        jPanel1.setLayout(new java.awt.BorderLayout());

        xDataTable2.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "prevpin"}
                , new Object[]{"caption", "PIN"}
                , new Object[]{"width", 170}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "newpin"}
                , new Object[]{"caption", "New PIN*"}
                , new Object[]{"width", 135}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "newsuffix"}
                , new Object[]{"caption", "New Suffix*"}
                , new Object[]{"width", 80}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler("0000", -1, -1)}
            })
        });
        xDataTable2.setHandler("listHandler");
        xDataTable2.setImmediate(true);
        xDataTable2.setName("selectedItem"); // NOI18N
        jPanel1.add(xDataTable2, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jPanel3.setLayout(new java.awt.BorderLayout());

        xFormPanel3.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xFormPanel3.setCaptionWidth(70);
        xFormPanel3.setPadding(new java.awt.Insets(2, 2, 2, 2));

        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel1.setCaption("TD No.");
        xLabel1.setDepends(new String[] {"selectedItem"});
        xLabel1.setExpression("#{selectedItem.prevtdno}");
        xLabel1.setFontStyle("font-weight:bold");
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel3.add(xLabel1);

        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel2.setCaption("Owner");
        xLabel2.setDepends(new String[] {"selectedItem"});
        xLabel2.setExpression("#{selectedItem.owner.name}");
        xLabel2.setFontStyle("font-weight:bold");
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel3.add(xLabel2);

        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel3.setCaption("Address");
        xLabel3.setDepends(new String[] {"selectedItem"});
        xLabel3.setExpression("#{selectedItem.owner.address}");
        xLabel3.setFontStyle("font-weight:bold");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel3.add(xLabel3);

        jPanel3.add(xFormPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        xFormPanel2.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xFormPanel2.setPadding(new java.awt.Insets(2, 2, 2, 0));

        xIntegerField2.setCaption("Total No. of Affected RPUs");
        xIntegerField2.setCaptionWidth(150);
        xIntegerField2.setCellPadding(new java.awt.Insets(2, 5, 0, 0));
        xIntegerField2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xIntegerField2.setEnabled(false);
        xIntegerField2.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        xIntegerField2.setName("count"); // NOI18N
        xIntegerField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xIntegerField2);

        jPanel2.add(xFormPanel2, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanel2, java.awt.BorderLayout.SOUTH);

        xActionBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xActionBar1.setName("formActions"); // NOI18N
        jPanel1.add(xActionBar1, java.awt.BorderLayout.PAGE_START);

        add(jPanel1, java.awt.BorderLayout.WEST);

        xSubFormPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153))));
        xSubFormPanel1.setDepends(new String[] {"selectedItem"});
        xSubFormPanel1.setDynamic(true);
        xSubFormPanel1.setHandler("opener");
        xSubFormPanel1.setName("opener"); // NOI18N
        add(xSubFormPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private com.rameses.rcp.control.XActionBar xActionBar1;
    private com.rameses.rcp.control.XDataTable xDataTable2;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XSubFormPanel xSubFormPanel1;
    // End of variables declaration//GEN-END:variables
    
}
