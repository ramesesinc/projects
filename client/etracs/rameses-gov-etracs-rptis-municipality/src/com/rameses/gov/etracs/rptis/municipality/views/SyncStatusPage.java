/*
 * SyncPage.java
 *
 * Created on June 28, 2014, 4:48 PM
 */

package com.rameses.gov.etracs.rptis.municipality.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Toshiba
 */
@Template(FormPage.class)
@StyleSheet
public class SyncStatusPage extends javax.swing.JPanel {
    
    /** Creates new form SyncPage */
    public SyncStatusPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel1 = new com.rameses.rcp.control.XLabel();

        jPanel1.setLayout(new java.awt.BorderLayout());

        xTextArea1.setEditable(false);
        xTextArea1.setHandler("loghandler");
        jScrollPane1.setViewportView(xTextArea1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        xFormPanel1.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);

        xIntegerField1.setCaption("Revision Year");
        xIntegerField1.setCaptionWidth(100);
        xIntegerField1.setName("ry"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(80, 20));
        xFormPanel1.add(xIntegerField1);

        xLabel4.setIconResource("com/rameses/rcp/icons/loading16.gif");
        xLabel4.setShowCaption(false);
        xLabel4.setVisibleWhen("#{processing==true}");
        xFormPanel1.add(xLabel4);

        xLabel1.setExpression(" Processing...");
        xLabel1.setShowCaption(false);
        xLabel1.setVisibleWhen("#{processing==true}");
        xFormPanel1.add(xLabel1);

        jPanel1.add(xFormPanel1, java.awt.BorderLayout.NORTH);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 443, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    // End of variables declaration//GEN-END:variables
    
}
