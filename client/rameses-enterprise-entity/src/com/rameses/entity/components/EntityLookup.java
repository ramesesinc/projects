/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.entity.components;

import com.rameses.common.PropertyResolver;
import com.rameses.rcp.control.XComponentPanel;
import com.rameses.rcp.ui.annotations.ComponentBean;

/**
 *
 * @author dell
 */
@ComponentBean("com.rameses.entity.components.EntityLookupModel")
public class EntityLookup extends XComponentPanel {

    private String onselect; 
    private String onempty;
    private String entitytype;
    
    private boolean allowCreate;
    private boolean allowOpen;
    
    
    public EntityLookup() {
        initComponents();
    }
    
    public String getExpression() {
        return xLookupField1.getExpression(); 
    }
    public void setExpression( String expression ) {
        xLookupField1.setExpression( expression ); 
    } 
    
    public String getOnselect() { return onselect; } 
    public void setOnselect( String onselect ) {
        this.onselect = onselect; 
    }
    
    public String getOnempty() { return onempty; } 
    public void setOnempty( String onempty ) {
        this.onempty = onempty; 
    } 
    
    public String getEntityType() {
        return entitytype;
    }
    public void setEntityType(String entitytype) {
        this.entitytype = entitytype;
    }    

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        xLookupField1.setEnabled(enabled);
        btnAdd.setEnabled(enabled);
        com.rameses.rcp.common.ComponentBean bean = (com.rameses.rcp.common.ComponentBean)getComponentBean(); 
        bean.setProperty("allowCreate", enabled);
        bean.setProperty("allowOpen", true);
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        
        Object caller = getBean();
        Object bean = getComponentBean(); 
        PropertyResolver pr = PropertyResolver.getInstance();
        if ( getOnselect() != null ) {
            Object handler = pr.getProperty(caller, getOnselect()); 
            pr.setProperty(bean, "onselect", handler);
        }
        if ( getOnempty() != null ) {
            Object handler = pr.getProperty(caller, getOnempty()); 
            pr.setProperty(bean, "onempty", handler);
        }

        pr.setProperty(bean, "entityTypeCaller", new EntityTypeCaller(getEntityType(), caller));
    }

    public class EntityTypeCaller { 
        private String type;
        private Object caller;
        
        EntityTypeCaller( String type, Object caller ) {
            this.type = type;
            this.caller = caller;
        }
        
        public Object getEntityType() {
            if ( type == null || type.trim().length()==0 ) {
                return null; 
            } 

            PropertyResolver pr = PropertyResolver.getInstance();
            return pr.getProperty(caller, type); 
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

        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        jPanel4 = new javax.swing.JPanel();
        btnView = new com.rameses.rcp.control.XButton();
        btnAdd = new com.rameses.rcp.control.XButton();

        setOpaque(false);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        xLookupField1.setExpression("#{entity.name} - #{entity.entityno} ");
        xLookupField1.setHandler("lookupEntity");
        xLookupField1.setName("entity"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(100, 19));
        add(xLookupField1);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jPanel4.setOpaque(false);
        com.rameses.rcp.control.layout.XLayout xLayout1 = new com.rameses.rcp.control.layout.XLayout();
        xLayout1.setSpacing(0);
        jPanel4.setLayout(xLayout1);

        btnView.setBorderPainted(false);
        btnView.setContentAreaFilled(false);
        btnView.setDisableWhen("#{allowOpen != true}");
        btnView.setIconResource("images/toolbars/open.png");
        btnView.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnView.setName("viewEntity"); // NOI18N
        btnView.setToolTipText("View Record");
        jPanel4.add(btnView);

        btnAdd.setBorderPainted(false);
        btnAdd.setCaption("");
        btnAdd.setContentAreaFilled(false);
        btnAdd.setDisableWhen("#{allowCreate != true}");
        btnAdd.setIconResource("images/toolbars/create.png");
        btnAdd.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnAdd.setName("addEntity"); // NOI18N
        btnAdd.setToolTipText("Add New Record");
        btnAdd.setVisibleWhen("#{allowCreate == true}");
        jPanel4.add(btnAdd);

        add(jPanel4);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XButton btnAdd;
    private com.rameses.rcp.control.XButton btnView;
    private javax.swing.JPanel jPanel4;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    // End of variables declaration//GEN-END:variables


}
