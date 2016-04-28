/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author rameses
 */
@Template(OKCancelPage.class)
public class ChangeAccountOwnerPage extends javax.swing.JPanel {

    /**
     * Creates new form ChangeOwnerPage
     */
    public ChangeAccountOwnerPage() {
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

        xPanel1 = new com.rameses.rcp.control.XPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xPanel3 = new com.rameses.rcp.control.XPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Current Account Information");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setCaptionWidth(100);

        xLabel2.setBackground(new java.awt.Color(250, 250, 250));
        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(190, 190, 190)));
        xLabel2.setCaption("Owner Name");
        xLabel2.setCaptionWidth(120);
        xLabel2.setExpression("#{entity.owner.name}");
        xLabel2.setOpaque(true);
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel2);

        xLabel3.setBackground(new java.awt.Color(250, 250, 250));
        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(190, 190, 190)));
        xLabel3.setCaption("Account Name");
        xLabel3.setCaptionWidth(120);
        xLabel3.setExpression("#{entity.acctname}");
        xLabel3.setOpaque(true);
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel3);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("New Account Information");
        xPanel2.setBorder(xTitledBorder2);

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setCaptionWidth(120);

        xLookupField1.setCaption("Owner Name");
        xLookupField1.setExpression("#{item.name}");
        xLookupField1.setHandler("individualentity:lookup");
        xLookupField1.setName("info.owner"); // NOI18N
        xLookupField1.setStretchWidth(100);
        xFormPanel2.add(xLookupField1);

        xTextField1.setCaption("Account Name");
        xTextField1.setName("info.acctname"); // NOI18N
        xTextField1.setRequired(true);
        xTextField1.setStretchWidth(100);
        xFormPanel2.add(xTextField1);

        javax.swing.GroupLayout xPanel2Layout = new javax.swing.GroupLayout(xPanel2);
        xPanel2.setLayout(xPanel2Layout);
        xPanel2Layout.setHorizontalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel2Layout.setVerticalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("Reason/Remarks for Modification");
        xPanel3.setBorder(xTitledBorder3);

        xTextArea1.setCaption("Reason");
        xTextArea1.setExitOnTabKey(true);
        xTextArea1.setName("info.reason"); // NOI18N
        xTextArea1.setRequired(true);
        jScrollPane1.setViewportView(xTextArea1);

        javax.swing.GroupLayout xPanel3Layout = new javax.swing.GroupLayout(xPanel3);
        xPanel3.setLayout(xPanel3Layout);
        xPanel3Layout.setHorizontalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel3Layout.setVerticalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel2;
    private com.rameses.rcp.control.XPanel xPanel3;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}
