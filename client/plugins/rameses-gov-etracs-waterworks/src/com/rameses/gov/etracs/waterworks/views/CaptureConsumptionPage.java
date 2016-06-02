/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author Rameses
 */
@Template(OKCancelPage.class)
@StyleSheet
public class CaptureConsumptionPage extends javax.swing.JPanel {

    /**
     * Creates new form CaptureConsumptionPage
     */
    public CaptureConsumptionPage() {
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
        xButton2 = new com.rameses.rcp.control.XButton();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel20 = new com.rameses.rcp.control.XLabel();
        xLabel13 = new com.rameses.rcp.control.XLabel();
        xLabel17 = new com.rameses.rcp.control.XLabel();
        xLabel14 = new com.rameses.rcp.control.XLabel();
        xLabel15 = new com.rameses.rcp.control.XLabel();
        xLabel16 = new com.rameses.rcp.control.XLabel();
        xPanel4 = new com.rameses.rcp.control.XPanel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField3 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField4 = new com.rameses.rcp.control.XIntegerField();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xButton1 = new com.rameses.rcp.control.XButton();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Billing Cycle Information");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setCaptionWidth(120);

        xButton2.setCellPadding(new java.awt.Insets(0, 0, 5, 0));
        xButton2.setImmediate(true);
        xButton2.setName("computeBillingCycle"); // NOI18N
        xButton2.setShowCaption(false);
        xButton2.setText("Get Billing Cycle");
        xFormPanel1.add(xButton2);

        xLabel2.setBackground(new java.awt.Color(245, 245, 245));
        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel2.setCaption("Sector");
        xLabel2.setExpression("#{entity.sector.objid}");
        xLabel2.setOpaque(true);
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel2);

        xLabel20.setBackground(new java.awt.Color(245, 245, 245));
        xLabel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel20.setCaption("Period");
        xLabel20.setExpression("#{info.year} - #{listTypes.months[ info.month -1 ].name}");
        xLabel20.setOpaque(true);
        xLabel20.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel20);

        xLabel13.setBackground(new java.awt.Color(245, 245, 245));
        xLabel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel13.setCaption("Begin Period");
        xLabel13.setExpression("#{billCycle.fromperiod}");
        xLabel13.setOpaque(true);
        xLabel13.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel13);

        xLabel17.setBackground(new java.awt.Color(245, 245, 245));
        xLabel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel17.setCaption("Ending Period");
        xLabel17.setExpression(" #{billCycle.toperiod} ");
        xLabel17.setOpaque(true);
        xLabel17.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel17);

        xLabel14.setBackground(new java.awt.Color(245, 245, 245));
        xLabel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel14.setCaption("Reading Date");
        xLabel14.setExpression("#{billCycle.readingdate} ");
        xLabel14.setOpaque(true);
        xLabel14.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel14);

        xLabel15.setBackground(new java.awt.Color(245, 245, 245));
        xLabel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel15.setCaption("Due Date");
        xLabel15.setExpression("#{billCycle.duedate} ");
        xLabel15.setOpaque(true);
        xLabel15.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel15);

        xLabel16.setBackground(new java.awt.Color(245, 245, 245));
        xLabel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel16.setCaption("Disconnection Date");
        xLabel16.setExpression("#{billCycle.disconnectiondate} ");
        xLabel16.setOpaque(true);
        xLabel16.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel16);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Consumption Information");
        xPanel4.setBorder(xTitledBorder2);

        xFormPanel4.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel4.setCaptionWidth(110);

        xIntegerField2.setCaption("Prev. Reading");
        xIntegerField2.setDepends(new String[] {"info.reading", "info.volume"});
        xIntegerField2.setName("info.prevreading"); // NOI18N
        xFormPanel4.add(xIntegerField2);

        xIntegerField3.setCaption("This Reading");
        xIntegerField3.setName("info.reading"); // NOI18N
        xFormPanel4.add(xIntegerField3);

        xIntegerField4.setCaption("Volume");
        xIntegerField4.setDepends(new String[] {"info.prevreading", "info.reading"});
        xIntegerField4.setName("info.volume"); // NOI18N
        xIntegerField4.setRequired(true);
        xFormPanel4.add(xIntegerField4);

        xCheckBox1.setName("info.postledger"); // NOI18N
        xCheckBox1.setOpaque(false);
        xCheckBox1.setText("Post To Ledger");

        xPanel2.setDepends(new String[] {"info.postledger"});
        xPanel2.setOpaque(false);
        xPanel2.setVisibleWhen("#{info.postledger == true}");

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setCaptionWidth(110);

        xButton1.setCaption(" ");
        xButton1.setCellPadding(new java.awt.Insets(0, 0, 2, 0));
        xButton1.setImmediate(true);
        xButton1.setName("computeAmount"); // NOI18N
        xButton1.setShowCaption(false);
        xButton1.setText("Compute Amount");
        xFormPanel2.add(xButton1);

        xLabel1.setBackground(new java.awt.Color(245, 245, 245));
        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel1.setCaption("Account");
        xLabel1.setExpression("#{info.item.title}");
        xLabel1.setOpaque(true);
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel1);

        xDecimalField2.setCaption("Amount");
        xDecimalField2.setEnabled(false);
        xDecimalField2.setName("info.amount"); // NOI18N
        xFormPanel2.add(xDecimalField2);

        xDecimalField3.setCaption("Amount Paid");
        xDecimalField3.setName("info.amtpaid"); // NOI18N
        xDecimalField3.setRequired(true);
        xFormPanel2.add(xDecimalField3);

        javax.swing.GroupLayout xPanel2Layout = new javax.swing.GroupLayout(xPanel2);
        xPanel2.setLayout(xPanel2Layout);
        xPanel2Layout.setHorizontalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel2Layout.setVerticalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xFormPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout xPanel4Layout = new javax.swing.GroupLayout(xPanel4);
        xPanel4.setLayout(xPanel4Layout);
        xPanel4Layout.setHorizontalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .addComponent(xPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(xPanel4Layout.createSequentialGroup()
                        .addComponent(xCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        xPanel4Layout.setVerticalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XIntegerField xIntegerField3;
    private com.rameses.rcp.control.XIntegerField xIntegerField4;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel13;
    private com.rameses.rcp.control.XLabel xLabel14;
    private com.rameses.rcp.control.XLabel xLabel15;
    private com.rameses.rcp.control.XLabel xLabel16;
    private com.rameses.rcp.control.XLabel xLabel17;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel20;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel2;
    private com.rameses.rcp.control.XPanel xPanel4;
    // End of variables declaration//GEN-END:variables
}
