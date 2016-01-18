/*
 * BrgyShareAccountMappingPage.java
 *
 * Created on April 8, 2014, 11:50 AM
 */

package com.rameses.gov.etracs.rptis.municipality.setting;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author  Rameses
 */
@Template(FormPage.class)
@StyleSheet
public class ProvinceTaxAccountMappingPage extends javax.swing.JPanel {
    
    /** Creates new form BrgyShareAccountMappingPage */
    public ProvinceTaxAccountMappingPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        xLookupField3 = new com.rameses.rcp.control.XLookupField();
        xLookupField4 = new com.rameses.rcp.control.XLookupField();
        xLookupField5 = new com.rameses.rcp.control.XLookupField();
        xLookupField6 = new com.rameses.rcp.control.XLookupField();
        xLookupField7 = new com.rameses.rcp.control.XLookupField();
        xLookupField15 = new com.rameses.rcp.control.XLookupField();
        xLookupField16 = new com.rameses.rcp.control.XLookupField();
        xLookupField17 = new com.rameses.rcp.control.XLookupField();
        xLookupField18 = new com.rameses.rcp.control.XLookupField();
        xLookupField19 = new com.rameses.rcp.control.XLookupField();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        xLookupField8 = new com.rameses.rcp.control.XLookupField();
        xLookupField9 = new com.rameses.rcp.control.XLookupField();
        xLookupField10 = new com.rameses.rcp.control.XLookupField();
        xLookupField11 = new com.rameses.rcp.control.XLookupField();
        xLookupField12 = new com.rameses.rcp.control.XLookupField();
        xLookupField13 = new com.rameses.rcp.control.XLookupField();
        xLookupField14 = new com.rameses.rcp.control.XLookupField();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Account Mapping");
        xFormPanel1.setBorder(xTitledBorder1);
        xFormPanel1.setCaptionWidth(200);

        xLabel1.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xLabel1.setExpression("#{'BASIC ACCOUNTS'}");
        xLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel1.setShowCaption(false);
        xFormPanel1.add(xLabel1);

        xLookupField1.setCaption("Prior Account");
        xLookupField1.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xLookupField1.setDepends(new String[] {"entity"});
        xLookupField1.setExpression("#{entity.basicprioracct.title}");
        xLookupField1.setHandler("revenueitem:lookup");
        xLookupField1.setName("entity.basicprioracct"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField1);

        xLookupField2.setCaption("Prior Interest Account");
        xLookupField2.setDepends(new String[] {"entity"});
        xLookupField2.setExpression("#{entity.basicpriorintacct.title}");
        xLookupField2.setHandler("revenueitem:lookup");
        xLookupField2.setName("entity.basicpriorintacct"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField2);

