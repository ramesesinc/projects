/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.etracs.common;

import com.rameses.rcp.common.CallbackHandler;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.UIEvent;
import com.rameses.rcp.framework.UIHandler;
import com.rameses.rcp.ui.UIControlAdapter;
import java.util.HashMap;

/**
 *
 * @author dell
 */
public class LocalAddressPanel extends javax.swing.JPanel {

    public LocalAddressPanel() {
        initComponents(); 
        register( "entity" );
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
        register( name );
    }

    private void register( String name ) {
        ControlHandlerImpl uihandler = new ControlHandlerImpl( name ); 
        txtUnitno.putClientProperty(UIHandler.class, uihandler);
    }
    
    private class ControlHandlerImpl extends UIControlAdapter implements CallbackHandler {

        String name; 
        UIEvent uie; 
        
        ControlHandlerImpl( String name ) { 
            this.name = (name == null? "entity" : name); 
        }

        public void bind( UIEvent uie ) { 
            this.uie = uie; 
            if ( uie != null ) { 
                Object o = uie.getValue( name ); 
                if ( o == null ) { 
                    uie.setValue( name, new HashMap()); 
                } 
                
                Binding binding = uie.getBinding(); 
                if ( binding != null ) { 
                    binding.getValueChangeSupport().add( name+".*", this ); 
                } 
            } 
        } 

        public void unbind(UIEvent e) {
            this.uie = null; 
        }

        public void refresh( UIEvent e ) { 
            if ( uie != null ) {
                call( "" );
            }
        }

        
        // 
        // CallbackHandler implementations 
        // 
        public Object call(Object arg) { 
            if ( uie != null ) {
                StringBuilder buffer = new StringBuilder(); 
                append( buffer, " ", "unitno" );
                append( buffer, " ", "bldgno" );
                append( buffer, " ", "bldgname" );
                append( buffer, ", ", "street" );
                append( buffer, ", ", "subdivision" ); 
                append( buffer, ", ", "barangay.name" ); 
                if ( buffer.length() > 0 ) {
                    uie.setValue( name+".text", buffer.toString() ); 
                } else { 
                    uie.setValue( name+".text", null ); 
                } 
            } 
            return null; 
        }
        
        public Object call() { 
            return null; 
        }
        public Object call(Object[] args) {
            return null; 
        } 
        
        private void append( StringBuilder buffer, String delimiter, String property ) { 
            Object o = uie.getValue( name +"."+ property ); 
            if ( o == null ) { return; }  
            
            if ( buffer.length() > 0 ) {
                buffer.append((delimiter==null ? "" : delimiter)); 
            }             
            buffer.append( o.toString() ); 
        }
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

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setName("entity"); // NOI18N

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

        xFormPanel3.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel3.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel3.setShowCaption(false);
        xFormPanel3.setStretchWidth(100);

        txtSubdivision.setCaption("Subdivision");
        txtSubdivision.setName("entity.subdivision"); // NOI18N
        txtSubdivision.setStretchWidth(100);
        xFormPanel3.add(txtSubdivision);

        lupBarangay.setCaption("Barangay");
        lupBarangay.setCaptionWidth(90);
        lupBarangay.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        lupBarangay.setExpression("#{entity.barangay.name}");
        lupBarangay.setHandler("barangay:lookup");
        lupBarangay.setName("entity.barangay"); // NOI18N
        lupBarangay.setStretchWidth(100);
        xFormPanel3.add(lupBarangay);

        xFormPanel1.add(xFormPanel3);

        txtPin.setCaption("PIN");
        txtPin.setName("entity.pin"); // NOI18N
        txtPin.setStretchWidth(48);
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
