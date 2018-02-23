package com.rameses.gov.treasury.cashticket; 

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
class  CashTicketUtil  {

    public static int getEndStub(def entry ) {
        if( !entry?.qty ) return 0;
        if( !entry?.startstub ) return 0;
        return entry.startstub + entry.qty - 1;
    }

    public static void validateQtyReceived(def itemInfo, def entry ) {
       if( itemInfo.qtyrequested < (entry.qty + itemInfo.qtyreceived))
            throw new Exception("Total qty must be less than or equal to qty requested");
    }

    public static def calculateValue(def itemInfo, def entry ) {
        if(itemInfo.unitqty==null) return 0;
        if(itemInfo.denomination==null) return 0;
        if(entry.qty==null) return 0;
        return itemInfo.unitqty * itemInfo.denomination * entry.qty
    }   
}