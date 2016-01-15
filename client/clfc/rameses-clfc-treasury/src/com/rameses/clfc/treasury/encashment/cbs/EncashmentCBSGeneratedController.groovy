package com.rameses.clfc.treasury.encashment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.LoanUtil;

class EncashmentCBSGeneratedController 
{
    def entity, generated, list;
    
    void init() {
        generated = entity.generated;
        if (!generated) {
            generated = [details: getDenominations()];
        }
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
            return generated?.details;
        }
    ] as BasicListModel;
    
    def getTotalbreakdown() {
        if (!generated?.details) return 0;
        
        def amt = generated?.details?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    /*
    void init() {
        if (entity.generated) {
            generated = entity.generated;
            list = generated.details.sort{ it.denomination }.reverse();
        }
    }

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

    def getTotalbreakdown() {
        if (!list) return 0;
        def amt = list.amount.sum();
        if (!amt) amt = 0;
        return amt;
    }
    */
}