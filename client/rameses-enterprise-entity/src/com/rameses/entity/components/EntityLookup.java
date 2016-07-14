/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.entity.components;

import com.rameses.rcp.control.XComponentPanel;
import com.rameses.rcp.ui.annotations.ComponentBean;

/**
 *
 * @author dell
 */
@ComponentBean("com.rameses.entity.components.EntityLookupModel")
public class EntityLookup extends XComponentPanel {

    public EntityLookup() {
        initComponents();
    }
    
    public String getExpression() {
        return xLookupField1.getExpression(); 
    }
    public void setExpression( String expression ) {
        xLookupField1.setExpression( expression ); 
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
        xButton1 = new com.rameses.rcp.control.XButton();
        xButton2 = new com.rameses.rcp.control.XButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        xLookupField1.setExpression("#{entity.name} - #{entity.entityno} ");
        xLookupField1.setHandler("lookupEntity");
        xLookupField1.setName("entity"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(100, 19));
        add(xLookupField1);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jPanel4.setLayout(new com.rameses.rcp.control.layout.XLayout());

        xButton1.setBorderPainted(false);
        xButton1.setCaption("");
        xButton1.setContentAreaFilled(false);
        xButton1.setIconResource("images/toolbars/create.png");
        xButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xButton1.setName("addEntity"); // NOI18N
        xButton1.setToolTipText("Add New Record");
        xButton1.setVisibleWhen("#{allowCreate}");
        jPanel4.add(xButton1);

        xButton2.setBorderPainted(false);
        xButton2.setContentAreaFilled(false);
        xButton2.setIconResource("images/toolbars/open.png");
        xButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xButton2.setName("viewEntity"); // NOI18N
        xButton2.setToolTipText("View Record");
        xButton2.setVisibleWhen("#{allowOpen}");
        jPanel4.add(xButton2);

        add(jPanel4);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel4;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    // End of variables declaration//GEN-END:variables
}
