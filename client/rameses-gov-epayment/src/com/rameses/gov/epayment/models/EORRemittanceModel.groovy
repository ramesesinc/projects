package com.rameses.gov.epayment.models;


import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
 
/*******************************************************************************
* This class is used for Rental, Other Fees and Utilities
********************************************************************************/
public class EORRemittanceModel extends CrudFormModel {
    
    def eorListHandler = [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;
    
    def fundListHandler = [
        fetchList: { o->
            def m = [_schemaname: 'eor_remittance'];
            m.findBy = [remittanceid: entity.objid ];
            return queryService.getList( m );
        }
    ] as BasicListModel;

}