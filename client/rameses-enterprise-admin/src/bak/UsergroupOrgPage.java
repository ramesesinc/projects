/*
 * UsergroupOrgPage.java
 *
 * Created on August 16, 2013, 11:30 AM
 */

package bak;

/**
 *
 * @author  wflores
 */
public class UsergroupOrgPage extends javax.swing.JPanel {
    
    public UsergroupOrgPage() {
        initComponents(); 
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xButton1 = new com.rameses.rcp.control.XButton();
        xTextField4 = new com.rameses.rcp.control.XTextField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("General Information");
        jPanel1.setBorder(xTitledBorder1);

        xTextField1.setCaption("User group");
        xTextField1.setCaptionWidth(110);
        xTextField1.setEnabled(false);
        xTextField1.setName("entity.title");
        xTextField1.setPreferredSize(new java.awt.Dimension(300, 20));
        xTextField1.setReadonly(true);
        xFormPanel1.add(xTextField1);

        xTextField2.setCaption("Security group");
        xTextField2.setCaptionWidth(110);
        xTextField2.setEnabled(false);
        xTextField2.setName("entity.user.fullname");
        xTextField2.setPreferredSize(new java.awt.Dimension(300, 20));
        xTextField2.setReadonly(true);
        xFormPanel1.add(xTextField2);

        xLookupField1.setCaption("Org. Name");
        xLookupField1.setCaptionWidth(110);
        xLookupField1.setExpression("#{entity.org.name}");
        xLookupField1.setHandler("lookupOrg");
        xLookupField1.setName("entity.org");
        xLookupField1.setPreferredSize(new java.awt.Dimension(300, 20));
        xFormPanel1.add(xLookupField1);

        xTextField3.setCaption("Org. Class");
        xTextField3.setCaptionWidth(110);
        xTextField3.setDepends(new String[] {"entity.org"});
        xTextField3.setEnabled(false);
        xTextField3.setName("entity.org.orgclass");
        xTextField3.setPreferredSize(new java.awt.Dimension(300, 20));
        xTextField3.setReadonly(true);
        xFormPanel1.add(xTextField3);

        xFormPanel2.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel2.setPadding(new java.awt.Insets(0, 0, 5, 5));
        xFormPanel2.setPreferredSize(new java.awt.Dimension(500, 22));
        xFormPanel2.setShowCaption(false);
        xComboBox2.setCaption("Security Group");
        xComboBox2.setCaptionWidth(110);
        xComboBox2.setExpression("#{item.name}");
        xComboBox2.setItemKey("objid");
        xComboBox2.setItems("securitygroups");
        xComboBox2.setName("entity.securitygroupid");
        xComboBox2.setPreferredSize(new java.awt.Dimension(200, 22));
        xComboBox2.setRequired(true);
        xFormPanel2.add(xComboBox2);

        xButton1.setFocusable(false);
        xButton1.setImmediate(true);
        xButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        xButton1.setName("showPermissions");
        xButton1.setShowCaption(false);
        xButton1.setText("...");
        xButton1.setVisible(false);
        xFormPanel2.add(xButton1);

        xFormPanel1.add(xFormPanel2);

        xTextField4.setCaption("User Txn Code");
        xTextField4.setCaptionWidth(110);
        xTextField4.setName("entity.usertxncode");
        xTextField4.setPreferredSize(new java.awt.Dimension(200, 20));
        xFormPanel1.add(xTextField4);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 515, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(xFormPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    // End of variables declaration//GEN-END:variables
    
}
