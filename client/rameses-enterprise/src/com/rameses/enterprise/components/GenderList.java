/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.components;

import com.rameses.rcp.control.XComponentPanel;
import com.rameses.rcp.ui.annotations.ComponentBean;
import java.beans.Beans;

/**
 *
 * @author dell
 */
@ComponentBean("com.rameses.enterprise.components.GenderListModel")
public class GenderList extends XComponentPanel{

    /**
     * Creates new form GenderList
     */
    public GenderList() {
        initComponents();
    }

    public void setEditable(boolean b) {
        component.setEditable(b);
    }
    
    public boolean isEditable() {
        return component.isEditable();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        component.setEnabled(enabled);
    }
    
    @Override
    public void setName(String name) {
        super.setName(name);
        if(Beans.isDesignTime()) {
            component.setName(name);
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

        component = new com.rameses.rcp.control.XComboBox();

        setLayout(new java.awt.BorderLayout());

        component.setCaption("Gender");
        component.setCaptionWidth(100);
        component.setExpression("#{item.value}");
        component.setItemKey("key");
        component.setItems("genderList");
        component.setName("gender"); // NOI18N
        component.setPreferredSize(new java.awt.Dimension(150, 22));
        component.setRequired(true);
        add(component, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox component;
    // End of variables declaration//GEN-END:variables
}
