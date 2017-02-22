package com.rameses.gov.etracs.rpt.util;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;


@StyleSheet
@Template(FormPage.class)
public class ExportImportPage extends javax.swing.JPanel {
    
    /** Creates new form ReportInitPage */
    public ExportImportPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xPanel1 = new com.rameses.rcp.control.XPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xFileBrowser1 = new com.rameses.rcp.control.XFileBrowser();

        xPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        xPanel1.setVisibleWhen("#{mode != 'init'}");
        xPanel1.setLayout(new java.awt.BorderLayout());

        xLabel1.setFontStyle("font-weight:bold;font-size:12;");
        xLabel1.setForeground(new java.awt.Color(51, 51, 51));
        xLabel1.setName("msg"); // NOI18N
        xLabel1.setPadding(new java.awt.Insets(1, 5, 1, 1));
        xLabel1.setPreferredSize(new java.awt.Dimension(150, 20));
        xLabel1.setText("Processing request please wait...");
        xPanel1.add(xLabel1, java.awt.BorderLayout.CENTER);

        xLabel2.setIconResource("com/rameses/rcp/icons/loading16.gif");
        xLabel2.setName("progress"); // NOI18N
        xPanel1.add(xLabel2, java.awt.BorderLayout.WEST);

        xFileBrowser1.setCaption("File Name");
        xFileBrowser1.setDepends(new String[] {"file"});
        xFileBrowser1.setName("file"); // NOI18N
        xFileBrowser1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFileBrowser1.setRequired(true);
        xFormPanel1.add(xFileBrowser1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XFileBrowser xFileBrowser1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XPanel xPanel1;
    // End of variables declaration//GEN-END:variables
    
}