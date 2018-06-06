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

        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xTextField12 = new com.rameses.rcp.control.XTextField();
        xTextField30 = new com.rameses.rcp.control.XTextField();
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xTextField14 = new com.rameses.rcp.control.XTextField();
        xTextField31 = new com.rameses.rcp.control.XTextField();
        drawComponent1 = new com.rameses.rcp.draw.components.DrawComponent();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Boundary Details");
        xFormPanel3.setBorder(xTitledBorder1);
        xFormPanel3.setCaptionPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel3.setPadding(new java.awt.Insets(0, 5, 2, 5));

        xFormPanel5.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel5.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel5.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel5.setShowCaption(false);

        xTextField12.setCaption("North");
        xTextField12.setName("entity.rp.north"); // NOI18N
        xTextField12.setStretchWidth(50);
        xFormPanel5.add(xTextField12);

        xTextField30.setCaption("East");
        xTextField30.setCaptionWidth(70);
        xTextField30.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xTextField30.setName("entity.rp.east"); // NOI18N
        xTextField30.setStretchWidth(50);
        xFormPanel5.add(xTextField30);

        xFormPanel3.add(xFormPanel5);

        xFormPanel6.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel6.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel6.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel6.setShowCaption(false);

        xTextField14.setCaption("South");
        xTextField14.setName("entity.rp.south"); // NOI18N
        xTextField14.setStretchWidth(50);
        xFormPanel6.add(xTextField14);

        xTextField31.setCaption("West");
        xTextField31.setCaptionWidth(70);
        xTextField31.setCellPadding(new java.awt.Insets(0, 5, 0, 0));
        xTextField31.setName("entity.rp.west"); // NOI18N
        xTextField31.setStretchWidth(50);
        xFormPanel6.add(xTextField31);

        xFormPanel3.add(xFormPanel6);

        drawComponent1.setHandler("handler");
        drawComponent1.setName("editor"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(drawComponent1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drawComponent1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.draw.components.DrawComponent drawComponent1;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XTextField xTextField12;
    private com.rameses.rcp.control.XTextField xTextField14;
    private com.rameses.rcp.control.XTextField xTextField30;
    private com.rameses.rcp.control.XTextField xTextField31;
    // End of variables declaration//GEN-END:variables
    
}
