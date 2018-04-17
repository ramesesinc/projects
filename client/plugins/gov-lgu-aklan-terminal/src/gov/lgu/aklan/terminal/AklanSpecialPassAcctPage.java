/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.lgu.aklan.terminal;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author wflores 
 */
@StyleSheet 
@Template(FormPage.class)
public class AklanSpecialPassAcctPage extends javax.swing.JPanel {

    /**
     * Creates new form AklanSpecialPassAcctPage
     */
    public AklanSpecialPassAcctPage() {
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
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xSeparator1 = new com.rameses.rcp.control.XSeparator();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xTextField5 = new com.rameses.rcp.control.XTextField();
        xTextField6 = new com.rameses.rcp.control.XTextField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("  General Information   ");
        jPanel1.setBorder(xTitledBorder1);

        xTextField1.setCaption("Acct No.");
        xTextField1.setCaptionWidth(100);
        xTextField1.setFontStyle("font-size:16;");
        xTextField1.setName("entity.acctno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(300, 22));
        xTextField1.setRequired(true);
        xTextField1.setSpaceChar('_');
        xFormPanel1.add(xTextField1);

        xLookupField1.setCaption("Acct Type");
        xLookupField1.setCaptionWidth(100);
        xLookupField1.setExpression("#{item.title}");
        xLookupField1.setHandler("lookupAcctType");
        xLookupField1.setName("entity.accttype"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(300, 22));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xDateField1.setCaption("Expiry Date");
        xDateField1.setCaptionWidth(100);
        xDateField1.setName("entity.expirydate"); // NOI18N
        xDateField1.setOutputFormat("MMM-dd-yyyy");
        xDateField1.setPreferredSize(new java.awt.Dimension(100, 22));
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        xSeparator1.setCellPadding(new java.awt.Insets(5, 0, 5, 0));
        xSeparator1.setPreferredSize(new java.awt.Dimension(0, 20));

        javax.swing.GroupLayout xSeparator1Layout = new javax.swing.GroupLayout(xSeparator1);
        xSeparator1.setLayout(xSeparator1Layout);
        xSeparator1Layout.setHorizontalGroup(
            xSeparator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 442, Short.MAX_VALUE)
        );
        xSeparator1Layout.setVerticalGroup(
            xSeparator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        xFormPanel1.add(xSeparator1);

        xTextField2.setCaption("Name");
        xTextField2.setCaptionWidth(100);
        xTextField2.setName("entity.name"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(300, 22));
        xTextField2.setRequired(true);
        xTextField2.setTextCase(com.rameses.rcp.constant.TextCase.NONE);
        xFormPanel1.add(xTextField2);

        xTextField3.setCaption("Address");
        xTextField3.setCaptionWidth(100);
        xTextField3.setName("entity.address"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(300, 22));
        xTextField3.setRequired(true);
        xTextField3.setTextCase(com.rameses.rcp.constant.TextCase.NONE);
        xFormPanel1.add(xTextField3);

        xComboBox1.setCaption("Gender");
        xComboBox1.setCaptionWidth(100);
        xComboBox1.setExpression("#{item.value}");
        xComboBox1.setItemKey("key");
        xComboBox1.setItems("genderTypes");
        xComboBox1.setName("entity.gender"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(100, 22));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xComboBox2.setCaption("ID Type");
        xComboBox2.setCaptionWidth(100);
        xComboBox2.setExpression("#{item.value}");
        xComboBox2.setItemKey("key");
        xComboBox2.setItems("idTypes");
        xComboBox2.setName("entity.idtype"); // NOI18N
        xComboBox2.setOpaque(false);
        xComboBox2.setPreferredSize(new java.awt.Dimension(300, 22));
        xComboBox2.setRequired(true);
        xFormPanel1.add(xComboBox2);

        xTextField4.setCaption("ID No.");
        xTextField4.setCaptionWidth(100);
        xTextField4.setName("entity.idno"); // NOI18N
        xTextField4.setPreferredSize(new java.awt.Dimension(300, 22));
        xTextField4.setRequired(true);
        xTextField4.setTextCase(com.rameses.rcp.constant.TextCase.NONE);
        xFormPanel1.add(xTextField4);

        xTextField5.setCaption("Citizenship");
        xTextField5.setCaptionWidth(100);
        xTextField5.setName("entity.citizenship"); // NOI18N
        xTextField5.setPreferredSize(new java.awt.Dimension(300, 22));
        xTextField5.setTextCase(com.rameses.rcp.constant.TextCase.NONE);
        xFormPanel1.add(xTextField5);

        xTextField6.setCaption("Civil Status");
        xTextField6.setCaptionWidth(100);
        xTextField6.setName("entity.civilstatus"); // NOI18N
        xTextField6.setPreferredSize(new java.awt.Dimension(300, 22));
        xTextField6.setTextCase(com.rameses.rcp.constant.TextCase.NONE);
        xFormPanel1.add(xTextField6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XSeparator xSeparator1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField5;
    private com.rameses.rcp.control.XTextField xTextField6;
    // End of variables declaration//GEN-END:variables
}