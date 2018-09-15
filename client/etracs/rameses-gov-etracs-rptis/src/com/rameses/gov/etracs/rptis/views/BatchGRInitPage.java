package com.rameses.gov.etracs.rptis.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

@Template(FormPage.class)
@StyleSheet
public class BatchGRInitPage extends javax.swing.JPanel {
    
    /** Creates new form FAASNewPage */
    public BatchGRInitPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xLabel12 = new com.rameses.rcp.control.XLabel();
        xLabel13 = new com.rameses.rcp.control.XLabel();
        jPanel1 = new javax.swing.JPanel();
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xComboBox4 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox5 = new com.rameses.rcp.control.XComboBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xFormPanel11 = new com.rameses.rcp.control.XFormPanel();
        formPanel6 = new com.rameses.rcp.util.FormPanel();
        xLookupField3 = new com.rameses.rcp.control.XLookupField();
        xDateField5 = new com.rameses.rcp.control.XDateField();
        formPanel4 = new com.rameses.rcp.util.FormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xDateField3 = new com.rameses.rcp.control.XDateField();
        formPanel5 = new com.rameses.rcp.util.FormPanel();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xDateField4 = new com.rameses.rcp.control.XDateField();
        formPanel7 = new com.rameses.rcp.util.FormPanel();
        xLookupField4 = new com.rameses.rcp.control.XLookupField();
        xDateField6 = new com.rameses.rcp.control.XDateField();

        setLayout(new java.awt.BorderLayout());

