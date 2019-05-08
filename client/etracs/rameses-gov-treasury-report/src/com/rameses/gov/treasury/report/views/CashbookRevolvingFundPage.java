/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.treasury.report.views;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Elmo Nazareno
 */
@Template(CrudFormPage.class)
@StyleSheet
public class CashbookRevolvingFundPage extends javax.swing.JPanel {

    /**
     * Creates new form CashbookRevolvingFundPage
     */
    public CashbookRevolvingFundPage() {
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
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setCaptionWidth(150);

        xDateField1.setCaption("Control Date");
        xDateField1.setName("entity.controldate"); // NOI18N
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        xDecimalField2.setCaption("Amount");
        xDecimalField2.setName("entity.amount"); // NOI18N
        xDecimalField2.setRequired(true);
        xFormPanel1.add(xDecimalField2);

        xLabel1.setCaption("State");
        xLabel1.setExpression("#{entity.state}");
        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel1.setPreferredSize(new java.awt.Dimension(250, 20));
        xFormPanel1.add(xLabel1);

        xLabel2.setCaption("Date Filed");
        xLabel2.setExpression("#{entity.dtfiled}");
        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel2.setPreferredSize(new java.awt.Dimension(250, 20));
        xFormPanel1.add(xLabel2);

        xLabel3.setCaption("Filed By");
        xLabel3.setExpression("#{entity.filedby.name}");
        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel3.setPreferredSize(new java.awt.Dimension(250, 20));
        xFormPanel1.add(xLabel3);

        xLabel4.setCaption("Issue To");
        xLabel4.setExpression("#{entity.issueto.name}");
        xLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel4.setPreferredSize(new java.awt.Dimension(250, 20));
        xFormPanel1.add(xLabel4);

        xLabel5.setCaption("Remarks");
        xLabel5.setExpression("#{entity.remarks}");
        xLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel5.setPreferredSize(new java.awt.Dimension(250, 50));
        xFormPanel1.add(xLabel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    // End of variables declaration//GEN-END:variables
}
