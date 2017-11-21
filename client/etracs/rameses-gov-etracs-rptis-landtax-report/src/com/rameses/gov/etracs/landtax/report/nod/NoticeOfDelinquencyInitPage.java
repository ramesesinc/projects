/*
 * NoticeOfDelinquencyInitPage.java
 *
 * Created on October 17, 2013, 10:57 PM
 */

package com.rameses.gov.etracs.landtax.report.nod;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;


@Template(FormPage.class)
@StyleSheet()
public class NoticeOfDelinquencyInitPage extends javax.swing.JPanel {
    
    /** Creates new form NoticeOfDelinquencyInitPage */
    public NoticeOfDelinquencyInitPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xDateField1 = new com.rameses.rcp.control.XDateField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(25, 10, 10, 10));
        xTitledBorder1.setTitle("Notice of Delinquency Initial");
        xFormPanel1.setBorder(xTitledBorder1);
        xFormPanel1.setCaption("Taxpayer");
        xFormPanel1.setCaptionWidth(110);
        xFormPanel1.setCellpadding(new java.awt.Insets(0, 5, 0, 0));
        xFormPanel1.setRequired(true);

        xLookupField1.setCaption("Taxpayer");
        xLookupField1.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xLookupField1.setExpression("#{item.name}");
        xLookupField1.setHandler("entity:lookup");
        xLookupField1.setName("taxpayer"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        com.rameses.rcp.control.border.XLineBorder xLineBorder1 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder1.setLineColor(new java.awt.Color(153, 153, 153));
        xLabel1.setBorder(xLineBorder1);
        xLabel1.setCaption("Address");
        xLabel1.setDepends(new String[] {"taxpayer"});
        xLabel1.setExpression("#{taxpayer.address.text}");
        xLabel1.setName("address"); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel1);

        xDateField1.setCaption("Date Computed");
        xDateField1.setName("dtcomputed"); // NOI18N
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(256, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(298, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    // End of variables declaration//GEN-END:variables
    
}
