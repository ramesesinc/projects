/*
 * NewBPApplicationInitPage.java
 *
 * Created on October 3, 2013, 7:41 PM
 */

package com.rameses.gov.etracs.bpls.view;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;



/**
 *
 * @author  Elmo
 */
@Template(ApplicationInitialTemplate.class)
@StyleSheet
public class EditBusinessOwnerPage extends javax.swing.JPanel {
    
    /** Creates new form NewBPApplicationInitPage */
    public EditBusinessOwnerPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xSuggest1 = new com.rameses.rcp.control.XSuggest();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        xButton5 = new com.rameses.rcp.control.XButton();
        xButton6 = new com.rameses.rcp.control.XButton();
        xButton4 = new com.rameses.rcp.control.XButton();
        xLabel1 = new com.rameses.rcp.control.XLabel();

        xFormPanel1.setCaptionWidth(150);
        xFormPanel1.setPadding(new java.awt.Insets(0, 5, 5, 0));

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Org Type");
        xComboBox1.setExpression("#{item.value}");
        xComboBox1.setItemKey("key");
        xComboBox1.setItems("owner.orgTypes");
        xComboBox1.setName("entity.business.orgtype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(250, 22));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xSuggest1.setCaption("Owner");
        xSuggest1.setExpression("#{entity.business.owner.name}");
        xSuggest1.setHandler("owner.find");
        xSuggest1.setItemExpression("#{item.name}");
        xSuggest1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xSuggest1);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setEnabled(false);
        jScrollPane1.setName("entity.owner.address.text"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 50));

        xTextArea1.setEditable(false);
        xTextArea1.setCaption("Owner Address");
        xTextArea1.setName("entity.business.owner.address.text"); // NOI18N
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(23, 0, 0, 0));
        jPanel1.setLayout(new com.rameses.rcp.control.layout.YLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        xButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/search.png"))); // NOI18N
        xButton5.setDepends(new String[] {"entity.business.owner"});
        xButton5.setDisableWhen("#{entity?.business?.owner?.objid == null}");
        xButton5.setImmediate(true);
        xButton5.setMargin(new java.awt.Insets(2, 4, 2, 4));
        xButton5.setName("owner.show"); // NOI18N
        xButton5.setPreferredSize(new java.awt.Dimension(25, 22));
        jPanel2.add(xButton5, java.awt.BorderLayout.WEST);

        jPanel1.add(jPanel2);

        xButton6.setDepends(new String[] {"entity.business.owner"});
        xButton6.setDisableWhen("#{entity?.business?.owner?.objid == null}");
        xButton6.setImmediate(true);
        xButton6.setName("owner.lookupAddress"); // NOI18N
        xButton6.setText("Lookup Address");
        jPanel1.add(xButton6);

        xButton4.setDepends(new String[] {"entity.business.owner"});
        xButton4.setDisableWhen("#{entity?.business?.owner?.objid == null}");
        xButton4.setImmediate(true);
        xButton4.setName("owner.reload"); // NOI18N
        xButton4.setText("Reload Address");
        jPanel1.add(xButton4);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 89, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XEtchedBorder xEtchedBorder1 = new com.rameses.rcp.control.border.XEtchedBorder();
        xEtchedBorder1.setHideLeft(true);
        xEtchedBorder1.setHideRight(true);
        xEtchedBorder1.setHideTop(true);
        xLabel1.setBorder(xEtchedBorder1);
        xLabel1.setExpression("Owner Information");
        xLabel1.setFontStyle("font-weight:bold; font-size:12;");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(xLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XButton xButton4;
    private com.rameses.rcp.control.XButton xButton5;
    private com.rameses.rcp.control.XButton xButton6;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XSuggest xSuggest1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    // End of variables declaration//GEN-END:variables
    
}
