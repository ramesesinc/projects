package com.rameses.enterprise.account.models; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import java.rmi.server.UID;

class AccountDetailController extends CRUDController {

    @Caller
    def caller;

    String entityName = "account";
    def node;

    public String getServiceName() {
        return caller.getServiceName();
    }

    public String getTitle() {
        return caller.getTitle();
    }  

    public String getPrefixId() {
        try { 
            return caller.getPrefixId(); 
        } catch(Throwable t) {
            return null; 
        } 
    }

    public def getAccountService() {
        return caller.accountService;
    }

    Map createEntity() {
        def m = [parentid:node.objid, type:'detail'];
        m.parent = [code:node.code, title: node.title];
        return m;
    }

    def transferParent() {
        def defaultInvType = 'accountgroup:lookup'; 

        def invtypes = []; 
        def prefix = getPrefixId(); 
        if ( prefix ) {
            invtypes << (defaultInvType +':'+ prefix.toLowerCase()); 
        } 
        invtypes << defaultInvType; 

        def p = [:]; 
        p.onselect = { o->
            if( MsgBox.confirm('You are about to transfer this account to another group. Continue?') ) {
                service.changeParent( [parentid:o.objid, objid:entity.objid] );
                entity.parentid = o.objid;
                entity.parent = o;
                binding.refresh();
            }
        }

        for (int i=0; i<invtypes.size(); i++) {
            try {
                def op = Inv.lookupOpener( invtypes[i], p ); 
                op.target = 'popup'; 
                return op;
            } catch(Throwable t) {;} 
        } 
        return null; 
    }

    def subAccountModel = [
        fetchList: { o->
            return service.getSubAccounts( entity );
        },
        onAddItem: { o->
            o.objid = "SUBACCT" + new UID();
            o.state = 'APPROVED';
            o.type = "subaccount";
            o.acctgroup = entity.acctgroup;
            o.parentid = entity.objid;
        },
        onRemoveItem: { o->
            if( MsgBox.confirm("Remove this item. Continue?")) {
                service.removeEntity( o ); 
                subAccountModel.reload();
            }
        },
        onCommitItem: {o ->
            service.saveSubAccount(o);
        }
    ] as EditorListModel;
}
