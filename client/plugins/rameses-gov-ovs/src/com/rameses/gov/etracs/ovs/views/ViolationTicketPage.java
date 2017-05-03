/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.ovs.views;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author Rameses
 */
@Template(CrudFormPage.class)
@StyleSheet
public class ViolationTicketPage extends javax.swing.JPanel {

    /**
     * Creates new form ViolatorPage
     */
    public ViolationTicketPage() {
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

        xPanel1 = new com.rameses.rcp.control.XPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        entityLookup1 = new com.rameses.entity.components.EntityLookup();
        entityAddressLookup1 = new com.rameses.entity.components.EntityAddressLookup();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xTextField5 = new com.rameses.rcp.control.XTextField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xLookupField4 = new com.rameses.rcp.control.XLookupField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xPanel3 = new com.rameses.rcp.control.XPanel();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Violator Information");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionWidth(130);

        xTextField2.setCaption("Ticket No.");
        xTextField2.setName("entity.ticketno"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(200, 20));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        entityLookup1.setCaption("Violator");
        entityLookup1.setEntityType("individual");
        entityLookup1.setName("entity.violator"); // NOI18N
        entityLookup1.setPreferredSize(new java.awt.Dimension(0, 21));
        entityLookup1.setRequired(true);
        xFormPanel1.add(entityLookup1);

        entityAddressLookup1.setCaption("Violator Address");
        entityAddressLookup1.setDepends(new String[] {"entity.violator"});
        entityAddressLookup1.setName("entity.violator.address"); // NOI18N
        entityAddressLookup1.setPreferredSize(new java.awt.Dimension(0, 53));
        xFormPanel1.add(entityAddressLookup1);

        xLookupField2.setCaption("Apprehending Officer");
        xLookupField2.setExpression("#{entity.apprehensionofficer.name}");
        xLookupField2.setHandler("ovs_officer:lookup");
        xLookupField2.setName("entity.apprehensionofficer"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField2.setRequired(true);
        xFormPanel1.add(xLookupField2);

        xTextField5.setCaption("Place of Apprehension");
        xTextField5.setName("entity.apprehensionaddress"); // NOI18N
        xTextField5.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField5.setRequired(true);
        xFormPanel1.add(xTextField5);

        xDateField1.setCaption("Date of Apprehension");
        xDateField1.setName("entity.apprehensiondate"); // NOI18N
        xDateField1.setRequired(true);
        xFormPanel1.add(xDateField1);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Vehicle Information");
        xPanel2.setBorder(xTitledBorder2);
        xPanel2.setPreferredSize(new java.awt.Dimension(849, 150));

        xFormPanel3.setCaptionWidth(130);
        xFormPanel3.setDepends(new String[] {"entity.forvehicle"});
        xFormPanel3.setVisibleWhen("#{entity.forvehicle == 1}");

        xLookupField4.setCaption("Plate No.");
        xLookupField4.setExpression("#{entity.vehicle.plateno}");
        xLookupField4.setHandler("vehicle:lookup");
        xLookupField4.setName("entity.vehicle"); // NOI18N
        xLookupField4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xLookupField4);

        xTextField3.setEditable(false);
        xTextField3.setCaption("Vehicle Name");
        xTextField3.setDepends(new String[]{"entity.vehicle"});
        xTextField3.setName("entity.vehicle.name"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xTextField3);

        xTextField4.setEditable(false);
        xTextField4.setCaption("Owner Name");
        xTextField4.setDepends(new String[]{"entity.vehicle"});
        xTextField4.setName("entity.vehicle.owner.name"); // NOI18N
        xTextField4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xTextField4);

        xTextField1.setCaption("Driver's License");
        xTextField1.setEnabled(false);
        xTextField1.setName("entity.licenseno"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(300, 20));
        xFormPanel3.add(xTextField1);

        xCheckBox1.setCaption("");
        xCheckBox1.setCheckValue(1);
        xCheckBox1.setName("entity.forvehicle"); // NOI18N
        xCheckBox1.setText("for vehicle");
        xCheckBox1.setUncheckValue(0);

        javax.swing.GroupLayout xPanel2Layout = new javax.swing.GroupLayout(xPanel2);
        xPanel2.setLayout(xPanel2Layout);
        xPanel2Layout.setHorizontalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        xPanel2Layout.setVerticalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("Violations");
        xPanel3.setBorder(xTitledBorder3);

        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "violation"}
                , new Object[]{"caption", "Violation"}
                , new Object[]{"width", 200}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"expression", "#{item.violation.title}"}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler(null, "violation:lookup")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "violationcount"}
                , new Object[]{"caption", "No. of Offense"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "amount"}
                , new Object[]{"caption", "Amount"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "RIGHT"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "amtpaid"}
                , new Object[]{"caption", "Amount Paid"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "RIGHT"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            })
        });
        xDataTable1.setHandler("itemHandlers.entries");
        xDataTable1.setImmediate(true);
        xDataTable1.setName("selectedItem"); // NOI18N

        javax.swing.GroupLayout xPanel3Layout = new javax.swing.GroupLayout(xPanel3);
        xPanel3.setLayout(xPanel3Layout);
        xPanel3Layout.setHorizontalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xDataTable1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel3Layout.setVerticalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(xDataTable1, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.entity.components.EntityAddressLookup entityAddressLookup1;
    private com.rameses.entity.components.EntityLookup entityLookup1;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XLookupField xLookupField4;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel2;
    private com.rameses.rcp.control.XPanel xPanel3;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField5;
    // End of variables declaration//GEN-END:variables
}
