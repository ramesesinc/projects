/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.clfc.audit.amnesty.online.update;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author louie
 */

@StyleSheet
@Template(FormPage.class)
public class AmnestyOnlineUpdatePage extends javax.swing.JPanel {

    /**
     * Creates new form AmnestyOnlineUpdatePage
     */
    public AmnestyOnlineUpdatePage() {
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

        xPanel1 = new com.rameses.rcp.control.XPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel9 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xLabel7 = new com.rameses.rcp.control.XLabel();
        xLabel8 = new com.rameses.rcp.control.XLabel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xLabel11 = new com.rameses.rcp.control.XLabel();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xLabel10 = new com.rameses.rcp.control.XLabel();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xFormPanel7 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        xLabel12 = new com.rameses.rcp.control.XLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xLabel13 = new com.rameses.rcp.control.XLabel();
        xLabel14 = new com.rameses.rcp.control.XLabel();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Amnesty Information");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCellspacing(3);
        xFormPanel1.setPadding(new java.awt.Insets(0, 5, 0, 5));

        xLookupField1.setCaption("Ref. No.");
        xLookupField1.setDisableWhen("#{mode=='read'}");
        xLookupField1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xLookupField1.setExpression("#{entity.amnesty.refno}");
        xLookupField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLookupField1.setHandler("amnestyLookup");
        xLookupField1.setPreferredSize(new java.awt.Dimension(200, 20));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xLabel1.setCaption("App. No.");
        xLabel1.setExpression("#{entity.amnesty.loanapp.appno}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel1);

        xLabel2.setCaption("Borrower");
        xLabel2.setExpression("#{entity.amnesty.borrower.name}");
        xLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel2);

        xLabel9.setCaption("Option");
        xLabel9.setExpression("#{entity.amnesty.amnestyoption}");
        xLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLabel9.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel9);

        xLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel3.setPadding(new java.awt.Insets(1, 5, 1, 1));
        xLabel3.setPreferredSize(new java.awt.Dimension(50, 20));
        xLabel3.setText("Offer");

        xLabel4.setCaption("Amount");
        xLabel4.setExpression("#{entity.amnesty.grantedoffer.amount}");
        xLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel4);

        xLabel5.setCaption("Months");
        xLabel5.setExpression("#{entity.amnesty.grantedoffer.months}");
        xLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLabel5.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel5);

        xLabel6.setCaption("Days");
        xLabel6.setExpression("#{entity.amnesty.grantedoffer.days}");
        xLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLabel6.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel6);

        xLabel7.setCaption("Is Spot Cash");
        xLabel7.setExpression("#{entity.amnesty.grantedoffer.isspotcash == 1? 'Yes' : 'No'}");
        xLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLabel7.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel7);

        xLabel8.setCaption("Date");
        xLabel8.setExpression("#{entity.amnesty.grantedoffer.date}");
        xLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLabel8.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel8);

        xLabel11.setCaption("End Date");
        xLabel11.setExpression("#{entity.amnesty.dtended}");
        xFormPanel3.add(xLabel11);

        xLabel10.setCaption("Start Date");
        xLabel10.setExpression("#{entity.amnesty.dtstarted}");
        xFormPanel5.add(xLabel10);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(xPanel1Layout.createSequentialGroup()
                        .addComponent(xLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(xPanel1Layout.createSequentialGroup()
                        .addGroup(xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(xFormPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(xFormPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(xFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))))
                .addContainerGap())
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(xPanel1Layout.createSequentialGroup()
                        .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(xLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(xPanel1Layout.createSequentialGroup()
                        .addComponent(xFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(37, 37, 37)))
                .addGroup(xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Update Information");
        xPanel2.setBorder(xTitledBorder2);

        xDateField1.setCaption("Start Date");
        xDateField1.setDisabledTextColor(java.awt.Color.black);
        xDateField1.setName("entity.update.dtstarted"); // NOI18N
        xDateField1.setOutputFormat("MMM-dd-yyyy");
        xDateField1.setRequired(true);
        xFormPanel6.add(xDateField1);

        xIntegerField1.setCaption("Months");
        xIntegerField1.setDisabledTextColor(java.awt.Color.black);
        xIntegerField1.setName("entity.update.months"); // NOI18N
        xIntegerField1.setRequired(true);
        xFormPanel6.add(xIntegerField1);

        xIntegerField2.setCaption("Days");
        xIntegerField2.setDisabledTextColor(java.awt.Color.black);
        xIntegerField2.setName("entity.update.days"); // NOI18N
        xIntegerField2.setRequired(true);
        xFormPanel6.add(xIntegerField2);

        xDecimalField1.setCaption("Amount");
        xDecimalField1.setDisabledTextColor(java.awt.Color.black);
        xDecimalField1.setName("entity.update.amount"); // NOI18N
        xDecimalField1.setRequired(true);
        xFormPanel7.add(xDecimalField1);

        xCheckBox1.setCaption("Is Spot Cash");
        xCheckBox1.setCheckValue(1);
        xCheckBox1.setName("entity.update.isspotcash"); // NOI18N
        xCheckBox1.setUncheckValue(0);
        xFormPanel7.add(xCheckBox1);

        xDateField2.setCaption("Date");
        xDateField2.setDisabledTextColor(java.awt.Color.black);
        xDateField2.setName("entity.update.date"); // NOI18N
        xDateField2.setOutputFormat("MMM-dd-yyyy");
        xDateField2.setRequired(true);
        xFormPanel7.add(xDateField2);

        xLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel12.setPadding(new java.awt.Insets(0, 5, 0, 0));
        xLabel12.setPreferredSize(new java.awt.Dimension(70, 20));
        xLabel12.setText("Reason");

        xTextArea1.setLineWrap(true);
        xTextArea1.setWrapStyleWord(true);
        xTextArea1.setCaption("Reason");
        xTextArea1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xTextArea1.setName("entity.remarks"); // NOI18N
        xTextArea1.setRequired(true);
        jScrollPane1.setViewportView(xTextArea1);

        javax.swing.GroupLayout xPanel2Layout = new javax.swing.GroupLayout(xPanel2);
        xPanel2.setLayout(xPanel2Layout);
        xPanel2Layout.setHorizontalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(xPanel2Layout.createSequentialGroup()
                        .addComponent(xFormPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xFormPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                    .addGroup(xPanel2Layout.createSequentialGroup()
                        .addComponent(xLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        xPanel2Layout.setVerticalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xFormPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(xFormPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        xLabel13.setExpression("Status:");
        xLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLabel13.setPreferredSize(new java.awt.Dimension(45, 20));

        xLabel14.setExpression("#{entity.txnstate}");
        xLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel14.setForeground(new java.awt.Color(0, 0, 203));
        xLabel14.setPreferredSize(new java.awt.Dimension(200, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(xPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XFormPanel xFormPanel7;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel10;
    private com.rameses.rcp.control.XLabel xLabel11;
    private com.rameses.rcp.control.XLabel xLabel12;
    private com.rameses.rcp.control.XLabel xLabel13;
    private com.rameses.rcp.control.XLabel xLabel14;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLabel xLabel7;
    private com.rameses.rcp.control.XLabel xLabel8;
    private com.rameses.rcp.control.XLabel xLabel9;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel2;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    // End of variables declaration//GEN-END:variables
}
