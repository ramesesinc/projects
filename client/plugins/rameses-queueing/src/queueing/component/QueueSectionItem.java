/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package queueing.component;

import com.rameses.rcp.control.XComponentPanel;
import com.rameses.rcp.ui.annotations.ComponentBean;

/**
 *
 * @author rameses
 */
@ComponentBean("queueing.component.QueueSectionItemModel")
public class QueueSectionItem extends XComponentPanel {

    public QueueSectionItem() {
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
        xLabel1 = new com.rameses.rcp.control.XLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        xButton2 = new com.rameses.rcp.control.XButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        xLabel1.setExpression("#{caption}");
        jPanel1.add(xLabel1);
        jPanel1.add(filler1);

        xButton2.setExpression("#{actionText}");
        xButton2.setName("execute"); // NOI18N
        jPanel1.add(xButton2);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        com.rameses.rcp.control.border.XLineBorder xLineBorder1 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder1.setHideBottom(true);
        xLineBorder1.setHideLeft(true);
        xLineBorder1.setHideRight(true);
        xLineBorder1.setLineColor(new java.awt.Color(180, 180, 180));
        xLineBorder1.setPadding(new java.awt.Insets(0, 0, 5, 0));
        jLabel1.setBorder(xLineBorder1);
        add(jLabel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XLabel xLabel1;
    // End of variables declaration//GEN-END:variables

}
