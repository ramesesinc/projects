/*
 * LoanLedgerCompromisePage.java
 *
 * Created on December 17, 2013, 4:15 PM
 */

package com.rameses.clfc.ledger.compromise;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  louie
 */
@StyleSheet
@Template(FormPage.class)
public class xLoanLedgerCompromisePage extends javax.swing.JPanel {
    
    /** Creates new form LoanLedgerCompromisePage */
    public xLoanLedgerCompromisePage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField5 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField4 = new com.rameses.rcp.control.XDecimalField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Compromise Information");
        xFormPanel1.setBorder(xTitledBorder1);
        xComboBox1.setCaption("Compromise Type");
        xComboBox1.setCaptionWidth(120);
        xComboBox1.setExpression("#{item.value}");
        xComboBox1.setItemKey("value");
        xComboBox1.setItems("compromiseTypes");
        xComboBox1.setName("data.compromisetype");
        xComboBox1.setPreferredSize(new java.awt.Dimension(170, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xDecimalField1.setCaption("Fixed Amount");
        xDecimalField1.setCaptionWidth(120);
        xDecimalField1.setDepends(new String[] {"data.compromisetype"});
        xDecimalField1.setName("data.fixedamount");
        xDecimalField1.setRequired(true);
        xFormPanel1.add(xDecimalField1);

        xDateField1.setCaption("Effective From");
        xDateField1.setCaptionWidth(120);
        xDateField1.setDepends(new String[] {"data.compromisetype"});
        xDateField1.setName("data.dteffectivefrom");
        xDateField1.setOutputFormat("MMM-dd-yyyy");
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        xDateField2.setCaption("Effective To");
        xDateField2.setCaptionWidth(120);
        xDateField2.setDepends(new String[] {"data.compromisetype"});
        xDateField2.setName("data.dteffectiveto");
        xDateField2.setOutputFormat("MMM-dd-yyyy");
        xDateField2.setRequired(true);
        xFormPanel1.add(xDateField2);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Ledger Information");
        xFormPanel2.setBorder(xTitledBorder2);
        com.rameses.rcp.control.border.XLineBorder xLineBorder1 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder1.setLineColor(new java.awt.Color(180, 180, 180));
        xLabel1.setBorder(xLineBorder1);
        xLabel1.setCaption("Borrower");
        xLabel1.setCaptionWidth(100);
        xLabel1.setExpression("#{entity.borrower.name}");
        xLabel1.setFont(new java.awt.Font("Courier New", 0, 12));
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel1);

        xDecimalField2.setBackground(new java.awt.Color(232, 232, 226));
        com.rameses.rcp.control.border.XLineBorder xLineBorder2 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder2.setLineColor(new java.awt.Color(180, 180, 180));
        xDecimalField2.setBorder(xLineBorder2);
        xDecimalField2.setCaption("Loan Amount");
        xDecimalField2.setCaptionWidth(100);
        xDecimalField2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField2.setEnabled(false);
        xDecimalField2.setName("entity.loanamount");
        xFormPanel2.add(xDecimalField2);

        xDecimalField5.setBackground(new java.awt.Color(232, 232, 232));
        com.rameses.rcp.control.border.XLineBorder xLineBorder3 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder3.setLineColor(new java.awt.Color(180, 180, 180));
        xDecimalField5.setBorder(xLineBorder3);
        xDecimalField5.setCaption("Balance");
        xDecimalField5.setCaptionWidth(100);
        xDecimalField5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField5.setEnabled(false);
        xDecimalField5.setName("entity.balance");
        xFormPanel2.add(xDecimalField5);

        xDecimalField3.setBackground(new java.awt.Color(232, 232, 226));
        com.rameses.rcp.control.border.XLineBorder xLineBorder4 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder4.setLineColor(new java.awt.Color(180, 180, 180));
        xDecimalField3.setBorder(xLineBorder4);
        xDecimalField3.setCaption("Interest");
        xDecimalField3.setCaptionWidth(100);
        xDecimalField3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField3.setEnabled(false);
        xDecimalField3.setName("entity.interest");
        xFormPanel2.add(xDecimalField3);

        xDecimalField4.setBackground(new java.awt.Color(232, 232, 226));
        com.rameses.rcp.control.border.XLineBorder xLineBorder5 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder5.setLineColor(new java.awt.Color(180, 180, 180));
        xDecimalField4.setBorder(xLineBorder5);
        xDecimalField4.setCaption("Overdue Penalty");
        xDecimalField4.setCaptionWidth(100);
        xDecimalField4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField4.setEnabled(false);
        xDecimalField4.setName("entity.overduepenalty");
        xFormPanel2.add(xDecimalField4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XDecimalField xDecimalField4;
    private com.rameses.rcp.control.XDecimalField xDecimalField5;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLabel xLabel1;
    // End of variables declaration//GEN-END:variables
    
}
