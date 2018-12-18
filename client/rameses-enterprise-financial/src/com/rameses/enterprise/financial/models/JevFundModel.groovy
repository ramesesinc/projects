package com.rameses.enterprise.financial.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class JevFundModel  extends CrudFormModel { 

    String printFormName = 'jev';
    def accttype = "itemaccount"
    def numformat = new java.text.DecimalFormat('#,##0.00'); 
    
    def getFormattedAmount() {
        if ( !(entity.amount instanceof Number )) {
            entity.amount = 0.0; 
        }
        return numformat.format( entity.amount ); 
    }
    
    def fixSubList( def list ) {
        def subList = list.findAll{ it.itemrefid };
        def newList = list.findAll{ !it.itemrefid };
        def arr = [];
        newList.each { v->
            arr << v;
            def sl = subList.findAll{ it.itemrefid == v.objid  };
            if( sl ) {
                sl.each { sv ->
                    sv.acctname = (" "*8) + sv.acctname;
                    arr << sv;
                }
            }
        }
        return arr;
    } 

    def list = [];
    def itemListModel = [
        fetchList : { o-> 
            def res = null; 
            if ( accttype == 'ngas' ) {
                res = jevSvc.getItemsInNGASAccount([ jevfundid: entity.objid ]); 
            } else {
                def m = [_schemaname:'jevitem'];
                m.findBy = [jevfundid: entity.objid ];
                res = queryService.getList( m ); 
            }
            
            list.clear(); 
            if ( res ) list.addAll( res ); 

            def drList = [];
            def crList = [];
            list.each {
                if( it.dr > it.cr ) {
                    drList << it;
                }
                else {
                    it.acctname = (" "*8) + it.acctname;
                    crList << it;
                }
            }
            
            def dr = list.sum{( it.dr ? it.dr : 0.0 )}
            def cr = list.sum{( it.cr ? it.cr : 0.0 )}
            def flist = fixSubList(drList) + fixSubList(crList);
            flist.add( [ acctname: (" "*8) + ' ------- T O T A L S ------- ', dr: dr , cr: cr ] ); 
            return flist; 
        }
    ] as BasicListModel;
    
    def getPrintFormData() {
        def m = [:]; 
        m.putAll( super.getPrintFormData()); 
        
        def olditems = m.remove('items'); 
        m.items = []; 
        m.items.addAll( olditems.findAll{( it.dr > 0 )} ); 
        m.items.addAll( olditems.findAll{( it.dr <= 0 )} ); 
        return m; 
    } 

    void viewItemAccounts() {
        accttype = 'itemaccount'; 
        binding.notifyDepends('accttype');
        itemListModel.reload(); 
    }
    void viewNGASAccounts() {
        accttype = 'ngas'; 
        binding.notifyDepends('accttype'); 
        itemListModel.reload(); 
    }
    
    
} 