package gov.zamboanga.landtax.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class LedgerTaxpayerMappingModel extends CrudListModel {

    def assignTaxpayer() {
        def onassign = {
            search();
        }
        def items = listHandler.selectedValue;
        if (!items) throw new Exception('At least one property to assign new taxpayer should be selected.');
        return Inv.lookupOpener('rptledger:taxpayer:mapping', [items: items, onassign: onassign]);
    }
}