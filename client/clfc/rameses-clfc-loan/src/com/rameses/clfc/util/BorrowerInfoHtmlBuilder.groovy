package com.rameses.clfc.util;

class BorrowerInfoHtmlBuilder extends HtmlBuilder
{
    private StringBuffer sb;
    
    def employmentlistHandler = {list->
        getEmploymentList(list);
    }
    
    def otherincomelistHandler = {list->
        getOtherIncomeList(list);
    }
    
    public def buildEducation( education ) {
        if( education == null ) return '';
        sb=new StringBuffer();
        sb.append(template.getResult(url+'borrower_education.gtpl', [education: education, ifNull: ifnull]));
        return getHtml(sb.toString());
    }
    
    public def buildOtherIncome( otherincome ) {
        if (otherincome == null) return '';
        sb=new StringBuffer();
        sb.append(template.getResult(url+'borrower_otherincome.gtpl', [otherincome: otherincome, ifNull: ifnull]));
        return getHtml(sb.toString());
    }
    
    public def buildEmployment( employment ) {
        if (employment == null) return '';
        sb=new StringBuffer();
        sb.append(template.getResult(url+'borrower_employment.gtpl', [employment: employment, ifNull: ifnull]));
        return getHtml(sb.toString());
    }
    
    public def buildSibling( sibling ) {
        if (sibling == null) return '';
        sb=new StringBuffer();
        sb.append(template.getResult(url+'borrower_sibling.gtpl', [
            sibling: sibling, 
            getEmploymentList: employmentlistHandler,
            getOtherIncomeList: otherincomelistHandler,
            ifNull: ifnull
        ]));
        return getHtml(sb.toString());
    }
    
    public def buildChild( child ) {
        if( child == null ) return '';
        sb=new StringBuffer();
        sb.append(template.getResult(url+'borrower_child.gtpl', [
            child: child,
            getEmploymentList: employmentlistHandler,
            getOtherIncomeList: otherincomelistHandler,
            ifNull: ifnull
        ]));
        return getHtml(sb.toString());
    }
    
    public def buildBankAccount( bankacct ) {
        if (bankacct == null) return '';
        sb=new StringBuffer();
        sb.append(template.getResult(url+'borrower_bankacct.gtpl', [bankacct: bankacct, ifNull: ifnull]));
        return getHtml(sb.toString());
    }
    
    public def buildBorrower( borrower ) {
        if( borrower == null ) return '';
        sb=new StringBuffer();
        sb.append(template.getResult(url+'borrower.gtpl', [borrower: borrower, ifNull: ifnull]));
        return getHtml(sb.toString());
    }
}
