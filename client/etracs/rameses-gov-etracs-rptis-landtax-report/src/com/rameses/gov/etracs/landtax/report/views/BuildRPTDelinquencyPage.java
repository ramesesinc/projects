/*
 * RPTDelinquencyBuildPage.java
 *
 * Created on March 19, 2014, 9:30 PM
 */

package com.rameses.gov.etracs.landtax.report.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Rameses
 */
@Template(FormPage.class)
@StyleSheet
public class BuildRPTDelinquencyPage extends javax.swing.JPanel {
    
    /** Creates new form RPTDelinquencyBuildPage */
    public BuildRPTDelinquencyPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xPanel1 = new com.rameses.rcp.control.XPanel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        jPanel10 = new javax.swing.JPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xDateField3 = new com.rameses.rcp.control.XDateField();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xDateField4 = new com.rameses.rcp.control.XDateField();
        xLabel7 = new com.rameses.rcp.control.XLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();
        jPanel5 = new javax.swing.JPanel();
        panel1 = new java.awt.Panel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField3 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField4 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField6 = new com.rameses.rcp.control.XIntegerField();
        jPanel2 = new javax.swing.JPanel();
        xActionBar1 = new com.rameses.rcp.control.XActionBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        xList1 = new com.rameses.rcp.control.XList();
        jPanel9 = new javax.swing.JPanel();
        xDataTable4 = new com.rameses.rcp.control.XDataTable();
        xFormPanel7 = new com.rameses.rcp.control.XFormPanel();
        xButton1 = new com.rameses.rcp.control.XButton();
        xButton2 = new com.rameses.rcp.control.XButton();
        xButton3 = new com.rameses.rcp.control.XButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        jPanel6 = new javax.swing.JPanel();
        xDataTable3 = new com.rameses.rcp.control.XDataTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField5 = new com.rameses.rcp.control.XIntegerField();
        jScrollPane3 = new javax.swing.JScrollPane();
        xList2 = new com.rameses.rcp.control.XList();

        setLayout(new java.awt.BorderLayout());

