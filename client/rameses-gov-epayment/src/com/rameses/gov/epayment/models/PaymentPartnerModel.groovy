package com.rameses.gov.epayment.models;


import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
 
public class PaymentPartnerModel extends CrudFormModel  {

    def selectedBankAcct;
    def acct = [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;
    
    
}