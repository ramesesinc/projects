/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.entity.components;

import com.rameses.rcp.control.XComponentPanel;
import com.rameses.rcp.ui.annotations.ComponentBean;
import java.beans.Beans;

/**
 *
 * @author dell
 */
@ComponentBean("com.rameses.entity.components.OrgTypeListModel")
public class OrgTypeList extends XComponentPanel {

    /**
     * Creates new form OrgTypeList
     */
    public OrgTypeList() {
        initComponents();
    }

    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        component.setEnabled(enabled);
    }

    public void setEditable(boolean b) {
        setEnabled(b);
    }
    
    public boolean isEditable() {
        return this.isEnabled();
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

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        component.setCaption("Org Type");
        component.setEmptyText("-select an org type-");
        component.setExpression("#{item.value}");
        component.setItemKey("key");
        component.setItems("orgTypes");
        component.setName("orgType"); // NOI18N
        component.setPreferredSize(new java.awt.Dimension(250, 22));
        component.setRequired(true);
        add(component);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox component;
    // End of variables declaration//GEN-END:variables
}
