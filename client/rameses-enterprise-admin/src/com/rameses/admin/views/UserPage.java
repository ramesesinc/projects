/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.admin.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author dell
 */
@Template(FormPage.class)
public class UserPage extends javax.swing.JPanel {

    /**
     * Creates new form UserPage
     */
    public UserPage() {
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

        xTabbedPane1 = new com.rameses.rcp.control.XTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField5 = new com.rameses.rcp.control.XTextField();
        xTextField6 = new com.rameses.rcp.control.XTextField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xListDomain = new com.rameses.rcp.control.XList();
        jPanel6 = new javax.swing.JPanel();
        xButton1 = new com.rameses.rcp.control.XButton();
        jPanel4 = new javax.swing.JPanel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xTableRole = new com.rameses.rcp.control.XDataTable();
        jPanel7 = new javax.swing.JPanel();
        xButton3 = new com.rameses.rcp.control.XButton();
        xButton2 = new com.rameses.rcp.control.XButton();
        jPanel1 = new javax.swing.JPanel();
        photoComponent1 = new com.rameses.enterprise.components.PhotoComponent();

        xTabbedPane1.setItems("sections");

        xFormPanel1.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel1.setCaptionWidth(100);

        xTextField1.setCaption("Username");
        xTextField1.setEnabled(false);
        xTextField1.setName("entity.username"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(200, 20));
        xTextField1.setRequired(true);
        xTextField1.setSpaceChar('_');
        xFormPanel1.add(xTextField1);

        xTextField3.setCaption("Last Name");
        xTextField3.setEnabled(false);
        xTextField3.setName("entity.lastname"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(200, 20));
        xTextField3.setRequired(true);
        xFormPanel1.add(xTextField3);

        xTextField2.setCaption("First Name");
        xTextField2.setEnabled(false);
        xTextField2.setName("entity.firstname"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(200, 20));
        xTextField2.setRequired(true);
        xFormPanel1.add(xTextField2);

        xTextField5.setCaption("Middle Name");
        xTextField5.setEnabled(false);
        xTextField5.setName("entity.middlename"); // NOI18N
        xTextField5.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xTextField5);

        xTextField6.setCaption("Job Title");
        xTextField6.setEnabled(false);
        xTextField6.setName("entity.jobtitle"); // NOI18N
        xTextField6.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xTextField6);

        xTextField4.setCaption("User Txn Code");
        xTextField4.setName("entity.txncode"); // NOI18N
        xTextField4.setEnabled(false);
        xTextField4.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xTextField4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(276, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(219, Short.MAX_VALUE))
        );

        xTabbedPane1.addTab("  General Information     ", jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        jPanel5.setLayout(new java.awt.BorderLayout());

        xLabel1.setDisplayedMnemonic('d');
        xLabel1.setLabelFor(xListDomain);
        xLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        xLabel1.setFontStyle("font-weight: bold;");
        xLabel1.setText("Domains");
        jPanel5.add(xLabel1, java.awt.BorderLayout.NORTH);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(150, 100));

        xListDomain.setItems("domainList");
        xListDomain.setName("selectedDomain"); // NOI18N
        xListDomain.setDynamic(true);
        jScrollPane1.setViewportView(xListDomain);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        jPanel6.setLayout(new com.rameses.rcp.control.layout.XLayout());

        xButton1.setMnemonic('a');
        xButton1.setName("addUsergroup"); // NOI18N
        xButton1.setText("Add");
        jPanel6.add(xButton1);

        jPanel5.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel5, java.awt.BorderLayout.WEST);

        jPanel4.setLayout(new java.awt.BorderLayout());

        xLabel2.setDisplayedMnemonic('r');
        xLabel2.setLabelFor(xTableRole);
        xLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        xLabel2.setFontStyle("font-weight: bold;");
        xLabel2.setText("Roles");
        jPanel4.add(xLabel2, java.awt.BorderLayout.NORTH);

        xTableRole.setDepends(new String[] {"selectedDomain"});
        xTableRole.setHandler("usergroupList");
        xTableRole.setName("selectedUsergroup"); // NOI18N
        xTableRole.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "role"}
                , new Object[]{"caption", "Role"}
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
                new Object[]{"name", "securitygroup.name"}
                , new Object[]{"caption", "Security Group"}
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
                new Object[]{"name", "org.name"}
                , new Object[]{"caption", "Org Name"}
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
            })
        });
        xTableRole.setDynamic(true);
        xTableRole.setReadonly(true);
        xTableRole.setReadonlyWhen("#{true}");
        xTableRole.setShowRowHeader(false);
        jPanel4.add(xTableRole, java.awt.BorderLayout.CENTER);

        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        jPanel7.setLayout(new com.rameses.rcp.control.layout.XLayout());

        xButton3.setDepends(new String[] {"selectedUsergroup"});
        xButton3.setDisableWhen("#{selectedUsergroup == null}");
        xButton3.setMnemonic('e');
        xButton3.setName("editUsergroup"); // NOI18N
        xButton3.setText("Edit");
        jPanel7.add(xButton3);

        xButton2.setDepends(new String[] {"selectedUsergroup"});
        xButton2.setDisableWhen("#{selectedUsergroup == null}");
        xButton2.setMnemonic('m');
        xButton2.setName("removeUsergroup"); // NOI18N
        xButton2.setText("Remove");
        jPanel7.add(xButton2);

        jPanel4.add(jPanel7, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        xTabbedPane1.addTab("  Roles and Permissions     ", jPanel3);

        jPanel1.setLayout(new com.rameses.rcp.control.layout.CenterLayout());

        photoComponent1.setName("entity.photo"); // NOI18N
        jPanel1.add(photoComponent1);

        xTabbedPane1.addTab("  Photo     ", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private com.rameses.enterprise.components.PhotoComponent photoComponent1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XList xListDomain;
    private com.rameses.rcp.control.XTabbedPane xTabbedPane1;
    private com.rameses.rcp.control.XDataTable xTableRole;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField5;
    private com.rameses.rcp.control.XTextField xTextField6;
    // End of variables declaration//GEN-END:variables
}
