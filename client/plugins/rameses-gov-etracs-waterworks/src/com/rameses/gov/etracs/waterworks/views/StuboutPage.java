/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Rameses
 */
@Template(CrudFormPage.class)
public class StuboutPage extends javax.swing.JPanel {

    /**
     * Creates new form StuboutPage
     */
    public StuboutPage() {
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

        xTabbedPane1 = new com.rameses.rcp.control.XTabbedPane();
        xPanel1 = new com.rameses.rcp.control.XPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLookupField5 = new com.rameses.rcp.control.XLookupField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLookupField6 = new com.rameses.rcp.control.XLookupField();

        xTabbedPane1.setDynamic(true);
        xTabbedPane1.setItems("sections");

        xFormPanel1.setCaptionWidth(100);

        xTextField1.setCaption("Code");
        xTextField1.setName("entity.code"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 43));

        xTextArea1.setCaption("Description");
        xTextArea1.setExitOnTabKey(true);
        xTextArea1.setName("entity.description"); // NOI18N
        xTextArea1.setPreferredSize(new java.awt.Dimension(0, 41));
        xTextArea1.setRequired(true);
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setCaptionWidth(100);
        xFormPanel2.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel2.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel2.setShowCaption(false);
        xFormPanel2.setStretchWidth(100);

        xLookupField5.setCaption("Zone");
        xLookupField5.setExpression("#{entity.zone.code}");
        xLookupField5.setHandler("waterworks_sector_zone:lookup");
        xLookupField5.setName("entity.zone"); // NOI18N
        xLookupField5.setRequired(true);
        xLookupField5.setStretchWidth(50);
        xFormPanel2.add(xLookupField5);

        xLabel1.setBackground(new java.awt.Color(245, 245, 245));
        xLabel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        xLabel1.setCaption("Sector");
        xLabel1.setCaptionWidth(60);
        xLabel1.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xLabel1.setDepends(new String[] {"entity.zone"});
        xLabel1.setExpression("#{entity.zone.sector.code}");
        xLabel1.setOpaque(true);
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLabel1);

        xFormPanel1.add(xFormPanel2);

        xLabel2.setBackground(new java.awt.Color(245, 245, 245));
        xLabel2.setCaption("Reader");
        xLabel2.setDepends(new String[] {"entity.zone"});
        xLabel2.setExpression("#{entity.zone.reader.name}");
        xLabel2.setOpaque(true);
        xLabel2.setPadding(new java.awt.Insets(1, 1, 1, 1));
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel2);

        xLookupField6.setCaption("Barangay ");
        xLookupField6.setExpression("#{entity.barangay.name}");
        xLookupField6.setHandler("barangay:lookup");
        xLookupField6.setName("entity.barangay"); // NOI18N
        xLookupField6.setPreferredSize(new java.awt.Dimension(200, 20));
        xLookupField6.setRequired(true);
        xLookupField6.setStretchWidth(100);
        xFormPanel1.add(xLookupField6);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(204, Short.MAX_VALUE))
        );

        xTabbedPane1.addTab("General information", xPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLookupField xLookupField5;
    private com.rameses.rcp.control.XLookupField xLookupField6;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
}
