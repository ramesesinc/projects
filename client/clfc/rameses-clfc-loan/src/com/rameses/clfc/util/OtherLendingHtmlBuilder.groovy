package com.rameses.clfc.util;

class OtherLendingHtmlBuilder extends HtmlBuilder
{    
    public def buildOtherLending( otherlending ) {
        if (otherlending == null) return '';
        StringBuffer sb=new StringBuffer();
        sb.append(template.getResult(url+'otherlending.gtpl', [otherlending: otherlending, ifNull: ifnull]));
        return getHtml(sb.toString());
    }
}
