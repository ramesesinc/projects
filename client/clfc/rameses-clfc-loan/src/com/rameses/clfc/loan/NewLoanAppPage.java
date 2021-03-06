/*
 * NewLoanAppPage.java
 *
 * Created on August 31, 2013, 3:10 PM
 */

package com.rameses.clfc.loan;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import java.math.BigDecimal;

/**
 *
 * @author  louie
 */
@StyleSheet
@Template(FormPage.class) 
public class NewLoanAppPage extends javax.swing.JPanel {
    
    public NewLoanAppPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLabel15 = new com.rameses.rcp.control.XLabel();
        xLabel16 = new com.rameses.rcp.control.XLabel();
        xLabel17 = new com.rameses.rcp.control.XLabel();
        jPanel2 = new javax.swing.JPanel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xNumberField1 = new com.rameses.rcp.control.XNumberField();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        jScrollPane3 = new javax.swing.JScrollPane();
        xTextArea3 = new com.rameses.rcp.control.XTextArea();
        xLabel12 = new com.rameses.rcp.control.XLabel();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Principal Borrower Information");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCellspacing(1);
        xFormPanel1.setPadding(new java.awt.Insets(5, 3, 0, 20));
        xLookupField1.setCaption("Name");
        xLookupField1.setCaptionWidth(100);
        xLookupField1.setExpression("#{item.name}");
        xLookupField1.setHandler("customerLookupHandler");
        xLookupField1.setIndex(-10);
        xLookupField1.setName("entity.borrower");
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xLabel1.setBackground(new java.awt.Color(250, 250, 250));
        com.rameses.rcp.control.border.XLineBorder xLineBorder1 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder1.setLineColor(new java.awt.Color(180, 180, 180));
        xLabel1.setBorder(xLineBorder1);
        xLabel1.setCaption("Address");
        xLabel1.setCaptionWidth(100);
        xLabel1.setDepends(new String[] {"entity.borrower"});
        xLabel1.setExpression("#{entity.borrower.address}");
        xLabel1.setOpaque(true);
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel1);

        xLabel2.setBackground(new java.awt.Color(250, 250, 250));
        com.rameses.rcp.control.border.XLineBorder xLineBorder2 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder2.setLineColor(new java.awt.Color(180, 180, 180));
        xLabel2.setBorder(xLineBorder2);
        xLabel2.setCaption("Birthdate");
        xLabel2.setCaptionWidth(100);
        xLabel2.setDepends(new String[] {"entity.borrower"});
        xLabel2.setExpression("#{entity.borrower.birthdate}");
        xLabel2.setOpaque(true);
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel2);

        xFormPanel2.setCellspacing(1);
        xFormPanel2.setPadding(new java.awt.Insets(5, 5, 0, 10));
        xLabel15.setBackground(new java.awt.Color(250, 250, 250));
        com.rameses.rcp.control.border.XLineBorder xLineBorder3 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder3.setLineColor(new java.awt.Color(180, 180, 180));
        xLabel15.setBorder(xLineBorder3);
        xLabel15.setCaption("Marital Status");
        xLabel15.setCaptionWidth(100);
        xLabel15.setDepends(new String[] {"entity.borrower"});
        xLabel15.setExpression("#{entity.borrower.civilstatus}");
        xLabel15.setOpaque(true);
        xLabel15.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel15);

        xLabel16.setBackground(new java.awt.Color(250, 250, 250));
        com.rameses.rcp.control.border.XLineBorder xLineBorder4 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder4.setLineColor(new java.awt.Color(180, 180, 180));
        xLabel16.setBorder(xLineBorder4);
        xLabel16.setCaption("Citizenship");
        xLabel16.setCaptionWidth(100);
        xLabel16.setDepends(new String[] {"entity.borrower"});
        xLabel16.setExpression("#{entity.borrower.citizenship}");
        xLabel16.setOpaque(true);
        xLabel16.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel16);

        xLabel17.setBackground(new java.awt.Color(250, 250, 250));
        com.rameses.rcp.control.border.XLineBorder xLineBorder5 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder5.setLineColor(new java.awt.Color(180, 180, 180));
        xLabel17.setBorder(xLineBorder5);
        xLabel17.setCaption("Contact No.");
        xLabel17.setCaptionWidth(100);
        xLabel17.setDepends(new String[] {"entity.borrower"});
        xLabel17.setExpression("#{entity.borrower.contactno}");
        xLabel17.setOpaque(true);
        xLabel17.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel17);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Loan Details");
        jPanel2.setBorder(xTitledBorder2);

        xFormPanel4.setCellspacing(1);
        xFormPanel4.setPadding(new java.awt.Insets(5, 3, 0, 20));
        xLabel3.setAntiAliasOn(true);
        xLabel3.setBackground(new java.awt.Color(0, 153, 204));
        xLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xLabel3.setCaption("App. Mode");
        xLabel3.setCaptionWidth(100);
        xLabel3.setExpression("#{entity.appmode}");
        xLabel3.setFontStyle("font-weight:bold;font-size:14;");
        xLabel3.setForeground(new java.awt.Color(255, 255, 255));
        xLabel3.setOpaque(true);
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel3);

        xComboBox3.setAllowNull(false);
        xComboBox3.setCaption("App. Type");
        xComboBox3.setCaptionWidth(100);
        xComboBox3.setExpression("#{item.value}");
        xComboBox3.setFontStyle("font-size:12");
        xComboBox3.setItemKey("value");
        xComboBox3.setItems("appTypes");
        xComboBox3.setName("entity.apptype");
        xComboBox3.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox3.setRequired(true);
        xFormPanel4.add(xComboBox3);

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Client Type");
        xComboBox1.setCaptionWidth(100);
        xComboBox1.setExpression("#{item.value}");
        xComboBox1.setFontStyle("font-size:12");
        xComboBox1.setImmediate(true);
        xComboBox1.setItemKey("value");
        xComboBox1.setItems("clientTypes");
        xComboBox1.setName("entity.clienttype");
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox1.setRequired(true);
        xFormPanel4.add(xComboBox1);

        xTextField1.setCaption("Interviewed By");
        xTextField1.setCaptionWidth(100);
        xTextField1.setDepends(new String[] {"entity.clienttype"});
        xTextField1.setName("entity.marketedby");
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xTextField1);

        xFormPanel5.setCellspacing(1);
        xFormPanel5.setPadding(new java.awt.Insets(5, 5, 0, 10));
        xComboBox2.setCaption("Product Type");
        xComboBox2.setCaptionWidth(100);
        xComboBox2.setExpression("#{item.name}");
        xComboBox2.setFontStyle("font-size:12");
        xComboBox2.setItems("productTypes");
        xComboBox2.setName("entity.producttype");
        xComboBox2.setPreferredSize(new java.awt.Dimension(210, 20));
        xComboBox2.setRequired(true);
        xFormPanel5.add(xComboBox2);

        xNumberField1.setCaption("Term");
        xNumberField1.setCaptionWidth(100);
        xNumberField1.setDepends(new String[] {"entity.producttype"});
        xNumberField1.setFieldType(Integer.class);
        xNumberField1.setFontStyle("font-size:12;");
        xNumberField1.setName("entity.producttype.term");
        xNumberField1.setPattern("#,##0");
        xNumberField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xNumberField1.setRequired(true);
        xFormPanel5.add(xNumberField1);

        xDecimalField1.setCaption("Amount Applied");
        xDecimalField1.setCaptionWidth(100);
        xDecimalField1.setFontStyle("font-size:14;");
        xDecimalField1.setName("entity.loanamount");
        xDecimalField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xDecimalField1.setRequired(true);
        xFormPanel5.add(xDecimalField1);

        xDateField1.setCaption("Date Released");
        xDateField1.setCaptionWidth(100);
        xDateField1.setFontStyle("font-size:12;");
        xDateField1.setName("entity.dtreleased");
        xDateField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xDateField1.setRequired(true);
        xFormPanel5.add(xDateField1);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(0, 50));
        xTextArea3.setCaption("Purpose of Loan");
        xTextArea3.setCaptionWidth(100);
        xTextArea3.setFontStyle("font-size:12");
        xTextArea3.setHint("Specify purpose of loan");
        xTextArea3.setName("entity.purpose");
        xTextArea3.setRequired(true);
        xTextArea3.setTextCase(com.rameses.rcp.constant.TextCase.UPPER);
        jScrollPane3.setViewportView(xTextArea3);

        com.rameses.rcp.control.border.XEtchedBorder xEtchedBorder1 = new com.rameses.rcp.control.border.XEtchedBorder();
        xEtchedBorder1.setHideLeft(true);
        xEtchedBorder1.setHideRight(true);
        xEtchedBorder1.setHideTop(true);
        xLabel12.setBorder(xEtchedBorder1);
        xLabel12.setFontStyle("font-weight:bold;");
        xLabel12.setForeground(new java.awt.Color(80, 80, 80));
        xLabel12.setText("Purpose of Loan");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                    .addComponent(xLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xFormPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xFormPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel12;
    private com.rameses.rcp.control.XLabel xLabel15;
    private com.rameses.rcp.control.XLabel xLabel16;
    private com.rameses.rcp.control.XLabel xLabel17;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XNumberField xNumberField1;
    private com.rameses.rcp.control.XTextArea xTextArea3;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
    
}
