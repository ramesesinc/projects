/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.obo.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author dell
 */
@Template(FormPage.class)
public class ApplicationSpecifyProfessionalPage extends javax.swing.JPanel {

    /**
     * Creates new form NewApplicationSpecifyProfessional
     */
    public ApplicationSpecifyProfessionalPage() {
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

        jPanel7 = new javax.swing.JPanel();
        xFormPanel6 = new com.rameses.rcp.control.XFormPanel();
        xLookupField5 = new com.rameses.rcp.control.XLookupField();
        xLabel7 = new com.rameses.rcp.control.XLabel();
        xLabel8 = new com.rameses.rcp.control.XLabel();
        xLabel9 = new com.rameses.rcp.control.XLabel();
        cTCIndividualEntryPage4 = new com.rameses.etracs.components.CTCIndividualEntryPage();
        jPanel6 = new javax.swing.JPanel();
        xFormPanel3 = new com.rameses.rcp.control.XFormPanel();
        xLookupField3 = new com.rameses.rcp.control.XLookupField();
        xLabel4 = new com.rameses.rcp.control.XLabel();
        xLabel5 = new com.rameses.rcp.control.XLabel();
        xLabel6 = new com.rameses.rcp.control.XLabel();
        cTCIndividualEntryPage3 = new com.rameses.etracs.components.CTCIndividualEntryPage();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Architect/Civil Engineer In-charge of plans and specification");
        jPanel7.setBorder(xTitledBorder1);
        jPanel7.setOpaque(false);

        xFormPanel6.setCaptionWidth(120);

        xLookupField5.setCaption("Name");
        xLookupField5.setExpression("#{entity.designer.name}");
        xLookupField5.setHandler("obo_profession:lookup");
        xLookupField5.setName("entity.designer"); // NOI18N
        xLookupField5.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel6.add(xLookupField5);

        xLabel7.setCaption("Profession");
        xLabel7.setDepends(new String[] {"entity.designer"});
        xLabel7.setExpression("#{entity.designer.profession}");
        xLabel7.setName("entity.designer.profession"); // NOI18N
        xLabel7.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel6.add(xLabel7);

        xLabel8.setCaption("PRC No");
        xLabel8.setDepends(new String[] {"entity.designer"});
        xLabel8.setExpression("#{entity.designer.prcno}");
        xLabel8.setName("entity.designer.prcno"); // NOI18N
        xLabel8.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel6.add(xLabel8);

        xLabel9.setCaption("PTR No");
        xLabel9.setDepends(new String[] {"entity.designer"});
        xLabel9.setExpression("#{entity.designer.ptrno}");
        xLabel9.setName("entity.designer.ptrno"); // NOI18N
        xLabel9.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel6.add(xLabel9);

        cTCIndividualEntryPage4.setCaption("Community Tax Certificate");
        cTCIndividualEntryPage4.setDepends(new String[] {"entity.designer"});
        cTCIndividualEntryPage4.setEntityName("entity.designer");
        cTCIndividualEntryPage4.setName("entity.designer.ctc"); // NOI18N
        cTCIndividualEntryPage4.setOpaque(false);
        cTCIndividualEntryPage4.setPreferredSize(new java.awt.Dimension(0, 69));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(xFormPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cTCIndividualEntryPage4, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cTCIndividualEntryPage4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Architect/Civil Engineer In-charge of Construction ");
        jPanel6.setBorder(xTitledBorder2);
        jPanel6.setOpaque(false);

        xFormPanel3.setCaptionWidth(120);

        xLookupField3.setCaption("Name");
        xLookupField3.setExpression("#{entity.supervisor.name}");
        xLookupField3.setHandler("obo_profession:lookup");
        xLookupField3.setName("entity.supervisor"); // NOI18N
        xLookupField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel3.add(xLookupField3);

        xLabel4.setCaption("Profession");
        xLabel4.setDepends(new String[] {"entity.supervisor"});
        xLabel4.setExpression("#{entity.supervisor.profession}");
        xLabel4.setName("entity.supervisor.profession"); // NOI18N
        xLabel4.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel3.add(xLabel4);

        xLabel5.setCaption("PRC No");
        xLabel5.setDepends(new String[] {"entity.supervisor"});
        xLabel5.setExpression("#{entity.supervisor.prcno}");
        xLabel5.setName("entity.supervisor.profession"); // NOI18N
        xLabel5.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel3.add(xLabel5);

        xLabel6.setCaption("PTR No");
        xLabel6.setDepends(new String[] {"entity.supervisor"});
        xLabel6.setExpression("#{entity.supervisor.ptrno}");
        xLabel6.setName("entity.supervisor.profession"); // NOI18N
        xLabel6.setPreferredSize(new java.awt.Dimension(0, 16));
        xFormPanel3.add(xLabel6);

        cTCIndividualEntryPage3.setCaption("Community Tax Certificate");
        cTCIndividualEntryPage3.setDepends(new String[] {"entity.supervisor"});
        cTCIndividualEntryPage3.setEntityName("entity.supervisor");
        cTCIndividualEntryPage3.setName("entity.supervisor.ctc"); // NOI18N
        cTCIndividualEntryPage3.setOpaque(false);
        cTCIndividualEntryPage3.setPreferredSize(new java.awt.Dimension(0, 69));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(xFormPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cTCIndividualEntryPage3, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cTCIndividualEntryPage3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(134, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.etracs.components.CTCIndividualEntryPage cTCIndividualEntryPage3;
    private com.rameses.etracs.components.CTCIndividualEntryPage cTCIndividualEntryPage4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private com.rameses.rcp.control.XFormPanel xFormPanel3;
    private com.rameses.rcp.control.XFormPanel xFormPanel6;
    private com.rameses.rcp.control.XLabel xLabel4;
    private com.rameses.rcp.control.XLabel xLabel5;
    private com.rameses.rcp.control.XLabel xLabel6;
    private com.rameses.rcp.control.XLabel xLabel7;
    private com.rameses.rcp.control.XLabel xLabel8;
    private com.rameses.rcp.control.XLabel xLabel9;
    private com.rameses.rcp.control.XLookupField xLookupField3;
    private com.rameses.rcp.control.XLookupField xLookupField5;
    // End of variables declaration//GEN-END:variables
}