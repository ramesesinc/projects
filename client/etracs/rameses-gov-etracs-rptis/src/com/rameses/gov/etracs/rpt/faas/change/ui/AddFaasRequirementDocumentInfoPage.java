/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rpt.faas.change.ui;

/**
 *
 * @author Toshiba
 */
public class AddFaasRequirementDocumentInfoPage extends javax.swing.JPanel {

    /**
     * Creates new form AddFaasRequirementDocumentInfoPage
     */
    public AddFaasRequirementDocumentInfoPage() {
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

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();

        setLayout(new java.awt.BorderLayout());

        xFormPanel1.setPadding(new java.awt.Insets(0, 0, 0, 0));

        xComboBox1.setCaption("Requirement");
        xComboBox1.setCaptionWidth(100);
        xComboBox1.setExpression("#{item.name}");
        xComboBox1.setItems("requirementtypes");
        xComboBox1.setName("redflag.info.requirementtype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        add(xFormPanel1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    // End of variables declaration//GEN-END:variables
}
