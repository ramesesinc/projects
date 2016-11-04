/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.vehicle.components;

import com.rameses.rcp.control.XComponentPanel;
import com.rameses.rcp.ui.annotations.ComponentBean;

/**
 * @author dell
 */
@ComponentBean("com.rameses.gov.etracs.vehicle.components.EngineDataTypeModel")
public class EngineDataType extends XComponentPanel {

    /**
     * Creates new form EngineDataType
     */
    public EngineDataType() {
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
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();

        setLayout(new java.awt.BorderLayout());

        xFormPanel1.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        xFormPanel1.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);

        xComboBox1.setCaption("Make");
        xComboBox1.setCaptionWidth(40);
        xComboBox1.setItems("makeList");
        xComboBox1.setName("engine.make"); // NOI18N
        xFormPanel1.add(xComboBox1);

        xTextField2.setCaption("Model");
        xTextField2.setCaptionWidth(50);
        xTextField2.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xTextField2.setName("engine.model"); // NOI18N
        xFormPanel1.add(xTextField2);

        xIntegerField1.setCaption("Horsepower");
        xIntegerField1.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xIntegerField1.setName("engine.horsepower"); // NOI18N
        xFormPanel1.add(xIntegerField1);

        add(xFormPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}