        xPanel1.setDepends(new String[] {"processing", "msg"});
        xPanel1.setVisibleWhen("#{processing || msg != null}");
        xPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(new com.rameses.rcp.control.border.XUnderlineBorder(), javax.swing.BorderFactory.createEmptyBorder(0, 7, 0, 0)));
        xPanel1.setPreferredSize(new java.awt.Dimension(72, 34));
        xPanel1.setLayout(new java.awt.BorderLayout());

        xLabel4.setName("loadingicon"); // NOI18N
        xLabel4.setVisibleWhen("#{processing}");
        xLabel4.setIconResource("com/rameses/rcp/icons/loading16.gif");
        xPanel1.add(xLabel4, java.awt.BorderLayout.WEST);

        xLabel2.setExpression("#{msg}");
        xLabel2.setVisibleWhen("#{msg != null}");
        xLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel2.setForeground(new java.awt.Color(153, 0, 0));
        xPanel1.add(xLabel2, java.awt.BorderLayout.CENTER);

        add(xPanel1, java.awt.BorderLayout.NORTH);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(25, 10, 5, 5));
        xTitledBorder1.setTitle("Build Information");
        xFormPanel2.setBorder(xTitledBorder1);
        xFormPanel2.setCaptionWidth(120);

        xLabel5.setCaption("State");
        xLabel5.setExpression("#{entity.state}");
        xLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        xLabel5.setForeground(new java.awt.Color(255, 0, 0));
        xLabel5.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel2.add(xLabel5);

        xFormPanel5.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel5.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel5.setPreferredSize(new java.awt.Dimension(0, 21));
        xFormPanel5.setShowCaption(false);

        xDateField3.setCaption("Date Generated");
        xDateField3.setName("entity.dtgenerated"); // NOI18N
        xDateField3.setCaptionWidth(120);
        xDateField3.setCellPadding(new java.awt.Insets(0, 0, 0, 50));
        xDateField3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDateField3.setEnabled(false);
        xFormPanel5.add(xDateField3);

        xLabel6.setCaption("Generated By");
        xLabel6.setExpression("#{entity.generatedby.name}");
        xLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        xLabel6.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel5.add(xLabel6);

        xFormPanel2.add(xFormPanel5);

        xFormPanel6.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel6.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel6.setPreferredSize(new java.awt.Dimension(0, 21));
        xFormPanel6.setShowCaption(false);

        xDateField4.setCaption("Computation Date");
        xDateField4.setName("entity.dtcomputed"); // NOI18N
        xDateField4.setCaptionWidth(120);
        xDateField4.setCellPadding(new java.awt.Insets(0, 0, 5, 50));
        xDateField4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDateField4.setEnabled(false);
        xFormPanel6.add(xDateField4);

        xLabel7.setCaption("Job Title");
        xLabel7.setExpression("#{entity.generatedby.title}");
        xLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel7.setCellPadding(null);
        xLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        xLabel7.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel6.add(xLabel7);

        xFormPanel2.add(xFormPanel6);

        jPanel1.setLayout(new java.awt.BorderLayout());

        xDataTable1.setHandler("listHandler");
        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "barangay.name"}
                , new Object[]{"caption", "Barangay"}
                , new Object[]{"width", 200}
                , new Object[]{"minWidth", 200}
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
                new Object[]{"name", "count"}
                , new Object[]{"caption", "Record Count"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler("#,##0", -1, -1)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "processed"}
                , new Object[]{"caption", "Processed"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler("#,##0", -1, -1)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "errors"}
                , new Object[]{"caption", "Errors"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler("#,##0", -1, -1)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "ignored"}
                , new Object[]{"caption", "Ignored Errors"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler("#,##0", -1, -1)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "completed"}
                , new Object[]{"caption", "Completed?"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.CheckBoxColumnHandler(java.lang.Boolean.class, true, false)}
            })
        });
        jPanel1.add(xDataTable1, java.awt.BorderLayout.CENTER);

        jPanel5.setLayout(new java.awt.BorderLayout());

        panel1.setLayout(new java.awt.BorderLayout());

        xFormPanel3.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xFormPanel3.setCaptionWidth(100);
        xFormPanel3.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);

        xIntegerField2.setCaption("No. of Records");
        xIntegerField2.setName("ledgerCount"); // NOI18N
        xIntegerField2.setPattern("#,##0");
        xIntegerField2.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xIntegerField2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xIntegerField2.setEnabled(false);
        xIntegerField2.setPreferredSize(new java.awt.Dimension(80, 20));
        xFormPanel3.add(xIntegerField2);

        xIntegerField3.setCaption("Processed");
        xIntegerField3.setName("totalProcessed"); // NOI18N
        xIntegerField3.setPattern("#,##0");
        xIntegerField3.setCaptionWidth(70);
        xIntegerField3.setCellPadding(new java.awt.Insets(5, 10, 0, 0));
        xIntegerField3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xIntegerField3.setEnabled(false);
        xIntegerField3.setPreferredSize(new java.awt.Dimension(80, 20));
        xFormPanel3.add(xIntegerField3);

        xIntegerField4.setCaption("Errors");
        xIntegerField4.setName("totalErrors"); // NOI18N
        xIntegerField4.setPattern("#,##0");
        xIntegerField4.setCaptionWidth(50);
        xIntegerField4.setCellPadding(new java.awt.Insets(5, 10, 0, 0));
        xIntegerField4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xIntegerField4.setEnabled(false);
        xIntegerField4.setPreferredSize(new java.awt.Dimension(80, 20));
        xFormPanel3.add(xIntegerField4);

        xIntegerField6.setCaption("Ignored");
        xIntegerField6.setName("totalIgnored"); // NOI18N
        xIntegerField6.setPattern("#,##0");
        xIntegerField6.setCaptionWidth(60);
        xIntegerField6.setCellPadding(new java.awt.Insets(5, 10, 0, 0));
        xIntegerField6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xIntegerField6.setEnabled(false);
        xIntegerField6.setPreferredSize(new java.awt.Dimension(80, 20));
        xFormPanel3.add(xIntegerField6);

        panel1.add(xFormPanel3, java.awt.BorderLayout.EAST);

        jPanel5.add(panel1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel5, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Delinquency Detail", jPanel1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        xActionBar1.setName("errorActions"); // NOI18N
        jPanel2.add(xActionBar1, java.awt.BorderLayout.PAGE_START);

        xList1.setExpression("#{item.barangay.name}");
        xList1.setItems("barangaysWithErrors");
        xList1.setName("selectedErrorBrgy"); // NOI18N
        xList1.setDynamic(true);
        xList1.setPreferredSize(new java.awt.Dimension(150, 100));
        jScrollPane2.setViewportView(xList1);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.LINE_START);

        jPanel9.setLayout(new java.awt.BorderLayout());

        xDataTable4.setDepends(new String[] {"selectedErrorBrgy"});
        xDataTable4.setHandler("errorListHandler");
        xDataTable4.setName("selectedError"); // NOI18N
        xDataTable4.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "selected"}
                , new Object[]{"caption", "?"}
                , new Object[]{"width", 40}
                , new Object[]{"minWidth", 40}
                , new Object[]{"maxWidth", 40}
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
                new Object[]{"name", "rptledger.tdno"}
                , new Object[]{"caption", "TD No."}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 150}
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
                new Object[]{"name", "error"}
                , new Object[]{"caption", "Error Information"}
                , new Object[]{"width", 300}
                , new Object[]{"minWidth", 300}
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
        xDataTable4.setDynamic(true);
        jPanel9.add(xDataTable4, java.awt.BorderLayout.CENTER);

        xFormPanel7.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);

        xButton1.setDepends(new String[] {"selectedError"});
        xButton1.setDisableWhen("#{disableSelectAll}");
        xButton1.setName("selectAll"); // NOI18N
        xButton1.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xButton1.setShowCaption(false);
        xButton1.setText("Select All");
        xFormPanel7.add(xButton1);

        xButton2.setDepends(new String[] {"selectedError"});
        xButton2.setDisableWhen("#{disableDeselectAll}");
        xButton2.setName("deselectAll"); // NOI18N
        xButton2.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xButton2.setShowCaption(false);
        xButton2.setText("Deselect All");
        xFormPanel7.add(xButton2);

        xButton3.setDepends(new String[] {"selectedError"});
        xButton3.setDisableWhen("#{selectedError == null}");
        xButton3.setName("openLedger"); // NOI18N
        xButton3.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xButton3.setShowCaption(false);
        xButton3.setText("Open Ledger");
        xFormPanel7.add(xButton3);

        jPanel9.add(xFormPanel7, java.awt.BorderLayout.PAGE_END);

        jPanel2.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        xFormPanel1.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        xIntegerField1.setCaption("No. of Records");
        xIntegerField1.setDepends(new String[] {"selectedErrorBrgy"});
        xIntegerField1.setName("errorCount"); // NOI18N
        xIntegerField1.setCaptionWidth(110);
        xIntegerField1.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xIntegerField1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xIntegerField1.setEnabled(false);
        xFormPanel1.add(xIntegerField1);

        jPanel3.add(xFormPanel1, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel2.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("Errors", jPanel2);

        jPanel6.setLayout(new java.awt.BorderLayout());

        xDataTable3.setDepends(new String[] {"selectedIgnoredBrgy"});
        xDataTable3.setHandler("ignoredListHandler");
        xDataTable3.setName("selectedIgnoredError"); // NOI18N
        xDataTable3.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "rptledger.tdno"}
                , new Object[]{"caption", "TD No."}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 150}
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
                new Object[]{"name", "error"}
                , new Object[]{"caption", "Error Information"}
                , new Object[]{"width", 300}
                , new Object[]{"minWidth", 300}
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
        xDataTable3.setDynamic(true);
        jPanel6.add(xDataTable3, java.awt.BorderLayout.CENTER);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());

        xFormPanel4.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        xIntegerField5.setCaption("No. of Records");
        xIntegerField5.setDepends(new String[] {"selectedIgnoredBrgy"});
        xIntegerField5.setName("ignoredCount"); // NOI18N
        xIntegerField5.setCaptionWidth(110);
        xIntegerField5.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xIntegerField5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xIntegerField5.setEnabled(false);
        xFormPanel4.add(xIntegerField5);

        jPanel8.add(xFormPanel4, java.awt.BorderLayout.EAST);

        jPanel7.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        jPanel6.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        xList2.setExpression("#{item.barangay.name}");
        xList2.setItems("barangaysWithIgnoredErrors");
        xList2.setName("selectedIgnoredBrgy"); // NOI18N
        xList2.setDynamic(true);
        xList2.setPreferredSize(new java.awt.Dimension(150, 100));
        jScrollPane3.setViewportView(xList2);

        jPanel6.add(jScrollPane3, java.awt.BorderLayout.LINE_START);

        jTabbedPane1.addTab("Ignored Errors", jPanel6);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(326, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                    .addGap(119, 119, 119)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        add(jPanel10, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private java.awt.Panel panel1;
    private com.rameses.rcp.control.XActionBar xActionBar1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XDataTable xDataTable3;
    private com.rameses.rcp.control.XDataTable xDataTable4;
    private com.rameses.rcp.control.XDateField xDateField3;
    private com.rameses.rcp.control.XDateField xDateField4;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XFormPanel xFormPanel7;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XIntegerField xIntegerField3;
    private com.rameses.rcp.control.XIntegerField xIntegerField4;
    private com.rameses.rcp.control.XIntegerField xIntegerField5;
    private com.rameses.rcp.control.XIntegerField xIntegerField6;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLabel xLabel7;
    private com.rameses.rcp.control.XList xList1;
    private com.rameses.rcp.control.XList xList2;
    private com.rameses.rcp.control.XPanel xPanel1;
    // End of variables declaration//GEN-END:variables
    
}