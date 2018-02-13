/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author ramesesinc
 */
@StyleSheet
@Template(CrudFormPage.class)
public class MigrationPage extends javax.swing.JPanel {

    /**
     * Creates new form MigrationPage
     */
    public MigrationPage() {
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

        jPanel3 = new javax.swing.JPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        jPanel1 = new javax.swing.JPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        jPanel2 = new javax.swing.JPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();
        jPanel4 = new javax.swing.JPanel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        xHtmlView1 = new com.rameses.rcp.control.XHtmlView();

        jPanel3.setLayout(new java.awt.BorderLayout());

        xLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 10));
        xLabel3.setFontStyle("font-size:12; font-weight:bold;");
        xLabel3.setForeground(new java.awt.Color(100, 100, 100));
        xLabel3.setText("General Information");
        jPanel3.add(xLabel3, java.awt.BorderLayout.NORTH);

        xFormPanel1.setPadding(new java.awt.Insets(10, 20, 10, 10));

        xTextField1.setCaption("Title");
        xTextField1.setName("entity.title"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 21));
        xFormPanel1.add(xTextField1);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 50));

        xTextArea1.setCaption("Remarks");
        xTextArea1.setName("entity.remarks"); // NOI18N
        xTextArea1.setPreferredSize(new java.awt.Dimension(75, 50));
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        xTextField3.setCaption("Status");
        xTextField3.setName("entity.state"); // NOI18N
        xTextField3.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xTextField3.setFontStyle("font-weight:bold; ");
        xTextField3.setForeground(new java.awt.Color(102, 102, 102));
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 21));
        xFormPanel1.add(xTextField3);

        xTextField2.setCaption("Posted By");
        xTextField2.setName("entity.createdby.name"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 21));
        xFormPanel1.add(xTextField2);

        xDateField1.setCaption("Date Posted");
        xDateField1.setName("entity.dtcreated"); // NOI18N
        xDateField1.setOutputFormat("yyyy-MM-dd");
        xDateField1.setPreferredSize(new java.awt.Dimension(100, 21));
        xFormPanel1.add(xDateField1);

        xIntegerField1.setCaption("Total Rows");
        xIntegerField1.setName("entity.totalrows"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(100, 21));
        xFormPanel1.add(xIntegerField1);

        jPanel3.add(xFormPanel1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new com.rameses.rcp.control.layout.YLayout());

        xLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 5, 10));
        xLabel1.setFontStyle("font-size:12; font-weight:bold;");
        xLabel1.setForeground(new java.awt.Color(100, 100, 100));
        xLabel1.setText("Selected File");
        jPanel1.add(xLabel1);

        xLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel4.setExpression("#{file.absolutePath}");
        xLabel4.setPreferredSize(new java.awt.Dimension(41, 21));
        jPanel1.add(xLabel4);

        jPanel2.setLayout(new java.awt.BorderLayout());

        xLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 5, 10));
        xLabel2.setFontStyle("font-size:12; font-weight:bold;");
        xLabel2.setForeground(new java.awt.Color(100, 100, 100));
        xLabel2.setText("Column Fields Mapping");
        jPanel2.add(xLabel2, java.awt.BorderLayout.NORTH);

        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "caption"}
                , new Object[]{"caption", "Field Name"}
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
                new Object[]{"name", "cell"}
                , new Object[]{"caption", "Cell Name"}
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
                , new Object[]{"expression", "#{item.cell.caption}"}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.ComboBoxColumnHandler("columnDefs", null, "#{item.caption}")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "defaultvalue"}
                , new Object[]{"caption", "Default Value"}
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
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "type"}
                , new Object[]{"caption", "Type"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 100}
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
        xDataTable1.setHandler("fieldListHandler");
        jPanel2.add(xDataTable1, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        com.rameses.rcp.control.layout.YLayout yLayout1 = new com.rameses.rcp.control.layout.YLayout();
        yLayout1.setAutoFill(true);
        jPanel4.setLayout(yLayout1);

        xLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 10));
        xLabel5.setFontStyle("font-size:12; font-weight:bold;");
        xLabel5.setForeground(new java.awt.Color(100, 100, 100));
        xLabel5.setText("Analyzed Data Overview");
        jPanel4.add(xLabel5);

        xHtmlView1.setName("html"); // NOI18N
        jScrollPane2.setViewportView(xHtmlView1);

        jPanel4.add(jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XHtmlView xHtmlView1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
}
