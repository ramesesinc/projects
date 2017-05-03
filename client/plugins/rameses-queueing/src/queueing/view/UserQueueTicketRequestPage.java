/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package queueing.view;

import queueing.component.PageLayout;

/**
 *
 * @author wflores
 */
public class UserQueueTicketRequestPage extends javax.swing.JPanel {

    public UserQueueTicketRequestPage() {
        initComponents();
                
        PageLayout layout = new PageLayout();
        layout = new PageLayout();
        tabitem2.setLayout( layout ); 
        tabitem2.removeAll(); 
        tabitem2.add( xFormPanel2, PageLayout.CONTENT);
        tabitem2.add( footer2panel, PageLayout.FOOTER );
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabitem2 = new javax.swing.JPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        footer2panel = new javax.swing.JPanel();
        xButton2 = new com.rameses.rcp.control.XButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setPreferredSize(new java.awt.Dimension(355, 451));
        setLayout(new java.awt.BorderLayout());

        tabitem2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabitem2.setLayout(new java.awt.BorderLayout());

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setName("queueSectionHandler"); // NOI18N
        xFormPanel2.setShowCaption(false);
        xFormPanel2.setShowCategory(true);
        tabitem2.add(xFormPanel2, java.awt.BorderLayout.NORTH);

        footer2panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        footer2panel.setLayout(new com.rameses.rcp.control.layout.XLayout());

        xButton2.setExpression("Refresh");
        xButton2.setName("refreshQueueSections"); // NOI18N
        footer2panel.add(xButton2);

        tabitem2.add(footer2panel, java.awt.BorderLayout.PAGE_END);

        add(tabitem2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel footer2panel;
    private javax.swing.JPanel tabitem2;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    // End of variables declaration//GEN-END:variables
}
