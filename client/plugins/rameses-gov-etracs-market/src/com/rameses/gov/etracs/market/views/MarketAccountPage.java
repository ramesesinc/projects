/*
 * MarketApplication.java
 *
 * Created on March 17, 2014, 11:01 AM
 */

package com.rameses.gov.etracs.market.views;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author  Elmo
 */
@Template(CrudFormPage.class)
@StyleSheet
public class MarketAccountPage extends javax.swing.JPanel {
    
    /** Creates new form MarketApplication */
    public MarketAccountPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xTabbedPane1 = new com.rameses.rcp.control.XTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLabel8 = new com.rameses.rcp.control.XLabel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xLabel9 = new com.rameses.rcp.control.XLabel();
        xButton1 = new com.rameses.rcp.control.XButton();
        xLabel10 = new com.rameses.rcp.control.XLabel();
        xLabel11 = new com.rameses.rcp.control.XLabel();
        jPanel3 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        xLabel7 = new com.rameses.rcp.control.XLabel();
        xLabel17 = new com.rameses.rcp.control.XLabel();
        jPanel4 = new javax.swing.JPanel();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xLabel13 = new com.rameses.rcp.control.XLabel();
        xLabel14 = new com.rameses.rcp.control.XLabel();
        xLabel25 = new com.rameses.rcp.control.XLabel();
        xLabel16 = new com.rameses.rcp.control.XLabel();
        jPanel5 = new javax.swing.JPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xLabel12 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();

        xTabbedPane1.setDynamic(true);
        xTabbedPane1.setItems("sections");

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Rental Unit Information");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel2.setCaptionWidth(120);

        xLabel8.setCaption("Cluster");
        xLabel8.setExpression("#{entity.unit.cluster.name } - #{entity.unit.cluster.market.name}");
        xLabel8.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel8);

        xFormPanel4.setCaptionWidth(120);
        xFormPanel4.setCellspacing(0);
        xFormPanel4.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel4.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel4.setPreferredSize(new java.awt.Dimension(0, 25));
        xFormPanel4.setShowCaption(false);

        xLabel9.setCaption("Unit No");
        xLabel9.setExpression("#{entity.unitno}");
        xLabel9.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel9);

        xButton1.setName("viewUnit"); // NOI18N
        xButton1.setShowCaption(false);
        xButton1.setText("View");
        xFormPanel4.add(xButton1);

        xFormPanel2.add(xFormPanel4);

        xLabel10.setCaption("Rented Area (sqm)");
        xLabel10.setExpression("#{entity.unit.areasqm}");
        xLabel10.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel10);

        xLabel11.setCaption("Extension Area");
        xLabel11.setCellPadding(new java.awt.Insets(20, 0, 0, 0));
        xLabel11.setExpression("#{entity.extarea}");
        xLabel11.setPadding(new java.awt.Insets(0, 3, 1, 1));
        xLabel11.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel11);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(xFormPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Account Information");
        jPanel3.setBorder(xTitledBorder2);

        xFormPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xFormPanel1.setCaptionWidth(101);

        xLabel4.setCaption("Acct No");
        xLabel4.setExpression("#{entity.acctno}");
        xLabel4.setPreferredSize(new java.awt.Dimension(150, 18));
        xFormPanel1.add(xLabel4);

        xLabel5.setCaption("Acct Name");
        xLabel5.setExpression("#{entity.acctname}");
        xLabel5.setPreferredSize(new java.awt.Dimension(0, 18));
        xFormPanel1.add(xLabel5);

        xLabel6.setCaption("Owner");
        xLabel6.setExpression("#{entity.owner.name} - #{entity.owner.entityno}");
        xLabel6.setPreferredSize(new java.awt.Dimension(0, 18));
        xFormPanel1.add(xLabel6);

        xLabel7.setCaption("Owner Address");
        xLabel7.setExpression("#{entity.owner.address.text}");
        xLabel7.setPreferredSize(new java.awt.Dimension(0, 18));
        xFormPanel1.add(xLabel7);

        xLabel17.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        xLabel17.setCaption("Remarks");
        xLabel17.setExpression("#{entity.remarks}");
        xLabel17.setPreferredSize(new java.awt.Dimension(0, 30));
        xFormPanel1.add(xLabel17);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 25, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("Ledger Information");
        jPanel4.setBorder(xTitledBorder3);

        xFormPanel5.setCaptionWidth(120);

        xLabel13.setCaption("Mode of Payment");
        xLabel13.setExpression("#{entity.payfrequency}");
        xLabel13.setPreferredSize(new java.awt.Dimension(0, 18));
        xFormPanel5.add(xLabel13);

        xLabel14.setCaption("Start Date");
        xLabel14.setExpression("#{entity.dtstarted}");
        xLabel14.setPreferredSize(new java.awt.Dimension(0, 18));
        xFormPanel5.add(xLabel14);

        xLabel25.setCaption("Paid Until Date");
        xLabel25.setCellPadding(new java.awt.Insets(40, 0, 0, 0));
        xLabel25.setExpression("#{entity.lastdatepaid}");
        xLabel25.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        xLabel25.setPreferredSize(new java.awt.Dimension(0, 18));
        xFormPanel5.add(xLabel25);

        xLabel16.setCaption("Partial Balance");
        xLabel16.setExpression("#{entity.partialbalance}");
        xLabel16.setPreferredSize(new java.awt.Dimension(0, 18));
        xFormPanel5.add(xLabel16);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(18, 18, 18)
                .add(xFormPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 341, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 178, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder4 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder4.setTitle("Business Reg. Info");
        jPanel5.setBorder(xTitledBorder4);

        xFormPanel3.setCaptionWidth(120);
        xFormPanel3.setPadding(new java.awt.Insets(5, 5, 5, 5));

        xLabel12.setCaption("BIN");
        xLabel12.setExpression("#{entity.business.bin}");
        xLabel12.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xLabel12);

        xLabel2.setCaption("Tradename");
        xLabel2.setExpression("#{entity.business.tradename}");
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xLabel2);

        xLabel3.setCaption("Reg. Owner");
        xLabel3.setExpression("#{entity.business.owner.name}");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xLabel3);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 387, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(218, Short.MAX_VALUE))
        );

        xTabbedPane1.addTab("General Info", jPanel2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(xTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(xTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XLabel xLabel10;
    private com.rameses.rcp.control.XLabel xLabel11;
    private com.rameses.rcp.control.XLabel xLabel12;
    private com.rameses.rcp.control.XLabel xLabel13;
    private com.rameses.rcp.control.XLabel xLabel14;
    private com.rameses.rcp.control.XLabel xLabel16;
    private com.rameses.rcp.control.XLabel xLabel17;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel25;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLabel xLabel7;
    private com.rameses.rcp.control.XLabel xLabel8;
    private com.rameses.rcp.control.XLabel xLabel9;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    // End of variables declaration//GEN-END:variables
    
}
