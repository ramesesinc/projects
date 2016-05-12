/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Rameses
 */
@Template(CrudFormPage.class)
public class AccountPage extends javax.swing.JPanel {

    /**
     * Creates new form CaptureAccountPage
     */
    public AccountPage() {
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

        xTabbedPane1 = new com.rameses.rcp.control.XTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        xPanel1 = new com.rameses.rcp.control.XPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xPanel3 = new com.rameses.rcp.control.XPanel();
        localAddressPanel1 = new com.rameses.etracs.common.LocalAddressPanel();
        jPanel4 = new javax.swing.JPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        xLabel20 = new com.rameses.rcp.control.XLabel();
        xButton3 = new com.rameses.rcp.control.XButton();
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xLabel11 = new com.rameses.rcp.control.XLabel();
        xLabel12 = new com.rameses.rcp.control.XLabel();
        xButton5 = new com.rameses.rcp.control.XButton();
        jPanel5 = new javax.swing.JPanel();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xLookupField4 = new com.rameses.rcp.control.XLookupField();
        xLabel17 = new com.rameses.rcp.control.XLabel();
        xLabel18 = new com.rameses.rcp.control.XLabel();
        xLabel19 = new com.rameses.rcp.control.XLabel();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xPanel4 = new com.rameses.rcp.control.XPanel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xDateField7 = new com.rameses.rcp.control.XDateField();
        xDateField9 = new com.rameses.rcp.control.XDateField();
        xLabel13 = new com.rameses.rcp.control.XLabel();
        xLabel21 = new com.rameses.rcp.control.XLabel();
        xLabel14 = new com.rameses.rcp.control.XLabel();
        xLabel15 = new com.rameses.rcp.control.XLabel();
        xLabel16 = new com.rameses.rcp.control.XLabel();
        xButton4 = new com.rameses.rcp.control.XButton();

        xTabbedPane1.setHandler("sections");

        com.rameses.rcp.control.layout.YLayout yLayout1 = new com.rameses.rcp.control.layout.YLayout();
        yLayout1.setSpacing(6);
        jPanel1.setLayout(yLayout1);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Account Information");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionWidth(110);

        xTextField1.setCaption("Account No.");
        xTextField1.setName("entity.acctno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(250, 20));
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        xTextField2.setCaption("Account Name");
        xTextField2.setName("entity.acctname"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        xLookupField1.setCaption("Owner Name");
        xLookupField1.setExpression("#{entity.owner.name}");
        xLookupField1.setHandler("individualentity:lookup");
        xLookupField1.setName("entity.owner"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField1);

        xComboBox1.setCaption("Classification");
        xComboBox1.setExpression("#{item.objid}");
        xComboBox1.setItems("listTypes.classification");
        xComboBox1.setName("entity.classification"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xComboBox1);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(xPanel1);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Address");
        xPanel3.setBorder(xTitledBorder2);

        localAddressPanel1.setName("entity.address"); // NOI18N

        javax.swing.GroupLayout xPanel3Layout = new javax.swing.GroupLayout(xPanel3);
        xPanel3.setLayout(xPanel3Layout);
        xPanel3Layout.setHorizontalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(localAddressPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel3Layout.setVerticalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel3Layout.createSequentialGroup()
                .addComponent(localAddressPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        jPanel1.add(xPanel3);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("Stubout Information");
        jPanel4.setBorder(xTitledBorder3);

        xFormPanel3.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel3.setCaptionWidth(110);

        xLabel6.setBackground(new java.awt.Color(245, 245, 245));
        xLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel6.setCaption("Stubout");
        xLabel6.setExpression("#{entity.stubout.code}");
        xLabel6.setOpaque(true);
        xLabel6.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xLabel6);

        xLabel20.setBackground(new java.awt.Color(245, 245, 245));
        xLabel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel20.setCaption("Stubout Node Index");
        xLabel20.setExpression("#{entity.stuboutnode.indexno}");
        xLabel20.setOpaque(true);
        xLabel20.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xLabel20);

        xButton3.setImmediate(true);
        xButton3.setName("assignStubout"); // NOI18N
        xButton3.setText("Assign Stubout");
        xButton3.setVisibleWhen("#{mode == 'create'}");

        xFormPanel6.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel6.setCaptionWidth(60);

        xLabel11.setBackground(new java.awt.Color(245, 245, 245));
        xLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel11.setCaption("Sector");
        xLabel11.setExpression("#{entity.stubout.zone.sector.code}");
        xLabel11.setOpaque(true);
        xLabel11.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel6.add(xLabel11);

        xLabel12.setBackground(new java.awt.Color(245, 245, 245));
        xLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel12.setCaption("Zone");
        xLabel12.setExpression("#{entity.stubout.zone.code}");
        xLabel12.setOpaque(true);
        xLabel12.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel6.add(xLabel12);

        xButton5.setImmediate(true);
        xButton5.setName("assignStuboutNode"); // NOI18N
        xButton5.setText("Select Node");
        xButton5.setVisibleWhen("#{mode == 'create'}");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xFormPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(xButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xFormPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(xFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder4 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder4.setTitle("Meter Information");
        jPanel5.setBorder(xTitledBorder4);

        xFormPanel5.setCaptionWidth(120);

        xLookupField4.setCaption("Serial No.");
        xLookupField4.setCaptionWidth(120);
        xLookupField4.setExpression("#{entity.meter.serialno}");
        xLookupField4.setHandler("waterworks_meter_wo_account:lookup");
        xLookupField4.setName("entity.meter"); // NOI18N
        xLookupField4.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField4.setRequired(true);
        xFormPanel5.add(xLookupField4);

        xLabel17.setBackground(new java.awt.Color(245, 245, 245));
        xLabel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel17.setCaption("Brand");
        xLabel17.setDepends(new String[] {"entity.meter"});
        xLabel17.setExpression("#{entity.meter.brand}");
        xLabel17.setOpaque(true);
        xLabel17.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel5.add(xLabel17);

        xLabel18.setBackground(new java.awt.Color(245, 245, 245));
        xLabel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel18.setCaption("Size");
        xLabel18.setDepends(new String[] {"entity.meter"});
        xLabel18.setExpression("#{entity.meter.size.title}");
        xLabel18.setOpaque(true);
        xLabel18.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel5.add(xLabel18);

        xLabel19.setBackground(new java.awt.Color(245, 245, 245));
        xLabel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel19.setCaption("Capacity");
        xLabel19.setDepends(new String[] {"entity.meter"});
        xLabel19.setExpression("#{entity.meter.capacity}");
        xLabel19.setOpaque(true);
        xLabel19.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel5.add(xLabel19);

        xIntegerField2.setCaption("Current Reading");
        xIntegerField2.setCaptionWidth(120);
        xIntegerField2.setName("entity.currentreading"); // NOI18N
        xIntegerField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xIntegerField2.setRequired(true);
        xFormPanel5.add(xIntegerField2);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder5 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder5.setTitle("Billing Cycle Information");
        xPanel4.setBorder(xTitledBorder5);

        xFormPanel4.setCaptionWidth(120);

        xDateField7.setCaption("Start Date");
        xDateField7.setName("entity.dtstarted"); // NOI18N
        xDateField7.setPreferredSize(new java.awt.Dimension(0, 20));
        xDateField7.setRequired(true);
        xFormPanel4.add(xDateField7);

        xDateField9.setCaption("Last Reading Date");
        xDateField9.setName("entity.lastdateread"); // NOI18N
        xDateField9.setPreferredSize(new java.awt.Dimension(0, 20));
        xDateField9.setRequired(true);
        xFormPanel4.add(xDateField9);

        xLabel13.setBackground(new java.awt.Color(245, 245, 245));
        xLabel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel13.setCaption("Period From");
        xLabel13.setExpression("#{entity.billingcycle.fromperiod}");
        xLabel13.setOpaque(true);
        xLabel13.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel13);

        xLabel21.setBackground(new java.awt.Color(245, 245, 245));
        xLabel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel21.setCaption("Period To");
        xLabel21.setExpression("#{entity.billingcycle.toperiod} ");
        xLabel21.setOpaque(true);
        xLabel21.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel21);

        xLabel14.setBackground(new java.awt.Color(245, 245, 245));
        xLabel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel14.setCaption("Next Reading Date");
        xLabel14.setExpression("#{entity.billingcycle.readingdate} ");
        xLabel14.setOpaque(true);
        xLabel14.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel14);

        xLabel15.setBackground(new java.awt.Color(245, 245, 245));
        xLabel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel15.setCaption("Next Due Date");
        xLabel15.setExpression("#{entity.billingcycle.duedate} ");
        xLabel15.setOpaque(true);
        xLabel15.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel15);

        xLabel16.setBackground(new java.awt.Color(245, 245, 245));
        xLabel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel16.setCaption("Disconnection Date");
        xLabel16.setExpression("#{entity.billingcycle.disconnectiondate} ");
        xLabel16.setOpaque(true);
        xLabel16.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel16);

