package com.rameses.clfc.report.controller;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class ClientInformationSheetReportController extends ReportModel
{
    @Service("ClientInformationSheetReportService")
    def service;
    
    String title = "Client Information Sheet";
    def loanappid;
    
    def close() {
        return '_close';
    }
    
    void preview() {
        viewReport();
    }

    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        return service.getReportData([loanappid: loanappid]);
    }
    
    public String getReportName() {
        return "com/rameses/clfc/report/clientinformation/ClientInformationReport.jasper";
    }

    public SubReport[] getSubReports() {
        return [
            // Principal Borrower Info
            new SubReport('COLLATERAL_PROPERTY', 'com/rameses/clfc/report/clientinformation/RealProperty.jasper'),
            new SubReport('COLLATERAL_APPLIANCE', 'com/rameses/clfc/report/clientinformation/Appliance.jasper'),
            new SubReport('COLLATERAL_VEHICLE', 'com/rameses/clfc/report/clientinformation/Vehicle.jasper'),
           /* new SubReport('MUST_COLLATERAL', 'com/rameses/clfc/report/clientinformation/MustCollateral.jasper'),
            new SubReport('MUST_COLLATERAL_APPLIANCE', 'com/rameses/clfc/report/clientinformation/MustCollateralList.jasper'),
            new SubReport('MUST_COLLATERAL_PROPERTY', 'com/rameses/clfc/report/clientinformation/MustCollateralList.jasper'),
            new SubReport('MUST_COLLATERAL_VEHICLE', 'com/rameses/clfc/report/clientinformation/MustCollateralList.jasper'),
            */
            new SubReport('PRINCIPAL_SPOUSE', 'com/rameses/clfc/report/clientinformation/Spouse.jasper'),
            new SubReport('PRINCIPAL_PARENT', 'com/rameses/clfc/report/clientinformation/Parent.jasper'),
            new SubReport('PRINCIPAL_BUSINESS', 'com/rameses/clfc/report/clientinformation/Business.jasper'),
            new SubReport('PRINCIPAL_RESIDENCY', 'com/rameses/clfc/report/clientinformation/Residency.jasper'),
            new SubReport('PRINCIPAL_EMPLOYMENT', 'com/rameses/clfc/report/clientinformation/Employment.jasper'),
            new SubReport('PRINCIPAL_OTHER_INCOME', 'com/rameses/clfc/report/clientinformation/OtherIncome.jasper'),
            new SubReport('PRINCIPAL_OTHER_LENDING', 'com/rameses/clfc/report/clientinformation/OtherLending.jasper'),
            new SubReport('PRINCIPAL_EDUCATIONAL_BACKGROUND', 'com/rameses/clfc/report/clientinformation/ProfessionalBackground.jasper'),

            new SubReport('PRINCIPAL_SIBLING_LIST', 'com/rameses/clfc/report/clientinformation/SiblingList.jasper'),
            new SubReport('PRINCIPAL_SIBLING_EMPLOYMENT', 'com/rameses/clfc/report/clientinformation/Employment2.jasper'),
            new SubReport('PRINCIPAL_SIBLING_OTHER_INCOME', 'com/rameses/clfc/report/clientinformation/OtherIncome2.jasper'),

            new SubReport('PRINCIPAL_CHILDREN_LIST', 'com/rameses/clfc/report/clientinformation/ChildrenList.jasper'),
            new SubReport('PRINCIPAL_CHILDREN_EMPLOYMENT', 'com/rameses/clfc/report/clientinformation/Employment2.jasper'),
            new SubReport('PRINCIPAL_CHILDREN_OTHER_INCOME', 'com/rameses/clfc/report/clientinformation/OtherIncome2.jasper'),

            new SubReport('PRINCIPAL_SAVING_LIST', 'com/rameses/clfc/report/clientinformation/SavingAccountList.jasper'),
            new SubReport('PRINCIPAL_CHECKING_LIST', 'com/rameses/clfc/report/clientinformation/CheckingAccountList.jasper'),

            // Other Loan Accounts
            /*new SubReport('PREVIOUS_LOAN', 'com/rameses/clfc/report/clientinformation/PreviousAmountAvailed.jasper'),
            new SubReport('CI_BUSINESS_REPORT', 'com/rameses/clfc/report/clientinformation/PrincipalCIBusiness.jasper'),
*/
            // Joint Borrower Info
            new SubReport('JOINTBORROWER_LIST', 'com/rameses/clfc/report/clientinformation/List.jasper'),
            new SubReport('JOINTBORROWER_SPOUSE', 'com/rameses/clfc/report/clientinformation/Spouse.jasper'),
            new SubReport('JOINTBORROWER_PARENT', 'com/rameses/clfc/report/clientinformation/Parent.jasper'),
            new SubReport('JOINTBORROWER_BUSINESS', 'com/rameses/clfc/report/clientinformation/Business.jasper'),
            new SubReport('JOINTBORROWER_RESIDENCY', 'com/rameses/clfc/report/clientinformation/Residency.jasper'),
            new SubReport('JOINTBORROWER_EMPLOYMENT', 'com/rameses/clfc/report/clientinformation/Employment.jasper'),
            new SubReport('JOINTBORROWER_OTHER_INCOME', 'com/rameses/clfc/report/clientinformation/OtherIncome.jasper'),
            new SubReport('JOINTBORROWER_EDUCATIONAL_BACKGROUND', 'com/rameses/clfc/report/clientinformation/ProfessionalBackground.jasper'),

            new SubReport('JOINTBORROWER_CHILDREN_LIST', 'com/rameses/clfc/report/clientinformation/ChildrenList.jasper'),
            new SubReport('JOINTBORROWER_CHILDREN_EMPLOYMENT', 'com/rameses/clfc/report/clientinformation/Employment2.jasper'),
            new SubReport('JOINTBORROWER_CHILDREN_OTHER_INCOME', 'com/rameses/clfc/report/clientinformation/OtherIncome2.jasper'),

            new SubReport('JOINTBORROWER_SIBLING_LIST', 'com/rameses/clfc/report/clientinformation/SiblingList.jasper'),
            new SubReport('JOINTBORROWER_SIBLING_EMPLOYMENT', 'com/rameses/clfc/report/clientinformation/Employment2.jasper'),
            new SubReport('JOINTBORROWER_SIBLING_OTHER_INCOME', 'com/rameses/clfc/report/clientinformation/OtherIncome2.jasper'),

            new SubReport('JOINTBORROWER_SAVINGS_LIST', 'com/rameses/clfc/report/clientinformation/SavingAccountList.jasper'),
            new SubReport('JOINTBORROWER_CHECKING_LIST', 'com/rameses/clfc/report/clientinformation/CheckingAccountList.jasper'),

            // Co-Maker Info
            new SubReport('COMAKER_LIST', 'com/rameses/clfc/report/clientinformation/List.jasper'),
            new SubReport('COMAKER_SPOUSE', 'com/rameses/clfc/report/clientinformation/Spouse.jasper'),
            new SubReport('COMAKER_PARENT', 'com/rameses/clfc/report/clientinformation/Parent.jasper'),
            new SubReport('COMAKER_BUSINESS', 'com/rameses/clfc/report/clientinformation/Business.jasper'),
            new SubReport('COMAKER_RESIDENCY', 'com/rameses/clfc/report/clientinformation/Residency.jasper'),
            new SubReport('COMAKER_EMPLOYMENT', 'com/rameses/clfc/report/clientinformation/Employment.jasper'),
            new SubReport('COMAKER_OTHER_INCOME', 'com/rameses/clfc/report/clientinformation/OtherIncome.jasper'),
            new SubReport('COMAKER_EDUCATIONAL_BACKGROUND', 'com/rameses/clfc/report/clientinformation/ProfessionalBackground.jasper'),

            new SubReport('COMAKER_CHILDREN_LIST', 'com/rameses/clfc/report/clientinformation/ChildrenList.jasper'),
            new SubReport('COMAKER_CHILDREN_EMPLOYMENT', 'com/rameses/clfc/report/clientinformation/Employment2.jasper'),
            new SubReport('COMAKER_CHILDREN_OTHER_INCOME', 'com/rameses/clfc/report/clientinformation/OtherIncome2.jasper'),

            new SubReport('COMAKER_SIBLING_LIST', 'com/rameses/clfc/report/clientinformation/SiblingList.jasper'),
            new SubReport('COMAKER_SIBLING_EMPLOYMENT', 'com/rameses/clfc/report/clientinformation/Employment2.jasper'),
            new SubReport('COMAKER_SIBLING_OTHER_INCOME', 'com/rameses/clfc/report/clientinformation/OtherIncome2.jasper'),

            new SubReport('COMAKER_SAVING_LIST', 'com/rameses/clfc/report/clientinformation/SavingAccountList.jasper'),
            new SubReport('COMAKER_CHECKING_LIST', 'com/rameses/clfc/report/clientinformation/CheckingAccountList.jasper')
        ];
    }
}
