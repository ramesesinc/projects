package com.rameses.clfc.util;

class CommentHtmlBuilder extends HtmlBuilder
{
    public def buildComments(comments) {
        StringBuffer sb=new StringBuffer();
        sb.append(template.getResult(url+'comment.gtpl', [list: comments]));
        return getHtml(sb.toString());
    }
}
