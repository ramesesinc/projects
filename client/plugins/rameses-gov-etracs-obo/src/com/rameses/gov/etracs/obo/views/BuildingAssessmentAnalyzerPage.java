/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.obo.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author dell
 */
@Template(FormPage.class)
public class BuildingAssessmentAnalyzerPage extends javax.swing.JPanel {

    /**
     * Creates new form BuildingAssessmentAnalyzer
     */
    public BuildingAssessmentAnalyzerPage() {
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
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        lOVList1 = new com.rameses.enterprise.components.LOVList();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xButton1 = new com.rameses.rcp.control.XButton();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Building Info");
        jPanel1.setBorder(xTitledBorder1);

        lOVList1.setCaption("Work Type");
        lOVList1.setListName("OBO_BUILDING_WORK_TYPE");
        lOVList1.setName("entity.worktype"); // NOI18N
        lOVList1.setRequired(true);
        xFormPanel1.add(lOVList1);

        xLookupField1.setCaption("Building Use");
        xLookupField1.setExpression("#{entity.buildinguse.objid}");
        xLookupField1.setHandler("obo_building_use:lookup");
        xLookupField1.setName("entity.buildinguse"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xDecimalField1.setCaption("Area (sqm)");
        xDecimalField1.setName("entity.floorarea"); // NOI18N
        xDecimalField1.setRequired(true);
        xFormPanel1.add(xDecimalField1);

        xIntegerField1.setCaption("No. of Units");
        xIntegerField1.setName("entity.numunits"); // NOI18N
        xIntegerField1.setRequired(true);
        xFormPanel1.add(xIntegerField1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        xButton1.setName("getInfos"); // NOI18N
        xButton1.setText("Get Infos");
        xButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(524, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(314, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private com.rameses.enterprise.components.LOVList lOVList1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    // End of variables declaration//GEN-END:variables
}
