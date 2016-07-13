/*
 * JuridicalEntityPage.java
 *
 * Created on August 14, 2013, 2:17 PM
 */

package com.rameses.entity.ui;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  wflores
 */
@Template(FormPage.class)
public class JuridicalEntityPage extends javax.swing.JPanel {
    
    public JuridicalEntityPage() {
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
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xTextField8 = new com.rameses.rcp.control.XTextField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xTextField7 = new com.rameses.rcp.control.XTextField();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField9 = new com.rameses.rcp.control.XTextField();
        xTextField10 = new com.rameses.rcp.control.XTextField();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        xTextArea2 = new com.rameses.rcp.control.XTextArea();
        xTextField11 = new com.rameses.rcp.control.XTextField();

        setPreferredSize(new java.awt.Dimension(733, 454));

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setPadding(new java.awt.Insets(30, 15, 15, 15));
        xTitledBorder1.setTitle("General Information");
        xFormPanel1.setBorder(xTitledBorder1);

        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel1.setCaption("Entity No");
        xLabel1.setCaptionWidth(120);
        xLabel1.setExpression("#{entity.entityno}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel1.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xLabel1);

        xTextField2.setCaption("Name");
        xTextField2.setCaptionWidth(120);
        xTextField2.setEnabled(false);
        xTextField2.setName("entity.name"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(400, 20));
        xTextField2.setRequired(true);
        xTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xTextField2ActionPerformed(evt);
            }
        });
        xFormPanel1.add(xTextField2);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setName("entity.address.text"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 50));
        jScrollPane1.setRequestFocusEnabled(false);

        xTextArea1.setCaption("Address");
        xTextArea1.setCaptionWidth(120);
        xTextArea1.setEnabled(false);
        xTextArea1.setName("entity.address.text"); // NOI18N
        xTextArea1.setPreferredSize(new java.awt.Dimension(45, 60));
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        xTextField8.setCaption("Organization Type");
        xTextField8.setCaptionWidth(120);
        xTextField8.setEnabled(false);
        xTextField8.setName("entity.orgtype"); // NOI18N
        xTextField8.setPreferredSize(new java.awt.Dimension(400, 20));
        xFormPanel1.add(xTextField8);

        xDateField1.setCaption("Date Registered");
        xDateField1.setCaptionWidth(120);
        xDateField1.setEnabled(false);
        xDateField1.setName("entity.dtregistered"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(110, 19));
        xFormPanel1.add(xDateField1);

        xTextField7.setCaption("Nature of Business");
        xTextField7.setCaptionWidth(120);
        xTextField7.setEnabled(false);
        xTextField7.setName("entity.nature"); // NOI18N
        xTextField7.setPreferredSize(new java.awt.Dimension(400, 20));
        xFormPanel1.add(xTextField7);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setPadding(new java.awt.Insets(30, 15, 15, 15));
        xTitledBorder2.setTitle("Contact Info");
        xFormPanel2.setBorder(xTitledBorder2);
        xFormPanel2.setCaptionWidth(120);

        xTextField1.setCaption("Mobile No");
        xTextField1.setCaptionWidth(120);
        xTextField1.setEnabled(false);
        xTextField1.setName("entity.mobileno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel2.add(xTextField1);

        xTextField9.setCaption("Phone No");
        xTextField9.setCaptionWidth(120);
        xTextField9.setEnabled(false);
        xTextField9.setName("entity.phoneno"); // NOI18N
        xTextField9.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel2.add(xTextField9);

        xTextField10.setCaption("Email");
        xTextField10.setCaptionWidth(120);
        xTextField10.setEnabled(false);
        xTextField10.setName("entity.email"); // NOI18N
        xTextField10.setPreferredSize(new java.awt.Dimension(200, 20));
        xTextField10.setTextCase(com.rameses.rcp.constant.TextCase.LOWER);
        xFormPanel2.add(xTextField10);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setPadding(new java.awt.Insets(30, 15, 15, 15));
        xTitledBorder3.setTitle("Administrator");
        xFormPanel3.setBorder(xTitledBorder3);
        xFormPanel3.setCaptionWidth(120);

        xTextField3.setCaption("Name");
        xTextField3.setCaptionWidth(120);
        xTextField3.setEnabled(false);
        xTextField3.setName("entity.administrator.name"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xTextField3);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane2.setName("entity.administrator.address"); // NOI18N
        jScrollPane2.setPreferredSize(new java.awt.Dimension(0, 50));

        xTextArea2.setCaption("Address");
        xTextArea2.setEnabled(false);
        xTextArea2.setName("entity.administrator.address"); // NOI18N
        jScrollPane2.setViewportView(xTextArea2);

        xFormPanel3.add(jScrollPane2);

        xTextField11.setCaption("Position");
        xTextField11.setCaptionWidth(120);
        xTextField11.setEnabled(false);
        xTextField11.setName("entity.administrator.position"); // NOI18N
        xTextField11.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xTextField11);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, xFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, xFormPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                    .add(xFormPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 198, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xFormPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xFormPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xTextField2ActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_xTextField2ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextArea xTextArea2;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField10;
    private com.rameses.rcp.control.XTextField xTextField11;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField7;
    private com.rameses.rcp.control.XTextField xTextField8;
    private com.rameses.rcp.control.XTextField xTextField9;
    // End of variables declaration//GEN-END:variables
    
}
