/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.financial.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author dell
 */
@Template(CrudFormPage.class)
public class ItemAccountPage extends javax.swing.JPanel {

    /**
     * Creates new form ItemAccountPage
     */
    public ItemAccountPage() {
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

        xDataTable1 = new com.rameses.rcp.control.XDataTable();
        xTabbedPane1 = new com.rameses.rcp.control.XTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        schemaList1 = new com.rameses.seti2.components.SchemaList();
        jPanel7 = new javax.swing.JPanel();
        xButton5 = new com.rameses.rcp.control.XButton();
        xButton11 = new com.rameses.rcp.control.XButton();
        xButton12 = new com.rameses.rcp.control.XButton();
        xButton13 = new com.rameses.rcp.control.XButton();
        xPanel1 = new com.rameses.rcp.control.XPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xDataTable2 = new com.rameses.rcp.control.XDataTable();

        setPreferredSize(new java.awt.Dimension(685, 424));

        xTabbedPane1.setItems("sections");
        xTabbedPane1.setDynamic(true);

        xFormPanel1.setCaptionWidth(120);
        xFormPanel1.setPadding(new java.awt.Insets(10, 5, 5, 5));

        xLabel3.setCaption("Parent Account");
        xLabel3.setExpression("#{ entity.parentaccount.code } #{ entity.parentaccount.title }");
        xLabel3.setVisibleWhen("#{ entity.parentid != null }");
        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel3);

        xTextField1.setCaption("Acct Code");
        xTextField1.setName("entity.code"); // NOI18N
        xTextField1.setEnabled(false);
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField1.setRequired(true);
        xTextField1.setStretchWidth(100);
        xFormPanel1.add(xTextField1);

