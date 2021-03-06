/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.police.views;

import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.seti2.views.CrudFormPage;

/**
 *
 * @author dell
 */
@StyleSheet
@Template(CrudFormPage.class)
public class PoliceClearanceApplicationPage extends javax.swing.JPanel {

    /**
     * Creates new form PoliceClearancePage
     */
    public PoliceClearanceApplicationPage() {
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
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xLabel11 = new com.rameses.rcp.control.XLabel();
        xFormPanel7 = new com.rameses.rcp.control.XFormPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xLabel12 = new com.rameses.rcp.control.XLabel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xLabel10 = new com.rameses.rcp.control.XLabel();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xFormPanel2 = new com.rameses.rcp.control.XFormPanel();
        xLabel7 = new com.rameses.rcp.control.XLabel();
        xLabel8 = new com.rameses.rcp.control.XLabel();
        xLabel9 = new com.rameses.rcp.control.XLabel();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xLabel13 = new com.rameses.rcp.control.XLabel();
        xLabel14 = new com.rameses.rcp.control.XLabel();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        xTextArea1 = new com.rameses.rcp.control.XTextArea();
        xPanel2 = new com.rameses.rcp.control.XPanel();
        xPhoto1 = new com.rameses.rcp.control.XPhoto();
        xPanel3 = new com.rameses.rcp.control.XPanel();
        xPhoto2 = new com.rameses.rcp.control.XPhoto();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        xPanel4 = new com.rameses.rcp.control.XPanel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xLabel15 = new com.rameses.rcp.control.XLabel();
        xLabel16 = new com.rameses.rcp.control.XLabel();
        xLabel17 = new com.rameses.rcp.control.XLabel();
        xPanel5 = new com.rameses.rcp.control.XPanel();
        xFormPanel8 = new com.rameses.rcp.control.XFormPanel();
        xLabel18 = new com.rameses.rcp.control.XLabel();
        xLabel19 = new com.rameses.rcp.control.XLabel();
        xLabel20 = new com.rameses.rcp.control.XLabel();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("  General Information   ");
        xPanel1.setBorder(xTitledBorder1);

        xFormPanel6.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel6.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel6.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel6.setShowCaption(false);
        xFormPanel6.setStretchWidth(100);

        xTextField2.setCaption("App No");
        xTextField2.setDisableWhen("#{entity.appno == null}");
        xTextField2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        xTextField2.setName("entity.appno"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField2.setReadonly(true);
        xTextField2.setStretchWidth(100);
        xFormPanel6.add(xTextField2);

        xLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel11.setCaption("App Date");
        xLabel11.setCaptionWidth(70);
        xLabel11.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xLabel11.setExpression("#{entity.appdate}");
        xLabel11.setPreferredSize(new java.awt.Dimension(0, 20));
        xLabel11.setStretchWidth(100);
        xFormPanel6.add(xLabel11);

        xFormPanel1.add(xFormPanel6);

        xFormPanel7.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel7.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel7.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel7.setShowCaption(false);
        xFormPanel7.setStretchWidth(100);

        xLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel3.setCaption("App Type");
        xLabel3.setExpression("#{entity.apptype.title}");
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel7.add(xLabel3);

        xLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel12.setCaption("Status");
        xLabel12.setCaptionWidth(70);
        xLabel12.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xLabel12.setExpression("#{entity.state}");
        xLabel12.setPreferredSize(new java.awt.Dimension(0, 20));
        xLabel12.setStretchWidth(100);
        xFormPanel7.add(xLabel12);

        xFormPanel1.add(xFormPanel7);

        xLabel1.setShowCaption(false);
        xLabel1.setText(" ");
        xFormPanel1.add(xLabel1);

        xFormPanel3.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel3.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel3.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel3.setShowCaption(false);
        xFormPanel3.setStretchWidth(100);

        xLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel10.setCaption("Applicant ");
        xLabel10.setExpression("#{entity.applicant.name}");
        xLabel10.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xLabel10);

        xFormPanel1.add(xFormPanel3);

        xLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        xLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel4.setCaption("Address");
        xLabel4.setExpression("#{entity.applicant.address.text}");
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 40));
        xFormPanel1.add(xLabel4);

        xFormPanel2.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel2.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel2.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel2.setShowCaption(false);
        xFormPanel2.setStretchWidth(100);

        xLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel7.setCaption("Gender");
        xLabel7.setExpression("#{entity.applicant.gender}");
        xLabel7.setPreferredSize(new java.awt.Dimension(80, 20));
        xLabel7.setStretchWidth(100);
        xFormPanel2.add(xLabel7);

        xLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel8.setCaption("Birth Date");
        xLabel8.setCaptionWidth(70);
        xLabel8.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xLabel8.setExpression("#{entity.applicant.birthdate}");
        xLabel8.setPreferredSize(new java.awt.Dimension(100, 20));
        xLabel8.setStretchWidth(100);
        xFormPanel2.add(xLabel8);

        xLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        xLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel9.setCaption("Age");
        xLabel9.setCaptionWidth(40);
        xLabel9.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xLabel9.setExpression("#{entity.applicant.age}");
        xLabel9.setPreferredSize(new java.awt.Dimension(50, 20));
        xFormPanel2.add(xLabel9);

        xFormPanel1.add(xFormPanel2);

        xFormPanel5.setCaptionVAlignment(com.rameses.rcp.constant.UIConstants.CENTER);
        xFormPanel5.setOrientation(com.rameses.rcp.constant.UIConstants.HORIZONTAL);
        xFormPanel5.setPadding(new java.awt.Insets(0, 0, 0, 0));
        xFormPanel5.setShowCaption(false);
        xFormPanel5.setStretchWidth(100);

        xLabel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel13.setCaption("Civil Status");
        xLabel13.setExpression("#{entity.applicant.civilstatus}");
        xLabel13.setPreferredSize(new java.awt.Dimension(100, 20));
        xLabel13.setStretchWidth(100);
        xFormPanel5.add(xLabel13);

        xLabel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel14.setCaption("Citizenship");
        xLabel14.setCaptionWidth(70);
        xLabel14.setCellPadding(new java.awt.Insets(0, 10, 0, 0));
        xLabel14.setExpression("#{entity.applicant.citizenship}");
        xLabel14.setPreferredSize(new java.awt.Dimension(100, 20));
        xLabel14.setStretchWidth(100);
        xFormPanel5.add(xLabel14);

        xFormPanel1.add(xFormPanel5);

        xLabel2.setShowCaption(false);
        xLabel2.setText(" ");
        xFormPanel1.add(xLabel2);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(402, 63));

        xTextArea1.setEditable(false);
        xTextArea1.setLineWrap(true);
        xTextArea1.setWrapStyleWord(true);
        xTextArea1.setCaption("Purpose");
        xTextArea1.setExitOnTabKey(true);
        xTextArea1.setName("entity.purpose"); // NOI18N
        xTextArea1.setOpaque(false);
        jScrollPane1.setViewportView(xTextArea1);

        xFormPanel1.add(jScrollPane1);

        javax.swing.GroupLayout xPanel1Layout = new javax.swing.GroupLayout(xPanel1);
        xPanel1.setLayout(xPanel1Layout);
        xPanel1Layout.setHorizontalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel1Layout.setVerticalGroup(
            xPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("  Photo   ");
        xPanel2.setBorder(xTitledBorder2);

        xPhoto1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xPhoto1.setName("photo"); // NOI18N
        xPhoto1.setNoImageIcon("images/blank.png");
        xPhoto1.setText("xPhoto1");

        javax.swing.GroupLayout xPanel2Layout = new javax.swing.GroupLayout(xPanel2);
        xPanel2.setLayout(xPanel2Layout);
        xPanel2Layout.setHorizontalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPhoto1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        xPanel2Layout.setVerticalGroup(
            xPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPhoto1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("  Finger Print   ");
        xPanel3.setBorder(xTitledBorder3);

        xPhoto2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xPhoto2.setName("fingerprintimage"); // NOI18N
        xPhoto2.setNoImageIcon("images/blank.png");
        xPhoto2.setText("xPhoto1");

        javax.swing.GroupLayout xPanel3Layout = new javax.swing.GroupLayout(xPanel3);
        xPanel3.setLayout(xPanel3Layout);
        xPanel3Layout.setHorizontalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPhoto2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        xPanel3Layout.setVerticalGroup(
            xPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xPhoto2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        xFormPanel4.setCaptionWidth(100);
        xFormPanel4.setPadding(new java.awt.Insets(5, 5, 5, 5));

        xLabel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel15.setCaption("CTC Number");
        xLabel15.setExpression("#{entity.ctc.refno}");
        xLabel15.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel15);

        xLabel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel16.setCaption("Issued At");
        xLabel16.setExpression("#{entity.ctc.issuedat}");
        xLabel16.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel16);

        xLabel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel17.setCaption("Issued On");
        xLabel17.setExpression("#{entity.ctc.issuedon}");
        xLabel17.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel17);

        javax.swing.GroupLayout xPanel4Layout = new javax.swing.GroupLayout(xPanel4);
        xPanel4.setLayout(xPanel4Layout);
        xPanel4Layout.setHorizontalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel4Layout.setVerticalGroup(
            xPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("  CTC Information   ", xPanel4);

        xFormPanel8.setCaptionWidth(100);
        xFormPanel8.setPadding(new java.awt.Insets(5, 5, 5, 5));

        xLabel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel18.setCaption("Receipt No");
        xLabel18.setExpression("#{entity.payment.refno}");
        xLabel18.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel8.add(xLabel18);

        xLabel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel19.setCaption("Receipt Date");
        xLabel19.setExpression("#{entity.payment.refdate}");
        xLabel19.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel8.add(xLabel19);

        xLabel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180)));
        xLabel20.setCaption("Amount");
        xLabel20.setExpression("#{entity.payment.amount}");
        xLabel20.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel8.add(xLabel20);

        javax.swing.GroupLayout xPanel5Layout = new javax.swing.GroupLayout(xPanel5);
        xPanel5.setLayout(xPanel5Layout);
        xPanel5Layout.setHorizontalGroup(
            xPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                .addContainerGap())
        );
        xPanel5Layout.setVerticalGroup(
            xPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(xPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("  Payment Information   ", xPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(70, 70, 70))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(xPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel2;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XFormPanel xFormPanel7;
    private com.rameses.rcp.control.XFormPanel xFormPanel8;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel10;
    private com.rameses.rcp.control.XLabel xLabel11;
    private com.rameses.rcp.control.XLabel xLabel12;
    private com.rameses.rcp.control.XLabel xLabel13;
    private com.rameses.rcp.control.XLabel xLabel14;
    private com.rameses.rcp.control.XLabel xLabel15;
    private com.rameses.rcp.control.XLabel xLabel16;
    private com.rameses.rcp.control.XLabel xLabel17;
    private com.rameses.rcp.control.XLabel xLabel18;
    private com.rameses.rcp.control.XLabel xLabel19;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLabel xLabel20;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel7;
    private com.rameses.rcp.control.XLabel xLabel8;
    private com.rameses.rcp.control.XLabel xLabel9;
    private com.rameses.rcp.control.XPanel xPanel1;
    private com.rameses.rcp.control.XPanel xPanel2;
    private com.rameses.rcp.control.XPanel xPanel3;
    private com.rameses.rcp.control.XPanel xPanel4;
    private com.rameses.rcp.control.XPanel xPanel5;
    private com.rameses.rcp.control.XPhoto xPhoto1;
    private com.rameses.rcp.control.XPhoto xPhoto2;
    private com.rameses.rcp.control.XTextArea xTextArea1;
    private com.rameses.rcp.control.XTextField xTextField2;
    // End of variables declaration//GEN-END:variables
}
