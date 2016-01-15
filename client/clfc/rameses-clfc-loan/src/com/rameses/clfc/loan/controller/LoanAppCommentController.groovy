package com.rameses.clfc.loan.controller;

import com.rameses.rcp.annotations.*;
import com.rameses.clfc.util.*;

class LoanAppCommentController
{
    //feed by the caller
    def caller, selectedMenu, loanapp; 
    
    @Service('LoanAppCommentService')
    def service;
    def htmlbuilder = new CommentHtmlBuilder();
    
    def getHtmlview() {
        def comments = service.getComments([appid: loanapp.objid]);
        return htmlbuilder.buildComments(comments);
    }
}
