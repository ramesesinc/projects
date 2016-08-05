/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.components;

import com.rameses.common.MethodResolver;
import com.rameses.rcp.control.XComponentPanel;
import com.rameses.rcp.ui.annotations.ComponentBean;

/**
 * @author dell
 */
@ComponentBean("com.rameses.enterprise.components.LOVSelectListModel")
public class LOVSelectList extends XComponentPanel {

    private String listName;  
    private boolean multiselect = true;
    
    /**
     * Creates new form LOVMultiSelect
     */
    public LOVSelectList() {
        initComponents();
    }

    
    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
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

        setLayout(new java.awt.BorderLayout());

        xFormPanel1.setDynamic(true);
        xFormPanel1.setName("formControls"); // NOI18N
        xFormPanel1.setShowCaption(false);
        add(xFormPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void afterLoad() {
        com.rameses.rcp.common.ComponentBean cb = (com.rameses.rcp.common.ComponentBean)getComponentBean();
        MethodResolver mr = MethodResolver.getInstance();
        cb.setProperty("listName", getListName());
        cb.setProperty("multiselect", isMultiselect() );
        try {
            mr.invoke(cb, "init", null);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }        
    }

    public boolean isMultiselect() {
        return multiselect;
    }

    public void setMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
    }
    
}
