/*
 * DefaultRequirementTypeHandlerPage.java
 *
 * Created on February 23, 2014, 3:32 PM
 */

package com.rameses.gov.etracs.rptis.master.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Rameses
 */
@Template(FormPage.class)
@StyleSheet
public class RequirementTypeDefaultHandlerPage extends javax.swing.JPanel {
    
    /** Creates new form DefaultRequirementTypeHandlerPage */
    public RequirementTypeDefaultHandlerPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xLabel1 = new com.rameses.rcp.control.XLabel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xDataTable2 = new com.rameses.rcp.control.XDataTable();
        xActionBar1 = new com.rameses.rcp.control.XActionBar();

        xLabel1.setExpression("#{caption}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        xLabel1.setPadding(new java.awt.Insets(5, 5, 5, 5));

        xFormPanel1.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xFormPanel1.setCaptionWidth(100);
        xFormPanel1.setPadding(new java.awt.Insets(0, 10, 5, 5));

        xCheckBox1.setName("entity.complied"); // NOI18N
        xCheckBox1.setShowCaption(false);
        xCheckBox1.setText("Complied?");
        xFormPanel1.add(xCheckBox1);

        xTextField1.setCaption("Ref. No.");
        xTextField1.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xTextField1.setName("entity.value.txnno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xTextField1);

        xDateField1.setCaption("Ref. Date");
        xDateField1.setName("entity.value.txndate"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xDateField1);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 60));

        xTextArea1.setCaption("Remarks");
        xTextArea1.setName("entity.value.remarks"); // NOI18N
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Support Documents");
        xDataTable2.setBorder(xTitledBorder1);
        xDataTable2.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "title"}
                , new Object[]{"caption", "Title"}
                , new Object[]{"width", 200}
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
                new Object[]{"name", "filesize"}
                , new Object[]{"caption", "Filesize"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler(null, -1, -1)}
            })
        });
        xDataTable2.setHandler("listHandler");
        xDataTable2.setName("selectedItem"); // NOI18N

        xActionBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xActionBar1.setDepends(new String[] {"selectedItem"});
        xActionBar1.setName("supportActions"); // NOI18N
        xActionBar1.setUseToolBar(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(xDataTable2, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE))
                    .addComponent(xLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(xActionBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(xLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xDataTable2, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xActionBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XActionBar xActionBar1;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XDataTable xDataTable2;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
    
}
