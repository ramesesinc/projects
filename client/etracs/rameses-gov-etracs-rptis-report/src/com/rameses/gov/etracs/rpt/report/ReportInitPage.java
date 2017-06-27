/*
 * ReportInitPage.java
 *
 * Created on July 18, 2013, 5:41 PM
 */

package com.rameses.gov.etracs.rpt.report;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;


@Template(FormPage.class)
@StyleSheet
public class ReportInitPage extends javax.swing.JPanel {
    
    /** Creates new form ReportInitPage */
    public ReportInitPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Initial Information");
        xFormPanel1.setBorder(xTitledBorder1);
        xFormPanel1.setCaptionWidth(110);
        xFormPanel1.setName("formControl"); // NOI18N

        xPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        xPanel2.setVisibleWhen("#{mode == 'processing'}");
        xPanel2.setLayout(new java.awt.BorderLayout());

        xLabel3.setFontStyle("font-weight:bold;font-size:12;");
        xLabel3.setForeground(new java.awt.Color(51, 51, 51));
        xLabel3.setPadding(new java.awt.Insets(1, 5, 1, 1));
        xLabel3.setPreferredSize(new java.awt.Dimension(150, 20));
        xLabel3.setText("Processing request please wait...");
        xPanel2.add(xLabel3, java.awt.BorderLayout.CENTER);

        xLabel4.setIconResource("com/rameses/rcp/icons/loading16.gif");
        xPanel2.add(xLabel4, java.awt.BorderLayout.WEST);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(27, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(346, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XPanel xPanel2;
    // End of variables declaration//GEN-END:variables
    
}
