/*
 * RPTCertificationPage.java
 *
 * Created on July 18, 2011, 11:16 AM
 */
package com.rameses.gov.etracs.rpt.report.certification;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import java.math.BigDecimal;

@Template(FormPage.class)
@StyleSheet
public class BIRCertificationPage extends javax.swing.JPanel {
    
    /** Creates new form RPTCertificationPage */
    public BIRCertificationPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField8 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        xTextArea2 = new com.rameses.rcp.control.XTextArea();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xTextField9 = new com.rameses.rcp.control.XTextField();
        xTextField10 = new com.rameses.rcp.control.XTextField();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xTextField11 = new com.rameses.rcp.control.XTextField();
        xTextField12 = new com.rameses.rcp.control.XTextField();
        xSeparator1 = new com.rameses.rcp.control.XSeparator();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xTextField6 = new com.rameses.rcp.control.XTextField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xNumberField1 = new com.rameses.rcp.control.XNumberField();
        xNumberField2 = new com.rameses.rcp.control.XNumberField();
        jPanel3 = new javax.swing.JPanel();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();
        jLabel1 = new javax.swing.JLabel();
        xButton1 = new com.rameses.rcp.control.XButton();
        xButton2 = new com.rameses.rcp.control.XButton();

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Certification Detail");
        formPanel1.setBorder(xTitledBorder1);

        xIntegerField1.setCaption("As of Year");
        xIntegerField1.setCaptionWidth(135);
        xIntegerField1.setName("entity.asofyear"); // NOI18N
        xIntegerField1.setRequired(true);
        formPanel1.add(xIntegerField1);

