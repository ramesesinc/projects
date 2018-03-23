/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.waterworks.views;

import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Elmo Nazareno
 */
@Template(CrudFormPage.class)
public class BatchBillingInitialPage extends javax.swing.JPanel {

    /**
     * Creates new form BatchBillingInitialPage
     */
    public BatchBillingInitialPage() {
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

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLabel8 = new com.rameses.rcp.control.XLabel();
        monthList1 = new com.rameses.enterprise.components.MonthList();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        monthList2 = new com.rameses.enterprise.components.MonthList();
        xLookupField3 = new com.rameses.rcp.control.XLookupField();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();

        xFormPanel1.setCaptionWidth(120);
        xFormPanel1.setDepends(new String[] {"entity.zone"});
        xFormPanel1.setVisibleWhen("#{ entity.zone != null }");

        xLabel1.setCaption("Sector");
        xLabel1.setDepends(new String[] {"entity.zone"});
        xLabel1.setExpression("#{ entity.zone.sector.code }");
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel1);

        xLabel8.setCaption("Start Year");
        xLabel8.setDepends(new String[] {"entity.zone"});
        xLabel8.setExpression("#{ entity.year }");
        xLabel8.setPreferredSize(new java.awt.Dimension(0, 20));
        xLabel8.setVisibleWhen("#{ entity.year !=null && entity.year > 0 }");
        xFormPanel1.add(xLabel8);

        monthList1.setCaption("Start Month");
        monthList1.setDepends(new String[] {"entity.zone"});
        monthList1.setDisableWhen("#{ 1 == 1 }");
        monthList1.setName("entity.month"); // NOI18N
        monthList1.setPreferredSize(new java.awt.Dimension(0, 20));
        monthList1.setVisibleWhen("#{ entity.month !=null && entity.month > 0  }");
        xFormPanel1.add(monthList1);

        xIntegerField1.setName("year"); // NOI18N
        xIntegerField1.setCaption("Start Year");
        xIntegerField1.setDepends(new String[] {"entity.zone"});
        xIntegerField1.setVisibleWhen("#{ entity.year == null || entity.year == 0 }");
        xFormPanel1.add(xIntegerField1);

        monthList2.setCaption("Start Month");
        monthList2.setDepends(new String[] {"entity.zone"});
        monthList2.setDisableWhen("");
        monthList2.setName("month"); // NOI18N
        monthList2.setPreferredSize(new java.awt.Dimension(0, 20));
        monthList2.setVisibleWhen("#{ entity.month ==null || entity.month == 0  }");
        xFormPanel1.add(monthList2);

        xLookupField3.setName("entity.reader"); // NOI18N
        xLookupField3.setCaption("Assign To Reader");
        xLookupField3.setCaptionWidth(120);
        xLookupField3.setExpression("#{ entity.reader.name }");
        xLookupField3.setHandler("waterworksreader:lookup");
        xLookupField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField3.setRequired(true);
        xFormPanel1.add(xLookupField3);

        xLookupField2.setName("entity.zone"); // NOI18N
        xLookupField2.setCaption("Zone");
        xLookupField2.setExpression("#{ entity.zone.code }");
        xLookupField2.setHandler("waterworks_zone:lookup");
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField2.setRequired(true);
        xFormPanel2.add(xLookupField2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(475, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.enterprise.components.MonthList monthList1;
    private com.rameses.enterprise.components.MonthList monthList2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel8;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XLookupField xLookupField3;
    // End of variables declaration//GEN-END:variables
}
