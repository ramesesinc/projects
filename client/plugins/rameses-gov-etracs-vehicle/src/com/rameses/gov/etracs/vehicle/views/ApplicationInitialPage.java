/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.vehicle.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;
 
/**
 *
 * @author dell
 */
@Template(FormPage.class)
public class ApplicationInitialPage extends javax.swing.JPanel {

    /**
     * Creates new form NewApplicationPage
     */
    public ApplicationInitialPage() {
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

        xSubFormPanel1 = new com.rameses.rcp.control.XSubFormPanel();
        xButton1 = new com.rameses.rcp.control.XButton();
        jPanel1 = new javax.swing.JPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        entityLookup1 = new com.rameses.entity.components.EntityLookup();
        entityAddressLookup1 = new com.rameses.entity.components.EntityAddressLookup();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        barangayLookup1 = new com.rameses.etracs.components.BarangayLookup();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel9 = new com.rameses.rcp.control.XLabel();
        xLabel8 = new com.rameses.rcp.control.XLabel();
        xLabel7 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();

        xSubFormPanel1.setHandler("vehicleTypeHandler");

        org.jdesktop.layout.GroupLayout xSubFormPanel1Layout = new org.jdesktop.layout.GroupLayout(xSubFormPanel1);
        xSubFormPanel1.setLayout(xSubFormPanel1Layout);
        xSubFormPanel1Layout.setHorizontalGroup(
            xSubFormPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        xSubFormPanel1Layout.setVerticalGroup(
            xSubFormPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 257, Short.MAX_VALUE)
        );

        xButton1.setImmediate(true);
        xButton1.setName("addVehicle"); // NOI18N
        xButton1.setText("Vehicle");

        jPanel1.setLayout(new java.awt.CardLayout());

        xFormPanel3.setCaptionWidth(150);
        xFormPanel3.setVisibleWhen("#{editmode != 'read' }");

        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel2.setCaption("App Type");
        xLabel2.setExpression("#{entity.apptype}");
        xLabel2.setPreferredSize(new java.awt.Dimension(80, 20));
        xFormPanel3.add(xLabel2);

        xIntegerField1.setCaption("App Year");
        xIntegerField1.setName("entity.appyear"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(80, 20));
        xFormPanel3.add(xIntegerField1);

        entityLookup1.setCaption("Owner");
        entityLookup1.setEntityType("entityindividual");
        entityLookup1.setName("entity.owner"); // NOI18N
        entityLookup1.setPreferredSize(new java.awt.Dimension(0, 21));
        entityLookup1.setRequired(true);
        xFormPanel3.add(entityLookup1);

        entityAddressLookup1.setCaption("Home Address");
        entityAddressLookup1.setDepends(new String[] {"entity.owner"});
        entityAddressLookup1.setName("entity.owner.address"); // NOI18N
        entityAddressLookup1.setPreferredSize(new java.awt.Dimension(0, 40));
        entityAddressLookup1.setRequired(true);
        xFormPanel3.add(entityAddressLookup1);

        xDateField1.setCaption("Date Filed");
        xDateField1.setName("entity.dtfiled"); // NOI18N
        xDateField1.setRequired(true);
        xFormPanel3.add(xDateField1);

        barangayLookup1.setCaption("Barangay");
        barangayLookup1.setName("entity.barangay"); // NOI18N
        barangayLookup1.setRequired(true);
        xFormPanel3.add(barangayLookup1);

        xTextField1.setCaption("Particulars");
        xTextField1.setName("entity.particulars"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xTextField1);

        xLookupField1.setCaption("Franchise Control No");
        xLookupField1.setExpression("#{entity.franchise.controlno}");
        xLookupField1.setHandler("lookupAvailableFranchise");
        xLookupField1.setName("entity.franchise"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(180, 20));
        xLookupField1.setRequired(true);
        xLookupField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xLookupField1ActionPerformed(evt);
            }
        });
        xFormPanel3.add(xLookupField1);

        jPanel1.add(xFormPanel3, "card3");

        xFormPanel4.setCaptionWidth(150);
        xFormPanel4.setVisibleWhen("#{editmode == 'read' }");

        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel3.setCaption("App Type");
        xLabel3.setExpression("#{entity.apptype}");
        xLabel3.setPreferredSize(new java.awt.Dimension(80, 20));
        xFormPanel4.add(xLabel3);

        xLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel9.setCaption("App Year");
        xLabel9.setExpression("#{entity.appyear}");
        xLabel9.setPreferredSize(new java.awt.Dimension(80, 20));
        xFormPanel4.add(xLabel9);

        xLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel8.setCaption("Owner Name");
        xLabel8.setExpression("#{entity.owner.name}");
        xLabel8.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel8);

        xLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel7.setCaption("Home Address");
        xLabel7.setExpression("#{entity.owner.address.text}");
        xLabel7.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel7);

        xLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel4.setCaption("Particulars");
        xLabel4.setExpression("#{entity.particulars}");
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel4);

        xLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel6.setCaption("Barangay");
        xLabel6.setExpression("#{entity.barangay.name}");
        xLabel6.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel6);

        xLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel5.setCaption("Franchise Control No");
        xLabel5.setExpression("#{entity.franchise.controlno}");
        xLabel5.setPreferredSize(new java.awt.Dimension(180, 20));
        xFormPanel4.add(xLabel5);

        jPanel1.add(xFormPanel4, "card3");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(38, 38, 38)
                        .add(xSubFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(7, 7, 7)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(xSubFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xLookupField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xLookupField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xLookupField1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.etracs.components.BarangayLookup barangayLookup1;
    private com.rameses.entity.components.EntityAddressLookup entityAddressLookup1;
    private com.rameses.entity.components.EntityLookup entityLookup1;
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLabel xLabel7;
    private com.rameses.rcp.control.XLabel xLabel8;
    private com.rameses.rcp.control.XLabel xLabel9;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XSubFormPanel xSubFormPanel1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}