        xFormPanel3.setName("statusBar"); // NOI18N
        xFormPanel3.setVisibleWhen("#{processing == true}");
        xFormPanel3.setCaptionPadding(new java.awt.Insets(5, 1, 0, 5));
        xFormPanel3.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);

        xLabel12.setVisibleWhen("#{processing==true}");
        xLabel12.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xLabel12.setIconResource("com/rameses/rcp/icons/loading16.gif");
        xLabel12.setShowCaption(false);
        xFormPanel3.add(xLabel12);

        xLabel13.setExpression("#{msg}");
        xLabel13.setName("msg"); // NOI18N
        xLabel13.setVisibleWhen("#{processing==true}");
        xLabel13.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel13.setForeground(new java.awt.Color(255, 0, 0));
        xLabel13.setShowCaption(false);
        xFormPanel3.add(xLabel13);

        add(xFormPanel3, java.awt.BorderLayout.NORTH);

        formPanel1.setCaptionWidth(150);
        formPanel1.setPadding(new java.awt.Insets(0, 5, 0, 0));

        xComboBox4.setCaption("LGU");
        xComboBox4.setExpression("#{item.name}");
        xComboBox4.setItems("lgus");
        xComboBox4.setName("entity.lgu"); // NOI18N
        xComboBox4.setAllowNull(false);
        xComboBox4.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox4.setRequired(true);
        formPanel1.add(xComboBox4);

        xComboBox3.setCaption("Barangay");
        xComboBox3.setDepends(new String[] {"entity.lgu"});
        xComboBox3.setExpression("#{item.name}");
        xComboBox3.setItems("barangays");
        xComboBox3.setName("entity.barangay"); // NOI18N
        xComboBox3.setAllowNull(false);
        xComboBox3.setDynamic(true);
        xComboBox3.setImmediate(true);
        xComboBox3.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox3.setRequired(true);
        formPanel1.add(xComboBox3);

        xComboBox1.setCaption("New Revision Year");
        xComboBox1.setDepends(new String[] {"params.lgu", "params.barangay"});
        xComboBox1.setItems("rylist");
        xComboBox1.setName("entity.ry"); // NOI18N
        xComboBox1.setAllowNull(false);
        xComboBox1.setDynamic(true);
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 22));
        xComboBox1.setRequired(true);
        formPanel1.add(xComboBox1);

        xComboBox2.setCaption("Property Type");
        xComboBox2.setExpression("#{item.name}");
        xComboBox2.setItems("rputypes");
        xComboBox2.setName("entity.rputype"); // NOI18N
        xComboBox2.setEmptyText("ALL");
        xComboBox2.setImmediate(true);
        xComboBox2.setPreferredSize(new java.awt.Dimension(150, 22));
        formPanel1.add(xComboBox2);

        xComboBox5.setCaption("Classification");
        xComboBox5.setExpression("#{item.name}");
        xComboBox5.setItems("classifications");
        xComboBox5.setName("entity.classification"); // NOI18N
        xComboBox5.setEmptyText("ALL");
        xComboBox5.setPreferredSize(new java.awt.Dimension(150, 22));
        formPanel1.add(xComboBox5);

        xTextField1.setCaption("Section");
        xTextField1.setName("entity.section"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(150, 20));
        formPanel1.add(xTextField1);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 70));

        xTextArea1.setCaption("Default Memoranda");
        xTextArea1.setLineWrap(true);
        xTextArea1.setName("entity.memoranda"); // NOI18N
        xTextArea1.setWrapStyleWord(true);
        xTextArea1.setPreferredSize(new java.awt.Dimension(0, 100));
        xTextArea1.setRequired(true);
        xTextArea1.setTextCase(com.rameses.rcp.constant.TextCase.UPPER);
        jScrollPane1.setViewportView(xTextArea1);

        formPanel1.add(jScrollPane1);

        xFormPanel11.setCaptionWidth(100);
        xFormPanel11.setCellspacing(1);
        xFormPanel11.setOpaque(true);
        xFormPanel11.setPadding(new java.awt.Insets(0, 5, 0, 0));

        formPanel6.setCaptionWidth(100);
        formPanel6.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        formPanel6.setPadding(new java.awt.Insets(0, 0, 0, 0));
        formPanel6.setPreferredSize(new java.awt.Dimension(0, 23));
        formPanel6.setShowCaption(false);

        xLookupField3.setCaption("Taxmapper");
        xLookupField3.setExpression("#{entity.taxmapper.name}");
        xLookupField3.setHandler("lookupTaxmapper");
        xLookupField3.setName("entity.taxmapper"); // NOI18N
        xLookupField3.setPreferredSize(new java.awt.Dimension(300, 21));
        formPanel6.add(xLookupField3);

        xDateField5.setCaption("Date Signed");
        xDateField5.setName("entity.taxmapper.dtsigned"); // NOI18N
        xDateField5.setCaptionWidth(90);
        xDateField5.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xDateField5.setPreferredSize(new java.awt.Dimension(100, 21));
        formPanel6.add(xDateField5);

        xFormPanel11.add(formPanel6);

        formPanel4.setCaptionWidth(100);
        formPanel4.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        formPanel4.setPadding(new java.awt.Insets(0, 0, 0, 0));
        formPanel4.setPreferredSize(new java.awt.Dimension(0, 22));
        formPanel4.setShowCaption(false);

        xLookupField1.setCaption("Appraiser");
        xLookupField1.setExpression("#{entity.appraiser.name}");
        xLookupField1.setHandler("lookupAppraiser");
        xLookupField1.setName("entity.appraiser"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(300, 21));
        xLookupField1.setRequired(true);
        formPanel4.add(xLookupField1);

        xDateField3.setCaption("Date Signed");
        xDateField3.setName("entity.appraiser.dtsigned"); // NOI18N
        xDateField3.setCaptionWidth(90);
        xDateField3.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xDateField3.setHint("Date Appraised");
        xDateField3.setPreferredSize(new java.awt.Dimension(100, 21));
        xDateField3.setRequired(true);
        formPanel4.add(xDateField3);

        xFormPanel11.add(formPanel4);

        formPanel5.setCaptionWidth(100);
        formPanel5.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        formPanel5.setPadding(new java.awt.Insets(0, 0, 0, 0));
        formPanel5.setPreferredSize(new java.awt.Dimension(0, 22));
        formPanel5.setShowCaption(false);

        xLookupField2.setCaption("Recommender");
        xLookupField2.setExpression("#{entity.recommender.name}");
        xLookupField2.setHandler("lookupRecommender");
        xLookupField2.setName("entity.recommender"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(300, 21));
        formPanel5.add(xLookupField2);

        xDateField4.setCaption("Date Signed");
        xDateField4.setName("entity.recommender.dtsigned"); // NOI18N
        xDateField4.setCaptionWidth(90);
        xDateField4.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xDateField4.setPreferredSize(new java.awt.Dimension(100, 21));
        formPanel5.add(xDateField4);

        xFormPanel11.add(formPanel5);

        formPanel7.setCaptionWidth(100);
        formPanel7.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        formPanel7.setPadding(new java.awt.Insets(0, 0, 0, 0));
        formPanel7.setPreferredSize(new java.awt.Dimension(0, 22));
        formPanel7.setShowCaption(false);

        xLookupField4.setCaption("Approver");
        xLookupField4.setExpression("#{entity.approver.name}");
        xLookupField4.setHandler("lookupApprover");
        xLookupField4.setName("entity.approver"); // NOI18N
        xLookupField4.setPreferredSize(new java.awt.Dimension(300, 21));
        xLookupField4.setRequired(true);
        formPanel7.add(xLookupField4);

        xDateField6.setCaption("Date Signed");
        xDateField6.setName("entity.approver.dtsigned"); // NOI18N
        xDateField6.setCaptionWidth(90);
        xDateField6.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xDateField6.setHint("Date Approved");
        xDateField6.setPreferredSize(new java.awt.Dimension(100, 21));
        xDateField6.setRequired(true);
        formPanel7.add(xDateField6);

        xFormPanel11.add(formPanel7);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 604, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xFormPanel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 619, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(135, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xFormPanel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private com.rameses.rcp.util.FormPanel formPanel4;
    private com.rameses.rcp.util.FormPanel formPanel5;
    private com.rameses.rcp.util.FormPanel formPanel6;
    private com.rameses.rcp.util.FormPanel formPanel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XComboBox xComboBox4;
    private com.rameses.rcp.control.XComboBox xComboBox5;
    private com.rameses.rcp.control.XDateField xDateField3;
    private com.rameses.rcp.control.XDateField xDateField4;
    private com.rameses.rcp.control.XDateField xDateField5;
    private com.rameses.rcp.control.XDateField xDateField6;
    private com.rameses.rcp.control.XFormPanel xFormPanel11;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XLabel xLabel12;
    private com.rameses.rcp.control.XLabel xLabel13;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XLookupField xLookupField3;
    private com.rameses.rcp.control.XLookupField xLookupField4;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
    
}