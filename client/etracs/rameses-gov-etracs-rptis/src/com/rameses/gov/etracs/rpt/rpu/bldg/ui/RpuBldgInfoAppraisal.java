/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rpt.rpu.bldg.ui;

import com.rameses.rcp.ui.annotations.StyleSheet;


@StyleSheet()
public class RpuBldgInfoAppraisal extends javax.swing.JPanel {

    /**
     * Creates new form BldgRpuInfoAppraisal
     */
    public RpuBldgInfoAppraisal() {
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

        jSplitPane2 = new javax.swing.JSplitPane();
        xDataTable7 = new com.rameses.rcp.control.XDataTable();
        tabAppraisal1 = new javax.swing.JTabbedPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        xDataTable11 = new com.rameses.rcp.control.XDataTable();
        xDataTable5 = new com.rameses.rcp.control.XDataTable();
        jPanel13 = new javax.swing.JPanel();
        xDataTable8 = new com.rameses.rcp.control.XDataTable();
        xDataTable12 = new com.rameses.rcp.control.XDataTable();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();

        setLayout(new java.awt.BorderLayout());

        jSplitPane2.setDividerLocation(80);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        xDataTable7.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "classification"}
                , new Object[]{"caption", "Classification*"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler("#{item.classification.name}", "propertyclassification:lookup")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "bldgtype"}
                , new Object[]{"caption", "Structural Type*"}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler("#{item.bldgtype.name}", "lookupBldgType")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "bldgkindbucc"}
                , new Object[]{"caption", "Building Kind*"}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler("#{item.bldgkindbucc.bldgkind.name}", "lookupBldgKindBucc")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "basefloorarea"}
                , new Object[]{"caption", "Base Floor Area*"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "totalfloorarea"}
                , new Object[]{"caption", "Floor Area"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "basevalue"}
                , new Object[]{"caption", "Base Value*"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            })
        });
        xDataTable7.setDynamic(true);
        xDataTable7.setHandler("structuralTypeListHandler");
        xDataTable7.setImmediate(true);
        xDataTable7.setName("selectedStructuralType"); // NOI18N
        xDataTable7.setPreferredSize(new java.awt.Dimension(200, 90));
        jSplitPane2.setLeftComponent(xDataTable7);

        jSplitPane3.setDividerLocation(80);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        xDataTable11.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "actualuse"}
                , new Object[]{"caption", "Actual Use*"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler("#{item.actualuse.name}", "lookupBldgActualUse")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "taxable"}
                , new Object[]{"caption", "Tax?"}
                , new Object[]{"width", 60}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.CheckBoxColumnHandler(java.lang.Boolean.class, true, false)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "area"}
                , new Object[]{"caption", "Area"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 0)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "basemarketvalue"}
                , new Object[]{"caption", "Base Market Value"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 0)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "depreciationvalue"}
                , new Object[]{"caption", "Depreciation"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 0)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "adjustment"}
                , new Object[]{"caption", "Adjustment"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 0)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "marketvalue"}
                , new Object[]{"caption", "Market Value"}
                , new Object[]{"width", 120}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 0)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "addlinfo"}
                , new Object[]{"caption", "Additional Info"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            })
        });
        xDataTable11.setDepends(new String[] {"selectedStructuralType"});
        xDataTable11.setDynamic(true);
        xDataTable11.setHandler("bldgUseListHandler");
        xDataTable11.setName("selectedBldgUse"); // NOI18N
        xDataTable11.setPreferredSize(new java.awt.Dimension(200, 75));
        jSplitPane3.setLeftComponent(xDataTable11);

        xDataTable5.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "floorno"}
                , new Object[]{"caption", "Floor No."}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", false}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler(null, -1, -1)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "area"}
                , new Object[]{"caption", "Area"}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "adjustment"}
                , new Object[]{"caption", "Adjustment"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            })
        });
        xDataTable5.setDepends(new String[] {"selectedBldgUse"});
        xDataTable5.setDynamic(true);
        xDataTable5.setHandler("floorHandler");
        xDataTable5.setName("selectedFloor"); // NOI18N
        jSplitPane3.setRightComponent(xDataTable5);

        tabAppraisal1.addTab("Actual Uses and Floor Information", jSplitPane3);

        xDataTable8.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", null}
                , new Object[]{"caption", "Code*"}
                , new Object[]{"width", 50}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler("#{item.additionalitem.code}", "lookupAdditionalItem")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "additionalitem.name"}
                , new Object[]{"caption", "Additional or Adjustment Type"}
                , new Object[]{"width", 200}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
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
                new Object[]{"name", "expr"}
                , new Object[]{"caption", "Computation"}
                , new Object[]{"width", 150}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
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
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 0)}
            })
        });
        xDataTable8.setDepends(new String[] {"selectedStructuralType", "floor"});
        xDataTable8.setDynamic(true);
        xDataTable8.setHandler("additionalItemHandler");
        xDataTable8.setImmediate(true);
        xDataTable8.setName("selectedAdditionalItem"); // NOI18N

        xDataTable12.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "param.caption"}
                , new Object[]{"caption", "Parameter"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
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
                new Object[]{"name", "value"}
                , new Object[]{"caption", "Value"}
                , new Object[]{"width", 90}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 90}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"visible", true}
                , new Object[]{"visibleWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 2)}
            })
        });
        xDataTable12.setDepends(new String[] {"selectedAdditionalItem"});
        xDataTable12.setDynamic(true);
        xDataTable12.setHandler("paramListHandler");
        xDataTable12.setName("selectedParam"); // NOI18N
        xDataTable12.setPreferredSize(new java.awt.Dimension(0, 50));

        xFormPanel2.setPadding(new java.awt.Insets(0, 0, 0, 0));

        xComboBox2.setAllowNull(false);
        xComboBox2.setCaption("Floor");
        xComboBox2.setDepends(new String[] {"selectedStructuralType", "selectedFloor"});
        xComboBox2.setDynamic(true);
        xComboBox2.setExpression("#{item.floorno} - (#{item.bldgtypecode}) - #{item.actualusename}");
        xComboBox2.setImmediate(true);
        xComboBox2.setItems("floors");
        xComboBox2.setName("floor"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(250, 22));
        xFormPanel2.add(xComboBox2);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(xDataTable8, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xDataTable12, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xDataTable8, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(xDataTable12, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabAppraisal1.addTab("Additional Items and Adjustments", jPanel13);

        jSplitPane2.setRightComponent(tabAppraisal1);

        add(jSplitPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel13;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane tabAppraisal1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XDataTable xDataTable11;
    private com.rameses.rcp.control.XDataTable xDataTable12;
    private com.rameses.rcp.control.XDataTable xDataTable5;
    private com.rameses.rcp.control.XDataTable xDataTable7;
    private com.rameses.rcp.control.XDataTable xDataTable8;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    // End of variables declaration//GEN-END:variables
}
