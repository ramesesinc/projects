/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.lgu.aklan.terminal;
 
/**
 *
 * @author wflores
 */
public class TerminalPassHomePage extends javax.swing.JPanel {
 
    /**
     * Creates new form TerminalPassHomePage
     */
    public TerminalPassHomePage() {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        xList1 = new com.rameses.rcp.control.XList();
        xSubFormPanel1 = new com.rameses.rcp.control.XSubFormPanel();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(160, 130));

        xList1.setCellHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xList1.setCellHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        xList1.setCellVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        xList1.setExpression("#{item.caption}");
        xList1.setFixedCellHeight(100);
        xList1.setFontStyle("font-size:14;font-weight:bold;");
        xList1.setHandler("listhandler");
        xList1.setName("selectedItem"); // NOI18N
        xList1.setPadding(new java.awt.Insets(5, 5, 5, 5));
        jScrollPane1.setViewportView(xList1);

        add(jScrollPane1, java.awt.BorderLayout.LINE_START);

        xSubFormPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0), null));
        xSubFormPanel1.setDepends(new String[] {"selectedItem"});
        xSubFormPanel1.setDynamic(true);
        xSubFormPanel1.setHandler("formhandler");
        xSubFormPanel1.setName("subform"); // NOI18N
        xSubFormPanel1.setOpaque(false);
        add(xSubFormPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XList xList1;
    private com.rameses.rcp.control.XSubFormPanel xSubFormPanel1;
    // End of variables declaration//GEN-END:variables
}
