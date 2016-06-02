/*
 * MarketPage.java
 *
* Created on March 31, 2014, 10:42 AM
 */

package com.rameses.gov.etracs.market.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author  Elmo
 */
@Template(CrudFormPage.class)
public class MarketRentalUnitPage extends javax.swing.JPanel {
    
    /** Creates new form MarketPage */
    public MarketRentalUnitPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xComboBox4 = new com.rameses.rcp.control.XComboBox();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xComboBox5 = new com.rameses.rcp.control.XComboBox();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Market Rental Unit");
        xFormPanel1.setBorder(xTitledBorder1);
        xFormPanel1.setCaptionWidth(100);

        xTextField1.setCaption("Code");
        xTextField1.setName("entity.code"); // NOI18N
        xTextField1.setRequired(true);
        xTextField1.setSpaceChar('_');
        xFormPanel1.add(xTextField1);

        xTextField2.setCaption("Description");
        xTextField2.setName("entity.description"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        xComboBox4.setCaption("Market");
        xComboBox4.setExpression("#{item.name}");
        xComboBox4.setItems("listTypes.market");
        xComboBox4.setName("entity.market"); // NOI18N
        xComboBox4.setPreferredSize(new java.awt.Dimension(220, 22));
        xComboBox4.setRequired(true);
        xFormPanel1.add(xComboBox4);

        xLabel1.setCaption("Market Address");
        xLabel1.setDepends(new String[] {"entity.market"});
        xLabel1.setExpression("#{entity.market.address.text}");
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel1);

        xComboBox5.setCaption("Cluster");
        xComboBox5.setExpression("#{item.name}");
        xComboBox5.setItems("listTypes.cluster");
        xComboBox5.setName("entity.cluster"); // NOI18N
        xComboBox5.setPreferredSize(new java.awt.Dimension(220, 22));
        xComboBox5.setRequired(true);
        xFormPanel1.add(xComboBox5);

        xComboBox1.setCaption("Unit Type");
        xComboBox1.setExpression("#{item.name}");
        xComboBox1.setItems("listTypes.unittype");
        xComboBox1.setName("entity.unittype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(120, 22));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xComboBox3.setCaption("Section");
        xComboBox3.setExpression("#{item.name}");
        xComboBox3.setItems("listTypes.section");
        xComboBox3.setName("entity.section"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(220, 22));
        xComboBox3.setRequired(true);
        xFormPanel1.add(xComboBox3);

        xDecimalField1.setCaption("Rate");
        xDecimalField1.setName("entity.rate"); // NOI18N
        xDecimalField1.setRequired(true);
        xFormPanel1.add(xDecimalField1);

        xComboBox2.setCaption("Term");
        xComboBox2.setExpression("#{item.name}");
        xComboBox2.setItems("listTypes.paymentterm");
        xComboBox2.setName("entity.paymentterm"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(100, 22));
        xComboBox2.setRequired(true);
        xFormPanel1.add(xComboBox2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 249, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XComboBox xComboBox4;
    private com.rameses.rcp.control.XComboBox xComboBox5;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
    
}
