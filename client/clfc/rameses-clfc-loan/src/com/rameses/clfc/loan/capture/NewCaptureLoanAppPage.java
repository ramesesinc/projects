/*
 * CaptureLoanAppPage.java
 *
 * Created on August 31, 2013, 3:10 PM
 */

package com.rameses.clfc.loan.capture;

import com.rameses.osiris2.themes.FormPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import java.math.BigDecimal;

/**
 *
 * @author  louie
 */
@StyleSheet
@Template(FormPage.class) 
public class NewCaptureLoanAppPage extends javax.swing.JPanel {
    
    public NewCaptureLoanAppPage() {
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
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xLabel1 = new com.rameses.rcp.control.XLabel();
        jPanel2 = new javax.swing.JPanel();
        xFormPanel4 = new com.rameses.rcp.control.XFormPanel();
        xLabel3 = new com.rameses.rcp.control.XLabel();
        xTextField3 = new com.rameses.rcp.control.XTextField();
        xComboBox4 = new com.rameses.rcp.control.XComboBox();
        xComboBox3 = new com.rameses.rcp.control.XComboBox();
        xComboBox1 = new com.rameses.rcp.control.XComboBox();
        xTextField1 = new com.rameses.rcp.control.XTextField();
        xFormPanel5 = new com.rameses.rcp.control.XFormPanel();
        xComboBox2 = new com.rameses.rcp.control.XComboBox();
        xNumberField1 = new com.rameses.rcp.control.XNumberField();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();
        xDateField1 = new com.rameses.rcp.control.XDateField();
        xLookupField2 = new com.rameses.rcp.control.XLookupField();
        jScrollPane3 = new javax.swing.JScrollPane();
        xTextArea3 = new com.rameses.rcp.control.XTextArea();
        xLabel12 = new com.rameses.rcp.control.XLabel();
        jPanel5 = new javax.swing.JPanel();
        xDataTable1 = new com.rameses.rcp.control.XDataTable();

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder1 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder1.setTitle("Principal Borrower Information");
        jPanel1.setBorder(xTitledBorder1);

        xFormPanel1.setCellspacing(1);
        xFormPanel1.setPadding(new java.awt.Insets(5, 3, 0, 20));

        xLookupField1.setCaption("Name");
        xLookupField1.setCaptionWidth(100);
        xLookupField1.setExpression("#{item.name}");
        xLookupField1.setHandler("customerLookupHandler");
        xLookupField1.setIndex(-10);
        xLookupField1.setName("entity.borrower"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(300, 20));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xLabel1.setBackground(new java.awt.Color(250, 250, 250));
        com.rameses.rcp.control.border.XLineBorder xLineBorder1 = new com.rameses.rcp.control.border.XLineBorder();
        xLineBorder1.setLineColor(new java.awt.Color(180, 180, 180));
        xLabel1.setBorder(xLineBorder1);
        xLabel1.setCaption("Address");
        xLabel1.setCaptionWidth(100);
        xLabel1.setDepends(new String[] {"entity.borrower"});
        xLabel1.setExpression("#{entity.borrower.address}");
        xLabel1.setOpaque(true);
        xLabel1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel1.add(xLabel1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder2 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder2.setTitle("Loan Details");
        jPanel2.setBorder(xTitledBorder2);

        xFormPanel4.setCellspacing(1);
        xFormPanel4.setPadding(new java.awt.Insets(5, 3, 0, 20));

        xLabel3.setAntiAliasOn(true);
        xLabel3.setBackground(new java.awt.Color(0, 153, 204));
        xLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xLabel3.setCaption("Mode");
        xLabel3.setCaptionWidth(100);
        xLabel3.setExpression("#{entity.appmode}");
        xLabel3.setFontStyle("font-weight:bold;font-size:14;");
        xLabel3.setForeground(new java.awt.Color(255, 255, 255));
        xLabel3.setOpaque(true);
        xLabel3.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xLabel3);

        xTextField3.setCaption("Loan No.");
        xTextField3.setCaptionWidth(100);
        xTextField3.setFontStyle("font-size:12;");
        xTextField3.setName("entity.loanno"); // NOI18N
        xTextField3.setPreferredSize(new java.awt.Dimension(0, 20));
        xTextField3.setRequired(true);
        xFormPanel4.add(xTextField3);

        xComboBox4.setCaption("Loan Type");
        xComboBox4.setCaptionWidth(100);
        xComboBox4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xComboBox4.setFontStyle("font-size:12");
        xComboBox4.setItems("loanTypes");
        xComboBox4.setName("entity.loantype"); // NOI18N
        xComboBox4.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox4.setRequired(true);
        xFormPanel4.add(xComboBox4);

        xComboBox3.setAllowNull(false);
        xComboBox3.setCaption("App. Type");
        xComboBox3.setCaptionWidth(100);
        xComboBox3.setExpression("#{item.value}");
        xComboBox3.setFontStyle("font-size:12");
        xComboBox3.setItemKey("value");
        xComboBox3.setItems("appTypes");
        xComboBox3.setName("entity.apptype"); // NOI18N
        xComboBox3.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox3.setRequired(true);
        xFormPanel4.add(xComboBox3);

        xComboBox1.setAllowNull(false);
        xComboBox1.setCaption("Client Type");
        xComboBox1.setCaptionWidth(100);
        xComboBox1.setExpression("#{item.value}");
        xComboBox1.setFontStyle("font-size:12");
        xComboBox1.setImmediate(true);
        xComboBox1.setItemKey("value");
        xComboBox1.setItems("clientTypes");
        xComboBox1.setName("entity.clienttype"); // NOI18N
        xComboBox1.setPreferredSize(new java.awt.Dimension(0, 20));
        xComboBox1.setRequired(true);
        xFormPanel4.add(xComboBox1);

        xTextField1.setCaption("Interviewed By");
        xTextField1.setCaptionWidth(100);
        xTextField1.setDepends(new String[] {"entity.clienttype"});
        xTextField1.setName("entity.marketedby"); // NOI18N
        xTextField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xFormPanel4.add(xTextField1);

        xFormPanel5.setCellspacing(1);
        xFormPanel5.setPadding(new java.awt.Insets(5, 5, 0, 10));

        xComboBox2.setCaption("Product Type");
        xComboBox2.setCaptionWidth(110);
        xComboBox2.setExpression("#{item.name}");
        xComboBox2.setFontStyle("font-size:12");
        xComboBox2.setItems("productTypes");
        xComboBox2.setName("entity.producttype"); // NOI18N
        xComboBox2.setPreferredSize(new java.awt.Dimension(210, 20));
        xComboBox2.setRequired(true);
        xFormPanel5.add(xComboBox2);

        xNumberField1.setCaption("Term");
        xNumberField1.setCaptionWidth(110);
        xNumberField1.setDepends(new String[] {"entity.producttype"});
        xNumberField1.setFieldType(Integer.class);
        xNumberField1.setFontStyle("font-size:12");
        xNumberField1.setName("entity.producttype.term"); // NOI18N
        xNumberField1.setPattern("#,##0");
        xNumberField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xNumberField1.setReadonly(true);
        xFormPanel5.add(xNumberField1);

        xDecimalField1.setCaption("Amount Released");
        xDecimalField1.setCaptionWidth(110);
        xDecimalField1.setFontStyle("font-size:14");
        xDecimalField1.setName("entity.loanamount"); // NOI18N
        xDecimalField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xDecimalField1.setRequired(true);
        xFormPanel5.add(xDecimalField1);

        xDateField1.setCaption("Date Released");
        xDateField1.setCaptionWidth(110);
        xDateField1.setFontStyle("font-size:12");
        xDateField1.setName("entity.dtreleased"); // NOI18N
        xDateField1.setPreferredSize(new java.awt.Dimension(0, 20));
        xDateField1.setRequired(true);
        xFormPanel5.add(xDateField1);

        xLookupField2.setCaption("Route");
        xLookupField2.setCaptionWidth(110);
        xLookupField2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xLookupField2.setExpression("#{item.description} #{item? ' - ' + item.area : ''}");
        xLookupField2.setHandler("routeLookupHandler");
        xLookupField2.setName("entity.route"); // NOI18N
        xLookupField2.setPreferredSize(new java.awt.Dimension(0, 20));
        xLookupField2.setRequired(true);
        xFormPanel5.add(xLookupField2);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(0, 50));

