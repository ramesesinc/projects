/*
 * BarangayPage.java
 *
 * Created on July 31, 2013, 9:19 PM
 */

package com.rameses.gov.lgu.ui;


import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Elmo
 */
@Template(value=FormPage.class)
@StyleSheet
public class BarangayPage extends javax.swing.JPanel {
    
    public BarangayPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xTextField5 = new com.rameses.rcp.control.XTextField();
        xTextField6 = new com.rameses.rcp.control.XTextField();
        xTextField7 = new com.rameses.rcp.control.XTextField();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xTextField10 = new com.rameses.rcp.control.XTextField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xTextField8 = new com.rameses.rcp.control.XTextField();
        xTextField9 = new com.rameses.rcp.control.XTextField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(25, 0, 0, 0));
        xTitledBorder1.setTitle("Barangay Information");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionWidth(120);
        xFormPanel1.setIndex(1);
        xTextField1.setCaption("Captain Name");
        xTextField1.setIndex(10);
        xTextField1.setName("entity.captain.name");
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField1.setRequired(true);
        xFormPanel1.add(xTextField1);

        xTextField2.setCaption("Captain Title");
        xTextField2.setIndex(11);
        xTextField2.setName("entity.captain.title");
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel1.add(xTextField2);

        xTextField3.setCaption("Captain Office");
        xTextField3.setIndex(12);
        xTextField3.setName("entity.captain.office");
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel1.add(xTextField3);

        xFormPanel3.setCaptionWidth(120);
        xFormPanel3.setIndex(2);
        xTextField5.setCaption("Treasurer Name");
        xTextField5.setIndex(13);
        xTextField5.setName("entity.treasurer.name");
        xTextField5.setNullWhenEmpty(false);
        xTextField5.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField5.setRequired(true);
        xFormPanel3.add(xTextField5);

        xTextField6.setCaption("Treasurer Title");
        xTextField6.setIndex(14);
        xTextField6.setName("entity.treasurer.title");
        xTextField6.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel3.add(xTextField6);

        xTextField7.setCaption("Treasurer Office");
        xTextField7.setIndex(15);
        xTextField7.setName("entity.treasurer.office");
        xTextField7.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel3.add(xTextField7);

        xFormPanel2.setCaptionWidth(120);
        xFormPanel2.setIndex(1);
        xTextField10.setCaption("Barangay Code");
        xTextField10.setName("entity.code");
        xTextField10.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField10.setRequired(true);
        xTextField10.setSpaceChar('_');
        xFormPanel2.add(xTextField10);

        xTextField4.setCaption("Barangay Name");
        xTextField4.setIndex(10);
        xTextField4.setName("entity.name");
        xTextField4.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField4.setRequired(true);
        xFormPanel2.add(xTextField4);

        xTextField8.setCaption("Index No");
        xTextField8.setIndex(11);
        xTextField8.setName("entity.indexno");
        xTextField8.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField8.setRequired(true);
        xFormPanel2.add(xTextField8);

        xTextField9.setCaption("PIN");
        xTextField9.setIndex(12);
        xTextField9.setName("entity.pin");
        xTextField9.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField9.setRequired(true);
        xFormPanel2.add(xTextField9);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, xFormPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, xFormPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .add(xFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(xFormPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xFormPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(24, 24, 24))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField5;
    private com.rameses.rcp.control.XTextField xTextField6;
    private com.rameses.rcp.control.XTextField xTextField7;
    private com.rameses.rcp.control.XTextField xTextField8;
    private com.rameses.rcp.control.XTextField xTextField9;
    // End of variables declaration//GEN-END:variables
    
}
