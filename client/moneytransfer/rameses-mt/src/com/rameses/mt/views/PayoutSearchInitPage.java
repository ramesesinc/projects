/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.mt.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author wflores 
 */
@Template(FormPage.class)
public class PayoutSearchInitPage extends javax.swing.JPanel {

    /**
     * Creates new form PayoutSearchPage
     */
    public PayoutSearchInitPage() {
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

        xTextField1 = new com.rameses.rcp.control.XTextField();
        xRadio1 = new com.rameses.rcp.control.XRadio();
        xRadio2 = new com.rameses.rcp.control.XRadio();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();

        xTextField1.setCaption("Transaction Number");
        xTextField1.setDepends(new String[] {"query.searchtype"});
        xTextField1.setDisableWhen("#{ query.searchtype != 'BY_TXN_NUMBER'}");
        xTextField1.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        xTextField1.setFontStyle("font-size:16");
        xTextField1.setName("query.txnno"); // NOI18N
        xTextField1.setRequired(true);

        xRadio1.setCaption("Search Type");
        xRadio1.setFontStyle("font-size:13;font-weight:bold;");
        xRadio1.setForeground(new java.awt.Color(10, 36, 106));
        xRadio1.setName("query.searchtype"); // NOI18N
        xRadio1.setOptionValue("BY_TXN_NUMBER");
        xRadio1.setText("Search By Transaction Number");
        xRadio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xRadio1ActionPerformed(evt);
            }
        });

        xRadio2.setCaption("Search Type");
        xRadio2.setFontStyle("font-size:13;font-weight:bold;");
        xRadio2.setForeground(new java.awt.Color(10, 36, 106));
        xRadio2.setName("query.searchtype"); // NOI18N
        xRadio2.setOptionValue("BY_RECEIVER");
        xRadio2.setText("Search By Receiver's Name");
        xRadio2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xRadio2ActionPerformed(evt);
            }
        });

        xTextField2.setCaption("Last Name");
        xTextField2.setCaptionFontStyle("font-size:14;");
        xTextField2.setCaptionWidth(90);
        xTextField2.setDepends(new String[] {"query.searchtype"});
        xTextField2.setDisableWhen("#{ query.searchtype != 'BY_RECEIVER'}");
        xTextField2.setFontStyle("font-size:16;");
        xTextField2.setName("query.receiverlastname"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 25));
        xTextField2.setStretchWidth(100);
        xFormPanel1.add(xTextField2);

        xTextField3.setCaption("First Name");
        xTextField3.setCaptionFontStyle("font-size:14;");
        xTextField3.setCaptionWidth(90);
        xTextField3.setDepends(new String[] {"query.searchtype"});
        xTextField3.setDisableWhen("#{ query.searchtype != 'BY_RECEIVER'}");
        xTextField3.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        xTextField3.setFontStyle("font-size:16;");
        xTextField3.setName("query.receiverfirstname"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 25));
        xTextField3.setStretchWidth(100);
        xFormPanel1.add(xTextField3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(xRadio2, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                        .addComponent(xRadio1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(xTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(427, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(xRadio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(xRadio2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(262, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xRadio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xRadio1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xRadio1ActionPerformed

    private void xRadio2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xRadio2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_xRadio2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XRadio xRadio1;
    private com.rameses.rcp.control.XRadio xRadio2;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
}