        xTextField2.setCaption("Acct Title");
        xTextField2.setName("entity.title"); // NOI18N
        xTextField2.setEnabled(false);
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setRequired(true);
        xTextField2.setStretchWidth(100);
        xFormPanel1.add(xTextField2);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 42));

        xTextArea1.setCaption("Description");
        xTextArea1.setEnabled(false);
        xTextArea1.setExitOnTabKey(true);
        xTextArea1.setName("entity.description"); // NOI18N
        xTextArea1.setStretchWidth(100);
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        xLookupField2.setCaption("Fund");
        xLookupField2.setExpression("#{item.code} #{item.title}");
        xLookupField2.setHandler("fund:all:lookup");
        xLookupField2.setName("entity.fund"); // NOI18N
        xLookupField2.setVisibleWhen("#{ !entity.type.matches('CASH_IN_TREASURY|CASH_IN_BANK') }");
        xLookupField2.setEnabled(false);
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField2.setRequired(true);
        xLookupField2.setStretchWidth(100);
        xFormPanel1.add(xLookupField2);

        xComboBox1.setCaption("Item Type");
        xComboBox1.setItems("listTypes.type");
        xComboBox1.setName("entity.type"); // NOI18N
        xComboBox1.setVisibleWhen("#{ mode == 'create' && defaultType == null }");
        xComboBox1.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xComboBox1.setStretchWidth(100);
        xFormPanel1.add(xComboBox1);

        xLabel1.setCaption("Item Type");
        xLabel1.setExpression("#{entity.type}");
        xLabel1.setVisibleWhen("#{ mode != 'create' || defaultType != null }");
        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel1.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel1);

        xLabel2.setCaption("Status");
        xLabel2.setExpression("#{ entity.state }");
        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel2);

        xLookupField1.setCaption("Org");
        xLookupField1.setExpression("#{ item.code } #{ item.title }");
        xLookupField1.setHandler("org:lookup");
        xLookupField1.setName("entity.org"); // NOI18N
        xLookupField1.setVisibleWhen("#{ entity.parentid != null }");
        xLookupField1.setCellPadding(new java.awt.Insets(20, 0, 0, 0));
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField1);

        xCheckBox1.setCaption("");
        xCheckBox1.setCheckValue(1);
        xCheckBox1.setName("entity.generic"); // NOI18N
        xCheckBox1.setUncheckValue(0);
        xCheckBox1.setVisibleWhen("#{ entity.parentid == null }");
        xCheckBox1.setCellPadding(new java.awt.Insets(20, 0, 0, 0));
        xCheckBox1.setText("  Use as Generic Account");
        xFormPanel1.add(xCheckBox1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        xTabbedPane1.addTab("General", jPanel1);

        xPanel2.setVisibleWhen("#{ mode!='create' && entity.generic == 1  }");
        xPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        xPanel2.setLayout(new java.awt.BorderLayout());

        schemaList1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "code"}
                , new Object[]{"caption", "Code"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 100}
                , new Object[]{"maxWidth", 150}
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
                new Object[]{"name", "title"}
                , new Object[]{"caption", "Title"}
                , new Object[]{"width", 200}
                , new Object[]{"minWidth", 200}
                , new Object[]{"maxWidth", 250}
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
                new Object[]{"name", "org.objid"}
                , new Object[]{"caption", "Org ID"}
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
                new Object[]{"name", "org.name"}
                , new Object[]{"caption", "Org Name"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 100}
                , new Object[]{"maxWidth", 150}
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
                new Object[]{"name", "fund.code"}
                , new Object[]{"caption", "Fund Code"}
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
        schemaList1.setCustomFilter("parentid = :objid");
        schemaList1.setHandlerName("listHandler");
        schemaList1.setQueryName("entity");
        schemaList1.setSchemaName("itemaccount");
        schemaList1.setMultiSelect(true);
        xPanel2.add(schemaList1, java.awt.BorderLayout.CENTER);

        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        com.rameses.rcp.control.layout.XLayout xLayout1 = new com.rameses.rcp.control.layout.XLayout();
        xLayout1.setSpacing(5);
        jPanel7.setLayout(xLayout1);

        xButton5.setDepends(new String[] {"selectedItem"});
        xButton5.setName("addSubItems"); // NOI18N
        xButton5.setPreferredSize(new java.awt.Dimension(160, 20));
        xButton5.setText("Add SubItems");
        jPanel7.add(xButton5);

        xButton11.setName("removeSubItems"); // NOI18N
        xButton11.setText("Remove SubItems");
        jPanel7.add(xButton11);

        xButton12.setDepends(new String[] {"selectedall"});
        xButton12.setName("selectAll"); // NOI18N
        xButton12.setVisibleWhen("#{ false }");
        xButton12.setText("Select All");
        jPanel7.add(xButton12);

        xButton13.setDepends(new String[] {"selectedall"});
        xButton13.setName("deselectAll"); // NOI18N
        xButton13.setVisibleWhen("#{ false }");
        xButton13.setText("Deselect All");
        jPanel7.add(xButton13);

        xPanel2.add(jPanel7, java.awt.BorderLayout.SOUTH);

        xTabbedPane1.addTab("Sub Accounts", xPanel2);

        xPanel1.setVisibleWhen("#{ mode != 'create' }");

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setCaptionWidth(100);
        xFormPanel2.setPadding(new java.awt.Insets(10, 5, 5, 5));

        xComboBox2.setAllowNull(false);
        xComboBox2.setCaption("Value Type");
        xComboBox2.setItems("listTypes.valuetype");
        xComboBox2.setName("entity.valuetype"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox2.setRequired(true);
        xFormPanel2.add(xComboBox2);

        xDecimalField1.setCaption("Default Value");
        xDecimalField1.setName("entity.defaultvalue"); // NOI18N
        xDecimalField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xDecimalField1.setRequired(true);
        xFormPanel2.add(xDecimalField1);

        xDataTable2.setHandler("itemHandlers.tags");
        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Report Tags");
        xDataTable2.setBorder(xTitledBorder1);
        xDataTable2.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "tag"}
                , new Object[]{"caption", "Tag"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            })
        });

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xDataTable2, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE))
                .addContainerGap(326, Short.MAX_VALUE))
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xDataTable2, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addContainerGap())
        );

        xTabbedPane1.addTab("Options", xPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.seti2.components.SchemaList schemaList1;
    private com.rameses.rcp.control.XButton xButton11;
    private com.rameses.rcp.control.XButton xButton12;
    private com.rameses.rcp.control.XButton xButton13;
    private com.rameses.rcp.control.XButton xButton5;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XDataTable xDataTable2;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel2;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}