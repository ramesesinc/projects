/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rpt.faas.ui;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;


@Template(FormPage.class)
@StyleSheet
public class FaasPage extends javax.swing.JPanel {

    /**
     * Creates new form FaasTemplatePage
     */
    public FaasPage() {
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        xFormPanel7 = new com.rameses.rcp.control.XFormPanel();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xFormPanel8 = new com.rameses.rcp.control.XFormPanel();
        xTextField8 = new com.rameses.rcp.control.XTextField();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        xTextField9 = new com.rameses.rcp.control.XTextField();
        xFormPanel11 = new com.rameses.rcp.control.XFormPanel();
        xFormPanel12 = new com.rameses.rcp.control.XFormPanel();
        xTextField15 = new com.rameses.rcp.control.XTextField();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xTextField12 = new com.rameses.rcp.control.XTextField();
        xTextField13 = new com.rameses.rcp.control.XTextField();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xTextField10 = new com.rameses.rcp.control.XTextField();
        xTextField25 = new com.rameses.rcp.control.XTextField();
        xFormPanel15 = new com.rameses.rcp.control.XFormPanel();
        xTextField26 = new com.rameses.rcp.control.XTextField();
        xTextField27 = new com.rameses.rcp.control.XTextField();
        xSubFormPanel1 = new com.rameses.rcp.control.XSubFormPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        xTextArea2 = new com.rameses.rcp.control.XTextArea();
        xFormPanel9 = new com.rameses.rcp.control.XFormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xFormPanel13 = new com.rameses.rcp.control.XFormPanel();
        xCheckBox4 = new com.rameses.rcp.control.XCheckBox();
        xComboBox11 = new com.rameses.rcp.control.XComboBox();
        xFormPanel10 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField4 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField3 = new com.rameses.rcp.control.XIntegerField();
        xComboBox10 = new com.rameses.rcp.control.XComboBox();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        xFormPanel7.setCellspacing(1);
        xFormPanel7.setPadding(new java.awt.Insets(0, 5, 2, 5));
        xFormPanel7.setShowCaption(false);
        xFormPanel7.setStretchWidth(50);

        xTextField3.setCaption("ARP/TD No.");
        xTextField3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        xTextField3.setIndex(-100);
        xTextField3.setName("entity.tdno"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(300, 20));
        xFormPanel7.add(xTextField3);

        xFormPanel8.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel8.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel8.setShowCaption(false);
        xFormPanel8.setStretchWidth(100);

        xTextField8.setCaption("Title No.");
        xTextField8.setName("entity.titleno"); // NOI18N
        xTextField8.setStretchWidth(80);
        xFormPanel8.add(xTextField8);

        xDateField2.setCaption("Title Date");
        xDateField2.setCaptionWidth(60);
        xDateField2.setName("entity.titledate"); // NOI18N
        xDateField2.setPreferredSize(new java.awt.Dimension(85, 20));
        xDateField2.setStretchWidth(20);
        xFormPanel8.add(xDateField2);

        xFormPanel7.add(xFormPanel8);

        xTextField9.setCaption("Survey No.");
        xTextField9.setName("entity.rp.surveyno"); // NOI18N
        xTextField9.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel7.add(xTextField9);

        jPanel2.add(xFormPanel7, java.awt.BorderLayout.WEST);

        xFormPanel11.setCaptionWidth(79);
        xFormPanel11.setCellspacing(1);
        xFormPanel11.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel11.setStretchWidth(50);

        xFormPanel12.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel12.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel12.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel12.setShowCaption(false);

        xTextField15.setCaption("PIN");
        xTextField15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        xTextField15.setName("entity.fullpin"); // NOI18N
        xTextField15.setStretchWidth(60);
        xFormPanel12.add(xTextField15);

        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel3.setCaption("Txn Code");
        xLabel3.setCaptionWidth(60);
        xLabel3.setCellPadding(new java.awt.Insets(0, 2, 0, 0));
        xLabel3.setExpression("#{entity.txntype.objid}");
        xLabel3.setFontStyle("font-weight:bold;font-size:12;");
        xLabel3.setPreferredSize(new java.awt.Dimension(43, 20));
        xLabel3.setStretchWidth(10);
        xFormPanel12.add(xLabel3);

        xLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel4.setCaption("Doc No.");
        xLabel4.setCaptionWidth(60);
        xLabel4.setCellPadding(new java.awt.Insets(0, 2, 0, 0));
        xLabel4.setExpression("#{entity.trackingno}");
        xLabel4.setFontStyle("font-weight:bold;font-size:12;");
        xLabel4.setName("entity.trackingno"); // NOI18N
        xLabel4.setPreferredSize(new java.awt.Dimension(43, 20));
        xLabel4.setStretchWidth(20);
        xFormPanel12.add(xLabel4);

        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel2.setCellPadding(new java.awt.Insets(0, 2, 0, 0));
        xLabel2.setExpression("#{'ANNOTATED'}");
        xLabel2.setFontStyle("font-weight:bold;font-size:12;");
        xLabel2.setForeground(new java.awt.Color(204, 0, 0));
        xLabel2.setName("entity.annotated"); // NOI18N
        xLabel2.setPreferredSize(new java.awt.Dimension(43, 20));
        xLabel2.setShowCaption(false);
        xLabel2.setStretchWidth(20);
        xFormPanel12.add(xLabel2);

        xFormPanel11.add(xFormPanel12);

        xTextField12.setCaption("Lot No.");
        xTextField12.setName("entity.rp.cadastrallotno"); // NOI18N
        xTextField12.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel11.add(xTextField12);

        xTextField13.setCaption("Block No.");
        xTextField13.setName("entity.rp.blockno"); // NOI18N
        xTextField13.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel11.add(xTextField13);

        jPanel2.add(xFormPanel11, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        xFormPanel4.setCaptionWidth(100);
        xFormPanel4.setCellspacing(1);
        xFormPanel4.setPadding(new java.awt.Insets(0, 5, 0, 0));
        xFormPanel4.setPreferredSize(new java.awt.Dimension(0, 67));

        xFormPanel5.setCaptionWidth(110);
        xFormPanel5.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel5.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel5.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel5.setShowCaption(false);

        xLookupField2.setCaption("Property Owner");
        xLookupField2.setCellPadding(new java.awt.Insets(0, 0, 0, 3));
        xLookupField2.setExpression("#{entity.taxpayer.entityno} - #{entity.taxpayer.name}");
        xLookupField2.setHandler("lookupTaxpayer");
        xLookupField2.setName("entity.taxpayer"); // NOI18N
        xLookupField2.setStretchWidth(50);
        xFormPanel5.add(xLookupField2);

        xLabel1.setCaption("Address");
        xLabel1.setCaptionWidth(70);
        xLabel1.setExpression("#{entity.taxpayer.address}");
        xLabel1.setPadding(new java.awt.Insets(1, 2, 1, 1));
        xLabel1.setPreferredSize(new java.awt.Dimension(100, 18));
        xLabel1.setShowCaption(false);
        xLabel1.setStretchWidth(50);
        xFormPanel5.add(xLabel1);

        xFormPanel4.add(xFormPanel5);

        xFormPanel6.setCaptionWidth(110);
        xFormPanel6.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel6.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel6.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel6.setShowCaption(false);

        xTextField10.setCaption("Declared Owner");
        xTextField10.setCellPadding(new java.awt.Insets(0, 0, 0, 3));
        xTextField10.setName("entity.owner.name"); // NOI18N
        xTextField10.setStretchWidth(50);
        xFormPanel6.add(xTextField10);

        xTextField25.setCaption("Address");
        xTextField25.setCaptionWidth(70);
        xTextField25.setName("entity.owner.address"); // NOI18N
        xTextField25.setRequired(true);
        xTextField25.setShowCaption(false);
        xTextField25.setStretchWidth(50);
        xFormPanel6.add(xTextField25);

        xFormPanel4.add(xFormPanel6);

        xFormPanel15.setCaptionWidth(110);
        xFormPanel15.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel15.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel15.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel15.setShowCaption(false);

        xTextField26.setCaption("Administrator");
        xTextField26.setCellPadding(new java.awt.Insets(0, 0, 0, 3));
        xTextField26.setName("entity.administrator.name"); // NOI18N
        xTextField26.setStretchWidth(50);
        xFormPanel15.add(xTextField26);

        xTextField27.setCaption("Address");
        xTextField27.setCaptionWidth(70);
        xTextField27.setName("entity.administrator.address"); // NOI18N
        xTextField27.setShowCaption(false);
        xTextField27.setStretchWidth(50);
        xFormPanel15.add(xTextField27);

        xFormPanel4.add(xFormPanel15);

        jPanel1.add(xFormPanel4, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        xSubFormPanel1.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xSubFormPanel1.setDynamic(true);
        xSubFormPanel1.setHandler("rpuopener");
        xSubFormPanel1.setName("opener"); // NOI18N

        javax.swing.GroupLayout xSubFormPanel1Layout = new javax.swing.GroupLayout(xSubFormPanel1);
        xSubFormPanel1.setLayout(xSubFormPanel1Layout);
        xSubFormPanel1Layout.setHorizontalGroup(
            xSubFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 913, Short.MAX_VALUE)
        );
        xSubFormPanel1Layout.setVerticalGroup(
            xSubFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 235, Short.MAX_VALUE)
        );

        add(xSubFormPanel1, java.awt.BorderLayout.CENTER);

        jPanel6.setPreferredSize(new java.awt.Dimension(553, 75));
        jPanel6.setLayout(new java.awt.BorderLayout());

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Cancellation Notes");
        xTitledBorder1.setTitlePadding(new java.awt.Insets(2, 4, 0, 4));
        jScrollPane2.setBorder(xTitledBorder1);

        xTextArea2.setLineWrap(true);
        xTextArea2.setWrapStyleWord(true);
        xTextArea2.setCaption("Cancellation Notes");
        xTextArea2.setCaptionWidth(90);
        xTextArea2.setHint("Memoranda");
        xTextArea2.setName("entity.cancelnote"); // NOI18N
        xTextArea2.setRequired(true);
        xTextArea2.setShowCaption(false);
        jScrollPane2.setViewportView(xTextArea2);

        jPanel6.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        xFormPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xFormPanel9.setCaptionBorder(null);
        xFormPanel9.setCaptionWidth(100);

        xComboBox1.setCaption("Restriction");
        xComboBox1.setCellPadding(new java.awt.Insets(2, 0, 0, 0));
        xComboBox1.setItems("restrictions");
        xComboBox1.setName("entity.restrictionid"); // NOI18N
        xComboBox1.setStretchWidth(100);
        xFormPanel9.add(xComboBox1);

        xFormPanel13.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xFormPanel13.setCaptionWidth(110);
        xFormPanel13.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel13.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel13.setShowCaption(false);

        xCheckBox4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox4.setCaption("Taxability");
        xCheckBox4.setCaptionWidth(100);
        xCheckBox4.setName("entity.rpu.taxable"); // NOI18N
        xCheckBox4.setOpaque(false);
        xCheckBox4.setText("Taxable? ");
        xFormPanel13.add(xCheckBox4);

        xComboBox11.setCaption("Exempt Reason");
        xComboBox11.setCaptionWidth(100);
        xComboBox11.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xComboBox11.setDepends(new String[] {"entity.rpu.taxable"});
        xComboBox11.setDynamic(true);
        xComboBox11.setEnabled(false);
        xComboBox11.setExpression("#{item.name}");
        xComboBox11.setItems("exemptions");
        xComboBox11.setName("entity.rpu.exemptiontype"); // NOI18N
        xComboBox11.setOpaque(false);
        xComboBox11.setPreferredSize(new java.awt.Dimension(187, 20));
        xComboBox11.setStretchWidth(100);
        xFormPanel13.add(xComboBox11);

        xFormPanel9.add(xFormPanel13);

        xFormPanel10.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xFormPanel10.setCaptionWidth(110);
        xFormPanel10.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel10.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel10.setShowCaption(false);

        xIntegerField4.setCaption("Revision Year");
        xIntegerField4.setCaptionWidth(100);
        xIntegerField4.setCellPadding(new java.awt.Insets(0, 0, 0, 5));
        xIntegerField4.setEnabled(false);
        xIntegerField4.setName("entity.rpu.ry"); // NOI18N
        xIntegerField4.setPreferredSize(new java.awt.Dimension(70, 20));
        xIntegerField4.setStretchWidth(60);
        xFormPanel10.add(xIntegerField4);

        xIntegerField3.setCaption("Effectivity Year");
        xIntegerField3.setCaptionWidth(100);
        xIntegerField3.setCellPadding(new java.awt.Insets(0, 8, 0, 0));
        xIntegerField3.setName("entity.effectivityyear"); // NOI18N
        xIntegerField3.setPreferredSize(new java.awt.Dimension(70, 20));
        xIntegerField3.setRequired(true);
        xIntegerField3.setStretchWidth(60);
        xFormPanel10.add(xIntegerField3);

        xComboBox10.setCaption("Qtr");
        xComboBox10.setCaptionWidth(45);
        xComboBox10.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xComboBox10.setDepends(new String[] {"rpu.taxable"});
        xComboBox10.setItems("quarters");
        xComboBox10.setName("entity.effectivityqtr"); // NOI18N
        xComboBox10.setOpaque(false);
        xComboBox10.setPreferredSize(new java.awt.Dimension(60, 20));
        xComboBox10.setRequired(true);
        xComboBox10.setStretchWidth(40);
        xFormPanel10.add(xComboBox10);

        xFormPanel9.add(xFormPanel10);

        jPanel6.add(xFormPanel9, java.awt.BorderLayout.WEST);

        add(jPanel6, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private com.rameses.rcp.control.XCheckBox xCheckBox4;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox10;
    private com.rameses.rcp.control.XComboBox xComboBox11;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XFormPanel xFormPanel10;
    private com.rameses.rcp.control.XFormPanel xFormPanel11;
    private com.rameses.rcp.control.XFormPanel xFormPanel12;
    private com.rameses.rcp.control.XFormPanel xFormPanel13;
    private com.rameses.rcp.control.XFormPanel xFormPanel15;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XFormPanel xFormPanel7;
    private com.rameses.rcp.control.XFormPanel xFormPanel8;
    private com.rameses.rcp.control.XFormPanel xFormPanel9;
    private com.rameses.rcp.control.XIntegerField xIntegerField3;
    private com.rameses.rcp.control.XIntegerField xIntegerField4;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XSubFormPanel xSubFormPanel1;
    private com.rameses.rcp.control.XTextArea xTextArea2;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField12;
    private com.rameses.rcp.control.XTextField xTextField13;
    private com.rameses.rcp.control.XTextField xTextField15;
    private com.rameses.rcp.control.XTextField xTextField25;
    private com.rameses.rcp.control.XTextField xTextField26;
    private com.rameses.rcp.control.XTextField xTextField27;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField8;
    private com.rameses.rcp.control.XTextField xTextField9;
    // End of variables declaration//GEN-END:variables
}
