/*
 * PreAssessmentPage.java
 *
 * Created on July 24, 2014, 3:18 PM
 */

package com.rameses.gov.etracs.bpls.view;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Elmo
 */
@Template(FormPage.class)
public class PreAssessmentPage extends javax.swing.JPanel {
    
    /** Creates new form PreAssessmentPage */
    public PreAssessmentPage() {
        initComponents();
       
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xActionBar2 = new com.rameses.rcp.control.XActionBar();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xComboBox5 = new com.rameses.rcp.control.XComboBox();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox4 = new com.rameses.rcp.control.XComboBox();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xDateField3 = new com.rameses.rcp.control.XDateField();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();
        jLabel1 = new javax.swing.JLabel();
        xActionBar1 = new com.rameses.rcp.control.XActionBar();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        xDataTable2 = new com.rameses.rcp.control.XDataTable();
        xDataTable4 = new com.rameses.rcp.control.XDataTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField4 = new com.rameses.rcp.control.XDecimalField();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField5 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField6 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField7 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField8 = new com.rameses.rcp.control.XDecimalField();
        xButton1 = new com.rameses.rcp.control.XButton();
        xDataTable3 = new com.rameses.rcp.control.XDataTable();

        xFormPanel1.setCaptionWidth(120);

        xTextField1.setCaption("Business Name");
        xTextField1.setName("entity.businessname"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        xTextField3.setCaption("Business Address");
        xTextField3.setName("entity.businessaddress"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField3.setRequired(true);
        xFormPanel1.add(xTextField3);

        xComboBox5.setAllowNull(false);
        xComboBox5.setCaption("Address Type");
        xComboBox5.setExpression("#{item.caption}");
        xComboBox5.setItems("addressTypes");
        xComboBox5.setName("entity.business.address.type"); // NOI18N
        xComboBox5.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel1.add(xComboBox5);

        xLookupField1.setCaption("Barangay");
        xLookupField1.setExpression("#{entity.business.address.barangay.name}");
        xLookupField1.setHandler("barangay:lookup");
        xLookupField1.setName("entity.business.address.barangay"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xTextField2.setCaption("Owner Name");
        xTextField2.setName("entity.ownername"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        xComboBox2.setCaption("Org");
        xComboBox2.setExpression("#{item.value}");
        xComboBox2.setItemKey("key");
        xComboBox2.setItems("orgTypes");
        xComboBox2.setName("entity.business.orgtype"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox2.setRequired(true);
        xFormPanel1.add(xComboBox2);

        xComboBox4.setCaption("App Type");
        xComboBox4.setItems("appTypes");
        xComboBox4.setName("entity.apptype"); // NOI18N
        xComboBox4.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox4.setRequired(true);
        xFormPanel1.add(xComboBox4);

        xIntegerField2.setCaption("App Year");
        xIntegerField2.setName("entity.appyear"); // NOI18N
        xIntegerField2.setPreferredSize(new java.awt.Dimension(50, 20));
        xIntegerField2.setRequired(true);
        xFormPanel1.add(xIntegerField2);

        xFormPanel4.setCaptionPadding(new java.awt.Insets(0, 0, 0, 5));
        xFormPanel4.setCellspacing(0);
        xFormPanel4.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel4.setPadding(new java.awt.Insets(0, 0, 5, 5));
        xFormPanel4.setShowCaption(false);
        xFormPanel1.add(xFormPanel4);

        xDateField2.setCaption("Date Applied");
        xDateField2.setName("entity.dtfiled"); // NOI18N
        xDateField2.setRequired(true);
        xFormPanel1.add(xDateField2);

        xComboBox3.setCaption("Permit Type");
        xComboBox3.setEmptyText("- Select a Permit Type -");
        xComboBox3.setExpression("#{item.title}");
        xComboBox3.setItems("application.permitTypes");
        xComboBox3.setName("application.permitType"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox3.setRequired(true);
        xFormPanel1.add(xComboBox3);

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Office Type");
        xComboBox1.setDepends(new String[] {"application.permitType"});
        xComboBox1.setItems("application.officeTypes");
        xComboBox1.setName("entity.business.officetype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xDateField3.setCaption("Bill Date");
        xDateField3.setName("entity.billdate"); // NOI18N
        xDateField3.setRequired(true);
        xFormPanel1.add(xDateField3);

        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "name"}
                , new Object[]{"caption", "Line of Business"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "assessmenttype"}
                , new Object[]{"caption", "Assessment Type"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.ComboBoxColumnHandler("appTypes", null, null)}
            })
        });
        xDataTable1.setHandler("lob.listModel");
        xDataTable1.setName("lob.selectedItem"); // NOI18N
        xDataTable1.setPreferredSize(new java.awt.Dimension(0, 80));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Line of Business");

        xActionBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xActionBar1.setDepends(new String[] {"lob.selectedItem"});
        xActionBar1.setDynamic(true);
        xActionBar1.setName("lobActions"); // NOI18N
        xActionBar1.setUseToolBar(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 816, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xDataTable1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xActionBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 801, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(142, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xDataTable1, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xActionBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Business Information", jPanel1);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Business Information");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Assessment Information");

        xDataTable2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xDataTable2.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "lob.name"}
                , new Object[]{"caption", "Line of business"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LabelColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "attribute.caption"}
                , new Object[]{"caption", "Attribute Name"}
                , new Object[]{"width", 250}
                , new Object[]{"minWidth", 250}
                , new Object[]{"maxWidth", 250}
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
                new Object[]{"name", "value"}
                , new Object[]{"caption", "Value"}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 150}
                , new Object[]{"maxWidth", 150}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "RIGHT"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            })
        });
        xDataTable2.setHandler("assessmentInfo.infoModel");

        xDataTable4.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "lob.name"}
                , new Object[]{"caption", "Line of Business"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "attribute.caption"}
                , new Object[]{"caption", "Attribute Name"}
                , new Object[]{"width", 250}
                , new Object[]{"minWidth", 250}
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
                new Object[]{"name", "value"}
                , new Object[]{"caption", "Value"}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "RIGHT"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            })
        });
        xDataTable4.setHandler("appinfo.listModel");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xDataTable4, javax.swing.GroupLayout.DEFAULT_SIZE, 948, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xDataTable2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xDataTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xDataTable2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Information", jPanel2);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Taxes and Fees");

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("  TOTALS   ");
        jPanel4.setBorder(xTitledBorder1);

        xFormPanel3.setCellspacing(20);
        xFormPanel3.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel3.setPadding(new java.awt.Insets(0, 20, 0, 20));

        xDecimalField1.setCaption("Total Taxes");
        xDecimalField1.setCaptionWidth(80);
        xDecimalField1.setEnabled(false);
        xDecimalField1.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        xDecimalField1.setName("entity.totals.tax"); // NOI18N
        xDecimalField1.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel3.add(xDecimalField1);

        xDecimalField2.setCaption("Reg Fees");
        xDecimalField2.setCaptionWidth(80);
        xDecimalField2.setEnabled(false);
        xDecimalField2.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        xDecimalField2.setName("entity.totals.regfee"); // NOI18N
        xDecimalField2.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel3.add(xDecimalField2);

        xDecimalField3.setCaption("Other Charges/ Fees");
        xDecimalField3.setCaptionWidth(120);
        xDecimalField3.setEnabled(false);
        xDecimalField3.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        xDecimalField3.setName("entity.totals.othercharge"); // NOI18N
        xDecimalField3.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel3.add(xDecimalField3);

        xDecimalField4.setCaption("Total");
        xDecimalField4.setCaptionWidth(60);
        xDecimalField4.setEnabled(false);
        xDecimalField4.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        xDecimalField4.setName("entity.totals.total"); // NOI18N
        xDecimalField4.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel3.add(xDecimalField4);

        xFormPanel5.setCellspacing(20);
        xFormPanel5.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel5.setPadding(new java.awt.Insets(0, 20, 5, 20));

        xDecimalField5.setCaption("Q1");
        xDecimalField5.setCaptionWidth(80);
        xDecimalField5.setEnabled(false);
        xDecimalField5.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        xDecimalField5.setName("entity.totals.q1"); // NOI18N
        xDecimalField5.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel5.add(xDecimalField5);

        xDecimalField6.setCaption("Q2");
        xDecimalField6.setCaptionWidth(80);
        xDecimalField6.setEnabled(false);
        xDecimalField6.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        xDecimalField6.setName("entity.totals.q2"); // NOI18N
        xDecimalField6.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel5.add(xDecimalField6);

        xDecimalField7.setCaption("Q3");
        xDecimalField7.setCaptionWidth(120);
        xDecimalField7.setEnabled(false);
        xDecimalField7.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        xDecimalField7.setName("entity.totals.q3"); // NOI18N
        xDecimalField7.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel5.add(xDecimalField7);

        xDecimalField8.setCaption("Q4");
        xDecimalField8.setCaptionWidth(60);
        xDecimalField8.setEnabled(false);
        xDecimalField8.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        xDecimalField8.setName("entity.totals.q4"); // NOI18N
        xDecimalField8.setPreferredSize(new java.awt.Dimension(120, 20));
        xFormPanel5.add(xDecimalField8);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xFormPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        xButton1.setName("assessmentInfo.showDetails"); // NOI18N
        xButton1.setText("Show Details");

        xDataTable3.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "lob.name"}
                , new Object[]{"caption", "Line of Business"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "account.code"}
                , new Object[]{"caption", "Acct Code"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 120}
                , new Object[]{"maxWidth", 120}
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
                new Object[]{"name", "account.title"}
                , new Object[]{"caption", "Account Title"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "taxfeetype"}
                , new Object[]{"caption", "Type"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 100}
                , new Object[]{"maxWidth", 100}
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
                new Object[]{"name", "amount"}
                , new Object[]{"caption", "Amount"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 100}
                , new Object[]{"maxWidth", 100}
                , new Object[]{"required", false}
                , new Object[]{"resizable", false}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "RIGHT"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "discount"}
                , new Object[]{"caption", "Discount"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "RIGHT"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "surcharge"}
                , new Object[]{"caption", "Surcharge"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "RIGHT"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "interest"}
                , new Object[]{"caption", "Interest"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "RIGHT"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "total"}
                , new Object[]{"caption", "Total"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "RIGHT"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            })
        });
        xDataTable3.setHandler("assessmentInfo.taxfeeModel");
        xDataTable3.setName("assessmentInfo.selectedTaxfee"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 942, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(xDataTable3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 14, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xDataTable3, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Taxes and Fees", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.rameses.rcp.control.XActionBar xActionBar1;
    private com.rameses.rcp.control.XActionBar xActionBar2;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XComboBox xComboBox4;
    private com.rameses.rcp.control.XComboBox xComboBox5;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XDataTable xDataTable2;
    private com.rameses.rcp.control.XDataTable xDataTable3;
    private com.rameses.rcp.control.XDataTable xDataTable4;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDateField xDateField3;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XDecimalField xDecimalField4;
    private com.rameses.rcp.control.XDecimalField xDecimalField5;
    private com.rameses.rcp.control.XDecimalField xDecimalField6;
    private com.rameses.rcp.control.XDecimalField xDecimalField7;
    private com.rameses.rcp.control.XDecimalField xDecimalField8;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}