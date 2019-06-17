/*
 * ImageViewerPage.java
 *
 * Created on February 25, 2014, 7:36 PM
 */

package com.rameses.gov.etracs.rptis.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Rameses
 */
@Template(FormPage.class)
@StyleSheet
public class SketchPage extends javax.swing.JPanel {
    
    /** Creates new form ImageViewerPage */
    public SketchPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        drawComponent1 = new com.rameses.rcp.draw.components.DrawComponent();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xTextField10 = new com.rameses.rcp.control.XTextField();
        xTextField13 = new com.rameses.rcp.control.XTextField();
        xTextField17 = new com.rameses.rcp.control.XTextField();
        xFormPanel7 = new com.rameses.rcp.control.XFormPanel();
        xTextField16 = new com.rameses.rcp.control.XTextField();
        xTextField33 = new com.rameses.rcp.control.XTextField();
        xTextField15 = new com.rameses.rcp.control.XTextField();
        xTextField32 = new com.rameses.rcp.control.XTextField();
        xTextField18 = new com.rameses.rcp.control.XTextField();
        xTextField34 = new com.rameses.rcp.control.XTextField();

        drawComponent1.setHandler("handler");
        drawComponent1.setName("editor"); // NOI18N

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(20, 10, 10, 10));
        xTitledBorder1.setTitle("Boundary Details");
        xFormPanel5.setBorder(xTitledBorder1);
        xFormPanel5.setCaptionPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel5.setPadding(new java.awt.Insets(0, 5, 2, 5));

        xFormPanel6.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel6.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel6.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel6.setShowCaption(false);

        xTextField10.setCaption("Survey No.");
        xTextField10.setName("entity.rp.surveyno"); // NOI18N
        xTextField10.setPreferredSize(new java.awt.Dimension(250, 20));
        xFormPanel6.add(xTextField10);

        xTextField13.setCaption("Lot No.");
        xTextField13.setName("entity.rp.cadastrallotno"); // NOI18N
        xTextField13.setCaptionWidth(70);
        xTextField13.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xTextField13.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField13.setStretchWidth(70);
        xFormPanel6.add(xTextField13);

        xTextField17.setCaption("Block No.");
        xTextField17.setName("entity.rp.blockno"); // NOI18N
        xTextField17.setCaptionWidth(75);
        xTextField17.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xTextField17.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField17.setStretchWidth(30);
        xFormPanel6.add(xTextField17);

        xFormPanel5.add(xFormPanel6);

        xFormPanel7.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel7.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel7.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel7.setShowCaption(false);

        xTextField16.setCaption("Street");
        xTextField16.setName("entity.rp.street"); // NOI18N
        xTextField16.setPreferredSize(new java.awt.Dimension(250, 20));
        xFormPanel7.add(xTextField16);

        xTextField33.setCaption("Purok");
        xTextField33.setName("entity.rp.purok"); // NOI18N
        xTextField33.setCaptionWidth(70);
        xTextField33.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xTextField33.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField33.setStretchWidth(50);
        xFormPanel7.add(xTextField33);

        xFormPanel5.add(xFormPanel7);

        xTextField15.setCaption("North");
        xTextField15.setName("entity.rp.north"); // NOI18N
        xTextField15.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField15.setStretchWidth(100);
        xFormPanel5.add(xTextField15);

        xTextField32.setCaption("East");
        xTextField32.setName("entity.rp.east"); // NOI18N
        xTextField32.setStretchWidth(100);
        xFormPanel5.add(xTextField32);

        xTextField18.setCaption("South");
        xTextField18.setName("entity.rp.south"); // NOI18N
        xTextField18.setStretchWidth(100);
        xFormPanel5.add(xTextField18);

        xTextField34.setCaption("West");
        xTextField34.setName("entity.rp.west"); // NOI18N
        xTextField34.setStretchWidth(100);
        xFormPanel5.add(xTextField34);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(drawComponent1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
                    .addComponent(xFormPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drawComponent1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.draw.components.DrawComponent drawComponent1;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XFormPanel xFormPanel7;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField13;
    private com.rameses.rcp.control.XTextField xTextField15;
    private com.rameses.rcp.control.XTextField xTextField16;
    private com.rameses.rcp.control.XTextField xTextField17;
    private com.rameses.rcp.control.XTextField xTextField18;
    private com.rameses.rcp.control.XTextField xTextField32;
    private com.rameses.rcp.control.XTextField xTextField33;
    private com.rameses.rcp.control.XTextField xTextField34;
    private com.rameses.rcp.control.XTextField xTextField9;
    // End of variables declaration//GEN-END:variables
    
}
