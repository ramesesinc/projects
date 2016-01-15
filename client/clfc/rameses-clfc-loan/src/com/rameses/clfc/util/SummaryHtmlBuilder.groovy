package com.rameses.clfc.util;

import com.rameses.util.TemplateProvider;

class SummaryHtmlBuilder extends HtmlBuilder
{
    public def buildSummary( summary ) {
        def url = 'com/rameses/clfc/loan/summary/';
        StringBuffer sb=new StringBuffer();
        
        sb.append(template.getResult(url+'loandetail.gtpl', [summary: summary, ifnull: ifnull]));
        sb.append(template.getResult(url+'borrower.gtpl', [borrower: summary.borrower, ifnull: ifnull]));
        sb.append(template.getResult(url+'spouse.gtpl', [spouse: summary.borrower.spouse, ifnull: ifnull]));
        sb.append(template.getResult(url+'children.gtpl', [children: summary.borrower.children, ifnull: ifnull]));
        sb.append(template.getResult(url+'education.gtpl', [educations: summary.borrower.educations, ifnull: ifnull]));
        sb.append(template.getResult(url+'employment.gtpl', [employments: summary.borrower.employments, ifnull: ifnull]));
        sb.append(template.getResult(url+'parent.gtpl', [parent: summary.borrower.parent, ifnull: ifnull]));
        sb.append(template.getResult(url+'sibling.gtpl', [siblings: summary.borrower.siblings, ifnull: ifnull]));
        sb.append(template.getResult(url+'otherincome.gtpl', [otherincomes: summary.borrower.otherincomes, ifnull: ifnull]));
        sb.append(template.getResult(url+'bankacct.gtpl', [
            savingaccts: summary.borrower.savingaccts,
            checkingaccts: summary.borrower.checkingaccts,
            otheracct: summary.borrower.otheracct,
            ifnull: ifnull
        ]));
        sb.append(template.getResult(url+'business.gtpl', [businesses: summary.businesses, ifnull: ifnull]));
        sb.append(template.getResult(url+'collateral.gtpl', [collateral: summary.collateral, ifnull: ifnull]));
        sb.append(template.getResult(url+'otherlending.gtpl', [otherlendings: summary.otherlendings, ifnull: ifnull]));
        sb.append(template.getResult(url+'jointborrower.gtpl', [jointborrowers: summary.jointborrowers, ifnull: ifnull]));
        sb.append(template.getResult(url+'comaker.gtpl', [comakers: summary.comakers, ifnull: ifnull]));
        sb.append(template.getResult(url+'recommendation.gtpl', [recommendation: null, ifnull: ifnull]));
        return getHtml(sb.toString());
    }
}
