/*
 * MachInformation.java
 *
 * Created on June 29, 2011, 2:02 PM
 */

package com.rameses.gov.etracs.rptis.views;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

@Template(FormPage.class)
@StyleSheet
public class RPUMachDetailPage extends javax.swing.JPanel {
    
    /** Creates new form MachInformation */
    public RPUMachDetailPage() {
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
        formPanel1 = new com.rameses.rcp.util.FormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel2 = new com.rameses.rcp.control.XLabel();
        jPanel2 = new javax.swing.JPanel();
        xCheckBox1 = new com.rameses.rcp.control.XCheckBox();
        xCheckBox2 = new com.rameses.rcp.control.XCheckBox();
        formPanel2 = new com.rameses.rcp.util.FormPanel();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        formPanel3 = new com.rameses.rcp.util.FormPanel();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xTextField2 = new com.rameses.rcp.control.XTextField();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xTextField4 = new com.rameses.rcp.control.XTextField();
        xTextField5 = new com.rameses.rcp.control.XTextField();
        xSeparator1 = new com.rameses.rcp.control.XSeparator();
        xNumberField1 = new com.rameses.rcp.control.XNumberField();
        xNumberField2 = new com.rameses.rcp.control.XNumberField();
        xNumberField3 = new com.rameses.rcp.control.XNumberField();
        xNumberField4 = new com.rameses.rcp.control.XNumberField();
        xNumberField5 = new com.rameses.rcp.control.XNumberField();
        xNumberField6 = new com.rameses.rcp.control.XNumberField();
        xCheckBox3 = new com.rameses.rcp.control.XCheckBox();
        xCheckBox5 = new com.rameses.rcp.control.XCheckBox();
        formPanel5 = new com.rameses.rcp.util.FormPanel();
        xDecimalField2 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField3 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField4 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField5 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField6 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField7 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField8 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField9 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField10 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField11 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField12 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField13 = new com.rameses.rcp.control.XDecimalField();
        xDecimalField14 = new com.rameses.rcp.control.XDecimalField();
        jPanel3 = new javax.swing.JPanel();
        xCheckBox4 = new com.rameses.rcp.control.XCheckBox();
        formPanel6 = new com.rameses.rcp.util.FormPanel();
        xDecimalField15 = new com.rameses.rcp.control.XDecimalField();

        setPreferredSize(new java.awt.Dimension(596, 510));
        setLayout(new java.awt.BorderLayout());

        formPanel1.setCaptionWidth(90);
        formPanel1.setPadding(new java.awt.Insets(0, 0, 0, 0));

        xLookupField1.setCaption("Machine Code");
        xLookupField1.setExpression("#{item.code}");
        xLookupField1.setHandler("machine:lookup");
        xLookupField1.setIndex(-100);
        xLookupField1.setName("machine"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(100, 19));
        xLookupField1.setRequired(true);
        formPanel1.add(xLookupField1);

        xLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        xLabel2.setCaption("Machine Name");
        xLabel2.setDepends(new String[] {"machine"});
        xLabel2.setName("machine.name"); // NOI18N
        xLabel2.setPreferredSize(new java.awt.Dimension(0, 19));
        formPanel1.add(xLabel2);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Machine Information");
        jPanel2.setBorder(xTitledBorder1);

        xCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox1.setIndex(-10);
        xCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox1.setName("newlyinstalled"); // NOI18N
        xCheckBox1.setText("Newly Installed?");

        xCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox2.setName("imported"); // NOI18N
        xCheckBox2.setText("Is Imported?");

        formPanel2.setCaptionWidth(110);

        xDecimalField1.setCaption("Price Index");
        xDecimalField1.setCaptionWidth(80);
        xDecimalField1.setName("conversionfactor"); // NOI18N
        xDecimalField1.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel2.add(xDecimalField1);

        xTextField1.setCaption("Brand");
        xTextField1.setCaptionWidth(100);
        xTextField1.setName("machdetail.brand"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 18));
        formPanel3.add(xTextField1);

