/*
 * CancelFAASPage.java
 *
 * Created on November 25, 2011, 2:27 PM
 */

package com.rameses.gov.etracs.rpt.cancelledfaas.ui;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

@Template(FormPage.class)
@StyleSheet
public class CancelledFAASPage extends javax.swing.JPanel {
    
    /** Creates new form CancelFAASPage */
    public CancelledFAASPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        formPanel2 = new com.rameses.rcp.util.FormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("FAAS to Cancel");
        formPanel1.setBorder(xTitledBorder1);

        xLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel4.setCaption("State");
        xLabel4.setCaptionWidth(90);
        xLabel4.setExpression("#{entity.state}");
        xLabel4.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        xLabel4.setForeground(new java.awt.Color(153, 0, 0));
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel1.add(xLabel4);

        xLookupField1.setCaption("TD No. ");
        xLookupField1.setCaptionWidth(90);
        xLookupField1.setExpression("#{entity.faas.tdno}");
        xLookupField1.setHandler("lookupFaas");
        xLookupField1.setIndex(-100);
        xLookupField1.setName("entity.faas"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 19));
        xLookupField1.setRequired(true);
        formPanel1.add(xLookupField1);

        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel1.setCaption("Owner");
        xLabel1.setCaptionWidth(90);
        xLabel1.setDepends(new String[] {"entity.faas"});
        xLabel1.setExpression("#{entity.faas.owner.name}");
        xLabel1.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel1.add(xLabel1);

        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel2.setCaption("PIN");
        xLabel2.setCaptionWidth(90);
        xLabel2.setDepends(new String[] {"entity.faas"});
        xLabel2.setExpression("#{entity.faas.fullpin}");
        xLabel2.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel1.add(xLabel2);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Cancellation Information");
        formPanel2.setBorder(xTitledBorder2);

        xComboBox1.setCaption("Reason");
        xComboBox1.setCaptionWidth(90);
        xComboBox1.setExpression("#{item.name}");
        xComboBox1.setItems("cancelReasons");
        xComboBox1.setName("entity.reason"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(200, 22));
        xComboBox1.setRequired(true);
        formPanel2.add(xComboBox1);

        xIntegerField1.setCaption("Last Tax Year");
        xIntegerField1.setCaptionWidth(90);
        xIntegerField1.setName("entity.lasttaxyear"); // NOI18N
        formPanel2.add(xIntegerField1);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 250));

        xTextArea1.setLineWrap(true);
        xTextArea1.setWrapStyleWord(true);
        xTextArea1.setCaption("Remarks");
        xTextArea1.setCaptionWidth(90);
        xTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        xTextArea1.setName("entity.remarks"); // NOI18N
        xTextArea1.setPreferredSize(new java.awt.Dimension(164, 200));
        xTextArea1.setRequired(true);
        jScrollPane1.setViewportView(xTextArea1);

        formPanel2.add(jScrollPane1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(formPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                    .add(formPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(formPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private com.rameses.rcp.util.FormPanel formPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    // End of variables declaration//GEN-END:variables
    
}
