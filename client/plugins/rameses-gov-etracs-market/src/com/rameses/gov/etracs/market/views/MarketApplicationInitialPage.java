/*
 * MarketApplication.java
 *
 * Created on March 17, 2014, 11:01 AM
 */

package com.rameses.gov.etracs.market.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author  Elmo
 */
@Template(CrudFormPage.class)
public class MarketApplicationInitialPage extends javax.swing.JPanel {
    
    /** Creates new form MarketApplication */
    public MarketApplicationInitialPage() {
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
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        xTextArea2 = new com.rameses.rcp.control.XTextArea();

        xFormPanel1.setCaptionWidth(101);

        xLookupField1.setCaption("Owner Name");
        xLookupField1.setExpression("#{entity.owner.name}");
        xLookupField1.setHandler("individualentity:lookup");
        xLookupField1.setName("entity.owner"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField1);

        xTextField1.setCaption("Acct Name");
        xTextField1.setName("entity.acctname"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xTextField1);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setName("entity.name"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 60));

        xTextArea1.setCaption("Owner Address");
        xTextArea1.setDepends(new String[] {"entity.owner"});
        xTextArea1.setName("entity.owner.address.text"); // NOI18N
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        xLookupField2.setCaption("Unit");
        xLookupField2.setExpression("#{entity.unit.code}");
        xLookupField2.setHandler("market_rentalunit:lookup");
        xLookupField2.setName("entity.unit"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField2);

        xLabel2.setCaption("Section");
        xLabel2.setDepends(new String[] {"entity.unit"});
        xLabel2.setExpression("#{entity.unit?.section?.name} ");
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel2);

        xLabel4.setCaption("Unit Type");
        xLabel4.setDepends(new String[] {"entity.unit"});
        xLabel4.setExpression("#{entity.unit?.unittype} ");
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel4);

        xLabel3.setCaption("Market Name");
        xLabel3.setDepends(new String[] {"entity.unit"});
        xLabel3.setExpression("#{entity.unit.market.name}");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel3);

        xLabel5.setCaption("Rate Type");
        xLabel5.setDepends(new String[] {"entity.unit"});
        xLabel5.setExpression("#{entity.unit.rate} - #{entity.unit.paymentterm}");
        xLabel5.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel5);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(0, 63));

        xTextArea2.setCaption("Remarks");
        xTextArea2.setName("entity.remarks"); // NOI18N
        jScrollPane2.setViewportView(xTextArea2);

        xFormPanel1.add(jScrollPane2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 590, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 520, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(60, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 472, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(xFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextArea xTextArea2;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
    
}
