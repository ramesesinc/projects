/*
 * AccountPage.java
 *
 * Created on February 27, 2011, 12:48 PM
 */

package com.rameses.enterprise.treasury.views;


import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  jzamss
 */
@StyleSheet
@Template(FormPage.class)
public class CollectionTypePage extends javax.swing.JPanel {
    
    /** Creates new form AccountPage */
    public CollectionTypePage() {
        initComponents();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xCheckBox3 = new com.rameses.rcp.control.XCheckBox();
        xCheckBox2 = new com.rameses.rcp.control.XCheckBox();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xSuggest1 = new com.rameses.rcp.control.XSuggest();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLookupField3 = new com.rameses.rcp.control.XLookupField();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        xDataTable2 = new com.rameses.rcp.control.XDataTable();
        jPanel3 = new javax.swing.JPanel();
        xButton1 = new com.rameses.rcp.control.XButton();
        xButton2 = new com.rameses.rcp.control.XButton();
        xButton3 = new com.rameses.rcp.control.XButton();

        xFormPanel1.setCaptionWidth(150);

        xTextField1.setCaption("Name");
        xTextField1.setName("entity.name"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField1.setRequired(true);
        xTextField1.setSpaceChar('_');
        xFormPanel1.add(xTextField1);

        xTextField2.setCaption("Title");
        xTextField2.setName("entity.title"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setRequired(true);
        xTextField2.setTextCase(com.rameses.rcp.constant.TextCase.NONE);
        xFormPanel1.add(xTextField2);

        xComboBox1.setCaption("Form name");
        xComboBox1.setItems("formTypes");
        xComboBox1.setName("entity.formno"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox3.setCaption("");
        xCheckBox3.setCellPadding(new java.awt.Insets(2, 0, 0, 0));
        xCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox3.setName("entity.allowonline"); // NOI18N
        xCheckBox3.setText("Allow Online");
        xCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xCheckBox3ActionPerformed(evt);
            }
        });
        xFormPanel1.add(xCheckBox3);

        xCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox2.setCaption("");
        xCheckBox2.setCellPadding(new java.awt.Insets(2, 0, 0, 0));
        xCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox2.setName("entity.allowoffline"); // NOI18N
        xCheckBox2.setText("Allow Offline");
        xCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xCheckBox2ActionPerformed(evt);
            }
        });
        xFormPanel1.add(xCheckBox2);

        xCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox1.setCaption("");
        xCheckBox1.setCellPadding(new java.awt.Insets(2, 0, 0, 0));
        xCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox1.setName("entity.allowbatch"); // NOI18N
        xCheckBox1.setText("Allow Batch Capture");
        xFormPanel1.add(xCheckBox1);

        xTextField3.setCaption("Barcode Key");
        xTextField3.setCellPadding(new java.awt.Insets(2, 0, 0, 0));
        xTextField3.setName("entity.barcodekey"); // NOI18N
        xFormPanel1.add(xTextField3);

        xIntegerField1.setCaption("Sort Order");
        xIntegerField1.setName("entity.sortorder"); // NOI18N
        xFormPanel1.add(xIntegerField1);

        xComboBox3.setCaption("GUI Screen Handler");
        xComboBox3.setDepends(new String[] {"selectedForm"});
        xComboBox3.setDynamic(true);
        xComboBox3.setItems("handlers");
        xComboBox3.setName("entity.handler"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox3.setRequired(true);
        xFormPanel1.add(xComboBox3);

        xSuggest1.setCaption("Category");
        xSuggest1.setExpression("#{item.category}");
        xSuggest1.setHandler("categoryModel");
        xSuggest1.setItemExpression("#{item.category}");
        xSuggest1.setName("entity.category"); // NOI18N
        xSuggest1.setPreferredSize(new java.awt.Dimension(0, 22));
        xSuggest1.setTextCase(com.rameses.rcp.constant.TextCase.UPPER);
        xFormPanel1.add(xSuggest1);

        xLabel1.setCellPadding(new java.awt.Insets(10, 0, 5, 0));
        xLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        xLabel1.setPadding(new java.awt.Insets(1, 0, 1, 1));
        xLabel1.setShowCaption(false);
        xLabel1.setText("Specify if this collection type is applicable only to the ff. org unit");
        xFormPanel1.add(xLabel1);

        xLookupField3.setCaption("          Org Unit");
        xLookupField3.setExpression("#{item.name}");
        xLookupField3.setHandler("org:lookup");
        xLookupField3.setName("entity.org"); // NOI18N
        xLookupField3.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel1.add(xLookupField3);

        xLabel2.setCellPadding(new java.awt.Insets(10, 0, 5, 0));
        xLabel2.setExpression("Specify if this collection type is applicable only to the ff. fund:");
        xLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        xLabel2.setPadding(new java.awt.Insets(1, 0, 1, 1));
        xLabel2.setShowCaption(false);
        xFormPanel1.add(xLabel2);

        xLookupField2.setCaption("          Fund");
        xLookupField2.setExpression("#{item.code} #{item.title}");
        xLookupField2.setHandler("fund:lookup");
        xLookupField2.setName("entity.fund"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel1.add(xLookupField2);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 508, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(" General Information  ", jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel5.setText("Limit account selection by the account below ");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        jPanel2.add(jLabel5, java.awt.BorderLayout.NORTH);

        xDataTable2.setHandler("accountModel");
        xDataTable2.setName("selectedAccount"); // NOI18N
        xDataTable2.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", null}
                , new Object[]{"caption", "Account"}
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
                , new Object[]{"expression", "#{item.account.code} - #{item.account.title}"}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LabelColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "valuetype"}
                , new Object[]{"caption", "Value Type"}
                , new Object[]{"width", 80}
                , new Object[]{"minWidth", 80}
                , new Object[]{"maxWidth", 80}
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
                new Object[]{"name", "defaultvalue"}
                , new Object[]{"caption", "Value"}
                , new Object[]{"width", 80}
                , new Object[]{"minWidth", 80}
                , new Object[]{"maxWidth", 80}
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
                new Object[]{"name", "tag"}
                , new Object[]{"caption", "Tag"}
                , new Object[]{"width", 80}
                , new Object[]{"minWidth", 80}
                , new Object[]{"maxWidth", 80}
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
                new Object[]{"name", "sortorder"}
                , new Object[]{"caption", "Sort Order"}
                , new Object[]{"width", 80}
                , new Object[]{"minWidth", 80}
                , new Object[]{"maxWidth", 80}
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
        jPanel2.add(xDataTable2, java.awt.BorderLayout.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        com.rameses.rcp.control.layout.XLayout xLayout1 = new com.rameses.rcp.control.layout.XLayout();
        xLayout1.setSpacing(5);
        jPanel3.setLayout(xLayout1);

        xButton1.setName("addAccount"); // NOI18N
        xButton1.setText("Add");
        jPanel3.add(xButton1);

        xButton2.setDepends(new String[] {"selectedAccount"});
        xButton2.setDisableWhen("#{selectedAccount == null}");
        xButton2.setName("editAccount"); // NOI18N
        xButton2.setText("Edit");
        jPanel3.add(xButton2);

        xButton3.setDepends(new String[] {"selectedAccount"});
        xButton3.setDisableWhen("#{selectedAccount == null}");
        xButton3.setName("removeAccount"); // NOI18N
        xButton3.setText("Remove");
        jPanel3.add(xButton3);

        jPanel2.add(jPanel3, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab(" Accounts  ", jPanel2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 397, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xCheckBox2ActionPerformed

    private void xCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xCheckBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xCheckBox3ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XCheckBox xCheckBox2;
    private com.rameses.rcp.control.XCheckBox xCheckBox3;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XDataTable xDataTable2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XLookupField xLookupField3;
    private com.rameses.rcp.control.XSuggest xSuggest1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}
