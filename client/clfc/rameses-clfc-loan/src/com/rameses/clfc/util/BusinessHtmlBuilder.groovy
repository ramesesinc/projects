package com.rameses.clfc.util;

class BusinessHtmlBuilder extends HtmlBuilder
{
    public def buildBusiness( business ) {
        if (business == null) return '';
        StringBuffer sb=new StringBuffer();
        sb.append(template.getResult(url+'business.gtpl', [business: business, ifNull: ifnull]));
        return getHtml(sb.toString());
    }
}
