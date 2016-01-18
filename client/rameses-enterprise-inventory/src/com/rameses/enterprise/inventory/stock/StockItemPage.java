/*
 * AFReceiptPage.java
 *
 * Created on August 10, 2013, 4:33 AM
 */

package com.rameses.enterprise.inventory.stock;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Elmo
 */
@Template(FormPage.class)
@StyleSheet
public class StockItemPage extends javax.swing.JPanel {
    
    /** Creates new form AFReceiptPage */
    public StockItemPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel4 = new javax.swing.JPanel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xSubFormPanel1 = new com.rameses.rcp.control.XSubFormPanel();

        setPreferredSize(new java.awt.Dimension(465, 348));
        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Stock Item Info");
        jPanel4.setBorder(xTitledBorder1);

        xFormPanel2.setCaptionWidth(120);
        xTextField1.setCaption("Item Code");
        xTextField1.setName("entity.code");
        xTextField1.setPreferredSize(new java.awt.Dimension(100, 19));
        xFormPanel2.add(xTextField1);

        xTextField2.setCaption("Item Title");
        xTextField2.setName("entity.title");
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 19));
        xFormPanel2.add(xTextField2);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 42));
        xTextArea1.setCaption("Description");
        xTextArea1.setName("entity.description");
        xTextArea1.setPreferredSize(new java.awt.Dimension(0, 40));
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel2.add(jScrollPane1);

        xTextField3.setCaption("Base Unit");
        xTextField3.setName("entity.baseunit");
        xTextField3.setPreferredSize(new java.awt.Dimension(100, 19));
        xFormPanel2.add(xTextField3);

        xComboBox1.setCaption("Item Type");
        xComboBox1.setExpression("#{item.caption}");
        xComboBox1.setItems("itemTypes");
        xComboBox1.setName("handler");
        xComboBox1.setPreferredSize(new java.awt.Dimension(150, 22));
        xFormPanel2.add(xComboBox1);

        xLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel1.setCaption("Item Class");
        xLabel1.setDepends(new String[] {"handler"});
        xLabel1.setExpression("#{entity.itemclass}");
        xLabel1.setPreferredSize(new java.awt.Dimension(150, 16));
        xFormPanel2.add(xLabel1);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 409, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(xFormPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                .addContainerGap())
        );

        xSubFormPanel1.setDepends(new String[] {"handler"});
        xSubFormPanel1.setDynamic(true);
        xSubFormPanel1.setHandler("handler");
        org.jdesktop.layout.GroupLayout xSubFormPanel1Layout = new org.jdesktop.layout.GroupLayout(xSubFormPanel1);
        xSubFormPanel1.setLayout(xSubFormPanel1Layout);
        xSubFormPanel1Layout.setHorizontalGroup(
            xSubFormPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 445, Short.MAX_VALUE)
        );
        xSubFormPanel1Layout.setVerticalGroup(
            xSubFormPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 122, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, xSubFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xSubFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XSubFormPanel xSubFormPanel1;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}
