/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package queueing.view;

import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author wflores
 */
@Template(QueuePanelTemplate.class)
public class UserQueuePage extends javax.swing.JPanel {

    public UserQueuePage() {
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        jPanel2 = new javax.swing.JPanel();
        xButton1 = new com.rameses.rcp.control.XButton();
        jPanel3 = new javax.swing.JPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        jPanel4 = new javax.swing.JPanel();
        xButton2 = new com.rameses.rcp.control.XButton();

        setPreferredSize(new java.awt.Dimension(370, 400));
        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel1.setLayout(new java.awt.BorderLayout());

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setDynamic(true);
        xFormPanel1.setName("formControls"); // NOI18N
        xFormPanel1.setShowCaption(false);
        xFormPanel1.setShowCategory(true);
        jPanel1.add(xFormPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanel2.setLayout(new com.rameses.rcp.control.layout.XLayout());

        xButton1.setExpression("Edit");
        xButton1.setName("edit"); // NOI18N
        jPanel2.add(xButton1);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("  Allowed Sections  ", jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel3.setLayout(new java.awt.BorderLayout());

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setName("queueSectionHandler"); // NOI18N
        xFormPanel2.setShowCaption(false);
        xFormPanel2.setShowCategory(true);
        jPanel3.add(xFormPanel2, java.awt.BorderLayout.NORTH);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanel4.setLayout(new com.rameses.rcp.control.layout.XLayout());

        xButton2.setExpression("Refresh");
        xButton2.setName("refreshQueueSections"); // NOI18N
        xButton2.setText("Refresh");
        jPanel4.add(xButton2);

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("  Request Queue Number  ", jPanel3);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    // End of variables declaration//GEN-END:variables
}
