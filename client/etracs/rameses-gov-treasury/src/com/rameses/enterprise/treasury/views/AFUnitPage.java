/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.treasury.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author elmonazareno
 */
@Template(CrudFormPage.class)
public class AFUnitPage extends javax.swing.JPanel {

    /**
     * Creates new form AFUnitPage
     */
    public AFUnitPage() {
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

        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xIntegerField3 = new com.rameses.rcp.control.XIntegerField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xComboBox6 = new com.rameses.rcp.control.XComboBox();
        xComboBox7 = new com.rameses.rcp.control.XComboBox();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(20, 10, 10, 10));
        xTitledBorder1.setTitle("General Info");
        xFormPanel2.setBorder(xTitledBorder1);
        xFormPanel2.setCaptionWidth(150);

        xLookupField2.setCaption("AF");
        xLookupField2.setDisableWhen("#{ mode != 'create' }");
        xLookupField2.setExpression("#{ entity.af.objid } #{entity.af.title }");
        xLookupField2.setHandler("af:lookup");
        xLookupField2.setName("entity.af"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField2.setRequired(true);
        xFormPanel2.add(xLookupField2);

        xTextField2.setCaption("Unit");
        xTextField2.setDisableWhen("#{ mode != 'create' }");
        xTextField2.setName("entity.unit"); // NOI18N
        xTextField2.setRequired(true);
        xTextField2.setSpaceChar('_');
        xFormPanel2.add(xTextField2);

        xIntegerField3.setCaption("Unit Qty");
        xIntegerField3.setName("entity.qty"); // NOI18N
        xIntegerField3.setRequired(true);
        xFormPanel2.add(xIntegerField3);

        xDecimalField2.setCaption("Sale Price");
        xDecimalField2.setName("entity.saleprice"); // NOI18N
        xDecimalField2.setCellPadding(new java.awt.Insets(20, 0, 0, 0));
        xFormPanel2.add(xDecimalField2);

        xIntegerField2.setCaption("Interval");
        xIntegerField2.setDepends(new String[] {"af"});
        xIntegerField2.setName("entity.interval"); // NOI18N
        xIntegerField2.setVisibleWhen("#{entity.af.formtype == 'cashticket' }");
        xIntegerField2.setRequired(true);
        xFormPanel2.add(xIntegerField2);

        xComboBox6.setCaption("Receipt Printout");
        xComboBox6.setExpression("#{ item.caption }");
        xComboBox6.setItemKey("name");
        xComboBox6.setItems("receiptPrintoutList");
        xComboBox6.setName("entity.cashreceiptprintout"); // NOI18N
        xComboBox6.setCellPadding(new java.awt.Insets(20, 0, 0, 0));
        xComboBox6.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xComboBox6);

        xComboBox7.setCaption("Detail Printout");
        xComboBox7.setExpression("#{ item.caption }");
        xComboBox7.setItemKey("name");
        xComboBox7.setItems("detailPrintoutList");
        xComboBox7.setName("entity.cashreceiptdetailprintout"); // NOI18N
        xComboBox7.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xComboBox7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox6;
    private com.rameses.rcp.control.XComboBox xComboBox7;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XIntegerField xIntegerField3;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}
