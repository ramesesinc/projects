package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;

class DepositVoucherFundModel extends CrudFormModel {

    @Service("DepositVoucherService")
    def depositSvc;    
    
    @Service("DepositFundTransferService")
    def fundService;
    
    def selectedDepositSlip;
    def depositSlipModel;
    
    def checkListModel;
    def selectedCheck;
    
    def selectedFundTransfer;
    def fundTransferModel;
    
    //DEPOSIT SLIP
    def addDepositSlip() {
        if( (entity.amount - entity.totaldr) == (entity.amountdeposited - entity.totalcr) )
            throw new Exception("No amount to deposit");
        def p = [depositvoucherfund: entity ];
        p.handler = { x->
            entity.putAll( x );
            reloadEntity();
        };
        return Inv.lookupOpener("depositslip:create", p );
    }
    
    void approveDepositSlip() {
        if(!selectedDepositSlip) throw new Exception("Please select a deposit slip");
        if(!MsgBox.confirm("You are about to approve this deposit slip for printing. Proceed?")) return;
        def m = [_schemaname:'depositslip'];
        m.objid = selectedDepositSlip.objid;
        m.state = "APPROVED";
        persistenceService.update(m);
        depositSlipModel.reload();
    }
    
    def printDepositSlip() {
        if(!selectedDepositSlip) throw new Exception("Please select a deposit slip");
        def bh = selectedDepositSlip.bankaccount.bank.depositsliphandler;
        if(!bh) throw new Exception("Please specify a depositsliphandler in the bank ");
        def br = "depositslip_printout:" + bh;
        return Inv.lookupOpener(br, [entity: selectedDepositSlip ]);
    }
    
    void validateDepositSlip() {
        if(!selectedDepositSlip) throw new Exception("Please select a deposit slip");
        def m = [:];
        m.fields = [
            [caption: 'Validation Ref No', name:'refno', required:true],
            [caption: 'Validation Ref Date', name:'refdate', required:true, datatype:'date'],            
        ];
        if(selectedDepositSlip.validation) {
            m.data = selectedDepositSlip.validation;
        }
        m.handler = { o->
            def zz = [_schemaname:"depositslip", validation: o];
            zz.findBy = [objid: selectedDepositSlip.objid];
            persistenceService.update( zz );
            depositSlipModel.reload();
            reloadEntity();
        };
        Modal.show( "dynamic:form", m, [title:"Validate Deposit Slip"] );
    }
    
    //CHECKS MANAGEMENT
    void addCheck() {
        def params = [:];
        params.onselect = { o->
            def v = [list: o*.objid, fundid: entity.fund.objid ];
            depositSvc.addChecks( v );
            checkListModel.reload();
            binding.refresh("checksCount");
        }
        params.listHandler = [
            isMultiSelect: {
                return true;
            },
            fetchList: {
                def m = [_schemaname: 'checkpayment'];
                m.where = [" depositvoucherid = :id AND fundid IS NULL", [id: entity.parentid ]];
                return queryService.getList(m);
            },
            getColumns: {
                return [
                    [name:'refno', caption:'Ref No'],
                    [name:'refdate', caption:'Ref Date'],
                    [name:'bank.name', caption:'Bank'],
                    [name:'amount', caption:'Amount', type:'decimal'],
                ];
            }
        ] as BasicListModel;
        params.title = "Please select the checks you want included in this deposit"; 
        Modal.show( "simple_list_lookup", params);
    }
    
    void removeCheck() {
        if(!selectedCheck) throw new Exception("Please select a check to remove");
        if(selectedCheck.depositslipid) throw new Exception("Cannot remove this check. There is already an associated deposit slip");
        depositSvc.removeCheck( [objid:selectedCheck.objid] );
        checkListModel.reload();
        binding.refresh("checksCount");
    }
    
    
    
    void addFundTransfer() {
        def h = { o->
            reloadEntity();
        }
        Modal.show( "deposit_fund_transfer:create", [ depositvoucherfund: entity, handler: h ]);
    }

    void removeFundTransfer() {
        if(!selectedFundTransfer) throw new Exception("Please select an outgoing fund");
        fundService.removeEntity( selectedFundTransfer );
        reloadEntity();
    }
    
}    