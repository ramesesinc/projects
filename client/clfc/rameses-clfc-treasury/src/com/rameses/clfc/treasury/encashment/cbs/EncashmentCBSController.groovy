package com.rameses.clfc.treasury.encashment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.LoanUtil;

class EncashmentCBSController 
{
    def entity, list, encashments;

    void init() {
        if (!entity?.reference) {
            entity.reference = getDenominations();
        }
        
        if (!entity?.breakdown) {
            entity?.breakdown = getDenominations();
        }
        /*
        if (entity.references) {
            list = entity.references.sort{ it.denomination }.reverse();
        }
        if (entity.details) {
            encashments = entity.details.sort{ it.denomination }.reverse();
        }
        */
    }

    def getDenominations() {
        def list = [];
        LoanUtil.denominations.each{
            def map = [:];
            map.putAll(it);
            list << map;
        }
        return list;
    }
    
    def listHandler = [
        fetchList: { o->
            return entity?.reference;
        }
    ] as BasicListModel;
    
    def getTotalbreakdown() {
        if (!entity.reference) return 0;
        
        def amt = entity?.reference?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    def breakdownListHandler = [
        fetchList: { o->
            return entity?.breakdown;
        }
    ] as BasicListModel;
    
    def getTotalencashment() {
        if (!entity.breakdown) return 0;
        
        def amt = entity?.breakdown?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    /*
    def listHandler = [
        fetchList: { o->
            if (!list) {
                list = [];
                LoanUtil.denominations.each{
                    def map = [:];
                    map.putAll(it);
                    list << map;
                }
            }
            return list;
        }
    ] as BasicListModel;

    def encashmentListHandler = [
        fetchList: { o->
            if (!encashments) {
                encashments = [];
                LoanUtil.denominations.each{
                    def map = [:];
                    map.putAll(it);
                    encashments << map;
                }
            }
            return encashments;
        }
    ] as BasicListModel;

    def getTotalbreakdown() {
        if (!list) return 0;
        def amt = list.amount.sum();
        if (!amt) amt = 0;
        return amt;
    }

    def getTotalencashment() {
        if (!encashments) return 0;
        def amt = encashments.amount.sum();
        if (!amt) amt = 0;
        return amt;
    }*/
}