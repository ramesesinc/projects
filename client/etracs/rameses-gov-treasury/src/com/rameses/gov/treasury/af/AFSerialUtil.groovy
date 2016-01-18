package com.rameses.gov.treasury.af; 

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
class  AFSerialUtil  {

    public static String getEndSeries(def afInfo,  def entry ) {
        if( !entry?.qty ) return "";
        if( !entry?.startseries ) return "";
        if( !afInfo ) return "";
        int endNum = Integer.parseInt(entry.startseries) + (entry.qty * afInfo.unitqty) - 1;
        //pad with 0 if length is less
        int len = (endNum+"").length();
        int diff = afInfo.serieslength - len;
        if(diff>0)
            return "0".multiply(diff)+endNum;
        else
            return endNum+"";
    }

    public static void validateQtyReceived(def itemInfo, def entry ) {
       if( itemInfo.qtyrequested < (entry.qty + itemInfo.qtyreceived))
            throw new Exception("Total qty must be less than or equal to qty requested");
    }

    public static void validateCost( def entry ) {
       if( entry.cost <= 0.0 ) 
            throw new Exception("Sale Amount must not less than or equal to zero.  ");
    }

    public static int getEndStub(def entry ) {
        if( !entry?.qty ) return 0;
        if( !entry?.startstub ) return 0;
        return entry.startstub + entry.qty - 1;
    }

    public static void validateSeries(def afInfo, def entry ) {
        if(!entry.startseries.isInteger() ) {
            throw new Exception("Start series must be a number");
        }    
        if(!entry.startseries.endsWith("1")) {
            throw new Exception("Last number of start series must be 1");
        }
        if( entry.startseries.length() != afInfo.serieslength ) {
            throw new Exception("Series number length must be " + afInfo.serieslength );
        }
        if( ( (Integer.parseInt(entry.startseries)-1) % afInfo.unitqty) != 0 ) {
            throw new Exception("Start series must be divisible by " + afInfo.unitqty);
        }
    }

    public static void checkConflict( def itemInfo, def entry ) {
        int aStart = Integer.parseInt(entry.startseries);
        int aEnd = Integer.parseInt(entry.endseries);
        itemInfo.items.each {
            int bStart = Integer.parseInt(it.startseries);
            int bEnd = Integer.parseInt(it.endseries);
            if(aStart >= bStart && aStart <= bEnd )
                throw new Exception("Start series in conflict with other series");    
            if(aEnd >= bStart && aEnd <= bEnd )
                throw new Exception("End series in conflict with other series");    
        }
    }
}