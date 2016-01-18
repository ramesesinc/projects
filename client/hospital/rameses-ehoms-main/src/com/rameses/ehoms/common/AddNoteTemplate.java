/*
 * LookupTemplate.java
 *
 * Created on April 4, 2014, 5:01 AM
 */

package com.rameses.ehoms.common;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Elmo
 */
@Template(OKCancelPage.class)
public class AddNoteTemplate extends javax.swing.JPanel {
    
    /** Creates new form LookupTemplate */
    public AddNoteTemplate() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xRadio1 = new com.rameses.rcp.control.XRadio();
        xRadio2 = new com.rameses.rcp.control.XRadio();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();

        setPreferredSize(new java.awt.Dimension(368, 258));

        xFormPanel1.setCellspacing(20);
        xFormPanel1.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xTextField1.setCaption("Keyword");
        xTextField1.setName("entity.keyword");
        xTextField1.setSpaceChar('_');
        xTextField1.setTextCase(com.rameses.rcp.constant.TextCase.NONE);
        xFormPanel1.add(xTextField1);

        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel1.setCaption("Section");
        xLabel1.setCaptionWidth(50);
        xLabel1.setExpression("#{entity.section}");
        xLabel1.setPreferredSize(new java.awt.Dimension(88, 20));
        xFormPanel1.add(xLabel1);

        xRadio1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xRadio1.setName("entity.scope");
        xRadio1.setOptionValue("private");
        xRadio1.setText("Private - for own use only");

        xRadio2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xRadio2.setName("entity.scope");
        xRadio2.setOptionValue("public");
        xRadio2.setText("Public - apply hospital wide");

        xTextArea1.setLineWrap(true);
        xTextArea1.setName("entity.text");
        jScrollPane1.setViewportView(xTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xRadio2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xRadio1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xRadio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xRadio2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XRadio xRadio1;
    private com.rameses.rcp.control.XRadio xRadio2;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    // End of variables declaration//GEN-END:variables
    
}
