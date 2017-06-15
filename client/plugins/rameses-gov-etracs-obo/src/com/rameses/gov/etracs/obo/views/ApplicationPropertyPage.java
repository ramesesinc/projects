/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.obo.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author dell
 */
@Template(FormPage.class)
public class ApplicationPropertyPage extends javax.swing.JPanel {

    /**
     * Creates new form NewApplicationOwnershipPage
     */
    public ApplicationPropertyPage() {
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

        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        cTCIndividualEntryPage1 = new com.rameses.etracs.components.CTCIndividualEntryPage();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(30, 10, 10, 10));
        xTitledBorder1.setTitle("Property Information");
        xFormPanel2.setBorder(xTitledBorder1);
        xFormPanel2.setCaption("");
        xFormPanel2.setCaptionWidth(120);
        xFormPanel2.setPreferredSize(new java.awt.Dimension(0, 100));
        xFormPanel2.setShowCaption(false);

        xLookupField1.setCaption("TCT / OCT");
        xLookupField1.setExpression("#{entity.rptinfo.tdno}");
        xLookupField1.setHandler("lookupRpu");
        xLookupField1.setName("entity.rptinfo"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLookupField1);

        xLabel1.setCaption("PIN");
        xLabel1.setExpression("#{entity.rptinfo.fullpin}");
        xLabel1.setName("entity.rptinfo.text"); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel2.add(xLabel1);

        xLabel3.setCaption("Title No");
        xLabel3.setExpression("#{entity.rptinfo.titleno}");
        xLabel3.setName("entity.rptinfo.text"); // NOI18N
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel2.add(xLabel3);

        xLabel4.setCaption("Lot Owner");
        xLabel4.setExpression("#{entity.lotowner.name}");
        xLabel4.setName("entity.rptinfo.text"); // NOI18N
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel2.add(xLabel4);

        xCheckBox1.setCaption("");
        xCheckBox1.setEnabled(false);
        xCheckBox1.setName("entity.lotowned"); // NOI18N
        xCheckBox1.setPreferredSize(new java.awt.Dimension(0, 23));
        xCheckBox1.setText("Lot Owned");
        xFormPanel2.add(xCheckBox1);

        cTCIndividualEntryPage1.setCaption("Lot Owner CTC");
        cTCIndividualEntryPage1.setDepends(new String[] {"entity.lotowned"});
        cTCIndividualEntryPage1.setEntityName("entity.lotowner");
        cTCIndividualEntryPage1.setName("entity.lotowner.ctc"); // NOI18N
        cTCIndividualEntryPage1.setOpaque(false);
        cTCIndividualEntryPage1.setPreferredSize(new java.awt.Dimension(0, 69));
        cTCIndividualEntryPage1.setVisibleWhen("#{ entity.lotowned == false }");
        xFormPanel2.add(cTCIndividualEntryPage1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addGap(749, 749, 749))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(330, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.etracs.components.CTCIndividualEntryPage cTCIndividualEntryPage1;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    // End of variables declaration//GEN-END:variables
}
