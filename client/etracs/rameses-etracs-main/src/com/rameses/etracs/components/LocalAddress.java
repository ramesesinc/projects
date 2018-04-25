/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.etracs.components;

import com.rameses.common.MethodResolver;
import com.rameses.rcp.control.XComponentPanel;
import com.rameses.rcp.ui.UIFocusableContainer;
import com.rameses.rcp.ui.annotations.ComponentBean;
import java.beans.Beans;

/**
 *
 * @author dell
 */
@ComponentBean("com.rameses.etracs.components.LocalAddressModel")
public class LocalAddress extends XComponentPanel implements UIFocusableContainer {

    /**
     * Creates new form LocalAddress
     */
    public LocalAddress() {
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
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        txtUnitno = new com.rameses.rcp.control.XTextField();
        txtBldgno = new com.rameses.rcp.control.XTextField();
        txtBldgname = new com.rameses.rcp.control.XTextField();
        txtStreet = new com.rameses.rcp.control.XTextField();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        txtSubdivision = new com.rameses.rcp.control.XTextField();
        lupBarangay = new com.rameses.rcp.control.XLookupField();

        xFormPanel1.setName("entity"); // NOI18N
        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setPadding(new java.awt.Insets(0, 0, 5, 5));

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel2.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel2.setShowCaption(false);
        xFormPanel2.setStretchWidth(100);

        txtUnitno.setCaption("Unit No ");
        txtUnitno.setName("entity.unitno"); // NOI18N
        txtUnitno.setStretchWidth(100);
        xFormPanel2.add(txtUnitno);

        txtBldgno.setCaption("House / Bldg No");
        txtBldgno.setCaptionWidth(90);
        txtBldgno.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        txtBldgno.setName("entity.bldgno"); // NOI18N
        txtBldgno.setStretchWidth(100);
        xFormPanel2.add(txtBldgno);

        xFormPanel1.add(xFormPanel2);

        txtBldgname.setCaption("Bldg Name");
        txtBldgname.setName("entity.bldgname"); // NOI18N
        txtBldgname.setStretchWidth(100);
        xFormPanel1.add(txtBldgname);

        txtStreet.setCaption("Street");
        txtStreet.setName("entity.street"); // NOI18N
        txtStreet.setStretchWidth(100);
        xFormPanel1.add(txtStreet);

        xFormPanel3.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel3.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel3.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel3.setShowCaption(false);
        xFormPanel3.setStretchWidth(100);

        txtSubdivision.setCaption("Subdivision");
        txtSubdivision.setName("entity.subdivision"); // NOI18N
        txtSubdivision.setStretchWidth(100);
        xFormPanel3.add(txtSubdivision);

        lupBarangay.setCaption("Barangay");
        lupBarangay.setCaptionWidth(75);
        lupBarangay.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        lupBarangay.setExpression("#{entity.barangay.name}");
        lupBarangay.setHandler("lookupBarangay");
        lupBarangay.setName("entity.barangay"); // NOI18N
        lupBarangay.setRequired(true);
        lupBarangay.setStretchWidth(100);
        xFormPanel3.add(lupBarangay);

        xFormPanel1.add(xFormPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XLookupField lupBarangay;
    private com.rameses.rcp.control.XTextField txtBldgname;
    private com.rameses.rcp.control.XTextField txtBldgno;
    private com.rameses.rcp.control.XTextField txtStreet;
    private com.rameses.rcp.control.XTextField txtSubdivision;
    private com.rameses.rcp.control.XTextField txtUnitno;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setName(String name) {
        super.setName(name);
        if(Beans.isDesignTime()) {
            txtUnitno.setText(name+".unitno");
            txtBldgno.setText(name+".bldgno");
            txtBldgname.setText(name+".bldgname");
            txtStreet.setText(name+".street");
            txtSubdivision.setText(name+".subdivision");
            lupBarangay.setText("#{"+name+".barangay.name}");
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        txtUnitno.setEnabled(enabled);
        txtBldgno.setEnabled(enabled);
        txtBldgname.setEnabled(enabled);
        txtStreet.setEnabled(enabled);
        txtSubdivision.setEnabled(enabled);
        lupBarangay.setEnabled(enabled);
     }
    
    @Override
    public void afterLoad() {
        com.rameses.rcp.common.ComponentBean cb = (com.rameses.rcp.common.ComponentBean)getComponentBean();
        try {
            MethodResolver mr = MethodResolver.getInstance();
            mr.invoke(cb, "init", null);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean focusFirstInput() { 
        return xFormPanel1.focusFirstInput(); 
    }
}
