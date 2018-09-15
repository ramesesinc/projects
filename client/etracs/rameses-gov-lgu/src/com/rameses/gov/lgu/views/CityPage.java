/*
 * CityPage.java
 *
 * Created on July 31, 2013, 9:19 PM
 */

package com.rameses.gov.lgu.views;



import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author  Elmo
 */
@Template(value=CrudFormPage.class)
@StyleSheet
public class CityPage extends javax.swing.JPanel {
    
    public CityPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xTextField15 = new com.rameses.rcp.control.XTextField();
        xTextField12 = new com.rameses.rcp.control.XTextField();
        xTextField13 = new com.rameses.rcp.control.XTextField();
        xTextField14 = new com.rameses.rcp.control.XTextField();
        xTextField10 = new com.rameses.rcp.control.XTextField();
        xTextField11 = new com.rameses.rcp.control.XTextField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField7 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xTextField8 = new com.rameses.rcp.control.XTextField();
        xTextField5 = new com.rameses.rcp.control.XTextField();
        xTextField6 = new com.rameses.rcp.control.XTextField();
        xTextField9 = new com.rameses.rcp.control.XTextField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(25, 0, 0, 0));
        xTitledBorder1.setTitle("City Information");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel5.setCaptionWidth(120);
        xFormPanel5.setIndex(1);

        xTextField15.setCaption("City code");
        xTextField15.setName("entity.code"); // NOI18N
        xTextField15.setEnabled(false);
        xTextField15.setIndex(10);
        xTextField15.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel5.add(xTextField15);

        xTextField12.setCaption("City name");
        xTextField12.setEnabled(false);
        xTextField12.setIndex(10);
        xTextField12.setName("entity.name"); // NOI18N
        xTextField12.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel5.add(xTextField12);

        xTextField13.setCaption("Index No");
        xTextField13.setEnabled(false);
        xTextField13.setIndex(11);
        xTextField13.setName("entity.indexno"); // NOI18N
        xTextField13.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel5.add(xTextField13);

        xTextField14.setCaption("PIN");
        xTextField14.setEnabled(false);
        xTextField14.setIndex(12);
        xTextField14.setName("entity.pin"); // NOI18N
        xTextField14.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel5.add(xTextField14);

        xTextField10.setCaption("Full Name");
        xTextField10.setIndex(10);
        xTextField10.setName("entity.fullname"); // NOI18N
        xTextField10.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField10.setRequired(true);
        xFormPanel5.add(xTextField10);

        xTextField11.setCaption("Address");
        xTextField11.setIndex(10);
        xTextField11.setName("entity.address"); // NOI18N
        xTextField11.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField11.setRequired(true);
        xFormPanel5.add(xTextField11);

        xTextField1.setCaption("Mayor Name");
        xTextField1.setName("entity.mayor.name"); // NOI18N
        xTextField1.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xTextField1.setIndex(10);
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField1.setRequired(true);
        xFormPanel5.add(xTextField1);

        xTextField2.setCaption("Mayor Title");
        xTextField2.setIndex(11);
        xTextField2.setName("entity.mayor.title"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField2.setRequired(true);
        xFormPanel5.add(xTextField2);

        xTextField7.setCaption("Mayor Office");
        xTextField7.setIndex(12);
        xTextField7.setName("entity.mayor.office"); // NOI18N
        xTextField7.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField7.setRequired(true);
        xFormPanel5.add(xTextField7);

        xTextField3.setCaption("Assessor Name");
        xTextField3.setName("entity.assessor.name"); // NOI18N
        xTextField3.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xTextField3.setIndex(13);
        xTextField3.setNullWhenEmpty(false);
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField3.setRequired(true);
        xFormPanel5.add(xTextField3);

        xTextField4.setCaption("Assessor Title");
        xTextField4.setIndex(14);
        xTextField4.setName("entity.assessor.title"); // NOI18N
        xTextField4.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField4.setRequired(true);
        xFormPanel5.add(xTextField4);

        xTextField8.setCaption("Assessor Office");
        xTextField8.setIndex(15);
        xTextField8.setName("entity.assessor.office"); // NOI18N
        xTextField8.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField8.setRequired(true);
        xFormPanel5.add(xTextField8);

        xTextField5.setCaption("Treasurer Name");
        xTextField5.setName("entity.treasurer.name"); // NOI18N
        xTextField5.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xTextField5.setIndex(16);
        xTextField5.setNullWhenEmpty(false);
        xTextField5.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField5.setRequired(true);
        xFormPanel5.add(xTextField5);

        xTextField6.setCaption("Treasurer Title");
        xTextField6.setIndex(17);
        xTextField6.setName("entity.treasurer.title"); // NOI18N
        xTextField6.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField6.setRequired(true);
        xFormPanel5.add(xTextField6);

        xTextField9.setCaption("Treasurer Office");
        xTextField9.setIndex(18);
        xTextField9.setName("entity.treasurer.office"); // NOI18N
        xTextField9.setPreferredSize(new java.awt.Dimension(0, 19));
        xTextField9.setRequired(true);
        xFormPanel5.add(xTextField9);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(xFormPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 396, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 116, Short.MAX_VALUE))
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
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField11;
    private com.rameses.rcp.control.XTextField xTextField12;
    private com.rameses.rcp.control.XTextField xTextField13;
    private com.rameses.rcp.control.XTextField xTextField14;
    private com.rameses.rcp.control.XTextField xTextField15;
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