        xTextArea3.setCaption("Purpose of Loan");
        xTextArea3.setCaptionWidth(100);
        xTextArea3.setHint("Specify purpose of loan");
        xTextArea3.setName("entity.purpose"); // NOI18N
        xTextArea3.setRequired(true);
        xTextArea3.setTextCase(com.rameses.rcp.constant.TextCase.UPPER);
        jScrollPane3.setViewportView(xTextArea3);

        com.rameses.rcp.control.border.XEtchedBorder xEtchedBorder1 = new com.rameses.rcp.control.border.XEtchedBorder();
        xEtchedBorder1.setHideLeft(true);
        xEtchedBorder1.setHideRight(true);
        xEtchedBorder1.setHideTop(true);
        xLabel12.setBorder(xEtchedBorder1);
        xLabel12.setFontStyle("font-weight:bold;");
        xLabel12.setForeground(new java.awt.Color(80, 80, 80));
        xLabel12.setText("Purpose of Loan");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xFormPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xFormPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        com.rameses.rcp.control.border.XTitledBorder xTitledBorder3 = new com.rameses.rcp.control.border.XTitledBorder();
        xTitledBorder3.setTitle("Previous Loans");
        jPanel5.setBorder(xTitledBorder3);

        xDataTable1.setBorder(new com.rameses.rcp.control.table.TableBorders.DefaultBorder());
        xDataTable1.setColumns(new com.rameses.rcp.common.Column[]{
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "loancount"}
                , new Object[]{"caption", "Count"}
                , new Object[]{"width", 5}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", false}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.IntegerColumnHandler("#,##0", -1, -1)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "loanno"}
                , new Object[]{"caption", "Loan No."}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "dtreleased"}
                , new Object[]{"caption", "Date Released"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DateColumnHandler("yyyy-MM-dd", "yyyy-MM-dd", "yyyy-MM-dd")}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "loanamount"}
                , new Object[]{"caption", "Loan Amount"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", true}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.DecimalColumnHandler("#,##0.00", -1.0, -1.0, false, 0)}
            }),
            new com.rameses.rcp.common.Column(new Object[]{
                new Object[]{"name", "remarks"}
                , new Object[]{"caption", "Remarks"}
                , new Object[]{"width", 100}
                , new Object[]{"minWidth", 0}
                , new Object[]{"maxWidth", 0}
                , new Object[]{"required", false}
                , new Object[]{"resizable", true}
                , new Object[]{"nullWhenEmpty", true}
                , new Object[]{"editable", true}
                , new Object[]{"editableWhen", null}
                , new Object[]{"textCase", com.rameses.rcp.constant.TextCase.UPPER}
                , new Object[]{"typeHandler", new com.rameses.rcp.common.TextColumnHandler()}
            })
        });
        xDataTable1.setDepends(new String[] {"entity.apptype"});
        xDataTable1.setHandler("previousLoansHandler");
        xDataTable1.setName("selectedPreviousLoan"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xDataTable1, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(xDataTable1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    private com.rameses.rcp.control.XComboBox xComboBox1;
    private com.rameses.rcp.control.XComboBox xComboBox2;
    private com.rameses.rcp.control.XComboBox xComboBox3;
    private com.rameses.rcp.control.XComboBox xComboBox4;
    private com.rameses.rcp.control.XDataTable xDataTable1;
    private com.rameses.rcp.control.XDateField xDateField1;
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XFormPanel xFormPanel4;
    private com.rameses.rcp.control.XFormPanel xFormPanel5;
    private com.rameses.rcp.control.XLabel xLabel1;
    private com.rameses.rcp.control.XLabel xLabel12;
    private com.rameses.rcp.control.XLabel xLabel3;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    private com.rameses.rcp.control.XLookupField xLookupField2;
    private com.rameses.rcp.control.XNumberField xNumberField1;
    private com.rameses.rcp.control.XTextArea xTextArea3;
    private com.rameses.rcp.control.XTextField xTextField1;
    private com.rameses.rcp.control.XTextField xTextField3;
    // End of variables declaration//GEN-END:variables
    
}
