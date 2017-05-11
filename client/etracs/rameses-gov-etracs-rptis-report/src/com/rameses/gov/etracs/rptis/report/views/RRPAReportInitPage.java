/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rptis.report.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

@Template(FormPage.class)
@StyleSheet()
public class RRPAReportInitPage extends javax.swing.JPanel {

    /**
     * Creates new form ROAReportInitPage
     */
    public RRPAReportInitPage() {
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
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xComboBox7 = new com.rameses.rcp.control.XComboBox();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xComboBox4 = new com.rameses.rcp.control.XComboBox();
        xComboBox5 = new com.rameses.rcp.control.XComboBox();
        xComboBox6 = new com.rameses.rcp.control.XComboBox();
        xSeparator1 = new com.rameses.rcp.control.XSeparator();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xPanel1 = new com.rameses.rcp.control.XPanel();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();

        xFormPanel1.setCaptionWidth(110);
        xFormPanel1.setPadding(new java.awt.Insets(5, 10, 5, 5));

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Report Format");
        xComboBox1.setExpression("#{item.caption}");
        xComboBox1.setItems("reporttypes");
        xComboBox1.setName("entity.reporttype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(200, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xComboBox7.setAllowNull(false);
        xComboBox7.setCaption("Period Type");
        xComboBox7.setExpression("#{item.caption}");
        xComboBox7.setItems("periodtypes");
        xComboBox7.setName("entity.periodtype"); // NOI18N
        xComboBox7.setPreferredSize(new java.awt.Dimension(200, 20));
        xComboBox7.setRequired(true);
        xFormPanel1.add(xComboBox7);

        xIntegerField1.setCaption("Year");
        xIntegerField1.setDepends(new String[] {"entity.reporttype"});
        xIntegerField1.setName("entity.year"); // NOI18N
        xIntegerField1.setPattern("####");
        xIntegerField1.setPreferredSize(new java.awt.Dimension(200, 20));
        xIntegerField1.setRequired(true);
        xFormPanel1.add(xIntegerField1);

        xComboBox2.setAllowNull(false);
        xComboBox2.setCaption("Quarter");
        xComboBox2.setDepends(new String[] {"entity.periodtype"});
        xComboBox2.setDynamic(true);
        xComboBox2.setExpression("#{item.name}");
        xComboBox2.setImmediate(true);
        xComboBox2.setItems("quarters");
        xComboBox2.setName("entity.qtr"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(200, 20));
        xComboBox2.setRequired(true);
        xFormPanel1.add(xComboBox2);

        xComboBox3.setCaption("Month");
        xComboBox3.setDepends(new String[] {"entity.periodtype"});
        xComboBox3.setDynamic(true);
        xComboBox3.setExpression("#{item.caption}");
        xComboBox3.setItems("months");
        xComboBox3.setName("entity.month"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(200, 20));
        xComboBox3.setRequired(true);
        xFormPanel1.add(xComboBox3);

        xDateField1.setCaption("As of");
        xDateField1.setDepends(new String[] {"entity.periodtype"});
        xDateField1.setName("entity.asofdate"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(200, 20));
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        xComboBox4.setCaption("LGU");
        xComboBox4.setEmptyText("ALL");
        xComboBox4.setExpression("#{item.name}");
        xComboBox4.setItems("lgus");
        xComboBox4.setName("entity.lgu"); // NOI18N
        xComboBox4.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xComboBox4);

        xComboBox5.setCaption("Barangay");
        xComboBox5.setDepends(new String[] {"entity.lgu"});
        xComboBox5.setDynamic(true);
        xComboBox5.setExpression("#{item.name}");
        xComboBox5.setItems("barangays");
        xComboBox5.setName("entity.barangay"); // NOI18N
        xComboBox5.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xComboBox5);

        xComboBox6.setCaption("Classification");
        xComboBox6.setExpression("#{item.name}");
        xComboBox6.setItems("propertyClassifications");
        xComboBox6.setName("entity.classification"); // NOI18N
        xComboBox6.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xComboBox6);

        xSeparator1.setPreferredSize(new java.awt.Dimension(0, 20));

        javax.swing.GroupLayout xSeparator1Layout = new javax.swing.GroupLayout(xSeparator1);
        xSeparator1.setLayout(xSeparator1Layout);
        xSeparator1Layout.setHorizontalGroup(
            xSeparator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 387, Short.MAX_VALUE)
        );
        xSeparator1Layout.setVerticalGroup(
            xSeparator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        xFormPanel1.add(xSeparator1);

        xDecimalField1.setCaption("Basic Rate");
        xDecimalField1.setDepends(new String[] {"entity.reporttype"});
        xDecimalField1.setName("entity.basicrate"); // NOI18N
        xDecimalField1.setRequired(true);
        xFormPanel1.add(xDecimalField1);

        xDecimalField2.setCaption("SEF Rate");
        xDecimalField2.setDepends(new String[] {"entity.reporttype"});
        xDecimalField2.setName("entity.sefrate"); // NOI18N
        xDecimalField2.setRequired(true);
        xFormPanel1.add(xDecimalField2);

        xPanel1.setDepends(new String[] {"entity.reporttype"});
        xPanel1.setName("classifications"); // NOI18N
        xPanel1.setPreferredSize(new java.awt.Dimension(0, 150));
        xPanel1.setShowCaption(false);
        xPanel1.setVisibleWhen("#{entity.reporttype.type != 'standard'}");
        xPanel1.setLayout(new java.awt.BorderLayout());

        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "name"}
                , new Object[]{"caption", "Classification"}
                , new Object[]{"width", 200}
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
                new Object[]{"name", "basicrate"}
                , new Object[]{"caption", "Basic Rate (%)"}
                , new Object[]{"width", 110}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 4)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "sefrate"}
                , new Object[]{"caption", "SEF Rate (%)"}
                , new Object[]{"width", 110}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 4)}
            })
        });
        xDataTable1.setDepends(new String[] {"entity.reporttype"});
        xDataTable1.setHandler("listHandler");
        xDataTable1.setName("selectedItem"); // NOI18N
        xDataTable1.setPreferredSize(new java.awt.Dimension(0, 200));
        xDataTable1.setShowCaption(false);
        xPanel1.add(xDataTable1, java.awt.BorderLayout.CENTER);

        xFormPanel1.add(xPanel1);

        xPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        xPanel2.setVisibleWhen("#{mode == 'processing'}");
        xPanel2.setLayout(new java.awt.BorderLayout());

        xLabel3.setFontStyle("font-weight:bold;font-size:12;");
        xLabel3.setForeground(new java.awt.Color(51, 51, 51));
        xLabel3.setPadding(new java.awt.Insets(1, 5, 1, 1));
        xLabel3.setPreferredSize(new java.awt.Dimension(150, 20));
        xLabel3.setText("Processing request please wait...");
        xPanel2.add(xLabel3, java.awt.BorderLayout.CENTER);

        xLabel4.setIconResource("com/rameses/rcp/icons/loading16.gif");
        xPanel2.add(xLabel4, java.awt.BorderLayout.WEST);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(545, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XComboBox xComboBox4;
    private com.rameses.rcp.control.XComboBox xComboBox5;
    private com.rameses.rcp.control.XComboBox xComboBox6;
    private com.rameses.rcp.control.XComboBox xComboBox7;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel2;
    private com.rameses.rcp.control.XSeparator xSeparator1;
    // End of variables declaration//GEN-END:variables
}