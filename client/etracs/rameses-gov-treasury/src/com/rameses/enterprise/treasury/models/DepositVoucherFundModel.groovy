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
    
    @Service("DepositSlipService")
    def depositSlipSvc;
    
    @Service("DepositFundTransferService")
    def fundService;
    
    def selectedDepositSlip;
    def depositSlipListModel;
    
    def checkListModel;
    def selectedCheck;
    
    def selectedFundTransfer;
    def fundTransferModel;
    
    def depositSlipModel;
    
    void afterOpen() {
        depositSlipModel =  ManagedObjects.instance.create( DepositSlipModel.class );
    }
    
    //DEPOSIT SLIP
    def addDepositSlip() {
        if( (entity.amount - entity.totaldr) == (entity.amountdeposited - entity.totalcr) )
            throw new Exception("No amount to deposit");
        def p = [depositvoucherfund: entity ];
        p.handler = { x->
            entity.putAll( x );
            reloadEntity();
            depositSlipListModel.reload();
        };
        return Inv.lookupOpener("depositslip:create", p );
    }
    
    void removeDepositSlips() {
        if(!depositSlipListModel.selectedValue) throw new Exception("Please select a deposit slip by checking it");
        if(!MsgBox.confirm("Only the deposit slips in DRAFT mode will be removed. Proceed?")) return;
        def list = depositSlipListModel.selectedValue.findAll{it.state=="DRAFT"};
        list.each {
            depositSlipSvc.removeDepositSlip([objid: it.objid]);
        }
        reloadEntity();
    }
    
    void approveDepositSlip() {
        if(!selectedDepositSlip) throw new Exception("Please select a deposit slip");
        depositSlipModel.entity = selectedDepositSlip;
        depositSlipModel.approve();
        depositSlipListModel.reload();
    }
    
    void printDepositSlip() {
        if(!selectedDepositSlip) throw new Exception("Please select a deposit slip");
        depositSlipModel.entity = selectedDepositSlip;
        depositSlipModel.print();
        depositSlipListModel.reload();
    }
    
    void markDepositSlipAsPrinted() {
        if(!selectedDepositSlip) throw new Exception("Please select a deposit slip");
        depositSlipModel.entity = selectedDepositSlip;
        depositSlipModel.markAsPrinted();
        depositSlipListModel.reload();
    }


    void validateDepositSlip() {
        if(!selectedDepositSlip) throw new Exception("Please select a deposit slip");
        depositSlipModel.entity = selectedDepositSlip;
        depositSlipModel.validate();
        depositSlipListModel.reload();
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