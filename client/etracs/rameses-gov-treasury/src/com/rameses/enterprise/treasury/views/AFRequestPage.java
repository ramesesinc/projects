/*
 * RISInitial.java
 *
 * Created on August 10, 2013, 10:29 PM
 */

package com.rameses.enterprise.treasury.views;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author  Elmo
 */
@StyleSheet
@Template(CrudFormPage.class)
public class AFRequestPage extends javax.swing.JPanel {
    
    /** Creates new form RISInitial */
    public AFRequestPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel7 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Requisition Info");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setCaptionWidth(100);

        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel2.setCaption("Requested By");
        xLabel2.setExpression("#{ entity.requester.name }");
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 20));
        xLabel2.setVisibleWhen("#{ mode == 'read' }");
        xFormPanel1.add(xLabel2);

        xLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel5.setCaption("Req Date");
        xLabel5.setExpression("#{ entity.dtfiled }");
        xLabel5.setPreferredSize(new java.awt.Dimension(0, 22));
        xLabel5.setVisibleWhen("#{ mode == 'read' }");
        xFormPanel1.add(xLabel5);

        xLookupField1.setCaption("Resp. Center");
        xLookupField1.setExpression("#{ entity.respcenter.name }");
        xLookupField1.setHandler("org:lookup");
        xLookupField1.setName("entity.respcenter"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField1.setVisibleWhen("#{ mode != 'read' }");
        xFormPanel1.add(xLookupField1);

        xLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel6.setCaption("Resp. Center");
        xLabel6.setExpression("#{ entity.respcenter.name }");
        xLabel6.setPreferredSize(new java.awt.Dimension(0, 22));
        xLabel6.setVisibleWhen("#{ mode == 'read' }");
        xFormPanel1.add(xLabel6);

        xLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel3.setCaption("Req No");
        xLabel3.setExpression("#{entity.reqno}");
        xLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel2.add(xLabel3);

        xLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel7.setCaption("Req Type");
        xLabel7.setExpression("#{ entity.reqtype }");
        xLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        xLabel7.setPreferredSize(new java.awt.Dimension(0, 22));
        xFormPanel2.add(xLabel7);

        xLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        xLabel4.setCaption("Status");
        xLabel4.setExpression("#{ entity.state }");
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 22));
        xLabel4.setVisibleWhen("#{ mode == 'read' }");
        xFormPanel2.add(xLabel4);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 344, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(35, 35, 35)
                .add(xFormPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(198, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(xFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(xFormPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "item"}
                , new Object[]{"caption", "Item Code"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 100}
                , new Object[]{"maxWidth", 100}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler("#{item.item.objid}", "afunit_collection:lookup")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "item.title"}
                , new Object[]{"caption", "Title"}
                , new Object[]{"width", 255}
                , new Object[]{"minWidth", 260}
                , new Object[]{"maxWidth", 200}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "unit"}
                , new Object[]{"caption", "Unit"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 100}
                , new Object[]{"maxWidth", 100}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "CENTER"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "qty"}
                , new Object[]{"caption", "Qty"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 100}
                , new Object[]{"maxWidth", 100}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"alignment", "CENTER"}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler(null, -1, -1)}
            })
        });
        xDataTable1.setHandler("itemHandlers.items");
        xDataTable1.setName("selectedItem"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(xDataTable1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(116, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(14, 14, 14)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(xDataTable1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 384, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLabel xLabel7;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    // End of variables declaration//GEN-END:variables
    
    
    
}
