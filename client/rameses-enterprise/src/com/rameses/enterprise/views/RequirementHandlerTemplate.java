/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.views;


/**
 *
 * @author dell
 */
public class RequirementHandlerTemplate extends javax.swing.JPanel {

    /**
     * Creates new form CrudFormPage
     */
    public RequirementHandlerTemplate() {
        initComponents();
        btnSave.setToolTipText("Update");
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlstat = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new com.rameses.rcp.control.XButton();
        xActionBar1 = new com.rameses.rcp.control.XActionBar();

        setLayout(new java.awt.BorderLayout());

        pnlstat.setPreferredSize(new java.awt.Dimension(400, 25));

        javax.swing.GroupLayout pnlstatLayout = new javax.swing.GroupLayout(pnlstat);
        pnlstat.setLayout(pnlstatLayout);
        pnlstatLayout.setHorizontalGroup(
            pnlstatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 727, Short.MAX_VALUE)
        );
        pnlstatLayout.setVerticalGroup(
            pnlstatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        add(pnlstat, java.awt.BorderLayout.PAGE_END);

        jPanel2.setPreferredSize(new java.awt.Dimension(420, 70));
        jPanel2.setLayout(new com.rameses.rcp.control.layout.YLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(420, 35));

        xLabel1.setBackground(new java.awt.Color(255, 255, 255));
        xLabel1.setExpression("#{title}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        xLabel1.setOpaque(true);
        xLabel1.setPreferredSize(new java.awt.Dimension(41, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel3);

        jToolBar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 30));

        btnSave.setAccelerator("ctrl S");
        btnSave.setCaption("");
        btnSave.setFocusable(false);
        btnSave.setIconResource("images/toolbars/save.png");
        btnSave.setName("update"); // NOI18N
        btnSave.setText("Update");
        jToolBar1.add(btnSave);

        xActionBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        xActionBar1.setDynamic(true);
        xActionBar1.setName("extActions"); // NOI18N
        jToolBar1.add(xActionBar1);

        jPanel2.add(jToolBar1);

        add(jPanel2, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XButton btnSave;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel pnlstat;
    private com.rameses.rcp.control.XActionBar xActionBar1;
    private com.rameses.rcp.control.XLabel xLabel1;
    // End of variables declaration//GEN-END:variables
}
