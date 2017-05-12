package com.rameses.gov.etracs.police.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

@Template(FormPage.class)
public class CashReceiptPage extends javax.swing.JPanel {
    
    /**
     * Creates new form CashReceiptPage
     */
    public CashReceiptPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xSeparator2 = new com.rameses.rcp.control.XSeparator();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();
        xSeparator1 = new com.rameses.rcp.control.XSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        xTextArea2 = new com.rameses.rcp.control.XTextArea();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Collection Information");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setPadding(new java.awt.Insets(10, 5, 5, 5));

        com.rameses.rcp.control.border.XLineBorder xLineBorder1 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder1.setLineColor(new java.awt.Color(153, 153, 153));
        xLabel2.setBorder(xLineBorder1);
        xLabel2.setCaption("Receipt No.");
        xLabel2.setCaptionWidth(130);
        xLabel2.setExpression("#{entity.receiptno}");
        xLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 28));
        xFormPanel1.add(xLabel2);

        xSeparator2.setPreferredSize(new java.awt.Dimension(0, 20));

        org.jdesktop.layout.GroupLayout xSeparator2Layout = new org.jdesktop.layout.GroupLayout(xSeparator2);
        xSeparator2.setLayout(xSeparator2Layout);
        xSeparator2Layout.setHorizontalGroup(
            xSeparator2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 520, Short.MAX_VALUE)
        );
        xSeparator2Layout.setVerticalGroup(
            xSeparator2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 20, Short.MAX_VALUE)
        );

        xFormPanel1.add(xSeparator2);

        xLookupField2.setCaption("Payer");
        xLookupField2.setCaptionWidth(130);
        xLookupField2.setExpression("#{item.name}");
        xLookupField2.setFontStyle("font-size:12;");
        xLookupField2.setHandler("lookupEntity");
        xLookupField2.setIndex(-1000);
        xLookupField2.setName("entity.payer"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 25));
        xLookupField2.setRequired(true);
        xLookupField2.setStretchWidth(100);
        xFormPanel1.add(xLookupField2);

        xTextField1.setCaption("Paid By");
        xTextField1.setCaptionMnemonic('b');
        xTextField1.setCaptionWidth(130);
        xTextField1.setFontStyle("font-size:12;");
        xTextField1.setName("entity.paidby"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 25));
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        xTextField2.setCaption("Address");
        xTextField2.setCaptionWidth(130);
        xTextField2.setFontStyle("font-size:12;");
        xTextField2.setName("entity.paidbyaddress"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 25));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        xComboBox1.setCaption("Type");
        xComboBox1.setCaptionWidth(130);
        xComboBox1.setCellPadding(new java.awt.Insets(15, 0, 0, 0));
        xComboBox1.setFontStyle("font-size:12;");
        xComboBox1.setItems("types");
        xComboBox1.setName("entity.app.type"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 25));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xDecimalField1.setEditable(false);
        xDecimalField1.setCaption("Amount Due");
        xDecimalField1.setCaptionWidth(130);
        xDecimalField1.setEnabled(false);
        xDecimalField1.setFontStyle("font-size:16;font-weight:bold;");
        xDecimalField1.setName("entity.amount"); // NOI18N
        xDecimalField1.setPreferredSize(new java.awt.Dimension(180, 25));
        xDecimalField1.setRequired(true);
        xFormPanel1.add(xDecimalField1);

        xDecimalField2.setCaption("Cash Received");
        xDecimalField2.setCaptionWidth(130);
        xDecimalField2.setFontStyle("font-size:16;font-weight:bold;");
        xDecimalField2.setName("entity.totalcash"); // NOI18N
        xDecimalField2.setPreferredSize(new java.awt.Dimension(180, 25));
        xDecimalField2.setRequired(true);
        xFormPanel1.add(xDecimalField2);

        xDecimalField3.setEditable(false);
        xDecimalField3.setCaption("Cash Change");
        xDecimalField3.setCaptionFontStyle("font-weight:bold;");
        xDecimalField3.setCaptionWidth(130);
        xDecimalField3.setEnabled(false);
        xDecimalField3.setFontStyle("font-size:16;font-weight:bold;");
        xDecimalField3.setName("entity.cashchange"); // NOI18N
        xDecimalField3.setPreferredSize(new java.awt.Dimension(180, 25));
        xDecimalField3.setRequired(true);
        xFormPanel1.add(xDecimalField3);

        xSeparator1.setPreferredSize(new java.awt.Dimension(0, 20));

        org.jdesktop.layout.GroupLayout xSeparator1Layout = new org.jdesktop.layout.GroupLayout(xSeparator1);
        xSeparator1.setLayout(xSeparator1Layout);
        xSeparator1Layout.setHorizontalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 520, Short.MAX_VALUE)
        );
        xSeparator1Layout.setVerticalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 20, Short.MAX_VALUE)
        );

        xFormPanel1.add(xSeparator1);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 40));

        xTextArea1.setLineWrap(true);
        xTextArea1.setWrapStyleWord(true);
        xTextArea1.setCaption("Purpose");
        xTextArea1.setCaptionWidth(130);
        xTextArea1.setFontStyle("font-size:12;");
        xTextArea1.setName("entity.app.purpose"); // NOI18N
        xTextArea1.setRequired(true);
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(0, 40));

        xTextArea2.setLineWrap(true);
        xTextArea2.setWrapStyleWord(true);
        xTextArea2.setCaption("Remarks");
        xTextArea2.setCaptionWidth(130);
        xTextArea2.setFontStyle("font-size:12;");
        xTextArea2.setName("entity.remarks"); // NOI18N
        jScrollPane2.setViewportView(xTextArea2);

        xFormPanel1.add(jScrollPane2);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XSeparator xSeparator1;
    private com.rameses.rcp.control.XSeparator xSeparator2;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextArea xTextArea2;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
    
}