        xButton4.setImmediate(true);
        xButton4.setName("computeBillingCycle"); // NOI18N
        xButton4.setText("Billing Cycle");
        xButton4.setVisibleWhen("#{mode == 'create'}");

        javax.swing.GroupLayout xPanel4Layout = new javax.swing.GroupLayout(xPanel4);
        xPanel4.setLayout(xPanel4Layout);
        xPanel4Layout.setHorizontalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                    .addGroup(xPanel4Layout.createSequentialGroup()
                        .addComponent(xButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        xPanel4Layout.setVerticalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(xButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(41, 41, 41))
        );

        xTabbedPane1.addTab("General Information", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 963, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private com.rameses.etracs.common.LocalAddressPanel localAddressPanel1;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XButton xButton4;
    private com.rameses.rcp.control.XButton xButton5;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XDateField xDateField7;
    private com.rameses.rcp.control.XDateField xDateField9;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XLabel xLabel11;
    private com.rameses.rcp.control.XLabel xLabel12;
    private com.rameses.rcp.control.XLabel xLabel13;
    private com.rameses.rcp.control.XLabel xLabel14;
    private com.rameses.rcp.control.XLabel xLabel15;
    private com.rameses.rcp.control.XLabel xLabel16;
    private com.rameses.rcp.control.XLabel xLabel17;
    private com.rameses.rcp.control.XLabel xLabel18;
    private com.rameses.rcp.control.XLabel xLabel19;
    private com.rameses.rcp.control.XLabel xLabel20;
    private com.rameses.rcp.control.XLabel xLabel21;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XLookupField xLookupField4;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel3;
    private com.rameses.rcp.control.XPanel xPanel4;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}
