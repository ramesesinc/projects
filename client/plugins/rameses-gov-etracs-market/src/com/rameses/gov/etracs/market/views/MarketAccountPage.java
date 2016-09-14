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
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xDateField2 = new com.rameses.rcp.control.XDateField();
        jPanel3 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        entityLookup1 = new com.rameses.entity.components.EntityLookup();
        entityAddressLookup1 = new com.rameses.entity.components.EntityAddressLookup();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();

        xTabbedPane1.setDynamic(true);
        xTabbedPane1.setItems("sections");

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Rental Information");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel2.setCaptionWidth(120);

        xDecimalField2.setCaption("Daily Rate");
        xDecimalField2.setName("entity.rate"); // NOI18N
        xDecimalField2.setRequired(true);
        xFormPanel2.add(xDecimalField2);

        xDecimalField3.setCaption("Extension Rate");
        xDecimalField3.setName("entity.extrate"); // NOI18N
        xDecimalField3.setRequired(true);
        xFormPanel2.add(xDecimalField3);

        xLabel5.setCaption("Term");
        xLabel5.setDepends(new String[] {"entity.unit"});
        xLabel5.setExpression("#{entity.term}");
        xLabel5.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel5);

        xDateField1.setCaption("Start date");
        xDateField1.setName("entity.startdate"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xDateField1);

        xDateField2.setText("entity.lastdatepaid");
        xDateField2.setCaption("Last Date Covered");
        xDateField2.setName("entity.lastdatepaid"); // NOI18N
        xDateField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xDateField2);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Account Information");
        jPanel3.setBorder(xTitledBorder2);

        xFormPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xFormPanel1.setCaptionWidth(101);

        xTextField2.setCaption("Acct No");
        xTextField2.setName("entity.acctno"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(150, 20));
        xFormPanel1.add(xTextField2);

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

        xLookupField2.setCaption("Unit");
        xLookupField2.setExpression("#{entity.unit.code}");
        xLookupField2.setHandler("market_rentalunit:lookup");
        xLookupField2.setName("entity.unit"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField2);

        xLabel2.setCaption("Section");
        xLabel2.setDepends(new String[] {"entity.unit"});
        xLabel2.setExpression("#{entity.unit?.section?.name}  #{entity.unit.unittype} ");
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel2);

        xLabel3.setCaption("Market Name");
        xLabel3.setDepends(new String[] {"entity.unit"});
        xLabel3.setExpression("#{entity.unit.market.name}");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel3);

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
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 217, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 36, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(166, Short.MAX_VALUE))
        );

        xTabbedPane1.addTab("General Info", jPanel2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(xTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(xTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.entity.components.EntityAddressLookup entityAddressLookup1;
    private com.rameses.entity.components.EntityLookup entityLookup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDateField xDateField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
    
}
