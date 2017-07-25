/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.landtax.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

@Template(FormPage.class)
@StyleSheet()
public class RPTBatchCollectionInitPage extends javax.swing.JPanel {

    /**
     * Creates new form RPTBatchCollectionInitPage
     */
    public RPTBatchCollectionInitPage() {
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

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xRadio1 = new com.rameses.rcp.control.XRadio();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xRadio2 = new com.rameses.rcp.control.XRadio();
        xTextField1 = new com.rameses.rcp.control.XTextField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(30, 10, 10, 10));
        xTitledBorder1.setTitle("Payment Option");
        xFormPanel1.setBorder(xTitledBorder1);
        xFormPanel1.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        xRadio1.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xRadio1.setName("entity.payoption"); // NOI18N
        xRadio1.setOpaque(false);
        xRadio1.setOptionValue("taxpayer");
        xRadio1.setShowCaption(false);
        xRadio1.setText("By Taxpayer");
        xFormPanel1.add(xRadio1);

        xLookupField1.setCaption("Taxpayer");
        xLookupField1.setCellPadding(new java.awt.Insets(0, 40, 0, 0));
        xLookupField1.setDepends(new String[] {"entity.payoption"});
        xLookupField1.setExpression("#{entity.taxpayer.name}");
        xLookupField1.setHandler("entity:lookup");
        xLookupField1.setName("entity.taxpayer"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField1);

        xRadio2.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xRadio2.setName("entity.payoption"); // NOI18N
        xRadio2.setOpaque(false);
        xRadio2.setOptionValue("barcode");
        xRadio2.setShowCaption(false);
        xRadio2.setText("By Barcode");
        xFormPanel1.add(xRadio2);

        xTextField1.setCaption("Barcode");
        xTextField1.setCellPadding(new java.awt.Insets(0, 40, 0, 0));
        xTextField1.setDepends(new String[] {"entity.payoption"});
        xTextField1.setName("entity.barcodeid"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xTextField1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(111, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XRadio xRadio1;
    private com.rameses.rcp.control.XRadio xRadio2;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}
