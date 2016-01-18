/*
 * BusinessRequirementPage.java
 *
 * Created on October 28, 2013, 9:40 PM
 */

package com.rameses.gov.etracs.bpls;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;


/**
 *
 * @author  Elmo
 */
@Template(FormPage.class)
@StyleSheet
public class BusinessRequirementTypePage extends javax.swing.JPanel {
    
    /** Creates new form BusinessRequirementPage */
    public BusinessRequirementTypePage() {
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
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xTextField4 = new com.rameses.rcp.control.XTextField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("General Info");
        xFormPanel1.setBorder(xTitledBorder1);
        xFormPanel1.setCaption("General Info");

        xTextField1.setCaption("Code");
        xTextField1.setName("entity.code"); // NOI18N
        xTextField1.setRequired(true);
        xTextField1.setSpaceChar('_');
        xFormPanel1.add(xTextField1);

        xTextField2.setCaption("Title");
        xTextField2.setName("entity.title"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        xTextField3.setCaption("Agency");
        xTextField3.setName("entity.agency"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xTextField3);

        xComboBox1.setCaption("Handler");
        xComboBox1.setItems("handlers");
        xComboBox1.setName("entity.type"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 22));
        xFormPanel1.add(xComboBox1);

        xIntegerField1.setCaption("Sort Index");
        xIntegerField1.setName("entity.sortindex"); // NOI18N
        xIntegerField1.setPreferredSize(new java.awt.Dimension(50, 20));
        xIntegerField1.setRequired(true);
        xFormPanel1.add(xIntegerField1);

        xTextField4.setCaption("Verifier");
        xTextField4.setName("entity.verifier"); // NOI18N
        xTextField4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xTextField4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    // End of variables declaration//GEN-END:variables
    
}