        xTextField2.setCaption("Model");
        xTextField2.setCaptionWidth(100);
        xTextField2.setName("machdetail.model"); // NOI18N
        xTextField2.setPreferredSize(new java.awt.Dimension(0, 18));
        formPanel3.add(xTextField2);

        xTextField3.setCaption("Capacity");
        xTextField3.setCaptionWidth(100);
        xTextField3.setName("machdetail.capacity"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 18));
        formPanel3.add(xTextField3);

        xTextField4.setCaption("Serial No.");
        xTextField4.setCaptionWidth(100);
        xTextField4.setName("machdetail.serialno"); // NOI18N
        xTextField4.setPreferredSize(new java.awt.Dimension(0, 18));
        formPanel3.add(xTextField4);

        xTextField5.setCaption("Status");
        xTextField5.setCaptionWidth(100);
        xTextField5.setName("machdetail.status"); // NOI18N
        xTextField5.setPreferredSize(new java.awt.Dimension(0, 18));
        formPanel3.add(xTextField5);

        xSeparator1.setPreferredSize(new java.awt.Dimension(0, 20));

        org.jdesktop.layout.GroupLayout xSeparator1Layout = new org.jdesktop.layout.GroupLayout(xSeparator1);
        xSeparator1.setLayout(xSeparator1Layout);
        xSeparator1Layout.setHorizontalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 256, Short.MAX_VALUE)
        );
        xSeparator1Layout.setVerticalGroup(
            xSeparator1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 20, Short.MAX_VALUE)
        );

        formPanel3.add(xSeparator1);

        xNumberField1.setCaption("Year Acquired");
        xNumberField1.setCaptionWidth(100);
        xNumberField1.setFieldType(int.class);
        xNumberField1.setName("yearacquired"); // NOI18N
        xNumberField1.setPreferredSize(new java.awt.Dimension(0, 18));
        xNumberField1.setRequired(true);
        formPanel3.add(xNumberField1);

        xNumberField2.setCaption("Year Installed");
        xNumberField2.setCaptionWidth(100);
        xNumberField2.setFieldType(int.class);
        xNumberField2.setName("yearinstalled"); // NOI18N
        xNumberField2.setPreferredSize(new java.awt.Dimension(0, 18));
        formPanel3.add(xNumberField2);

        xNumberField3.setCaption("Operation Year");
        xNumberField3.setCaptionWidth(100);
        xNumberField3.setDepends(new String[] {"yearacquired"});
        xNumberField3.setFieldType(int.class);
        xNumberField3.setName("operationyear"); // NOI18N
        xNumberField3.setPreferredSize(new java.awt.Dimension(0, 18));
        xNumberField3.setRequired(true);
        formPanel3.add(xNumberField3);

        xNumberField4.setCaption("Years Used");
        xNumberField4.setCaptionWidth(100);
        xNumberField4.setDepends(new String[] {"yearacquired", "yearinstalled", "operationyear"});
        xNumberField4.setEnabled(false);
        xNumberField4.setFieldType(int.class);
        xNumberField4.setName("machdetail.yearsused"); // NOI18N
        xNumberField4.setPreferredSize(new java.awt.Dimension(0, 18));
        xNumberField4.setRequired(true);
        formPanel3.add(xNumberField4);

        xNumberField5.setCaption("Estimated Life");
        xNumberField5.setCaptionWidth(100);
        xNumberField5.setFieldType(int.class);
        xNumberField5.setName("estimatedlife"); // NOI18N
        xNumberField5.setPreferredSize(new java.awt.Dimension(0, 18));
        formPanel3.add(xNumberField5);

        xNumberField6.setCaption("Remaining Life");
        xNumberField6.setCaptionWidth(100);
        xNumberField6.setDepends(new String[] {"machdetail.estimatedlife", "machdetail.yearsused", "machdetail.operationyear", "machdetail.yearacquired", "machdetail.yearinstalled"});
        xNumberField6.setEnabled(false);
        xNumberField6.setFieldType(int.class);
        xNumberField6.setName("machdetail.remaininglife"); // NOI18N
        xNumberField6.setPreferredSize(new java.awt.Dimension(0, 18));
        formPanel3.add(xNumberField6);

        xCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox3.setName("autodepreciate"); // NOI18N
        xCheckBox3.setText("Auto Depreciate?");

        xCheckBox5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox5.setIndex(-10);
        xCheckBox5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox5.setName("machdetail.taxable"); // NOI18N
        xCheckBox5.setText("Taxable?");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(formPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                        .add(26, 26, 26)
                        .add(formPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(xCheckBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(90, 90, 90)))
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(114, Short.MAX_VALUE)
                .add(xCheckBox3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(71, 71, 71))
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(xCheckBox1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                        .add(100, 100, 100))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(xCheckBox5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(xCheckBox5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xCheckBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xCheckBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(formPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(formPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(xCheckBox3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Cost Related Information");
        formPanel5.setBorder(xTitledBorder2);
        formPanel5.setCaptionWidth(110);

        xDecimalField2.setCaption("Original Cost");
        xDecimalField2.setName("originalcost"); // NOI18N
        xDecimalField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xDecimalField2.setRequired(true);
        formPanel5.add(xDecimalField2);

        xDecimalField3.setCaption("Freight Cost");
        xDecimalField3.setDepends(new String[] {"newlyinstalled"});
        xDecimalField3.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField3.setEnabled(false);
        xDecimalField3.setName("freightcost"); // NOI18N
        xDecimalField3.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField3);

        xDecimalField4.setCaption("Installation Cost");
        xDecimalField4.setDepends(new String[] {"newlyinstalled"});
        xDecimalField4.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField4.setEnabled(false);
        xDecimalField4.setName("installationcost"); // NOI18N
        xDecimalField4.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField4);

        xDecimalField5.setCaption("Insurance Cost");
        xDecimalField5.setDepends(new String[] {"newlyinstalled"});
        xDecimalField5.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField5.setEnabled(false);
        xDecimalField5.setName("insurancecost"); // NOI18N
        xDecimalField5.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField5);

        xDecimalField6.setCaption("Brokerage Cost");
        xDecimalField6.setDepends(new String[] {"newlyinstalled"});
        xDecimalField6.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField6.setEnabled(false);
        xDecimalField6.setName("brokeragecost"); // NOI18N
        xDecimalField6.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField6);

        xDecimalField7.setCaption("Arrastre/Handling");
        xDecimalField7.setDepends(new String[] {"newlyinstalled"});
        xDecimalField7.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField7.setEnabled(false);
        xDecimalField7.setName("arrastrecost"); // NOI18N
        xDecimalField7.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField7);

        xDecimalField8.setCaption("Other Cost");
        xDecimalField8.setDepends(new String[] {"newlyinstalled"});
        xDecimalField8.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField8.setEnabled(false);
        xDecimalField8.setName("othercost"); // NOI18N
        xDecimalField8.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField8);

        xDecimalField9.setCaption("FERAC");
        xDecimalField9.setDepends(new String[] {"machdetail.newlyinstalled", "machdetail.imported"});
        xDecimalField9.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField9.setEnabled(false);
        xDecimalField9.setName("machdetail.ferac"); // NOI18N
        xDecimalField9.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField9);

        xDecimalField10.setCaption("Current Forex");
        xDecimalField10.setDepends(new String[] {"machdetail.newlyinstalled", "machdetail.imported"});
        xDecimalField10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField10.setEnabled(false);
        xDecimalField10.setName("machdetail.forex"); // NOI18N
        xDecimalField10.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField10);

        xDecimalField11.setCaption("Acquisition Cost");
        xDecimalField11.setDepends(new String[] {"machdetail.newlyinstalled", "machdetail.imported"});
        xDecimalField11.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField11.setEnabled(false);
        xDecimalField11.setName("machdetail.acquisitioncost"); // NOI18N
        xDecimalField11.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField11);

        xDecimalField12.setCaption("Replacement Cost");
        xDecimalField12.setDepends(new String[] {"machdetail.newlyinstalled", "machdetail.imported"});
        xDecimalField12.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField12.setEnabled(false);
        xDecimalField12.setName("machdetail.replacementcost"); // NOI18N
        xDecimalField12.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField12);

        xDecimalField13.setCaption("Depreciation (%)");
        xDecimalField13.setDepends(new String[] {"machdetail.autodepreciate", "autodepreciate"});
        xDecimalField13.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField13.setEnabled(false);
        xDecimalField13.setName("depreciation"); // NOI18N
        xDecimalField13.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField13);

        xDecimalField14.setCaption("Depreciation Value");
        xDecimalField14.setDepends(new String[] {"machdetail.autodepreciate", "autodepreciate"});
        xDecimalField14.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField14.setEnabled(false);
        xDecimalField14.setName("machdetail.depreciationvalue"); // NOI18N
        xDecimalField14.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel5.add(xDecimalField14);

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("Sworn Statement");
        jPanel3.setBorder(xTitledBorder3);

        xCheckBox4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xCheckBox4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        xCheckBox4.setName("machdetail.useswornamount"); // NOI18N
        xCheckBox4.setText("Use Sworn Amount?");

        formPanel6.setCaptionWidth(95);

        xDecimalField15.setCaption("Sworn Amount");
        xDecimalField15.setDepends(new String[] {"machdetail.useswornamount"});
        xDecimalField15.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xDecimalField15.setEnabled(false);
        xDecimalField15.setName("machdetail.swornamount"); // NOI18N
        xDecimalField15.setPreferredSize(new java.awt.Dimension(0, 20));
        formPanel6.add(xDecimalField15);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(xCheckBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(153, Short.MAX_VALUE))
            .add(formPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(xCheckBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(formPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(formPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, formPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(formPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(formPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 335, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.util.FormPanel formPanel1;
    private com.rameses.rcp.util.FormPanel formPanel2;
    private com.rameses.rcp.util.FormPanel formPanel3;
    private com.rameses.rcp.util.FormPanel formPanel5;
    private com.rameses.rcp.util.FormPanel formPanel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private com.rameses.rcp.control.XCheckBox xCheckBox1;
    private com.rameses.rcp.control.XCheckBox xCheckBox2;
    private com.rameses.rcp.control.XCheckBox xCheckBox3;
    private com.rameses.rcp.control.XCheckBox xCheckBox4;
    private com.rameses.rcp.control.XCheckBox xCheckBox5;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField10;
    private com.rameses.rcp.control.XDecimalField xDecimalField11;
    private com.rameses.rcp.control.XDecimalField xDecimalField12;
    private com.rameses.rcp.control.XDecimalField xDecimalField13;
    private com.rameses.rcp.control.XDecimalField xDecimalField14;
    private com.rameses.rcp.control.XDecimalField xDecimalField15;
    private com.rameses.rcp.control.XDecimalField xDecimalField2;
    private com.rameses.rcp.control.XDecimalField xDecimalField3;
    private com.rameses.rcp.control.XDecimalField xDecimalField4;
    private com.rameses.rcp.control.XDecimalField xDecimalField5;
    private com.rameses.rcp.control.XDecimalField xDecimalField6;
    private com.rameses.rcp.control.XDecimalField xDecimalField7;
    private com.rameses.rcp.control.XDecimalField xDecimalField8;
    private com.rameses.rcp.control.XDecimalField xDecimalField9;
    private com.rameses.rcp.control.XLabel xLabel2;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XNumberField xNumberField1;
    private com.rameses.rcp.control.XNumberField xNumberField2;
    private com.rameses.rcp.control.XNumberField xNumberField3;
    private com.rameses.rcp.control.XNumberField xNumberField4;
    private com.rameses.rcp.control.XNumberField xNumberField5;
    private com.rameses.rcp.control.XNumberField xNumberField6;
    private com.rameses.rcp.control.XSeparator xSeparator1;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField2;
    private com.rameses.rcp.control.XTextField xTextField3;
    private com.rameses.rcp.control.XTextField xTextField4;
    private com.rameses.rcp.control.XTextField xTextField5;
    // End of variables declaration//GEN-END:variables
    
}
