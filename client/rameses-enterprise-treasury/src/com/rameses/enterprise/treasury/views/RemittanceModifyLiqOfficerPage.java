/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.treasury.views;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author wflores
 */
@Template(OKCancelPage.class)
public class RemittanceModifyLiqOfficerPage extends javax.swing.JPanel {

    public RemittanceModifyLiqOfficerPage() {
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
        xLabel1 = new com.rameses.rcp.control.XLabel();
        jPanel2 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();

        jPanel1.setLayout(new java.awt.BorderLayout());

        xLabel1.setBackground(new java.awt.Color(255, 255, 255));
        xLabel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(new com.rameses.rcp.control.border.XEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        xLabel1.setExpression("#{title}");
        xLabel1.setFontStyle("font-size:14; font-weight:bold;");
        xLabel1.setOpaque(true);
        jPanel1.add(xLabel1, java.awt.BorderLayout.PAGE_START);

        xFormPanel1.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        xFormPanel1.setCaptionOrientation(com.rameses.rcp.constant.UIConstants.TOP);

        xTextArea1.setCaption("Reason");
        xTextArea1.setName("reason"); // NOI18N
        xTextArea1.setRequired(true);
        xTextArea1.setStretchWidth(100);
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        xFormPanel2.setCaptionBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        xFormPanel2.setCaptionOrientation(com.rameses.rcp.constant.UIConstants.TOP);

        xLookupField1.setCaption("Liquidatiing Officer");
        xLookupField1.setCaptionWidth(150);
        xLookupField1.setExpression("#{info.liquidatingofficer.name}");
        xLookupField1.setHandler("lookupHandler");
        xLookupField1.setName("info.liquidatingofficer"); // NOI18N
        xLookupField1.setRequired(true);
        xLookupField1.setStretchWidth(100);
        xFormPanel2.add(xLookupField1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    // End of variables declaration//GEN-END:variables
}