        xLookupField3.setCaption("Previous Account");
        xLookupField3.setDepends(new String[] {"entity"});
        xLookupField3.setExpression("#{entity.basicprevacct.title}");
        xLookupField3.setHandler("revenueitem:lookup");
        xLookupField3.setName("entity.basicprevacct"); // NOI18N
        xLookupField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField3);

        xLookupField4.setCaption("Previous Interest Account");
        xLookupField4.setDepends(new String[] {"entity"});
        xLookupField4.setExpression("#{entity.basicprevintacct.title}");
        xLookupField4.setHandler("revenueitem:lookup");
        xLookupField4.setName("entity.basicprevintacct"); // NOI18N
        xLookupField4.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField4);

        xLookupField5.setCaption("Current Account");
        xLookupField5.setDepends(new String[] {"entity"});
        xLookupField5.setExpression("#{entity.basiccurracct.title}");
        xLookupField5.setHandler("revenueitem:lookup");
        xLookupField5.setName("entity.basiccurracct"); // NOI18N
        xLookupField5.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField5);

        xLookupField6.setCaption("Current Interest Account");
        xLookupField6.setDepends(new String[] {"entity"});
        xLookupField6.setExpression("#{entity.basiccurrintacct.title}");
        xLookupField6.setHandler("revenueitem:lookup");
        xLookupField6.setName("entity.basiccurrintacct"); // NOI18N
        xLookupField6.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField6);

        xLookupField7.setCaption("Advance Account");
        xLookupField7.setDepends(new String[] {"entity"});
        xLookupField7.setExpression("#{entity.basicadvacct.title}");
        xLookupField7.setHandler("revenueitem:lookup");
        xLookupField7.setName("entity.basicadvacct"); // NOI18N
        xLookupField7.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField7);

        xLookupField15.setCaption("Idle Land Current Account");
        xLookupField15.setDepends(new String[] {"entity"});
        xLookupField15.setExpression("#{entity.basicidlecurracct.title}");
        xLookupField15.setHandler("revenueitem:lookup");
        xLookupField15.setName("entity.basicidlecurracct"); // NOI18N
        xLookupField15.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField15);

        xLookupField16.setCaption("Idle Land Current Interest Account");
        xLookupField16.setDepends(new String[] {"entity"});
        xLookupField16.setExpression("#{entity.basicidlecurrintacct.title}");
        xLookupField16.setHandler("revenueitem:lookup");
        xLookupField16.setName("entity.basicidlecurrintacct"); // NOI18N
        xLookupField16.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField16);

        xLookupField17.setCaption("Idle Land Previous Account");
        xLookupField17.setDepends(new String[] {"entity"});
        xLookupField17.setExpression("#{entity.basicidleprevacct.title}");
        xLookupField17.setHandler("revenueitem:lookup");
        xLookupField17.setName("entity.basicidleprevacct"); // NOI18N
        xLookupField17.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField17);

        xLookupField18.setCaption("Idle Land Previous Interest Account");
        xLookupField18.setDepends(new String[] {"entity"});
        xLookupField18.setExpression("#{entity.basicidleprevintacct.title}");
        xLookupField18.setHandler("revenueitem:lookup");
        xLookupField18.setName("entity.basicidleprevintacct"); // NOI18N
        xLookupField18.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField18);

        xLookupField19.setCaption("Idle Land Advance Account");
        xLookupField19.setDepends(new String[] {"entity"});
        xLookupField19.setExpression("#{entity.basicidleadvacct.title}");
        xLookupField19.setHandler("revenueitem:lookup");
        xLookupField19.setName("entity.basicidleadvacct"); // NOI18N
        xLookupField19.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField19);

        xLabel2.setCellPadding(new java.awt.Insets(10, 0, 0, 0));
        xLabel2.setExpression("#{'SEF ACCOUNTS'}");
        xLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        xLabel2.setShowCaption(false);
        xFormPanel1.add(xLabel2);

        xLookupField8.setCaption("Prior Account");
        xLookupField8.setCellPadding(new java.awt.Insets(5, 0, 0, 0));
        xLookupField8.setDepends(new String[] {"entity"});
        xLookupField8.setExpression("#{entity.sefprioracct.title}");
        xLookupField8.setHandler("revenueitem:lookup");
        xLookupField8.setName("entity.sefprioracct"); // NOI18N
        xLookupField8.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField8);

        xLookupField9.setCaption("Prior Interest Account");
        xLookupField9.setDepends(new String[] {"entity"});
        xLookupField9.setExpression("#{entity.sefpriorintacct.title}");
        xLookupField9.setHandler("revenueitem:lookup");
        xLookupField9.setName("entity.sefpriorintacct"); // NOI18N
        xLookupField9.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField9);

        xLookupField10.setCaption("Previous Account");
        xLookupField10.setDepends(new String[] {"entity"});
        xLookupField10.setExpression("#{entity.sefprevacct.title}");
        xLookupField10.setHandler("revenueitem:lookup");
        xLookupField10.setName("entity.sefprevacct"); // NOI18N
        xLookupField10.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField10);

        xLookupField11.setCaption("Previous Interest Account");
        xLookupField11.setDepends(new String[] {"entity"});
        xLookupField11.setExpression("#{entity.sefprevintacct.title}");
        xLookupField11.setHandler("revenueitem:lookup");
        xLookupField11.setName("entity.sefprevintacct"); // NOI18N
        xLookupField11.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField11);

        xLookupField12.setCaption("Current Account");
        xLookupField12.setDepends(new String[] {"entity"});
        xLookupField12.setExpression("#{entity.sefcurracct.title}");
        xLookupField12.setHandler("revenueitem:lookup");
        xLookupField12.setName("entity.sefcurracct"); // NOI18N
        xLookupField12.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField12);

        xLookupField13.setCaption("Current Interest Account");
        xLookupField13.setDepends(new String[] {"entity"});
        xLookupField13.setExpression("#{entity.sefcurrintacct.title}");
        xLookupField13.setHandler("revenueitem:lookup");
        xLookupField13.setName("entity.sefcurrintacct"); // NOI18N
        xLookupField13.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField13);

        xLookupField14.setCaption("Advance Account");
        xLookupField14.setDepends(new String[] {"entity"});
        xLookupField14.setExpression("#{entity.sefadvacct.title}");
        xLookupField14.setHandler("revenueitem:lookup");
        xLookupField14.setName("entity.sefadvacct"); // NOI18N
        xLookupField14.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLookupField14);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(304, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XLookupField xLookupField10;
    private com.rameses.rcp.control.XLookupField xLookupField11;
    private com.rameses.rcp.control.XLookupField xLookupField12;
    private com.rameses.rcp.control.XLookupField xLookupField13;
    private com.rameses.rcp.control.XLookupField xLookupField14;
    private com.rameses.rcp.control.XLookupField xLookupField15;
    private com.rameses.rcp.control.XLookupField xLookupField16;
    private com.rameses.rcp.control.XLookupField xLookupField17;
    private com.rameses.rcp.control.XLookupField xLookupField18;
    private com.rameses.rcp.control.XLookupField xLookupField19;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XLookupField xLookupField3;
    private com.rameses.rcp.control.XLookupField xLookupField4;
    private com.rameses.rcp.control.XLookupField xLookupField5;
    private com.rameses.rcp.control.XLookupField xLookupField6;
    private com.rameses.rcp.control.XLookupField xLookupField7;
    private com.rameses.rcp.control.XLookupField xLookupField8;
    private com.rameses.rcp.control.XLookupField xLookupField9;
    // End of variables declaration//GEN-END:variables
    
}
