package com.rameses.gov.etracs.rptis.master.views;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

@StyleSheet
@Template(CrudFormPage.class)
public class StructurePage extends javax.swing.JPanel {
    
    /** Creates new form AccountPage */
    public StructurePage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        xDataTable2 = new com.rameses.rcp.control.XDataTable();
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xNumberField1 = new com.rameses.rcp.control.XNumberField();
        xSeparator1 = new com.rameses.rcp.control.XSeparator();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();

        setPreferredSize(new java.awt.Dimension(748, 396));

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Applicable Materials");
        jPanel6.setBorder(xTitledBorder1);

        xDataTable2.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "material"}
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
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.NONE}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.LookupColumnHandler("#{item.material.code}", "material:lookup")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "material.name"}
                , new Object[]{"caption", "Material"}
                , new Object[]{"width", 200}
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
                new Object[]{"name", "display"}
                , new Object[]{"caption", "Show in FAAS?"}
                , new Object[]{"width", 150}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.CheckBoxColumnHandler(java.lang.Boolean.class, 1, 0)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "idx"}
                , new Object[]{"caption", "Order"}
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
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler(null, -1, -1)}
            })
        });
        xDataTable2.setDepends(new String[] {"selectedItem"});
        xDataTable2.setDynamic(true);
        xDataTable2.setHandler("listHandler");
        xDataTable2.setImmediate(true);
        xDataTable2.setName("selectedItem"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(xDataTable2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(xDataTable2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
        );

        formPanel1.setCaptionWidth(120);
        formPanel1.setShowCaption(false);

        xTextField1.setCaption("Code");
        xTextField1.setDepends(new String[] {"selectedItem"});
        xTextField1.setName("entity.code"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(80, 18));
        xTextField1.setRequired(true);
        formPanel1.add(xTextField1);

        xTextField2.setCaption("Structure");
        xTextField2.setDepends(new String[] {"selectedItem"});
        xTextField2.setName("entity.name"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 18));
        xTextField2.setRequired(true);
        formPanel1.add(xTextField2);

        xNumberField1.setCaption("Index No.");
        xNumberField1.setFieldType(Integer.class);
        xNumberField1.setName("entity.indexno"); // NOI18N
        xNumberField1.setPreferredSize(new java.awt.Dimension(50, 19));
        xNumberField1.setRequired(true);
        formPanel1.add(xNumberField1);

        xSeparator1.setPreferredSize(new java.awt.Dimension(0, 10));

        org.jdesktop.layout.GroupLayout xSeparator1Layout = new org.jdesktop.layout.GroupLayout(xSeparator1);
        xSeparator1.setLayout(xSeparator1Layout);
        xSeparator1Layout.setHorizontalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 409, Short.MAX_VALUE)
        );
        xSeparator1Layout.setVerticalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 10, Short.MAX_VALUE)
        );

        formPanel1.add(xSeparator1);

        xCheckBox1.setCaption("");
        xCheckBox1.setCaptionWidth(100);
        xCheckBox1.setName("entity.showinfaas"); // NOI18N
        xCheckBox1.setShowCaption(false);
        xCheckBox1.setText("Show in FAAS Report?");
        formPanel1.add(xCheckBox1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 419, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(170, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private javax.swing.JPanel jPanel6;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XDataTable xDataTable2;
    private com.rameses.rcp.control.XNumberField xNumberField1;
    private com.rameses.rcp.control.XSeparator xSeparator1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
    
}
