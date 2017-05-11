package com.rameses.enterprise.treasury.views;

/**
 *
 * @author  wflores 
 */
public class CashReceiptQueryFilter extends javax.swing.JPanel {
    
    /** Creates new form CashReceiptQueryFilter */
    public CashReceiptQueryFilter() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        defaultLabel1 = new com.rameses.rcp.control.text.DefaultLabel();
        xActionTextField1 = new com.rameses.rcp.control.XActionTextField();
        defaultLabel2 = new com.rameses.rcp.control.text.DefaultLabel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setAlignmentX(0.1F);
        setAlignmentY(0.1F);
        setLayout(new com.rameses.rcp.control.layout.XLayout());

        defaultLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        defaultLabel1.setDisplayedMnemonic('s');
        defaultLabel1.setForeground(new java.awt.Color(80, 80, 80));
        defaultLabel1.setText("Search");
        defaultLabel1.setFontStyle("font-weight:bold;");
        defaultLabel1.setPreferredSize(new java.awt.Dimension(50, 14));
        add(defaultLabel1);

        xActionTextField1.setActionName("search");
        xActionTextField1.setCaption("Search");
        xActionTextField1.setFocusAccelerator('s');
        xActionTextField1.setFocusKeyStroke("F3");
        xActionTextField1.setName("query.searchtext"); // NOI18N
        xActionTextField1.setPreferredSize(new java.awt.Dimension(220, 22));
        add(xActionTextField1);

        defaultLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 5));
        defaultLabel2.setForeground(new java.awt.Color(80, 80, 80));
        defaultLabel2.setText("Sort By");
        defaultLabel2.setFontStyle("font-weight:bold;");
        add(defaultLabel2);

        xComboBox1.setExpression("#{item.caption}");
        xComboBox1.setItems("sortFields");
        xComboBox1.setName("sortField"); // NOI18N
        add(xComboBox1);

        xComboBox2.setDepends(new String[] {"sortField"});
        xComboBox2.setDisableWhen("#{sortField == null}");
        xComboBox2.setItems("sortTypes");
        xComboBox2.setName("sortType"); // NOI18N
        add(xComboBox2);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.text.DefaultLabel defaultLabel1;
    private com.rameses.rcp.control.text.DefaultLabel defaultLabel2;
    private com.rameses.rcp.control.XActionTextField xActionTextField1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    // End of variables declaration//GEN-END:variables
    
}