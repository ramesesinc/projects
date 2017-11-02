/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.market.views;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.Template;
import java.util.Date;

/**
 *
 * @author Elmo Nazareno
 */
@Template(OKCancelPage.class)
public class MarketChangeActionPage extends javax.swing.JPanel {

    /**
     * Creates new form MarketChangeActionPage
     */
    public MarketChangeActionPage() {
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
        xPanel1 = new com.rameses.rcp.control.XPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        entityLookup1 = new com.rameses.entity.components.EntityLookup();
        entityAddressLookup1 = new com.rameses.entity.components.EntityAddressLookup();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xPanel3 = new com.rameses.rcp.control.XPanel();
        xButton2 = new com.rameses.rcp.control.XButton();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xPanel4 = new com.rameses.rcp.control.XPanel();
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xComboBox6 = new com.rameses.rcp.control.XComboBox();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        xPanel5 = new com.rameses.rcp.control.XPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xPanel6 = new com.rameses.rcp.control.XPanel();
        xFormPanel7 = new com.rameses.rcp.control.XFormPanel();
        xDateField5 = new com.rameses.rcp.control.XDateField();

        jPanel1.setLayout(new java.awt.CardLayout());

        xPanel1.setName("owner"); // NOI18N
        xPanel1.setVisibleWhen("#{ txntype == 'owner' }");

        xFormPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xFormPanel1.setCaptionWidth(101);

        xTextField1.setCaption("Acct Name");
        xTextField1.setName("entity.acctname"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xTextField1);

        entityLookup1.setCaption("Owner");
        entityLookup1.setName("entity.owner"); // NOI18N
        entityLookup1.setPreferredSize(new java.awt.Dimension(0, 21));
        xFormPanel1.add(entityLookup1);

        entityAddressLookup1.setCaption("Owner Address");
        entityAddressLookup1.setDepends(new String[] {"entity.owner"});
        entityAddressLookup1.setName("entity.owner.address"); // NOI18N
        entityAddressLookup1.setParentIdName("entity.owner.objid");
        entityAddressLookup1.setPreferredSize(new java.awt.Dimension(0, 53));
        xFormPanel1.add(entityAddressLookup1);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 63));

        xTextArea1.setCaption("Remarks");
        xTextArea1.setName("entity.remarks"); // NOI18N
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(209, Short.MAX_VALUE))
        );

        jPanel1.add(xPanel1, "owner");

        xPanel2.setName("rentalunit"); // NOI18N
        xPanel2.setVisibleWhen("#{ txntype == 'rentalunit' }");

        xFormPanel2.setCaptionWidth(120);

        xLookupField1.setCaption("Unit No");
        xLookupField1.setExpression("#{entity.unit.code}");
        xLookupField1.setHandler("market_rentalunit:open:lookup");
        xLookupField1.setName("entity.unit"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLookupField1);

        xLabel4.setCaption("Cluster");
        xLabel4.setDepends(new String[] {"entity.unit"});
        xLabel4.setExpression("#{entity.unit.cluster.market.name}  #{entity.unit.cluster.name} ");
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel4);

        javax.swing.GroupLayout xPanel2Layout = new javax.swing.GroupLayout(xPanel2);
        xPanel2.setLayout(xPanel2Layout);
        xPanel2Layout.setHorizontalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );
        xPanel2Layout.setVerticalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(288, Short.MAX_VALUE))
        );

        jPanel1.add(xPanel2, "rentalunit");

        xPanel3.setVisibleWhen("#{ txntype == 'business' }");

        xButton2.setImmediate(true);
        xButton2.setName("selectBusiness"); // NOI18N
        xButton2.setText("Select a business");

        xFormPanel5.setCaptionWidth(120);

        xLabel6.setCaption("BIN");
        xLabel6.setExpression("#{entity.business.bin}");
        xLabel6.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel5.add(xLabel6);

        xLabel2.setCaption("Trade Name");
        xLabel2.setExpression("#{entity.business.tradename}");
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel5.add(xLabel2);

        xLabel3.setCaption("Owner Name");
        xLabel3.setExpression("#{entity.business.owner.name}");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel5.add(xLabel3);

        javax.swing.GroupLayout xPanel3Layout = new javax.swing.GroupLayout(xPanel3);
        xPanel3.setLayout(xPanel3Layout);
        xPanel3Layout.setHorizontalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(xPanel3Layout.createSequentialGroup()
                        .addComponent(xButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(xFormPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE))
                .addContainerGap())
        );
        xPanel3Layout.setVerticalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(290, Short.MAX_VALUE))
        );

        jPanel1.add(xPanel3, "card4");

        xPanel4.setVisibleWhen("#{ txntype == 'ledger' }");

        xFormPanel6.setCaptionPadding(new java.awt.Insets(0, 0, 0, 5));
        xFormPanel6.setCaptionWidth(120);

        xComboBox6.setCaption("Mode of Payment");
        xComboBox6.setItems("lov.MARKET_PAY_FREQUENCY");
        xComboBox6.setName("entity.payfrequency"); // NOI18N
        xComboBox6.setRequired(true);
        xFormPanel6.add(xComboBox6);

        xDateField2.setCaption("Start Date");
        xDateField2.setName("entity.dtstarted"); // NOI18N
        xDateField2.setRequired(true);
        xFormPanel6.add(xDateField2);

        javax.swing.GroupLayout xPanel4Layout = new javax.swing.GroupLayout(xPanel4);
        xPanel4.setLayout(xPanel4Layout);
        xPanel4Layout.setHorizontalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        xPanel4Layout.setVerticalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(238, Short.MAX_VALUE))
        );

        jPanel1.add(xPanel4, "card5");

        xPanel5.setVisibleWhen("#{ txntype == 'extarea' }");

        xFormPanel3.setCaptionWidth(120);

        xDecimalField2.setCaption("Extension Area (sqm)");
        xDecimalField2.setCellPadding(new java.awt.Insets(40, 0, 0, 0));
        xDecimalField2.setName("entity.extarea"); // NOI18N
        xFormPanel3.add(xDecimalField2);

        javax.swing.GroupLayout xPanel5Layout = new javax.swing.GroupLayout(xPanel5);
        xPanel5.setLayout(xPanel5Layout);
        xPanel5Layout.setHorizontalGroup(
            xPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );
        xPanel5Layout.setVerticalGroup(
            xPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(320, Short.MAX_VALUE))
        );

        jPanel1.add(xPanel5, "card6");

        xPanel6.setVisibleWhen("#{ txntype == 'lastdatepaid' }");

        xFormPanel7.setCaptionPadding(new java.awt.Insets(0, 0, 0, 5));
        xFormPanel7.setCaptionWidth(120);

        xDateField5.setCaption("Paid Until Date");
        xDateField5.setCellPadding(new java.awt.Insets(40, 0, 0, 0));
        xDateField5.setName("entity.lastdatepaid"); // NOI18N
        xDateField5.setRequired(true);
        xFormPanel7.add(xDateField5);

        javax.swing.GroupLayout xPanel6Layout = new javax.swing.GroupLayout(xPanel6);
        xPanel6.setLayout(xPanel6Layout);
        xPanel6Layout.setHorizontalGroup(
            xPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        xPanel6Layout.setVerticalGroup(
            xPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(344, Short.MAX_VALUE))
        );

        jPanel1.add(xPanel6, "card6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.entity.components.EntityAddressLookup entityAddressLookup1;
    private com.rameses.entity.components.EntityLookup entityLookup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XComboBox xComboBox6;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDateField xDateField5;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XFormPanel xFormPanel7;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel2;
    private com.rameses.rcp.control.XPanel xPanel3;
    private com.rameses.rcp.control.XPanel xPanel4;
    private com.rameses.rcp.control.XPanel xPanel5;
    private com.rameses.rcp.control.XPanel xPanel6;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}
