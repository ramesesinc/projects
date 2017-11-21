package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class PostRealtyTaxShareModel
{
    @Service('DateService')
    def dtSvc;
    
    @Service('RPTReceiptUtilService')
    def svc;
    
    
    def entity = [:]
    def months
    
    void init(){
        months = dtSvc.getMonths() 
        def pdate = dtSvc.parseCurrentDate();
        entity.year = pdate.year;
        entity.month = months.find{it.index == pdate.month}
    }
    
    void postShares(){
        if (MsgBox.confirm('Post?')){
            svc.postShares(entity)
            MsgBox.alert('Shares has been successfully posted.')
        }
    }
    
}