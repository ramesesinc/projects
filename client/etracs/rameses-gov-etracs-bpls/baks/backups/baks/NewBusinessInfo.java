/*
 * NewBusinessInfo.java
 *
 * Created on March 13, 2014, 5:07 AM
 */

package backups.baks;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Elmo
 */
@Template(FormPage.class)
public class NewBusinessInfo extends javax.swing.JPanel {
    
    /** Creates new form NewBusinessInfo */
    public NewBusinessInfo() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xButton1 = new com.rameses.rcp.control.XButton();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();
        jPanel4 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xSuggest2 = new com.rameses.rcp.control.XSuggest();
        jScrollPane3 = new javax.swing.JScrollPane();
        xTextArea3 = new com.rameses.rcp.control.XTextArea();
        jPanel1 = new javax.swing.JPanel();
        xButton3 = new com.rameses.rcp.control.XButton();
        xButton8 = new com.rameses.rcp.control.XButton();
        jPanel5 = new javax.swing.JPanel();
        xButton2 = new com.rameses.rcp.control.XButton();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        xTextArea4 = new com.rameses.rcp.control.XTextArea();
        xIntegerField1 = new com.rameses.rcp.control.XIntegerField();
        xIntegerField2 = new com.rameses.rcp.control.XIntegerField();
        xButton9 = new com.rameses.rcp.control.XButton();

        xLookupField2.setCaption("Add New Line of Business");
        xLookupField2.setCaptionWidth(150);
        xLookupField2.setHandler("addLob");
        xLookupField2.setIndex(-1);
        xLookupField2.setName("lob");
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel2.add(xLookupField2);

        xButton1.setImmediate(true);
        xButton1.setMargin(new java.awt.Insets(2, 5, 2, 5));
        xButton1.setName("removeLob");
        xButton1.setText("Remove");

        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "name"}
                , new Object[]{"caption", "Line of Business"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "assessmenttype"}
                , new Object[]{"caption", "Assessment Type"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.ComboBoxColumnHandler("lobAssessmentTypes", null, null)}
            })
        });
        xDataTable1.setHandler("lobModel");
        xDataTable1.setName("selectedLob");
        xDataTable1.setPreferredSize(new java.awt.Dimension(0, 80));

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Owners Information");
        jPanel4.setBorder(xTitledBorder1);

        xFormPanel1.setCaptionWidth(150);
        xFormPanel1.setPadding(new java.awt.Insets(0, 5, 5, 0));
        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Org Type");
        xComboBox1.setExpression("#{item.value}");
        xComboBox1.setItemKey("key");
        xComboBox1.setItems("orgTypes");
        xComboBox1.setName("entity.orgtype");
        xComboBox1.setPreferredSize(new java.awt.Dimension(250, 22));
        xComboBox1.setRequired(true);
        xFormPanel1.add(xComboBox1);

        xSuggest2.setCaption("Owner");
        xSuggest2.setExpression("#{entity.owner.name}");
        xSuggest2.setHandler("findOwner");
        xSuggest2.setItemExpression("#{item.name}");
        xSuggest2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xSuggest2);

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane3.setEnabled(false);
        jScrollPane3.setName("entity.owner.address.text");
        jScrollPane3.setPreferredSize(new java.awt.Dimension(0, 63));
        xTextArea3.setCaption("Owner Address");
        xTextArea3.setEnabled(false);
        xTextArea3.setName("entity.owner.address.text");
        jScrollPane3.setViewportView(xTextArea3);

        xFormPanel1.add(jScrollPane3);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 22));
        xButton3.setImmediate(true);
        xButton3.setMargin(new java.awt.Insets(2, 5, 2, 4));
        xButton3.setName("addOwner");
        xButton3.setPreferredSize(new java.awt.Dimension(25, 22));
        xButton3.setText("+");
        jPanel1.add(xButton3);

        xButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/search.png")));
        xButton8.setImmediate(true);
        xButton8.setMargin(new java.awt.Insets(2, 4, 2, 4));
        xButton8.setName("showOwner");
        xButton8.setPreferredSize(new java.awt.Dimension(25, 22));
        jPanel1.add(xButton8);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Business Information");
        jPanel5.setBorder(xTitledBorder2);
        xButton2.setMnemonic('c');
        xButton2.setImmediate(true);
        xButton2.setIndex(3);
        xButton2.setName("copyBusinessName");
        xButton2.setText("Copy");

        xFormPanel5.setCaptionWidth(150);
        xFormPanel5.setPadding(new java.awt.Insets(0, 5, 5, 0));
        xTextField3.setCaption("Business Name");
        xTextField3.setDepends(new String[] {"entity.businessname"});
        xTextField3.setIndex(2);
        xTextField3.setName("entity.businessname");
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField3.setRequired(true);
        xFormPanel5.add(xTextField3);

        xTextField2.setCaption("Trade Name");
        xTextField2.setIndex(4);
        xTextField2.setName("entity.tradename");
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setRequired(true);
        xFormPanel5.add(xTextField2);

        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane4.setEnabled(false);
        jScrollPane4.setName("entity.owner.address.text");
        jScrollPane4.setPreferredSize(new java.awt.Dimension(0, 63));
        xTextArea4.setCaption("Business Address");
        xTextArea4.setEnabled(false);
        xTextArea4.setName("entity.businessaddress.text");
        jScrollPane4.setViewportView(xTextArea4);

        xFormPanel5.add(jScrollPane4);

        xIntegerField1.setCaption("Year started");
        xIntegerField1.setName("entity.yearstarted");
        xFormPanel5.add(xIntegerField1);

        xIntegerField2.setCaption("Last Year Renewed");
        xIntegerField2.setName("entity.activeyear");
        xFormPanel5.add(xIntegerField2);

        xButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.png")));
        xButton9.setImmediate(true);
        xButton9.setName("editBusinessAddress");
        xButton9.setPreferredSize(new java.awt.Dimension(25, 22));
        xButton9.setShowCaption(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(xButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(xButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(xDataTable1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(xFormPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(656, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xFormPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xDataTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(116, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XButton xButton8;
    private com.rameses.rcp.control.XButton xButton9;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XIntegerField xIntegerField1;
    private com.rameses.rcp.control.XIntegerField xIntegerField2;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XSuggest xSuggest2;
    private com.rameses.rcp.control.XTextArea xTextArea3;
    private com.rameses.rcp.control.XTextArea xTextArea4;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}
