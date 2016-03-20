/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.etracs.common;

/**
 *
 * @author dell
 */
public class LocalAddressPanel extends javax.swing.JPanel {

    /**
     * Creates new form LocalAddressPanel
     */
    public LocalAddressPanel() {
        initComponents();
    }
    
    public void setName(String name) {
        super.setName(name);
        txtUnitno.setName(name+".unitno");
        txtBldgno.setName(name+".bldgno");
        txtBldgname.setName(name+".bldgname");
        txtStreet.setName(name+".street");
        txtSubdivision.setName(name+".subdivision");
        lupBarangay.setName(name+".barangay");
        lupBarangay.setExpression("#{"+name+".barangay.name}");
        txtPin.setName( name + ".pin");
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
        txtPin = new com.rameses.rcp.control.XTextField();

        setOpaque(false);
        setLayout(new java.awt.BorderLayout());

        xFormPanel1.setName("entity"); // NOI18N

        xFormPanel2.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel2.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel2.setShowCaption(false);

        txtUnitno.setCaption("Unit No ");
        txtUnitno.setName("entity.unitno"); // NOI18N
        txtUnitno.setPreferredSize(new java.awt.Dimension(80, 22));
        xFormPanel2.add(txtUnitno);

        txtBldgno.setCaption("House / Bldg No");
        txtBldgno.setCaptionWidth(90);
        txtBldgno.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        txtBldgno.setName("entity.bldgno"); // NOI18N
        txtBldgno.setPreferredSize(new java.awt.Dimension(100, 22));
        xFormPanel2.add(txtBldgno);

        xFormPanel1.add(xFormPanel2);

        txtBldgname.setCaption("Bldg Name");
        txtBldgname.setName("entity.bldgname"); // NOI18N
        txtBldgname.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel1.add(txtBldgname);

        txtStreet.setCaption("Street");
        txtStreet.setName("entity.street"); // NOI18N
        txtStreet.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel1.add(txtStreet);

        xFormPanel3.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel3.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel3.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel3.setShowCaption(false);

        txtSubdivision.setCaption("Subdivision");
        txtSubdivision.setName("entity.subdivision"); // NOI18N
        txtSubdivision.setPreferredSize(new java.awt.Dimension(80, 22));
        xFormPanel3.add(txtSubdivision);

        lupBarangay.setCaption("Barangay");
        lupBarangay.setCaptionWidth(60);
        lupBarangay.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        lupBarangay.setExpression("#{entity.barangay.name}");
        lupBarangay.setHandler("barangay:lookup");
        lupBarangay.setName("entity.barangay"); // NOI18N
        lupBarangay.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel3.add(lupBarangay);

        xFormPanel1.add(xFormPanel3);

        txtPin.setCaption("PIN");
        txtPin.setName("entity.pin"); // NOI18N
        txtPin.setPreferredSize(new java.awt.Dimension(150, 22));
        xFormPanel1.add(txtPin);

        add(xFormPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XLookupField lupBarangay;
    private com.rameses.rcp.control.XTextField txtBldgname;
    private com.rameses.rcp.control.XTextField txtBldgno;
    private com.rameses.rcp.control.XTextField txtPin;
    private com.rameses.rcp.control.XTextField txtStreet;
    private com.rameses.rcp.control.XTextField txtSubdivision;
    private com.rameses.rcp.control.XTextField txtUnitno;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    // End of variables declaration//GEN-END:variables
}