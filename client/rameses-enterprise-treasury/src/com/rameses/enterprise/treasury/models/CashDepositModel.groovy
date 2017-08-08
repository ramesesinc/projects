package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class CashDepositModel  { 

    @Service("PersistenceService")
    def persistSvc;

    String title = "Cash Deposit";

    def entity;

    void open() {
         entity._schemaname = 'cashdeposit';
         entity = persistSvc.read( entity );
         entity.each { k,v->
            println k+"="+v;
         }
    }

    def checkModel = [
         fetchList: { o->
             return entity.checks;
         }
     ] as BasicListModel;

     def itemModel = [
        fetchList: { o->
            return entity.items;
        }
     ] as BasicListModel;
} 