        xLookupField1.setCaption("Taxpayer");
        xLookupField1.setCaptionWidth(135);
        xLookupField1.setDepends(new String[] {"entity.taxpayer", "entity.tdno", "entity.certtype"});
        xLookupField1.setExpression("#{entity.taxpayer.name}");
        xLookupField1.setHandler("lookupTaxpayer");
        xLookupField1.setName("entity.taxpayer"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 21));
        xLookupField1.setRequired(true);
        formPanel1.add(xLookupField1);

        com.rameses.rcp.control.border.XLineBorder xLineBorder1 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder1.setLineColor(new java.awt.Color(153, 153, 153));
        xLabel3.setBorder(xLineBorder1);
        xLabel3.setCaption("Address");
        xLabel3.setCaptionWidth(135);
        xLabel3.setDepends(new String[] {"entity.taxpayer", "entity.tdno", "entity.certtype"});
        xLabel3.setExpression("#{entity.taxpayer.address}");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 21));
        formPanel1.add(xLabel3);

        xTextField2.setCaption("Requested By");
        xTextField2.setCaptionWidth(135);
        xTextField2.setDepends(new String[] {"entity.taxpayer", "entity.tdno"});
        xTextField2.setName("entity.requestedby"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 21));
        xTextField2.setRequired(true);
        formPanel1.add(xTextField2);

        xTextField8.setCaption("Address");
        xTextField8.setCaptionWidth(135);
        xTextField8.setDepends(new String[] {"entity.taxpayer", "entity.tdno"});
        xTextField8.setName("entity.requestedbyaddress"); // NOI18N
        xTextField8.setPreferredSize(new java.awt.Dimension(0, 21));
        xTextField8.setRequired(true);
        formPanel1.add(xTextField8);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 50));

        xTextArea1.setLineWrap(true);
        xTextArea1.setWrapStyleWord(true);
        xTextArea1.setCaption("Purpose");
        xTextArea1.setCaptionWidth(135);
        xTextArea1.setName("entity.purpose"); // NOI18N
        xTextArea1.setPreferredSize(new java.awt.Dimension(120, 60));
        xTextArea1.setRequired(true);
        jScrollPane1.setViewportView(xTextArea1);

        formPanel1.add(jScrollPane1);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(0, 50));

        xTextArea2.setLineWrap(true);
        xTextArea2.setWrapStyleWord(true);
        xTextArea2.setCaption("Additional Info");
        xTextArea2.setCaptionWidth(135);
        xTextArea2.setName("entity.addlinfo"); // NOI18N
        xTextArea2.setPreferredSize(new java.awt.Dimension(120, 60));
        jScrollPane2.setViewportView(xTextArea2);

        formPanel1.add(jScrollPane2);

        xFormPanel1.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel1.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel1.setShowCaption(false);

        xTextField3.setCaption("<html><u>C</u>ertified By:<font color=\"red\">*</font></html>");
        xTextField3.setCaptionWidth(135);
        xTextField3.setName("entity.certifiedby"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(200, 21));
        xTextField3.setRequired(true);
        xFormPanel1.add(xTextField3);

        xTextField4.setCaption("Job Title:");
        xTextField4.setCaptionWidth(70);
        xTextField4.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xTextField4.setName("entity.certifiedbytitle"); // NOI18N
        xTextField4.setPreferredSize(new java.awt.Dimension(200, 21));
        xTextField4.setRequired(true);
        xFormPanel1.add(xTextField4);

        formPanel1.add(xFormPanel1);

        xFormPanel2.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel2.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel2.setShowCaption(false);

        xTextField9.setCaption("Authority of");
        xTextField9.setCaptionWidth(135);
        xTextField9.setName("entity.byauthority"); // NOI18N
        xTextField9.setPreferredSize(new java.awt.Dimension(200, 21));
        xFormPanel2.add(xTextField9);

        xTextField10.setCaption("Job Title:");
        xTextField10.setCaptionWidth(70);
        xTextField10.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xTextField10.setName("entity.byauthoritytitle"); // NOI18N
        xTextField10.setPreferredSize(new java.awt.Dimension(200, 21));
        xFormPanel2.add(xTextField10);

        formPanel1.add(xFormPanel2);

        xFormPanel3.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel3.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel3.setShowCaption(false);

        xTextField11.setCaption("Attested By");
        xTextField11.setCaptionWidth(135);
        xTextField11.setName("entity.attestedby"); // NOI18N
        xTextField11.setPreferredSize(new java.awt.Dimension(200, 21));
        xFormPanel3.add(xTextField11);

        xTextField12.setCaption("Job Title:");
        xTextField12.setCaptionWidth(70);
        xTextField12.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xTextField12.setName("entity.attestedbytitle"); // NOI18N
        xTextField12.setPreferredSize(new java.awt.Dimension(200, 21));
        xFormPanel3.add(xTextField12);

        formPanel1.add(xFormPanel3);

        xSeparator1.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xSeparator1.setPreferredSize(new java.awt.Dimension(0, 10));

        org.jdesktop.layout.GroupLayout xSeparator1Layout = new org.jdesktop.layout.GroupLayout(xSeparator1);
        xSeparator1.setLayout(xSeparator1Layout);
        xSeparator1Layout.setHorizontalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 617, Short.MAX_VALUE)
        );
        xSeparator1Layout.setVerticalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 10, Short.MAX_VALUE)
        );

        formPanel1.add(xSeparator1);

        xCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox1.setCellPadding(new java.awt.Insets(0, 0, 10, 0));
        xCheckBox1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        xCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox1.setName("officialuse"); // NOI18N
        xCheckBox1.setOpaque(false);
        xCheckBox1.setShowCaption(false);
        xCheckBox1.setText("  Is for Official Use?");
        formPanel1.add(xCheckBox1);

        xTextField6.setCaption("O.R. No.");
        xTextField6.setCaptionWidth(117);
        xTextField6.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xTextField6.setDepends(new String[] {"officialuse"});
        xTextField6.setName("entity.orno"); // NOI18N
        xTextField6.setPreferredSize(new java.awt.Dimension(150, 21));
        xTextField6.setRequired(true);
        formPanel1.add(xTextField6);

        xDateField1.setCaption("O.R. Date");
        xDateField1.setCaptionWidth(117);
        xDateField1.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xDateField1.setDepends(new String[] {"officialuse"});
        xDateField1.setName("entity.ordate"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(150, 21));
        xDateField1.setRequired(true);
        formPanel1.add(xDateField1);

        xNumberField1.setCaption("O.R. Amount");
        xNumberField1.setCaptionWidth(117);
        xNumberField1.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xNumberField1.setDepends(new String[] {"officialuse"});
        xNumberField1.setFieldType(BigDecimal.class);
        xNumberField1.setName("entity.oramount"); // NOI18N
        xNumberField1.setPattern("#,##0.00");
        xNumberField1.setPreferredSize(new java.awt.Dimension(150, 21));
        formPanel1.add(xNumberField1);

        xNumberField2.setCaption("Stamp Amount");
        xNumberField2.setCaptionWidth(117);
        xNumberField2.setCellPadding(new java.awt.Insets(0, 20, 0, 0));
        xNumberField2.setFieldType(BigDecimal.class);
        xNumberField2.setName("entity.stampamount"); // NOI18N
        xNumberField2.setPattern("#,##0.00");
        xNumberField2.setPreferredSize(new java.awt.Dimension(150, 21));
        formPanel1.add(xNumberField2);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 631, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Certification Information", jPanel1);

        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "selected"}
                , new Object[]{"caption", "Include?"}
                , new Object[]{"width", 70}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.CheckBoxColumnHandler(java.lang.Boolean.class, true, false)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "state"}
                , new Object[]{"caption", "State"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "tdno"}
                , new Object[]{"caption", "TD No."}
                , new Object[]{"width", 180}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "fullpin"}
                , new Object[]{"caption", "PIN"}
                , new Object[]{"width", 200}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "rputype"}
                , new Object[]{"caption", "Kind"}
                , new Object[]{"width", 60}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "totalmv"}
                , new Object[]{"caption", "Market Value"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "totalav"}
                , new Object[]{"caption", "Assessed Value"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            })
        });
        xDataTable1.setHandler("listHandler");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setText("Select the properties to be included in this certification.");

        xButton1.setMnemonic('a');
        xButton1.setName("selectAll"); // NOI18N
        xButton1.setText("Select All");

        xButton2.setMnemonic('d');
        xButton2.setName("deselectAll"); // NOI18N
        xButton2.setText("Deselect All");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(xDataTable1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xDataTable1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Certified Properties", jPanel3);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XNumberField xNumberField1;
    private com.rameses.rcp.control.XNumberField xNumberField2;
    private com.rameses.rcp.control.XSeparator xSeparator1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextArea xTextArea2;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField11;
    private com.rameses.rcp.control.XTextField xTextField12;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField6;
    private com.rameses.rcp.control.XTextField xTextField8;
    private com.rameses.rcp.control.XTextField xTextField9;
    // End of variables declaration//GEN-END:variables
    
}