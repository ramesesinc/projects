/*
 * NewPatientPage.java
 *
 * Created on March 29, 2014, 11:15 AM
 */

package com.rameses.ehoms.patient;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Elmo
 */
@Template(FormPage.class)
@StyleSheet
public class ConfirmInfoPage extends javax.swing.JPanel {
    
    /** Creates new form NewPatientPage */
    public ConfirmInfoPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xPhoto1 = new com.rameses.rcp.control.XPhoto();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        jLabel1 = new javax.swing.JLabel();

        setName("selectedItem");
        setPreferredSize(new java.awt.Dimension(744, 446));
        xPhoto1.setDepends(new String[] {"selectedItem"});
        xPhoto1.setName("selectedPhoto");
        xPhoto1.setText("xPhoto1");

        xLabel1.setCaption("Lastname");
        xLabel1.setDepends(new String[] {"selectedItem"});
        xLabel1.setExpression("#{selectedItem.lastname}");
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel1.add(xLabel1);

        xLabel2.setCaption("First Name");
        xLabel2.setDepends(new String[] {"selectedItem"});
        xLabel2.setExpression("#{selectedItem.firstname}");
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel1.add(xLabel2);

        xLabel3.setCaption("Middle Name");
        xLabel3.setDepends(new String[] {"selectedItem"});
        xLabel3.setExpression("#{selectedItem.middlename}");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel1.add(xLabel3);

        xLabel4.setCaption("Birthdate");
        xLabel4.setDepends(new String[] {"selectedItem"});
        xLabel4.setExpression("#{selectedItem.birthdate}");
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel1.add(xLabel4);

        xLabel5.setCaption("Age");
        xLabel5.setDepends(new String[] {"selectedItem"});
        xLabel5.setExpression("#{selectedItem.age}");
        xLabel5.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel1.add(xLabel5);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Please verify if the information is correct");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xPhoto1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(217, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(23, 23, 23)
                .addComponent(xPhoto1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XPhoto xPhoto1;
    // End of variables declaration//GEN-END:variables
    
}
