package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
        
public class PaymentOptionModel {

    @Binding
    def binding;

    String title = "Payment Option";
    def handler;

    def payOption = "FULL";
    def date;
    def numdays;
    def amount;
    def monthyear;
    def qtr;
                
    def qtrList = [1,2,3,4];

    def monthList = [
        [month:1, monthname:'JAN'],
        [month:2, monthname:'FEB'],
        [month:3, monthname:'MAR'],
        [month:4, monthname:'APR'],
        [month:5, monthname:'MAY'],
        [month:6, monthname:'JUN'],
        [month:7, monthname:'JUL'],
        [month:8, monthname:'AUG'],
        [month:9, monthname:'SEP'],
        [month:10, monthname:'OCT'],
        [month:11, monthname:'NOV'],
        [month:12, monthname:'DEC'],
    ];

    def doOk() {
        if(!handler) 
            throw new Exception("Pls. include handler in payoption");

        def qry = [type: payOption];
        switch( payOption ) {
            case 'DATE':
                qry.date = date;
                break;
            case 'NUMDAYS':
                qry.numdays = numdays;
                break;
            case 'SPECIFYAMOUNT':
                qry.amount = amount;
                break;
            case 'MONTH':
                qry.month = monthyear.month;
                qry.year = monthyear.year;
                break;    
            case 'QTR':
                qry.qtr = qtr;
                break;    
        }
        handler(qry);
        return "_close";
    }

    def doCancel() {
        return "_close";
    }

}       