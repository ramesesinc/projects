package com.rameses.clfc.util;

import com.rameses.util.TemplateProvider;

class HtmlBuilder
{
    private def template = TemplateProvider.instance;
    private StringBuffer sb;
    private String url = "com/rameses/clfc/util/";
    protected def ifnull = {v, dv->
        ifNull(v, dv);
    }
    
    public def getTemplate() { return template; }
    public String getUrl() { return url; }
    protected def ifNull( value, defaultvalue ) {
        if( value == null ) return defaultvalue;
        return value;
    }
    
    protected def getHtml( htmlbody ) {
        sb=new StringBuffer();
        sb.append(template.getResult(url+'html.gtpl', [htmlbody: htmlbody]));
        return sb.toString();
    }
    
    protected def getEmploymentList( list ) {
        sb=new StringBuffer();
        sb.append(template.getResult(url+'list_employment.gtpl', [list: list, ifNull: ifnull]));
        return sb.toString();
    }
    
    protected def getOtherIncomeList( list ) {
        sb=new StringBuffer();
        sb.append(template.getResult(url+'list_otherincome.gtpl', [list: list, ifNull: ifnull]));
        return sb.toString();
    }
}