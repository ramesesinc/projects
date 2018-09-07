/*
 * NewBPApplicationInitPage.java
 *
 * Created on October 3, 2013, 7:41 PM
 */

package com.rameses.gov.etracs.bpls.view;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.common.Action;
import com.rameses.rcp.common.PageListModel;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;



/**
 *
 * @author  Elmo
 */
@Template(FormPage.class)
@StyleSheet
public class EditBusinessNamePage extends javax.swing.JPanel {
    
    /** Creates new form NewBPApplicationInitPage */
    public EditBusinessNamePage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();

        xFormPanel2.setCaptionWidth(220);
        xFormPanel2.setPadding(new java.awt.Insets(0, 5, 5, 0));

        xTextField3.setCaption("Unique Business Name");
        xTextField3.setDepends(new String[] {"entity.businessname"});
        xTextField3.setIndex(2);
        xTextField3.setName("entity.business.businessname"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField3.setRequired(true);
        xFormPanel2.add(xTextField3);

        xTextField2.setCaption("Registered Tradename (DTI,SEC or CDA)");
        xTextField2.setIndex(4);
        xTextField2.setName("entity.business.tradename"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setRequired(true);
        xFormPanel2.add(xTextField2);

        xComboBox2.setAllowNull(false);
        xComboBox2.setCaption("Permit Type");
        xComboBox2.setEmptyText("- Select a Permit Type -");
        xComboBox2.setExpression("#{item.title}");
        xComboBox2.setItems("application.permitTypes");
        xComboBox2.setName("application.permitType"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(0, 22));
        xComboBox2.setRequired(true);
        xFormPanel2.add(xComboBox2);

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Office Type");
        xComboBox1.setDepends(new String[] {"application.permitType"});
        xComboBox1.setItems("application.officeTypes");
        xComboBox1.setName("entity.business.officetype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 20));
        xComboBox1.setRequired(true);
        xFormPanel2.add(xComboBox1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(279, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(177, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}
