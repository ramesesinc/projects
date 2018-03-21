/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.common.treasury.components;

import com.rameses.rcp.control.XComponentPanel;
import com.rameses.rcp.ui.annotations.ComponentBean;

/**
 *
 * @author Elmo Nazareno
 */
@ComponentBean("com.rameses.common.treasury.components.ProgressPanelModel")
public class ProgressPanel extends XComponentPanel {

    private String handler;
    
    /**
     * Creates new form ProgressPanel
     */
    public ProgressPanel() {
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xButton2 = new com.rameses.rcp.control.XButton();
        xButton3 = new com.rameses.rcp.control.XButton();

        jPanel1.setLayout(new com.rameses.rcp.control.layout.YLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 0, 20, 10));
        jPanel2.setLayout(new com.rameses.rcp.control.layout.XLayout());

        xLabel4.setName("label"); // NOI18N
        xLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xLabel4.setExpression("#{label}");
        xLabel4.setFontStyle("font-size:12; font-weight:bold;");
        xLabel4.setForeground(new java.awt.Color(100, 100, 100));
        jPanel2.add(xLabel4);

        xLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xLabel5.setName("progressvalue"); // NOI18N
        xLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        xLabel5.setExpression("#{progressvalue}");
        xLabel5.setFontStyle("font-size:14; font-weight:bold;");
        xLabel5.setForeground(new java.awt.Color(0, 51, 153));
        jPanel2.add(xLabel5);

        jPanel1.add(jPanel2);

        xButton2.setName("doStop"); // NOI18N
        xButton2.setText("Stop");
        xButton2.setVisibleWhen("#{ mode == 'processing' }");

        xButton3.setName("doStart"); // NOI18N
        xButton3.setText("Start");
        xButton3.setVisibleWhen("#{ mode == 'init' }");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    // End of variables declaration//GEN-END:variables


    public String getHandler() {
        return handler;
    }
    public void setHandler(String handler) {
        this.handler = handler;
    }

    @Override
    protected void initComponentBean(com.rameses.rcp.common.ComponentBean bean) {
        bean.setProperty("handler", getProperty(getHandler())); 
        
        // this must be called always at the last part...
        bean.setProperty("init", null);        
    }
}
