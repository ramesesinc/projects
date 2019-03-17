/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rptis.views;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

@Template(CrudFormPage.class)
@StyleSheet
public class RPTTrackingPage extends javax.swing.JPanel {

    /**
     * Creates new form RPTTrackingPage
     */
    public RPTTrackingPage() {
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

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField3 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField4 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField5 = new com.rameses.rcp.control.XIntegerField();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(30, 10, 10, 10));
        xTitledBorder1.setTitle("Acknowledgement Information");
        xFormPanel1.setBorder(xTitledBorder1);
        xFormPanel1.setCaptionWidth(120);

        xLabel1.setCaption("Tracking No");
        xLabel1.setExpression("#{entity.trackingno}");
        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 21));
        xFormPanel1.add(xLabel1);

        xDateField2.setCaption("Date Filed");
        xDateField2.setDisableWhen("#{true}");
        xDateField2.setName("entity.dtfiled"); // NOI18N
        xDateField2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDateField2.setEnabled(false);
        xDateField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xDateField2);

        xLookupField1.setCaption("Property Owner");
        xLookupField1.setExpression("#{item.name}");
        xLookupField1.setHandler("entity:lookup");
        xLookupField1.setName("entity.taxpayer"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xComboBox1.setCaption("Transaction Type");
        xComboBox1.setExpression("#{item.name}");
        xComboBox1.setItems("txntypes");
        xComboBox1.setName("entity.txntype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xTextField2.setCaption("PIN");
        xTextField2.setName("entity.pin"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xTextField2);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 70));

        xTextArea1.setCaption("Remarks");
        xTextArea1.setLineWrap(true);
        xTextArea1.setName("entity.remarks"); // NOI18N
        xTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        xDateField1.setCaption("Release Date");
        xDateField1.setName("entity.releasedate"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(120, 20));
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        xComboBox2.setCaption("Mode of Release");
        xComboBox2.setItems("releaseModes");
        xComboBox2.setName("entity.releasemode"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(120, 20));
        xComboBox2.setRequired(true);
        xFormPanel1.add(xComboBox2);

        xTextField1.setCaption("Received By");
        xTextField1.setName("entity.receivedby"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        xFormPanel2.setCaption("Document Count");
        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setPadding(new java.awt.Insets(30, 10, 10, 10));
        xTitledBorder2.setTitle("Kind of Property");
        xFormPanel2.setBorder(xTitledBorder2);
        xFormPanel2.setCaptionWidth(100);
        xFormPanel2.setPreferredSize(new java.awt.Dimension(0, 150));
        xFormPanel2.setShowCaption(false);

        xIntegerField1.setCaption("Land");
        xIntegerField1.setName("entity.landcount"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(50, 20));
        xIntegerField1.setRequired(true);
        xFormPanel2.add(xIntegerField1);

        xIntegerField2.setCaption("Building");
        xIntegerField2.setName("entity.bldgcount"); // NOI18N
        xIntegerField2.setPreferredSize(new java.awt.Dimension(50, 20));
        xIntegerField2.setRequired(true);
        xFormPanel2.add(xIntegerField2);

        xIntegerField3.setCaption("Machinery");
        xIntegerField3.setName("entity.machcount"); // NOI18N
        xIntegerField3.setPreferredSize(new java.awt.Dimension(50, 20));
        xIntegerField3.setRequired(true);
        xFormPanel2.add(xIntegerField3);

        xIntegerField4.setCaption("Plant/Tree");
        xIntegerField4.setName("entity.planttreecount"); // NOI18N
        xIntegerField4.setPreferredSize(new java.awt.Dimension(50, 20));
        xIntegerField4.setRequired(true);
        xFormPanel2.add(xIntegerField4);

        xIntegerField5.setCaption("Miscellaneous");
        xIntegerField5.setName("entity.misccount"); // NOI18N
        xIntegerField5.setPreferredSize(new java.awt.Dimension(50, 20));
        xIntegerField5.setRequired(true);
        xFormPanel2.add(xIntegerField5);

        xDataTable1.setHandler("listHandler");
        xDataTable1.setVisibleWhen("#{mode=='read'}");
        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("Tracking Information");
        xDataTable1.setBorder(xTitledBorder3);
        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "dtfiled"}
                , new Object[]{"caption", "Date"}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 150}
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
                new Object[]{"name", "action"}
                , new Object[]{"caption", "Action"}
                , new Object[]{"width", 200}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 200}
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
                new Object[]{"name", "assignee.name"}
                , new Object[]{"caption", "Assigned To"}
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(xDataTable1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xDataTable1, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XIntegerField xIntegerField3;
    private com.rameses.rcp.control.XIntegerField xIntegerField4;
    private com.rameses.rcp.control.XIntegerField xIntegerField5;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}
